/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSemantico;

import analizadorSemantico.DescripcionDefinicionTupla.DefinicionMiembro;
import analizadorSemantico.DescripcionFuncion.Parametro;
import analizadorSemantico.genCodigoIntermedio.GestorCodigoIntermedio;
import analizadorSemantico.genCodigoIntermedio.Operador;
import analizadorSemantico.genCodigoIntermedio.TipoVariable;
import analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr;
import analizadorSintactico.ParserSym;
import java.util.ArrayList;
import analizadorSintactico.symbols.*;
import static dartam.Dartam.DEBUG;
import java.util.List;
import java.util.Stack;
import java_cup.runtime.ComplexSymbolFactory.Location;
import jflex.base.Pair;

public class AnalizadorSemantico {

    private GestorCodigoIntermedio g3d;

    private String varActual;

    public TablaSimbolos tablaSimbolos;

    // Description to the function in which we are currently.
    private Pair<String, DescripcionFuncion> metodoActualmenteSiendoTratado;
    private Pair<String, DescripcionDefinicionTupla> tuplaActualmenteSiendoTratada;
    private Stack<Pair<String, String>> pilaEtiquetasBucle = new Stack(); // 1o es inicio y 2o es final
    private Stack<String> pilaEtiquetasSwitch = new Stack();

    private List<String> errores;//, symbols;

    public String getErrores() {
        if (errores.isEmpty()) {
            return "";
        }
        String s = "Localizaciones de los errores multilinea en formato (linea, columna)\n\n";
        for (String e : errores) {
            s += e + "\n";
        }
        return s;
    }

    public String getSymbols() {
        return tablaSimbolos.toString();
    }

    public GestorCodigoIntermedio getGenerador() {
        return g3d;
    }

    private void indicarLocalizacion(SymbolBase s) {
        String loc;
        int idx = errores.size() - 1;
        if (s.xleft == null) {
            loc = "ERROR: No ha sido posible localizarlo: ";
        } else if (s.xleft.getLine() == s.xright.getLine()) {
            loc = "ERROR: En la linea " + s.xleft.getLine() + " entre las columnas " + s.xleft.getColumn() + " y " + s.xright.getColumn() + ": ";
        } else {
            loc = "ERROR: Desde (" + s.xleft.getLine() + ", " + s.xleft.getColumn() + ") hasta (" + s.xright.getLine() + ", " + s.xright.getColumn() + "): ";
        }

        errores.set(idx, loc + errores.get(idx));
    }

    public AnalizadorSemantico(SymbolScript scriptElementosAntesDeMain) throws Exception {
        tablaSimbolos = new TablaSimbolos();
        errores = new ArrayList<>();
        ArrayList<SymbolDecs> declaraciones = new ArrayList<>();
        ArrayList<SymbolScriptElemento> tuplas = new ArrayList<>();
        ArrayList<SymbolScriptElemento> metodos = new ArrayList<>();
        g3d = new GestorCodigoIntermedio();
        int idMidDecs = 0, idMidTuplas = 0, idMidMetodos = 0;
        // elementos antes del main
        SymbolScriptElemento elem = scriptElementosAntesDeMain.elemento;
        while (elem != null) {
            switch (elem.getTipo()) {
                case DECS -> {
                    declaraciones.add(elem.declaraciones);
                    idMidDecs++;
                }
                case TUPLA -> {
                    tuplas.add(elem);
                    idMidTuplas++;
                }
                case METODO -> {
                    metodos.add(elem);
                    idMidMetodos++;
                }
            }
            scriptElementosAntesDeMain = scriptElementosAntesDeMain.siguienteElemento;
            elem = scriptElementosAntesDeMain.elemento;
        }
        SymbolMain scriptMainYElementos = scriptElementosAntesDeMain.main;
        // elementos despues del main
        elem = scriptMainYElementos.elemento;
        while (elem != null) {
            switch (elem.getTipo()) {
                case DECS ->
                    declaraciones.add(idMidDecs, elem.declaraciones);
                case TUPLA ->
                    tuplas.add(idMidTuplas, elem);
                case METODO ->
                    metodos.add(idMidMetodos, elem);
            }
            scriptMainYElementos = scriptMainYElementos.siguienteElemento;
            elem = scriptMainYElementos.elemento;
        }
        inicializarMetodosEspeciales();
        // tuplas
        for (SymbolScriptElemento tupla : tuplas) {
            DescripcionDefinicionTupla d = (DescripcionDefinicionTupla) tablaSimbolos.consulta(tupla.id);
            if (d != null) {
                errores.add("Se han intentado nombrar dos tuplas iguales: " + tupla.id);
                indicarLocalizacion(tupla);
                continue;
            }
            d = new DescripcionDefinicionTupla(tupla.id, new ArrayList<>(), g3d.nuevaVariable(tupla.id, TipoVariable.PUNTERO));
            tablaSimbolos.poner(tupla.id, d);
            g3d.anyadirTupla(tupla.id, d);
        }
        // interor de las tuplas
        for (SymbolScriptElemento tupla : tuplas) {
            DescripcionDefinicionTupla d = (DescripcionDefinicionTupla) tablaSimbolos.consulta(tupla.id);
            tuplaActualmenteSiendoTratada = new Pair<>(tupla.id, d);
            procesarDeclaracionTupla(tupla);
            tuplaActualmenteSiendoTratada = null;
            g3d.anyadirBytesEstructura(d.variableAsociada, d.getBytes());
        }
        // declaraciones
        for (SymbolDecs decs : declaraciones) {
            procesarDeclaraciones(decs);
        }
        // metodos
        for (SymbolScriptElemento metodo : metodos) {
            DescripcionSimbolo d = tablaSimbolos.consulta(metodo.id);
            if (d != null) {
                errores.add("Se ha intentado nombrar el metodo " + metodo.id + " pero este identificador ya esta en uso");
                indicarLocalizacion(metodo);
                continue;
            }
            tablaSimbolos.entraBloque();
            ArrayList<Parametro> parametros = procesarParametros(metodo.parametros, metodo.id);
            tablaSimbolos.salirBloque();
            d = new DescripcionFuncion(metodo.tipoRetorno.getTipo(), parametros, g3d.nuevaEtiqueta(metodo.id));
            tablaSimbolos.poner(metodo.id, d);
        }
        // main
        DescripcionFuncion d = new DescripcionFuncion(ParserSym.terminalNames[ParserSym.VOID], g3d.nuevaEtiqueta(scriptMainYElementos.nombreMain), true);
        tablaSimbolos.poner(scriptMainYElementos.nombreMain, d);
        procesarMain(scriptMainYElementos);
        // interior de los metodos
        for (SymbolScriptElemento metodo : metodos) {
            procesarMetodo(metodo);
        }
    }

    private void inicializarMetodosEspeciales() throws Exception {
        String nombre, tipoString = ParserSym.terminalNames[ParserSym.CAR] + " []";
        DescripcionFuncion df;
        int numeroProcedure;
        // metodos especiales
        nombre = ParserSym.terminalNames[ParserSym.SCAN].toLowerCase();
        df = new DescripcionFuncion(ParserSym.terminalNames[ParserSym.VOID], "input", tipoString, g3d.nuevaEtiqueta(nombre));
        numeroProcedure = g3d.nuevoProcedimiento(nombre, df.variableAsociada, df.getParametros(), 0);
        tablaSimbolos.poner(nombre, df);
        nombre = ParserSym.terminalNames[ParserSym.SHOW].toLowerCase();
        df = new DescripcionFuncion(ParserSym.terminalNames[ParserSym.VOID], "output", tipoString, g3d.nuevaEtiqueta(nombre));
        numeroProcedure = g3d.nuevoProcedimiento(nombre, df.variableAsociada, df.getParametros(), 0);
        tablaSimbolos.poner(nombre, df);
        nombre = ParserSym.terminalNames[ParserSym.FROM].toLowerCase();
        df = new DescripcionFuncion(ParserSym.terminalNames[ParserSym.VOID], "file", tipoString, "input", tipoString, g3d.nuevaEtiqueta(nombre));
        numeroProcedure = g3d.nuevoProcedimiento(nombre, df.variableAsociada, df.getParametros(), 0);
        tablaSimbolos.poner(nombre, df);
        nombre = ParserSym.terminalNames[ParserSym.INTO].toLowerCase();
        df = new DescripcionFuncion(ParserSym.terminalNames[ParserSym.VOID], "file", tipoString, "output", tipoString, g3d.nuevaEtiqueta(nombre));
        numeroProcedure = g3d.nuevoProcedimiento(nombre, df.variableAsociada, df.getParametros(), 0);
        tablaSimbolos.poner(nombre, df);
    }

    private void procesarDeclaraciones(SymbolDecs decs) throws Exception {
        SymbolTipo tipo = decs.tipo;
        for (SymbolDecAsigLista dec = decs.iddecslista; dec != null; dec = dec.siguienteDeclaracion) {
            boolean error = false;

            String id = dec.id; // nombre variable
            SymbolOperand valorAsignado = (dec.asignacion == null) ? null : dec.asignacion.operando;
            if (valorAsignado != null && valorAsignado.atomicExp != null) {
                DescripcionSimbolo d = tablaSimbolos.consulta(valorAsignado.atomicExp.value.toString());
                if (d != null && !d.tieneValorAsignado()) {
                    errores.add("No se puede asignar con la variable '" + valorAsignado.atomicExp.value.toString() + "' porque no ha sido inicializada con anterioridad");
                    indicarLocalizacion(tipo);
                    error = true;
                }
                if (d != null && (d.isArray() || d.isTipoTupla())) {
                    errores.add("No está permitido realizar asignaciones entre identificadores de estructuras de datos complejas (" + id + ")");
                    indicarLocalizacion(dec);
                    error = true;
                }
            }
            if (tuplaActualmenteSiendoTratada == null) { // si forma parte de una tupla si se puede declarar  ---------------------------------------------------------------------
                String errMsg = tablaSimbolos.sePuedeDeclarar(id);
                if (!errMsg.isEmpty()) {
                    errores.add(errMsg);
                    indicarLocalizacion(dec);
                    error = true;
                }
            } else {
                if (tipo.isArray() || tipo.isTupla()) {
                    errores.add("El miembro '" + id + "' de la tupla " + tuplaActualmenteSiendoTratada.fst + " ha sido declarado como " + tipo.getTipo() + ". En las tuplas solo se permiten tipos primitivos");
                    indicarLocalizacion(tipo);
                    return;
                }
            }
            if (tipo.isArray() && tipo.isTupla()) {
                errores.add("La variable '" + id + "' se ha declarado de tipo " + tipo.getTipo() + ". No se permiten arrays de tuplas");
                indicarLocalizacion(tipo);
                return;
            }
            // si es array
            ArrayList<String> variablesDimension = null;
            ArrayList<Integer> valoresDimension = null;
            boolean arrayInicializado = false;
            if (tipo.isArray()) {
                arrayInicializado = !tipo.dimArray.isEmpty();
                variablesDimension = new ArrayList<>();
                valoresDimension = new ArrayList<>();
                int n = 0;
                // procesamiento de izquierda a derecha
                for (SymbolDimensiones dim = tipo.dimArray; dim != null; dim = dim.siguienteDimension) {
                    n++;
                    if (dim.isEmpty()) { // si una es Empty todas lo son
                        variablesDimension.add(null);
                        continue;
                    }
                    String tipoIdx = procesarOperando(dim.operando);
                    if (tipoIdx == null) {
                        errores.add("Se realizan operaciones no validas para el calculo del indice " + n + " del array " + id);
                        indicarLocalizacion(dim.operando);
                        error = true;
                    } else if (!tipoIdx.equals(ParserSym.terminalNames[ParserSym.ENT])) {
                        errores.add("Las operaciones para el calculo del indice " + n + " del array " + id + " resultan en " + tipoIdx + ", cuando tendria que ser un entero");
                        indicarLocalizacion(dim.operando);
                        error = true;
                    }
                    if (error) {
                        continue;
                    }
                    if (tuplaActualmenteSiendoTratada == null) {
                        String varValor = varActual;
                        String varDimension = g3d.nuevaDimension(id, TipoVariable.INT);
                        g3d.generarInstr(TipoInstr.COPY, new Operador(TipoVariable.INT, varValor), null, new Operador(TipoVariable.INT, varDimension));
                        variablesDimension.add(varDimension);
                    }
                    valoresDimension.add((Integer) dim.operando.atomicExp.value); // suponiendo que son cosntantes valores
                }
            }
            // si se ha asignado un valor
            String variableQueSeAsigna = null;
            String tipoValor = null;
            boolean seHaAsignado = valorAsignado != null;
            if (seHaAsignado) {
                if (arrayInicializado) {
                    errores.add("No esta permitido definir las dimensiones " + tipo.dimArray + " del array '" + id + "' cuando esta siendo asignado un valor " + valorAsignado);
                    indicarLocalizacion(valorAsignado);
                    error = true;
                }
                tipoValor = procesarOperando(valorAsignado); // se actualiza variableTratadaActualmente
                variableQueSeAsigna = varActual;
                if (tipoValor == null) {
                    errores.add("Los valores del operando " + valorAsignado + " no son compatibles");
                    indicarLocalizacion(valorAsignado);
                    error = true;
                } else if (!tipoValor.equals(tipo.getTipo())) {
                    errores.add("El tipo " + tipo.getTipo() + " con el que se ha declarado '" + id + "' no es compatible con el que se esta intentado asignar a la variable (" + tipoValor + ")");
                    indicarLocalizacion(valorAsignado);
                    error = true;
                }
                if (!error) {
                    int numDimAsignadas = tipoValor.length() - tipoValor.replace("[", "").length();
                    if (tipo.isArray() && variablesDimension.size() != numDimAsignadas) {
                        errores.add("Se ha intentado asignar a '" + id + "' de tipo " + tipo.getTipo() + " un valor de tipo " + tipoValor);
                        indicarLocalizacion(valorAsignado);
                        error = true;
                    }
                }
            }
            // si es una variable tipo tupla: guarda aqui su definicion
            DescripcionDefinicionTupla descTipoTupla = null;
            String tuplaName = null;
            TipoVariable t = null;
            if (tipo.isTupla()) {
                t = TipoVariable.getTipo(tipo.getTipo(), false);
                tuplaName = tipo.getTipo();
                tuplaName = tuplaName.substring(tuplaName.indexOf(" ") + 1);
                if (tipo.isArray()) {
                    tuplaName = tuplaName.substring(0, tuplaName.indexOf(" "));
                }
                DescripcionSimbolo ds = tablaSimbolos.consulta(tuplaName);
                if (ds == null) {
                    errores.add("No se ha encontrado ninguna tupla con el identificador " + tuplaName);
                    indicarLocalizacion(tipo);
                    error = true;
                } else {
                    descTipoTupla = (DescripcionDefinicionTupla) ds;
                }
            }
            if (error) {
                continue;
            }
            if (tuplaActualmenteSiendoTratada != null) { // declaracion de un miembro de una definicion de una tupla
                if (tuplaActualmenteSiendoTratada.snd.tieneMiembro(id)) {
                    errores.add("Se ha intentado añadir el miembro repetido " + id + " a la tupla " + tuplaName);
                    indicarLocalizacion(dec);
                    continue;
                }
                // si no hay error
                tuplaActualmenteSiendoTratada.snd.anyadirMiembro(
                        new DefinicionMiembro(id, tipo.getTipo(), decs.isConst,
                                seHaAsignado || arrayInicializado, descTipoTupla, seHaAsignado ? valorAsignado.atomicExp.getValorCodigoIntermedio() : null)); // suponiendo que se asignan constantes
                continue;
            }
            // no es un miembro de una tupla
            if (t == null) {
                if (variableQueSeAsigna == null) {
                    if (seHaAsignado || arrayInicializado) {
                        t = TipoVariable.getTipo(tipo.getTipo(), false);
                    } else {
                        t = TipoVariable.getTipo(tipo.getTipo(), tipo.getTipo().contains("[") || tipo.getTipo().startsWith(ParserSym.terminalNames[ParserSym.TUPLE]));
                    }
                } else {
                    t = g3d.getTipoFromVar(variableQueSeAsigna);
                }
            }
            TipoVariable tOther = t.getAssignedType();
            String variableCodigoIntermedio;
            if (seHaAsignado && this.tuplaActualmenteSiendoTratada == null && !tipo.isTupla()) {
                variableCodigoIntermedio = g3d.nuevaVariable(id, tOther);
                g3d.generarInstr(TipoInstr.COPY, new Operador(t, variableQueSeAsigna), null, new Operador(tOther, variableCodigoIntermedio));
//            } else if(tipo.isArray() && arrayInicializado){
//                variableCodigoIntermedio = g3d.nuevaVariable(id, t);
            } else {
                variableCodigoIntermedio = g3d.nuevaVariable(id, t);
            }
            DescripcionSimbolo descVar;
            if (tipo.isArray()) {
                if (tipo.dimArray.isEmpty()) { // array sin inicializar
                    descVar = new DescripcionArray(tipo.getTipo(), decs.isConst,
                            variablesDimension.size(),
                            seHaAsignado || arrayInicializado, descTipoTupla, variablesDimension,
                            variableCodigoIntermedio);
                } else { // array inicializado
                    // suponiendo que valores son constantes
                    int total = 1;
                    for (int i = 0; i < valoresDimension.size(); i++) {
                        total *= valoresDimension.get(i);
                    }
                    descVar = new DescripcionArray(tipo.getTipo(), decs.isConst,
                            tipo.dimArray.getDimensiones(),
                            seHaAsignado || arrayInicializado, descTipoTupla, variablesDimension,
                            variableCodigoIntermedio);
                    g3d.anyadirBytesEstructura(descVar.variableAsociada, (tipo.isTupla() ? descTipoTupla.getBytes() : TipoVariable.getBytes(tipo.getTipoSinDimensiones(), false)) * total);
                }
            } else { // variable no array (puede o no ser de tipo tupla)
                descVar = new DescripcionSimbolo(tipo.getTipo(), decs.isConst,
                        seHaAsignado || arrayInicializado, descTipoTupla,
                        variableCodigoIntermedio);
                if (tipo.isTupla()) {
                    g3d.relacionarDatoVariableConTupla(descVar.variableAsociada, descTipoTupla);
                    g3d.anyadirBytesEstructura(descVar.variableAsociada, descTipoTupla.getBytes());//descVar.getBytes());
                }
            }
            tablaSimbolos.poner(id, descVar);
        }
    }

    private void procesarBody(SymbolBody body) throws Exception {
        for (; body != null; body = body.siguienteMetodo) {
            SymbolMetodoElemento elem = body.metodo;
            switch (elem.getTipo()) {
                case INSTR ->
                    procesarInstruccion(elem.instruccion);
                case IF ->
                    procesarIf(elem.iff);
                case LOOP ->
                    procesarLoop(elem.loop);
                case SWITCH ->
                    procesarSwitch(elem.sw);
                case BLOQUE ->
                    procesarBloque(elem.block);
            }
        }
    }

    private void procesarBloque(SymbolBloque bloque) throws Exception {
        tablaSimbolos.entraBloque();
        procesarBody(bloque.cuerpo);
        tablaSimbolos.salirBloque();
    }

    private void procesarInstruccion(SymbolInstr instr) throws Exception {
        switch (instr.getTipo()) {
            case ASIGS ->
                procesarAsignaciones(instr.asigs);
            case DECS ->
                procesarDeclaraciones(instr.decs);
            case FCALL ->
                procesarLlamadaFuncion(instr.fcall);
            case RET ->
                procesarReturn(instr.ret);
            case SWAP ->
                procesarSwap(instr.swap);
            case CONTINUE ->
                procesarContinue(instr);
            case BREAK ->
                procesarBreak(instr);
        }
    }

    private void procesarAsignaciones(SymbolAsigs asigs) throws Exception {
        for (; asigs != null; asigs = asigs.siguienteAsig) {
            boolean error = false;
            SymbolAsig asig = asigs.asig;
            DescripcionSimbolo d = tablaSimbolos.consulta(asig.id);
            if (d == null) {
                errores.add("La variable " + asig.id + " no ha sido declarada con anterioridad");
                indicarLocalizacion(asig);
                error = true;
            } else if (d.isFunction()) {
                errores.add("La variable " + asig.id + " corresponde a una funcion y no a una variable");
                indicarLocalizacion(asig);
                error = true;
            }
            String tipoValor; // tipo por la derecha
            if (!asig.operacion.isBasicAsig()) {
                int sym = asig.operacion.getBinaryOpEquivalent();
                Location l = asig.xleft, r = asig.xright;
                SymbolBinaryExpression bexp = new SymbolBinaryExpression(new SymbolOperand(new SymbolAtomicExpression(true, asig.id, l, r), l, r), new SymbolBinaryOperator(sym, ParserSym.terminalNames[sym], l, r), asig.valor, l, r);
                tipoValor = procesarOperando(new SymbolOperand(bexp, l, r));
            } else {
                tipoValor = procesarOperando(asig.valor);
            }
            if (tipoValor == null) {
                errores.add("Se realizan operaciones no validas en el valor a asignar a '" + asig.id + "'");
                indicarLocalizacion(asig.valor);
                error = true;
            } else {
                DescripcionSimbolo ds = tablaSimbolos.consulta(asig.valor.toString());
                if (ds != null && !ds.tieneValorAsignado()) {
                    errores.add("No se puede asignar la variable '" + asig.valor + "' a '" + asig.id + "' si no ha sido inicializada antes");
                    indicarLocalizacion(asig.valor);
                    error = true;
                }
            }
            if (error) {
                continue;
            }
            String variableParaAsignar = varActual;
            // Tipo por la izquierda
            String tipoVariable = null;
            // array
            String indexReal = null;
            // fin array
            switch (asig.getTipo()) {
                case ID -> {
                    DescripcionArray da = null;
                    if (d.isArray()) {
                        da = (DescripcionArray) d;
                    }
                    if ((d.isArray() || d.isTipoTupla()) && da != null && !da.isString()) {
                        errores.add("No está permitido realizar asignaciones entre identificadores de estructuras de datos complejas (" + asig.id + ")");
                        indicarLocalizacion(asig);
                        error = true;
                    }
                    if (d.tieneValorAsignado() && d.isConstante()) {
                        errores.add("Se esta intentado asignar un valor a la constante '" + asig.id + "' que ya tenia valor");
                        indicarLocalizacion(asig);
                        error = true;
                    }
                    DescripcionSimbolo ds = tablaSimbolos.consulta(asig.valor.toString());
                    
                    
                    if (tipoValor.equals(d.tipo) && da != null && da.isString()) {
                        //stringAsignado = true;
                    } else if ((d.isArray() || d.isTipoTupla()) && ds == null) {
                        errores.add("Se ha intentado asignar '" + asig.valor + "' que no es una variable a '" + asig.id + "'");
                        indicarLocalizacion(asig.valor);
                        error = true;
                    }
                    tipoVariable = d.getTipo();
                }
                case ELEM_ARRAY -> {
                    if (!d.isArray()) {
                        errores.add("Se ha intentado acceder a un elemento de la variable " + asig.id + " que no es un array (es de tipo " + d.getTipo() + ")");
                        indicarLocalizacion(asig);
                        error = true;
                    }
                    SymbolDimensiones dim = asig.dim;
                    int ind = -2;
                    String tipoDesplazado = d.getTipo();
                    ArrayList<String> variablesIndice = new ArrayList<>();
                    for (int n = 1; dim != null; n++) {
                        if (ind != -1) {
                            ind = tipoDesplazado.lastIndexOf("[");
                        }
                        if (ind > -1) {
                            tipoDesplazado = tipoDesplazado.substring(0, ind).trim();
                        }
                        String tipoIdx = procesarOperando(dim.operando);
                        if (tipoIdx == null) {
                            errores.add("Se realizan operaciones no validas para el calculo del indice " + n + " del array " + asig.id);
                            indicarLocalizacion(dim.operando);
                            error = true;
                        } else if (!tipoIdx.equals(ParserSym.terminalNames[ParserSym.ENT])) {
                            errores.add("Las operaciones para el calculo del indice " + n + " del array " + asig.id + " resultan en " + tipoIdx + ", cuando tendria que ser un entero");
                            indicarLocalizacion(dim.operando);
                            error = true;
                        }
                        if (!error) {
//                            TipoVariable tv = TipoVariable.PUNTERO;
//                            String varIndice = g3d.nuevaVariable(tv);
//                            g3d.generarInstr(TipoInstr.COPY, new Operador(g3d.getTipoFromVar(varActual), varActual), null, new Operador(tv, varIndice));
                            variablesIndice.add(varActual);//varIndice);
                        }
                        dim = dim.siguienteDimension;
                    }
                    if (ind < 0) {
                        errores.add("Se ha intentado acceder a mas dimensiones de las definidas en el array " + asig.id);
                        indicarLocalizacion(dim);
                        error = true;
                    }
                    if (!error) {
                        tipoVariable = tipoDesplazado;
                        DescripcionArray da = (DescripcionArray) d;
                        ArrayList<String> variablesDimension = da.getVariablesDimension();
                        String varInd = variablesIndice.get(0);
                        for (int i = 0; i < variablesIndice.size() - 1; i++) {
                            //String varIndice = variablesIndice.get(i);
                            String varDesplazamiento = variablesDimension.get(i);
                            String varTemp = g3d.nuevaVariable(TipoVariable.INT);
                            g3d.generarInstr(TipoInstr.MUL, new Operador(TipoVariable.INT, varInd), new Operador(TipoVariable.INT, varDesplazamiento), new Operador(TipoVariable.INT, varTemp));
                            String varInd2 = variablesIndice.get(i + 1);
                            varInd = g3d.nuevaVariable(TipoVariable.INT);
                            g3d.generarInstr(TipoInstr.ADD, new Operador(TipoVariable.INT, varInd2), new Operador(TipoVariable.INT, varTemp), new Operador(TipoVariable.INT, varInd));
                        }
                        // no hace falta calcular b porque nuestros arrays empiezan en 0
//                        String indexByte = g3d.nuevaVariable(TipoReferencia.var);
//                        g3d.generarInstr(TipoInstr.SUB, new Operador(varInd), new Operador(da.getOffsetTempsCompilacio()), new Operador(indexByte));
                        indexReal = g3d.nuevaVariable(TipoVariable.INT);
                        TipoVariable t = TipoVariable.getTipo(da.tipoElementoDelArray, false);
                        g3d.generarInstr(TipoInstr.MUL, new Operador(TipoVariable.INT, varInd), new Operador(TipoVariable.INT, t.bytes), new Operador(TipoVariable.INT, indexReal));
                    }
                }
                case MIEMBRO -> {
                    if (!d.isTipoTupla()) {
                        errores.add("Se ha intentado acceder a un miembro de la variable " + asig.id + " que no es una tupla (es de tipo " + d.getTipo() + ")");
                        indicarLocalizacion(asig);
                        error = true;
                    } else {
                        DescripcionDefinicionTupla dt = (DescripcionDefinicionTupla) tablaSimbolos.consulta(d.getNombreTupla());
                        DefinicionMiembro miembro = d.getMember(asig.miembro);
                        if (miembro == null) {
                            errores.add("Se esta intentado acceder al miembro" + asig.miembro + " pero no existe");
                            indicarLocalizacion(asig);
                            error = true;
                        } else if (miembro.tieneValorAsignado() && miembro.isConst) {
                            errores.add("Se esta intentado asignar un valor al miembro constante '" + asig.miembro + "' de la variable '" + asig.id + "' de la tupla '" + d.getNombreTupla() + "', el cual ya tenia un valor asignado");
                            indicarLocalizacion(asig);
                            error = true;
                        }
                        Integer bytesDesplazamiento = dt.getDesplazamiento(asig.miembro);
                        if (bytesDesplazamiento == null) {
                            errores.add("El miembro " + asig.miembro + " no ha sido encontrado en la tupla " + d.getNombreTupla());
                            indicarLocalizacion(asig);
                            error = true;
                        } else if (!error) {
                            miembro.asignarValor();
                            tipoVariable = miembro.tipo;
                            indexReal = g3d.nuevaVariable(TipoVariable.INT);
                            g3d.generarInstr(TipoInstr.COPY, new Operador(TipoVariable.INT, bytesDesplazamiento), null, new Operador(TipoVariable.INT, indexReal));
                        }
                    }
                }
            }
            if (error) {
                continue;
            }
            if (!tipoValor.equals(tipoVariable)) {
                errores.add("Se esta intentado asignar un valor de tipo " + tipoValor + " a una variable de tipo " + tipoVariable);
                indicarLocalizacion(asig.valor);
                error = true;
            }
            // si operacion convierte a booleano y es un entero -> error
            if (!error && !asig.operacion.doesOperationResultInSameType(tipoValor)) {
                errores.add("Se esta intentado aplicar una operacion no valida entre los tipos " + tipoVariable + " y " + tipoValor);
                indicarLocalizacion(asig);
                error = true;
            }
            if (error) {
                continue;
            }
            if (!asig.operacion.isBasicAsig() && !d.tieneValorAsignado()) {
                errores.add("No se puede realizar asignacion compuesta si la variable '" + asig.id + "' no ha sido asignada de forma simple anteriormente");
                indicarLocalizacion(asig.operacion);
                continue;
            }
            if (d.isArray() || d.isTipoTupla()) {
                d.asignarValor();
            }
            TipoVariable t1 = g3d.getTipoFromVar(variableParaAsignar);
            TipoVariable t2 = g3d.getTipoFromVar(d.variableAsociada);
            if (asig.getTipo() != SymbolAsig.TIPO.ID) {
                g3d.generarInstr(TipoInstr.IND_ASS, new Operador(t1, variableParaAsignar), new Operador(TipoVariable.INT, indexReal), new Operador(t2, d.variableAsociada));
            } else {
                boolean isString = tipoValor.equals(ParserSym.terminalNames[ParserSym.CAR] + " []");
                DescripcionSimbolo ds = tablaSimbolos.consulta(asig.valor.toString());
                if (d.isArray() && !isString) {
                    ds = new DescripcionArray((DescripcionArray) ds);
                } else if (d.isTipoTupla()) {
                    ds = new DescripcionSimbolo(ds);
                }
                if ((!isString && d.isArray()) || d.isTipoTupla()) {
                    tablaSimbolos.sustituir(asig.valor.toString(), ds);
                }
                g3d.generarInstr(TipoInstr.COPY, new Operador(t1, variableParaAsignar), null, new Operador(t2, d.variableAsociada));
            }
            d.asignarValor();
        }
    }

    private void procesarLlamadaFuncion(SymbolFCall fcall) throws Exception {
        String nombre = (String) fcall.methodName.value;
        DescripcionSimbolo ds = tablaSimbolos.consulta(nombre);
        if (ds == null) {
            errores.add("La funcion " + nombre + " no ha sido declarada");
            indicarLocalizacion(fcall);
            return;
        } else if (!ds.isFunction()) {
            errores.add("El identificador " + nombre + " pertenece a una variable, no una funcion");
            indicarLocalizacion(fcall);
            return;
        }
        DescripcionFuncion df = (DescripcionFuncion) ds;
        if (df.isMain()) {
            errores.add("No se puede llamar al metodo inicial explicitamente, llamada producida en " + this.metodoActualmenteSiendoTratado.fst);
            indicarLocalizacion(fcall);
            return;
        }
        if (nombre.equals(this.metodoActualmenteSiendoTratado.fst)) {
            errores.add("No se puede llamar a un metodo (" + nombre + ") dentro de ese metodo");
            indicarLocalizacion(fcall);
            return;
        }
        ArrayList<Parametro> params = df.getParametros();
        SymbolOperandsLista opLista = fcall.operandsLista;
        int n = 0;
        boolean error = false;
        for (Parametro param : params) {
            n++;
            if (opLista == null) {
                errores.add("La funcion " + nombre + " tiene " + params.size() + " parametros, pero se han pasado mas al momento de llamarla");
                indicarLocalizacion(fcall);
                error = true;
                break;
            }
            SymbolOperand op = opLista.operand;
            String tipoOp = procesarOperando(op);
            String varOp = this.varActual;
            if (tipoOp == null) {
                errores.add("Se ha intentado pasar por parametros operaciones no validas en el parametro " + n + " de la funcion " + nombre);
                indicarLocalizacion(op);
                opLista = opLista.siguienteOperando;
                error = true;
                continue;
            } else if (!tipoOp.equals(param.tipo)) {
                errores.add("Se ha intentado pasar por parametro un operando de tipo " + tipoOp + " en el parametro " + n + " de la funcion " + nombre + " que es de tipo " + param.tipo);
                indicarLocalizacion(op);
                opLista = opLista.siguienteOperando;
                error = true;
                continue;
            }
            DescripcionSimbolo d = tablaSimbolos.consulta(param.id);
            if (d != null && !d.tieneValorAsignado()) {
                errores.add("No se puede pasar por parametro la variable " + param.id + " porque no ha sido inicializada");
                indicarLocalizacion(op);
                opLista = opLista.siguienteOperando;
                error = true;
                continue;
            }
            TipoVariable t = g3d.getTipoFromVar(varOp);
            g3d.generarInstr(TipoInstr.PARAM_S, null, null, new Operador(t, varOp));
            opLista = opLista.siguienteOperando;
        }
        if (opLista != null) {
            errores.add("La funcion " + nombre + " recibe " + params.size() + " parametros, pero se han pasado mas");
            indicarLocalizacion(fcall);
            error = true;
        }
        if (error) {
            return;
        }
        if (df.devuelveValor()) {
            TipoVariable t = TipoVariable.getTipo(df.tipo, df.tipo.contains("[") || df.tipo.startsWith(ParserSym.terminalNames[ParserSym.TUPLE]));
            String variableValorRetorno = g3d.nuevaVariable(t);
            g3d.generarInstr(TipoInstr.CALL, new Operador(t, variableValorRetorno), null, new Operador(df.variableAsociada));
            varActual = variableValorRetorno;
        } else {
            g3d.generarInstr(TipoInstr.CALL, null, null, new Operador(df.variableAsociada));
        }
    }

    private void procesarReturn(SymbolReturn ret) throws Exception {
        DescripcionFuncion df = metodoActualmenteSiendoTratado.snd;
        String tipo = df.getTipo();
        SymbolOperand op = ret.op;
        if (op == null) {
            if (tipo == null) {
                df.asignarReturn(tablaSimbolos.getProfundidad());
                g3d.generarInstr(TipoInstr.RETURN, null, null, new Operador(df.variableAsociada));
                return;
            }
            errores.add("Se ha realizado pop sin nada de la funcion " + metodoActualmenteSiendoTratado.fst + " que devuelve valores de tipo " + tipo);
            indicarLocalizacion(ret);
            return;
        }
        String tipoOp = procesarOperando(op);
        String varOp = this.varActual;
        if (tipoOp == null) {
            errores.add("Se ha intentado hacer pop de una operacion no valida en la funcion " + metodoActualmenteSiendoTratado.fst);
            indicarLocalizacion(ret);
            return;
        } else if (!tipoOp.equals(tipo)) {
            errores.add("Se ha intentado hacer pop de un tipo (" + tipoOp + ") diferente al que devuelve la funcion (" + tipo + ")");
            indicarLocalizacion(ret);
            return;
        }
        if (op.atomicExp != null) {
            DescripcionSimbolo d = tablaSimbolos.consulta(op.atomicExp.value.toString());
            if (d != null && !d.tieneValorAsignado()) {
                errores.add("No se puede hacer retorno de la variable " + op.atomicExp.value.toString() + " porque no ha sido inicializada");
                indicarLocalizacion(op);
                return;
            }
        }
        df.asignarReturn(tablaSimbolos.getProfundidad());
        TipoVariable t = g3d.getTipoFromVar(varOp);
        g3d.generarInstr(TipoInstr.RETURN, new Operador(t, varOp), null, new Operador(df.variableAsociada));
    }

    private void procesarSwap(SymbolSwap swap) throws Exception {
        boolean error = false;
        DescripcionSimbolo ds1 = tablaSimbolos.consulta(swap.op1);
        if (ds1 == null) {
            errores.add("La variable " + swap.op1 + " no ha sido declarada");
            indicarLocalizacion(swap);
            error = true;
        }
        DescripcionSimbolo ds2 = tablaSimbolos.consulta(swap.op2);
        if (ds2 == null) {
            errores.add("La variable " + swap.op2 + " no ha sido declarada");
            indicarLocalizacion(swap);
            error = true;
        }
        if (error) {
            return;
        }
        if (!ds1.getTipo().equals(ds2.getTipo())) {
            errores.add("La variable " + swap.op1 + " y la variable " + swap.op2 + " son de tipos diferentes (" + ds1.getTipo() + ", " + ds2.getTipo() + "), no se puede realizar el swap");
            indicarLocalizacion(swap);
            return;
        } else if (!ds1.tieneValorAsignado() || !ds2.tieneValorAsignado()) {
            errores.add("Se ha intentado hacer swap de las variables " + swap.op1 + " y " + swap.op2 + ", de las cuales al menos una no ha estado inicializada");
            indicarLocalizacion(swap);
            return;
        }
        TipoVariable t = g3d.getTipoFromVar(ds1.variableAsociada);
        String temp = g3d.nuevaVariable(t), varOp1 = ds1.variableAsociada, varOp2 = ds2.variableAsociada;
        g3d.generarInstr(TipoInstr.COPY, new Operador(t, varOp1), null, new Operador(t, temp));
        g3d.generarInstr(TipoInstr.COPY, new Operador(t, varOp2), null, new Operador(t, varOp1));
        g3d.generarInstr(TipoInstr.COPY, new Operador(t, temp), null, new Operador(t, varOp2));
    }

    private void procesarIf(SymbolIf cond) throws Exception {
        String tipoCond = procesarOperando(cond.cond);
        String varCond = this.varActual;
        if (tipoCond == null) {
            errores.add("La condicion del 'si' realiza una operacion no valida");
            indicarLocalizacion(cond.cond);
            return;
        } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.PROP])) {
            errores.add("La condicion del 'si' no resulta en una proposicion evualable como verdadera o falsa, sino en " + tipoCond);
            indicarLocalizacion(cond.cond);
            return;
        }
        if (cond.cond.atomicExp != null) {
            DescripcionSimbolo d = tablaSimbolos.consulta(cond.cond.atomicExp.value.toString());
            if (d != null && !d.tieneValorAsignado()) {
                errores.add("No se puede hacer comprobacion de la variable " + cond.cond.atomicExp.value.toString() + " porque no ha sido inicializada");
                indicarLocalizacion(cond.cond);
                return;
            }
        }
        //Creamos la etiqueta
        String efi = g3d.nuevaEtiqueta(); //Nueva etiqueta para el if
        String etSigCond = g3d.nuevaEtiqueta();
        //String eElse = g3d.nuevaEtiqueta();
        //Llegados aqui si la condicion es falsa salta, sino ejecuta
        TipoVariable t = g3d.getTipoFromVar(varCond);
        g3d.generarInstr(TipoInstr.IFEQ, new Operador(t, varCond), new Operador(TipoVariable.BOOL, TipoVariable.FALSE), new Operador(etSigCond)); //Si es falso saltaremos

        tablaSimbolos.entraBloque();
        procesarBody(cond.cuerpo);
        tablaSimbolos.salirBloque();
        //Si se cumplio la condicion no continuares revisando el resto
        g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(efi));
        g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etSigCond));

        for (SymbolElifs elifs = cond.elifs; elifs != null; elifs = elifs.siguienteElif) {
            SymbolElif elif = elifs.elif;
            tipoCond = procesarOperando(elif.cond);
            varCond = varActual;
            if (tipoCond == null) {
                errores.add("La condicion del 'sino' realiza una operacion no valida");
                indicarLocalizacion(elif.cond);
                return;
            } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.PROP])) {
                errores.add("La condicion del 'sino' no resulta en una proposicion evualable como verdadera o falsa");
                indicarLocalizacion(elif.cond);
                return;
            }
            if (elif.cond.atomicExp != null) {
                DescripcionSimbolo d = tablaSimbolos.consulta(elif.cond.atomicExp.value.toString());
                if (d != null && !d.tieneValorAsignado()) {
                    errores.add("No se puede hacer comprobacion de la variable " + elif.cond.atomicExp.value.toString() + " porque no ha sido inicializada");
                    indicarLocalizacion(elif.cond);
                    return;
                }
            }
            etSigCond = g3d.nuevaEtiqueta();
            t = g3d.getTipoFromVar(varCond);
            g3d.generarInstr(TipoInstr.IFEQ, new Operador(t, varCond), new Operador(TipoVariable.BOOL, TipoVariable.FALSE), new Operador(etSigCond)); //Si es falso saltaremos

            tablaSimbolos.entraBloque();
            procesarBody(elif.cuerpo);
            tablaSimbolos.salirBloque();
            g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(efi));
            g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etSigCond));
        }
        if (cond.els != null) {
            tablaSimbolos.entraBloque();
            procesarBody(cond.els.cuerpo);
            tablaSimbolos.salirBloque();
        }
        g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(efi));
    }

    private void procesarLoop(SymbolLoop loop) throws Exception {
        tablaSimbolos.entraBloque();
        SymbolLoopCond loopCond = loop.loopCond;
        if (loopCond.decs != null) {
            procesarDeclaraciones(loopCond.decs);
        }
        String inicioLoop = g3d.nuevaEtiqueta(), salirLoop = g3d.nuevaEtiqueta(), etContinue = g3d.nuevaEtiqueta();
        g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(inicioLoop));
        pilaEtiquetasBucle.push(new Pair(etContinue, salirLoop));
        if (loop.isDoWhile()) { // si es do while primero se ejecuta y luego se comprueba la condicion
            procesarBody(loop.cuerpo);
            g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etContinue)); //Etiqueta para continuar en el bucle
            if (loopCond.asig != null) {
                procesarAsignaciones(loopCond.asig);
            }
        }
        String tipoCond = procesarOperando(loopCond.cond);
        String varCond = this.varActual;
        if (tipoCond == null) {
            errores.add("La condicion del 'loop' realiza una operacion no valida");
            indicarLocalizacion(loopCond.cond);
            tablaSimbolos.salirBloque();
            return;
        } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.PROP])) {
            errores.add("La condicion del 'loop' no resulta en una proposicion evualable como verdadera o falsa");
            indicarLocalizacion(loopCond.cond);
            tablaSimbolos.salirBloque();
            return;
        }
        if (loopCond.cond.atomicExp != null) {
            DescripcionSimbolo d = tablaSimbolos.consulta(loopCond.cond.atomicExp.value.toString());
            if (d != null && !d.tieneValorAsignado()) {
                errores.add("No se puede hacer comprobacion de la variable " + loopCond.cond.atomicExp.value.toString() + " porque no ha sido inicializada");
                indicarLocalizacion(loopCond.cond);
                return;
            }
        }
        //En el caso de no cumplirse la condicion saltaremos al fin del bucle
        if (!loop.isDoWhile()) { // si es while do primero se ha comprobado las condiciones y luego se ejecuta el cuerpo
            TipoVariable t = g3d.getTipoFromVar(varCond);
            g3d.generarInstr(TipoInstr.IFEQ, new Operador(t, varCond), new Operador(TipoVariable.BOOL, TipoVariable.FALSE), new Operador(salirLoop));
            procesarBody(loop.cuerpo);
            g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etContinue)); //Etiqueta para continuar en el bucle
            if (loopCond.asig != null) {
                procesarAsignaciones(loopCond.asig);
            }
        }
        if (loop.isDoWhile()) {
            TipoVariable t = g3d.getTipoFromVar(varCond);
            g3d.generarInstr(TipoInstr.IFEQ, new Operador(t, varCond), new Operador(TipoVariable.BOOL, TipoVariable.FALSE), new Operador(salirLoop));
        }
        tablaSimbolos.salirBloque();
        pilaEtiquetasBucle.pop();

        g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(inicioLoop)); //Saltaremos al mismo bucle
        g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(salirLoop)); //Etiqueta para fin de bucle
    }

    private void procesarSwitch(SymbolSwitch sw) throws Exception {
        String efi = g3d.nuevaEtiqueta();
        pilaEtiquetasSwitch.push(efi);
        String tipo1 = procesarOperando(sw.cond);
        String varCondSwitch = this.varActual;
        if (tipo1 == null) {
            errores.add("La operacion del 'select' realiza una operacion no valida");
            indicarLocalizacion(sw.cond);
            return;
        }
        if (sw.cond.atomicExp != null) {
            DescripcionSimbolo d = tablaSimbolos.consulta(sw.cond.atomicExp.value.toString());
            if (d != null && !d.tieneValorAsignado()) {
                errores.add("No se puede hacer comprobacion de la variable " + sw.cond.atomicExp.value.toString() + " porque no ha sido inicializada");
                indicarLocalizacion(sw.cond);
                return;
            }
        }
        int n = 0;
        TipoVariable t1 = g3d.getTipoFromVar(varCondSwitch);
        ArrayList<SymbolBody> cuerposCasos = new ArrayList<>();
        ArrayList<String> etiquetasCasos = new ArrayList<>();
        ArrayList<String> variablesCasos = new ArrayList<>();
        String etCondiciones = g3d.nuevaEtiqueta();
        g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(etCondiciones));
        for (SymbolCaso caso = sw.caso; caso != null; caso = caso.siguienteCaso) {
            n++;
            String tipo2 = procesarOperando(caso.cond);
            String varCondCaso = this.varActual;
            String etCondActual = g3d.nuevaEtiqueta();
            if (tipo2 == null) {
                errores.add("La operacion del 'caso' realiza una operacion no valida");
                indicarLocalizacion(caso.cond);
                return;
            } else if (tipo1 != null && !tipo1.equals(tipo2)) {
                errores.add("Los tipos de la condicion (" + tipo1 + ") del select y del caso " + n + " (" + tipo2 + ") no coinciden");
                indicarLocalizacion(caso.cond);
                return;
            }
            if (caso.cond.atomicExp != null) {
                DescripcionSimbolo d = tablaSimbolos.consulta(caso.cond.atomicExp.value.toString());
                if (d != null && !d.tieneValorAsignado()) {
                    errores.add("No se puede hacer comprobacion de la variable " + caso.cond.atomicExp.value.toString() + " porque no ha sido inicializada");
                    indicarLocalizacion(caso.cond);
                    return;
                }
            }
            cuerposCasos.add(caso.cuerpo);
            etiquetasCasos.add(etCondActual);
            variablesCasos.add(varCondCaso);
        }
        for (int i = etiquetasCasos.size() - 1; i >= 0; i--) {
            SymbolBody caso = cuerposCasos.get(i);
            String etCondActual = etiquetasCasos.get(i);
            g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etCondActual));
            tablaSimbolos.entraBloque();
            procesarBody(caso);
            tablaSimbolos.salirBloque();

        }
        if (sw.pred != null) {
            tablaSimbolos.entraBloque();
            procesarBody(sw.pred.cuerpo);
            tablaSimbolos.salirBloque();
        }
        g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(efi));
        g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etCondiciones));
        for (int i = etiquetasCasos.size() - 1; i >= 0; i--) {
            String etCondActual = etiquetasCasos.get(i);
            String varCondCaso = variablesCasos.get(i);
            g3d.generarInstr(TipoInstr.IFEQ, new Operador(t1, varCondSwitch), new Operador(g3d.getTipoFromVar(varCondCaso), varCondCaso), new Operador(etCondActual));
        }
        g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(efi));
        pilaEtiquetasSwitch.pop();
    }

    private void procesarMetodo(SymbolScriptElemento metodo) throws Exception {
        //Añadimos el metodo

        tablaSimbolos.entraBloque();
        DescripcionFuncion df = (DescripcionFuncion) tablaSimbolos.consulta(metodo.id);
        //        g3d.añadirFuncion(metodo.id);
        //String efi = g3d.nuevaEtiqueta(metodo.id); //Creamos la etiqueta de la funcion
        //Añadimos la funcion a la tabla
        int numeroProcedure = g3d.nuevoProcedimiento(metodo.id, df.variableAsociada, df.getParametros(), df.getBytes());
        g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(df.variableAsociada));
        g3d.generarInstr(TipoInstr.PMB, null, null, new Operador(df.variableAsociada));
        metodoActualmenteSiendoTratado = new Pair(metodo.id, df);
        for (Parametro p : df.getParametros()) {
            DescripcionDefinicionTupla tupla = null;
            String id = p.id;
            String tipo = p.tipo;
            if (tipo.startsWith(ParserSym.terminalNames[ParserSym.TUPLE])) {
                int from = tipo.indexOf(" ") + 1;
                int to = tipo.length();
                if (tipo.contains("[")) {
                    to = tipo.indexOf("[");
                }
                String idTupla = tipo.substring(from, to);
                tupla = (DescripcionDefinicionTupla) tablaSimbolos.consulta(idTupla);
            }
            try {
                DescripcionSimbolo ds;
                if (tipo.contains("[")) {
                    int numDim = tipo.length() - tipo.replace("[", "").length();
                    ArrayList<String> variablesDimension = new ArrayList<>(numDim);
                    for (int i = 0; i < numDim; i++) {
                        variablesDimension.add(null);
                    }
                    ds = new DescripcionArray(tipo, false, variablesDimension.size(), true, tupla, variablesDimension, p.variable);
                } else {
                    ds = new DescripcionSimbolo(tipo, false, true, tupla, p.variable);
                }

                tablaSimbolos.poner(id, ds);
            } catch (Exception ex) {
                errores.add(ex.getMessage());
            }
        }

        procesarBody(metodo.cuerpo);
//        g3d.eliminarFuncion();

        if (df.necesitaReturnStatement() && !df.tieneReturnStatement()) {
            errores.add("No se ha puesto una instruccion para salir del metodo " + metodo.id + " en el mismo ambito de su cuerpo (si x es el nivel donde esta declarado el metodo, x + 1 es el nivel donde debe de estar la instruccion para salir de el)");
            indicarLocalizacion(metodo);
            tablaSimbolos.salirBloque();
            return;
        } else if (!df.tieneReturnStatement()) { // es de tipo void y no le han puesto return al final
            g3d.generarInstr(TipoInstr.RETURN, null, null, new Operador(df.variableAsociada));
        }
        tablaSimbolos.salirBloque();
        g3d.generarInstr(TipoInstr.SEPARADOR, null, null, null);
    }

    private ArrayList<Parametro> procesarParametros(SymbolParams params, String idMetodo) throws Exception {
        ArrayList<Parametro> parametros = new ArrayList<>();
        if (params == null) {
            return parametros;
        }
        SymbolParamsLista param = params.paramsLista;
        while (param != null) {
            boolean error = false;
            String nombreParam = param.idParam;
            //if (tablaSimbolos.contains(id)) { // imposible
            if (nombreParam.equals(idMetodo)) {
                error = true;
                errores.add("El parametro " + nombreParam + " tiene el mismo nombre que el metodo en el que esta siendo declarado");
                indicarLocalizacion(param);
            }
            String errMsg = tablaSimbolos.sePuedeDeclarar(nombreParam);
            if (!errMsg.isEmpty()) {
                error = true;
                errores.add(errMsg);
                indicarLocalizacion(param);
            }
            if (!error) {
                String tipo = param.tipoParam.getTipo();
                TipoVariable t = TipoVariable.getTipo(tipo, tipo.contains("[") || tipo.startsWith(ParserSym.terminalNames[ParserSym.TUPLE]));
                String variable = g3d.nuevaVariable(nombreParam, t);
                //parametros.add(new Pair<>(nombreParam, param.tipoParam.getTipo()));
                Parametro p = new Parametro(nombreParam, variable, tipo);
                parametros.add(p);
            }
            param = param.siguienteParam;
        }
        return parametros;
    }

    private void procesarDeclaracionTupla(SymbolScriptElemento tupla) throws Exception {
        SymbolMiembrosTupla miembros = tupla.miembrosTupla;
        while (miembros != null) {
            procesarDeclaraciones(miembros.decs);
            miembros = miembros.siguienteDeclaracion;
        }
    }

    private void procesarContinue(SymbolInstr instr) throws Exception { // ------------------------------------------------------------------------------------------------------------
        if (pilaEtiquetasBucle.isEmpty()) {
            errores.add("Se ha intentado continuar en un bucle en un ambito sin bucles");
            indicarLocalizacion(instr);
            return;
        }
        Pair<String, String> p = pilaEtiquetasBucle.peek();
        String etini = p.fst;
        g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(etini));
    }

    private void procesarBreak(SymbolInstr instr) throws Exception {
        // si esta en un switch sale de el
        if (!pilaEtiquetasSwitch.isEmpty()) {
            String etfi = pilaEtiquetasSwitch.peek();
            g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(etfi));
            return;
        }
        // si no esta en un switch comprueba que este en un bucle
        if (!pilaEtiquetasBucle.isEmpty()) {
            Pair<String, String> p = pilaEtiquetasBucle.peek();
            String etfi = p.snd;
            g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(etfi));
            return;
        }
        errores.add("Se ha intentado salir de un bucle o un select en un ambito sin bucles ni selects");
        indicarLocalizacion(instr);
    }

    private void procesarMain(SymbolMain main) throws Exception {
        SymbolBody body = main.main;
        tablaSimbolos.entraBloque();
        DescripcionFuncion df = (DescripcionFuncion) tablaSimbolos.consulta(main.nombreMain);
        metodoActualmenteSiendoTratado = new Pair(main.nombreMain, df);

//        int numeroProcedure = g3d.nuevoProcedimiento(main.nombreMain, metodoActualmenteSiendoTratado.snd.nivel, etiquetaMain);
        g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(df.variableAsociada));
        g3d.generarInstr(TipoInstr.PMB, null, null, new Operador(df.variableAsociada));

        procesarBody(body);
        tablaSimbolos.salirBloque();
        g3d.generarInstr(TipoInstr.RETURN, null, null, new Operador(df.variableAsociada));
        g3d.generarInstr(TipoInstr.SEPARADOR, null, null, null);
        g3d.nuevoProcedimientoMain(main.nombreMain, df.variableAsociada, new ArrayList<>(), 0);
        g3d.generarInstr(TipoInstr.SEPARADOR, null, null, null);
    }

    /**
     * Devuelve el tipo, array o tupla al que pertenece el operando. Null si no
     * se respetan los tipos.
     *
     * @param op
     * @return
     */
    private String procesarOperando(SymbolOperand op) throws Exception {
        switch (op.getTipo()) {
            case ATOMIC_EXPRESSION -> {
                SymbolAtomicExpression literal = op.atomicExp;
                String tipo = literal.tipo;

                if (!tipo.equals(ParserSym.terminalNames[ParserSym.ID])) {
                    if (tipo.equals(ParserSym.terminalNames[ParserSym.ENT])
                            && ((Integer) literal.value > SymbolAtomicExpression.LIMIT_SUP || (Integer) literal.value < SymbolAtomicExpression.LIMIT_INF)) {
                        errores.add("Se ha utilizado un valor " + literal.value.toString() + " fuera del rango " + SymbolAtomicExpression.LIMIT_INF + "..." + SymbolAtomicExpression.LIMIT_SUP);
                        indicarLocalizacion(literal);
                        return null;
                    }
                    //Copiamos el valor x -> A
                    if (tuplaActualmenteSiendoTratada == null) {
                        TipoVariable t = TipoVariable.getTipo(tipo, false);
                        String variable = g3d.nuevaVariable(t);
                        g3d.anyadirBytesEstructura(variable, TipoVariable.STRING.bytes);
                        Object valor = literal.getValorCodigoIntermedio();
                        g3d.generarInstr(TipoInstr.COPY, new Operador(t, valor), null, new Operador(t, variable));
                        varActual = variable;

                    }
                    return tipo; // si no es ID
                }
                String nombreID = (String) literal.value;
                DescripcionSimbolo ds = tablaSimbolos.consulta(nombreID);
                if (ds == null) {
                    errores.add("No se ha declarado con anterioridad la variable " + nombreID);
                    indicarLocalizacion(literal);
                    return null;
                }
                varActual = ds.variableAsociada;
                return ds.getTipo();
            }
            case FCALL -> {
                SymbolFCall fcall = op.fcall;
                int nErrores = errores.size();
                procesarLlamadaFuncion(fcall);
                // variableTratadaActualmente modificada en procesarLlamadaFuncion
                if (nErrores != errores.size()) { // si han habido errores
                    return null;
                }
                String nombre = (String) fcall.methodName.value;
                DescripcionFuncion df = (DescripcionFuncion) tablaSimbolos.consulta(nombre);
                if (!df.devuelveValor()) {
                    errores.add("No se puede operar llamando a una funcion que no devuelve valor: " + nombre);
                    indicarLocalizacion(fcall);
                    return null;
                }
//                String variable = g3d.nuevaVariable();
//                g3d.generarInstr(TipoInstr.CALL, new Operador(variable), null, new Operador(nombre));
//                variableTratadaActualmente = variable;
                return df.getTipo();
            }
            case OP_BETWEEN_PAREN -> {
                return procesarOperando(op.op);
            }
            case UNARY_EXPRESSION -> {
                SymbolUnaryExpression exp = op.unaryExp;
                String tipo = procesarOperando(exp.op);
                String variableOperando = varActual;
                if (tipo == null) {
                    errores.add("Se realizan operaciones no validas en " + exp.op);
                    indicarLocalizacion(exp.op);
                    return null;
                }
                if (exp.op.atomicExp != null) {
                    DescripcionSimbolo d = tablaSimbolos.consulta(exp.op.atomicExp.value.toString());
                    if (d != null && !d.tieneValorAsignado()) {
                        errores.add("No se puede hacer operaciones con la variable " + exp.op.atomicExp.value.toString() + " porque no ha sido inicializada");
                        indicarLocalizacion(exp.op);
                        return null;
                    }
                }
                TipoVariable t = g3d.getTipoFromVar(variableOperando);
                int operation;
                if (exp.isLeftUnaryOperator()) {
                    SymbolLUnaryOperator operator = exp.leftOp;
                    operation = operator.unaryOperator;
                } else {
                    SymbolRUnaryOperator operator = exp.rightOp;
                    operation = operator.unaryOperator;
                }
                if (ParserSym.OP_INC == operation || ParserSym.OP_DEC == operation) {
                    boolean isDecremento = ParserSym.OP_DEC == operation;
                    String s = "incremento";
                    if (isDecremento) {
                        s = "decremento";
                    }
                    SymbolAtomicExpression symbolId = exp.op.atomicExp;
                    if (symbolId == null) {
                        errores.add("No se puede realizar un " + s + " sobre un operando diferente a una variable primitiva (" + exp + ")");
                        indicarLocalizacion(exp);
                        return null;
                    }
                    if (!symbolId.tipo.equals(ParserSym.terminalNames[ParserSym.ID])) {
                        errores.add("No se puede realizar un " + s + " sobre un operando que no sea un identificador (" + exp + ")");
                        indicarLocalizacion(exp);
                        return null;
                    }
                    String nombre = symbolId.toString();
                    DescripcionSimbolo ds = tablaSimbolos.consulta(nombre);
                    if (ds == null) {
                        errores.add("No se puede realizar un " + s + " sobre la variable " + nombre + " no declarada anteriormente");
                        indicarLocalizacion(exp);
                        return null;
                    } else if (!SymbolTipoPrimitivo.isTipoNumericoDiscreto(ds.tipo)) {
                        errores.add("No se puede realizar un " + s + " sobre una variable de tipo " + ds.tipo);
                        indicarLocalizacion(exp);
                        return null;
                    } else if (!ds.tieneValorAsignado()) {
                        errores.add("No se puede realizar un " + s + " si la variable " + nombre + " no ha sido asignada anteriormente");
                        indicarLocalizacion(exp);
                        return null;
                    } else if (ds.isConstante()) {
                        errores.add("No se puede realizar un " + s + " en una constante (" + nombre + ")");
                        indicarLocalizacion(exp);
                        return null;
                    }
                    String nuevaVariable = g3d.nuevaVariable(t);
                    if (exp.isLeftUnaryOperator()) { // pre
                        g3d.generarInstr(isDecremento ? TipoInstr.SUB : TipoInstr.ADD, new Operador(t, variableOperando), new Operador(TipoVariable.INT, 1), new Operador(t, nuevaVariable));
                        g3d.generarInstr(TipoInstr.COPY, new Operador(t, nuevaVariable), null, new Operador(t, variableOperando));
                        varActual = variableOperando;
                    } else { // post
                        String varAntiguoValor = g3d.nuevaVariable(t);
                        g3d.generarInstr(isDecremento ? TipoInstr.SUB : TipoInstr.ADD, new Operador(t, variableOperando), new Operador(TipoVariable.INT, 1), new Operador(t, nuevaVariable));
                        g3d.generarInstr(TipoInstr.COPY, new Operador(t, variableOperando), null, new Operador(t, varAntiguoValor));
                        g3d.generarInstr(TipoInstr.COPY, new Operador(t, nuevaVariable), null, new Operador(t, variableOperando));
                        varActual = varAntiguoValor;
                    }
                    return tipo;
                }
                // operador zurdo
                if (exp.isLeftUnaryOperator()) {
                    if (!tipo.equals(ParserSym.terminalNames[ParserSym.PROP]) && !tipo.equals(ParserSym.terminalNames[ParserSym.ENT])) {// && !tipo.equals(ParserSym.terminalNames[ParserSym.REAL])) {
                        errores.add("Se ha intentado realizar una operacion " + exp + " no valida sobre un " + tipo); // error, no se puede operar si no es int ni bool
                        indicarLocalizacion(exp);
                        return null;
                    } else if (tipo.equals(ParserSym.terminalNames[ParserSym.PROP]) && ParserSym.OP_NOT != operation) {
                        errores.add("Se ha intentado realizar una operacion " + exp + " no valida sobre un " + tipo); // no se puede operar booleano con inc/dec
                        indicarLocalizacion(exp);
                        return null;
//                    } else if (tipo.equals(ParserSym.terminalNames[ParserSym.REAL]) && (ParserSym.OP_ADD != operation && ParserSym.OP_SUB != operation)) {
//                        errores.add("Se ha intentado realizar una operacion " + exp + " no valida sobre un " + tipo); // no se puede operar booleano con signo +/-
//                        indicarLocalizacion(exp);
//                        return null;
                    } else if (tipo.equals(ParserSym.terminalNames[ParserSym.ENT]) && (ParserSym.OP_ADD != operation && ParserSym.OP_SUB != operation)) {
                        errores.add("Se ha intentado realizar una operacion " + exp + " no valida sobre un " + tipo); // no se puede operar entero con not
                        indicarLocalizacion(exp);
                        return null;
                    }
                    if (operation == ParserSym.OP_ADD) {
                        return tipo;
                    }
                    // si llega aqui es porque cumple algunas de las siguientes: NOT boolean, +num o -num
                    String variable = g3d.nuevaVariable(t);
                    if (tipo.equals(ParserSym.terminalNames[ParserSym.PROP]) && ParserSym.OP_NOT == operation) {
                        g3d.generarInstr(TipoInstr.NOT, new Operador(t, variableOperando), null, new Operador(t, variable));
                    } else if (operation == ParserSym.OP_SUB) {
                        g3d.generarInstr(TipoInstr.NEG, new Operador(t, variableOperando), null, new Operador(t, variable));
                    }
//                    else if (operation != ParserSym.OP_ADD) { // por si acaso
//                        errores.add("Se ha intentado realizar una operacion " + exp + " no valida sobre un " + tipo);
//                        indicarLocalizacion(exp);
//                        return null;
//                    }
                    this.varActual = variable;
                    return tipo;
                }
                // operador diestro
//                if (!tipo.equals(ParserSym.terminalNames[ParserSym.REAL]) && !tipo.equals(ParserSym.terminalNames[ParserSym.ENT])) {
//                    errores.add("Se ha intentado realizar una operacion " + exp + " no valida sobre un " + tipo); // error, no se puede operar si no es int ni double
//                    indicarLocalizacion(exp);
//                    return null;
//                } else if (tipo.equals(ParserSym.terminalNames[ParserSym.REAL]) && ParserSym.OP_PCT != operation) {
//                    errores.add("Se ha intentado realizar una operacion " + exp + " no valida sobre un " + tipo); // no se puede operar double con inc/dec
//                    indicarLocalizacion(exp);
//                    return null;
//                } else
                if (tipo.equals(ParserSym.terminalNames[ParserSym.ENT])) { //|| tipo.equals(ParserSym.terminalNames[ParserSym.REAL])) {
                    String nuevaVariable = g3d.nuevaVariable(t);
                    g3d.generarInstr(TipoInstr.DIV, new Operador(t, variableOperando), new Operador(TipoVariable.INT, 100), new Operador(t, nuevaVariable));
                    this.varActual = nuevaVariable;
                    return ParserSym.terminalNames[ParserSym.ENT];//.REAL]; // !!! se puede operar entero con OP_PCT
                }
                return tipo; // pendiente ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            }
            case BINARY_EXPRESSION -> {
                SymbolBinaryExpression exp = op.binaryExp;
                String tipo1 = procesarOperando(exp.op1);
                String variableOp1 = varActual;
                boolean error = false;
                if (tipo1 == null) {
                    errores.add("El primer operando de la expresion binaria realiza una operacion " + exp.op1 + " no valida");
                    indicarLocalizacion(exp);
                    error = true;
                }
                String tipo2 = procesarOperando(exp.op2);
                String variableOp2 = varActual;
                if (tipo2 == null) {
                    errores.add("El segundo operando de la expresion binaria realiza una operacion " + exp.op2 + " no valida");
                    indicarLocalizacion(exp);
                    error = true;
                }
                if (error) {
                    return null;
                }
                if (exp.op1.atomicExp != null) {
                    DescripcionSimbolo d = tablaSimbolos.consulta(exp.op1.atomicExp.value.toString());
                    if (d != null && !d.tieneValorAsignado()) {
                        errores.add("No se puede hacer operaciones con la variable " + exp.op1.atomicExp.value.toString() + " porque no ha sido inicializada");
                        indicarLocalizacion(exp.op1);
                        return null;
                    }
                }
                if (exp.op2.atomicExp != null) {
                    DescripcionSimbolo d = tablaSimbolos.consulta(exp.op2.atomicExp.value.toString());
                    if (d != null && !d.tieneValorAsignado()) {
                        errores.add("No se puede hacer operaciones con la variable " + exp.op2.atomicExp.value.toString() + " porque no ha sido inicializada");
                        indicarLocalizacion(exp.op2);
                        return null;
                    }
                }
                TipoVariable t1 = g3d.getTipoFromVar(variableOp1);
                TipoVariable t2 = g3d.getTipoFromVar(variableOp2);
                SymbolBinaryOperator operator = exp.bop;
                boolean concatenarStringConString = tipo1.equals(ParserSym.terminalNames[ParserSym.CAR] + " []") && tipo2.equals(ParserSym.terminalNames[ParserSym.CAR] + " []");
                if (operator.isAdd() && (concatenarStringConString)) { //|| concatenarStringConCaracter || concatenarCaracterConString)) {
                    errores.add("Se ha intentado concatenar dos strings en la expresion binaria " + exp + ", cuya operacion no esta permitida");
                    indicarLocalizacion(exp);// error, no se puede operar con tipos diferentes (excepto int y double)
                    return null;
//                    String var = g3d.nuevaVariable(TipoVariable.STRING);
//                    g3d.generarInstr(TipoInstr.CONCAT, new Operador(t1, variableOp1), new Operador(t2, variableOp2), new Operador(TipoVariable.STRING, var));
//                    varActual = var;
//                    return ParserSym.terminalNames[ParserSym.CAR] + " []";
                }
//                boolean unoIntOtroDouble = (tipo1.equals(ParserSym.terminalNames[ParserSym.REAL]) && tipo2.equals(ParserSym.terminalNames[ParserSym.ENT]))
//                        || (tipo2.equals(ParserSym.terminalNames[ParserSym.REAL]) && tipo1.equals(ParserSym.terminalNames[ParserSym.ENT]));
                if (!tipo1.equals(tipo2)) {// && !unoIntOtroDouble) {
                    errores.add("Se ha intentado realizar una operacion ilegal " + exp.bop.value + " entre " + tipo1 + " y " + tipo2 + " en la expresion binaria " + exp);
                    indicarLocalizacion(exp);// error, no se puede operar con tipos diferentes (excepto int y double)
                    error = true;
                } else if (!SymbolTipoPrimitivo.isTipoPrimitivo(tipo1) && !tipo1.contains("[")) {
                    errores.add("Se ha intentado realizar una operacion ilegal " + exp.bop + " entre tipos no primitivos (" + tipo1 + ") en la expresion binaria " + exp);
                    indicarLocalizacion(exp);// error, no se puede operar con tuplas y arrays
                    error = true;
                }
                if (error) {
                    return null;
                }
                TipoInstr ti = operator.getTipoInstruccion();
                // si llega aqui es porque ambos son del mismo tipo o uno es int y otro double
                String tipoOperandos = tipo1, varOperandos = variableOp1; // si uno es int y otro double se considera que los dos son double
//                if (unoIntOtroDouble) {
//                    if (!operator.isForOperandsOfType(ParserSym.terminalNames[ParserSym.REAL])) {
//                        errores.add("Se ha intentado realizar una operacion ilegal " + exp.bop + " para los tipos " + tipo1 + " y " + tipo2 + " en la expresion binaria " + exp);
//                        indicarLocalizacion(exp);
//                        return null; // error, operandos no pueden operar con operador
//                    }
//                    tipoOperandos = ParserSym.terminalNames[ParserSym.REAL];
//                    if (!tipo1.equals(ParserSym.terminalNames[ParserSym.REAL])) {
//                        varOperandos = variableOp2;
//                    }
//                } else 
                if (!operator.isForOperandsOfType(tipo1)) {
                    errores.add("Se ha intentado realizar una operacion ilegal " + exp.bop + " para los tipos " + tipo1 + " y " + tipo2 + " en la expresion binaria " + exp);
                    indicarLocalizacion(exp);
                    return null; // error, operandos no pueden operar con operador
                }
                TipoVariable t = g3d.getTipoFromVar(varOperandos);
                if (operator.isPotencia()) {
                    String etInit = g3d.nuevaEtiqueta();
                    String etLoop = g3d.nuevaEtiqueta();
                    String etFi = g3d.nuevaEtiqueta();
                    String varRes = g3d.nuevaVariable(t);
                    String varDecrementadora = g3d.nuevaVariable(t2);
                    g3d.generarInstr(TipoInstr.IFGT, new Operador(t2, variableOp2), new Operador(TipoVariable.INT, 0), new Operador(etInit));
                    g3d.generarInstr(TipoInstr.COPY, new Operador(TipoVariable.INT, 1), null, new Operador(t, varRes));
                    g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(etFi));
                    g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etInit));
                    g3d.generarInstr(TipoInstr.COPY, new Operador(t1, variableOp1), null, new Operador(t, varRes));
                    g3d.generarInstr(TipoInstr.COPY, new Operador(t2, variableOp2), null, new Operador(t2, varDecrementadora));
                    g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etLoop));
                    g3d.generarInstr(TipoInstr.IFEQ, new Operador(TipoVariable.INT, 1), new Operador(t2, varDecrementadora), new Operador(etFi));
                    String varAux = g3d.nuevaVariable(t);
                    g3d.generarInstr(TipoInstr.MUL, new Operador(t1, variableOp1), new Operador(t, varRes), new Operador(t, varAux));
                    g3d.generarInstr(TipoInstr.COPY, new Operador(t, varAux), null, new Operador(t, varRes));
                    g3d.generarInstr(TipoInstr.SUB, new Operador(t2, varDecrementadora), new Operador(TipoVariable.INT, 1), new Operador(t, varAux));
                    g3d.generarInstr(TipoInstr.COPY, new Operador(t, varAux), null, new Operador(t2, varDecrementadora));
                    g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(etLoop));
                    g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etFi));
                    varActual = varRes;
                    return tipoOperandos;
                }
//                if(ti.isTipo(TipoInstr.AND) || ti.isTipo(TipoInstr.OR)) {
//                    String var = g3d.nuevaVariable(TipoReferencia.var);
//                    g3d.generarInstr(ti, new Operador(variableOp1), new Operador(variableOp2), new Operador(var));
//                    variableTratadaActualmente = var;
//                    return ParserSym.terminalNames[ParserSym.PROP];
//                }
                if (operator.isRelationalOperator()) {
                    String etTrue = g3d.nuevaEtiqueta();
                    String etFalse = g3d.nuevaEtiqueta();
                    String varBool = g3d.nuevaVariable(TipoVariable.BOOL);
                    g3d.generarInstr(ti, new Operador(t1, variableOp1), new Operador(t2, variableOp2), new Operador(etTrue));
                    g3d.generarInstr(TipoInstr.COPY, new Operador(TipoVariable.BOOL, TipoVariable.FALSE), null, new Operador(TipoVariable.BOOL, varBool));
                    g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(etFalse));
                    g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etTrue));
                    g3d.generarInstr(TipoInstr.COPY, new Operador(TipoVariable.BOOL, TipoVariable.TRUE), null, new Operador(TipoVariable.BOOL, varBool));
                    g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etFalse));
                    varActual = varBool;
                    return ParserSym.terminalNames[ParserSym.PROP];
                }
                String var = g3d.nuevaVariable(t);
                g3d.generarInstr(ti, new Operador(t1, variableOp1), new Operador(t2, variableOp2), new Operador(t, var));
                varActual = var;
                return tipoOperandos; // si no resulta en booleano, resulta en el mismo tipo
            }
            case CONDITIONAL_EXPRESSION -> {
                SymbolConditionalExpression exp = op.conditionalExp;
                String tipoCond = procesarOperando(exp.cond);
                String varCond = varActual;
                boolean error = false;
                if (tipoCond == null) {
                    errores.add("La condicion " + exp.cond + " de la expresion ternaria " + exp + " realiza una operacion no valida");
                    indicarLocalizacion(exp.cond);
                    error = true;
                } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.PROP])) {
                    errores.add("La condicion " + exp.cond + " de la expresion ternaria " + exp + " no resulta en una proposicion evualable como verdadera o falsa, sino en " + tipoCond);
                    indicarLocalizacion(exp.cond); // error, no se puede utilizar de condicion algo que no sea una proposicion
                    error = true;
                }
                if (exp.cond.atomicExp != null) {
                    DescripcionSimbolo d = tablaSimbolos.consulta(exp.cond.atomicExp.value.toString());
                    if (d != null && !d.tieneValorAsignado()) {
                        errores.add("No se puede hacer comprobar la variable " + exp.cond.atomicExp.value.toString() + " porque no ha sido inicializada");
                        indicarLocalizacion(exp.cond);
                        return null;
                    }
                }
                String tipo1 = procesarOperando(exp.caseTrue);
                String varTrue = varActual;
                if (tipo1 == null) {
                    errores.add("El operando a asignar en caso positivo " + exp.caseTrue + " de la expresion ternaria " + exp + " realiza operaciones no validas");
                    indicarLocalizacion(exp.caseTrue);
                    error = true;
                }
                String tipo2 = procesarOperando(exp.caseFalse);
                String varFalse = varActual;
                if (tipo2 == null) {
                    errores.add("El operando a asignar en caso negativo " + exp.caseFalse + " de la expresion ternaria " + exp + " realiza operaciones no validas");
                    indicarLocalizacion(exp.caseFalse);
                    error = true;
                }
                if (error) {
                    return null;
                }
                if (exp.caseTrue.atomicExp != null) {
                    DescripcionSimbolo d = tablaSimbolos.consulta(exp.caseTrue.atomicExp.value.toString());
                    if (d != null && !d.tieneValorAsignado()) {
                        errores.add("No se puede hacer comprobar la variable " + exp.caseTrue.atomicExp.value.toString() + " porque no ha sido inicializada");
                        indicarLocalizacion(exp.caseTrue);
                        return null;
                    }
                }
                if (exp.caseFalse.atomicExp != null) {
                    DescripcionSimbolo d = tablaSimbolos.consulta(exp.caseFalse.atomicExp.value.toString());
                    if (d != null && !d.tieneValorAsignado()) {
                        errores.add("No se puede hacer comprobar la variable " + exp.caseFalse.atomicExp.value.toString() + " porque no ha sido inicializada");
                        indicarLocalizacion(exp.caseFalse);
                        return null;
                    }
                }
                if (!tipo1.equals(tipo2)) {
                    errores.add("No se puede operar con tipos diferentes (" + tipo1 + ", " + tipo2 + ") en la operacion ternaria " + exp);
                    indicarLocalizacion(exp);
                    return null;
                }
                String etTrue = g3d.nuevaEtiqueta();
                String etFalse = g3d.nuevaEtiqueta();
                String varBool = g3d.nuevaVariable(TipoVariable.BOOL);
                g3d.generarInstr(TipoInstr.IFEQ, new Operador(TipoVariable.BOOL, varCond), new Operador(TipoVariable.BOOL, TipoVariable.FALSE), new Operador(etFalse));
                g3d.generarInstr(TipoInstr.COPY, new Operador(TipoVariable.BOOL, varTrue), null, new Operador(TipoVariable.BOOL, varBool));
                g3d.generarInstr(TipoInstr.GOTO, null, null, new Operador(etTrue));
                g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etFalse));
                g3d.generarInstr(TipoInstr.COPY, new Operador(TipoVariable.BOOL, varFalse), null, new Operador(TipoVariable.BOOL, varBool));
                g3d.generarInstr(TipoInstr.SKIP, null, null, new Operador(etTrue));
                varActual = varBool;
                return tipo1;
            }
            case IDX_ARRAY -> {
                SymbolOperand arr = op.op;
                String tipoArr = procesarOperando(arr);
                String varArray = varActual;
                boolean error = false;
                if (tipoArr == null) {
                    errores.add("Se realizan operaciones no validas en el array " + arr + " del cual se quiere coger un indice");
                    indicarLocalizacion(arr);
                    error = true;
                }
                DescripcionArray da;
                if (arr.atomicExp == null) {
                    errores.add("Error, el array indicado no pertenece a un identificador: " + arr);
                    indicarLocalizacion(arr);
                    return null;
                }
                DescripcionSimbolo ds = tablaSimbolos.consulta(arr.atomicExp.toString());
                if (ds == null) {
                    errores.add("Error, no se ha declarado anteriormente el array " + arr.atomicExp.toString());
                    indicarLocalizacion(arr);
                    return null;
                }
                da = (DescripcionArray) ds;
                String id = arr.atomicExp.value.toString();
                String aux = tipoArr;
                if (tipoArr != null && aux.startsWith(ParserSym.terminalNames[ParserSym.TUPLE])) {
                    aux = aux.substring(aux.indexOf(" ") + 1);
                }
                //aux = aux.substring(aux.indexOf(" ") + 1);
                if (!error && (aux == null || aux.length() < 2)) {// !tipoArr.endsWith("]")) {
                    errores.add("El operador " + arr + " del cual se quiere coger un indice no es un array, es de tipo " + tipoArr);
                    indicarLocalizacion(arr); // operador a la izquierda no termina siendo un array
                    error = true;
                }
                if (tipoArr != null && !tipoArr.contains("[")) {
                    errores.add("Se ha intentado acceder al elemento " + op.dim + " de un operando tipo " + tipoArr + " (" + arr + ") que no es array");
                    indicarLocalizacion(arr); // operador a la izquierda no termina siendo un array 
                    error = true;
                }
                if (error) {
                    return null;
                }
                String nuevoTipo = tipoArr.substring(0, tipoArr.lastIndexOf("[")).trim();
                if (nuevoTipo.startsWith(ParserSym.terminalNames[ParserSym.TUPLE])) {
                    String tipo = nuevoTipo.substring(nuevoTipo.indexOf(" ") + 1); // -------------------------------------------------------------------------------------------------
                    if (!SymbolTipoPrimitivo.isTipoPrimitivo(tipo)) {
                        DescripcionSimbolo d = tablaSimbolos.consulta(tipo);
                        if (d == null) {
                            // error, tupla no encontrada
                            errores.add("El tipo del array " + arr + " es de una tupla no declarada (" + tipo + ")");
                            indicarLocalizacion(arr); // operador a la izquierda no termina siendo un array
                            error = true;
                        } else if (!d.tieneValorAsignado()) {
                            errores.add("Se ha intentado acceder a un elemento del array " + arr + " pero este no ha reservado memoria");
                            indicarLocalizacion(arr); // operador a la izquierda no termina siendo un array
                            error = true;
                        }
                    }
                }
                if (error) {
                    return null;
                }
                SymbolDimensiones dim = op.dim;
                if(dim == null) {
                    errores.add("El array " + id + " no tiene dimensiones");
                    indicarLocalizacion(arr); // operador a la izquierda no termina siendo un array 
                    return null;
                }
                int ind = -2;
                String tipoDesplazado = da.getTipo();
                ArrayList<String> variablesIndice = new ArrayList<>();
                for (int n = 1; dim != null; n++) {
                    if (ind != -1) {
                        ind = tipoDesplazado.lastIndexOf("[");
                    }
                    if (ind > -1) {
                        tipoDesplazado = tipoDesplazado.substring(0, ind).trim();
                    }
                    String tipoIdx = procesarOperando(dim.operando);
                    if (tipoIdx == null) {
                        errores.add("Se realizan operaciones no validas para el calculo del indice " + n + " del array " + id);
                        indicarLocalizacion(dim.operando);
                        error = true;
                    } else if (!tipoIdx.equals(ParserSym.terminalNames[ParserSym.ENT])) {
                        errores.add("Las operaciones para el calculo del indice " + n + " del array " + id + " resultan en " + tipoIdx + ", cuando tendria que ser un entero");
                        indicarLocalizacion(dim.operando);
                        error = true;
                    }
                    if (!error) {
//                            TipoVariable tv = TipoVariable.PUNTERO;
//                            String varIndice = g3d.nuevaVariable(tv);
//                            g3d.generarInstr(TipoInstr.COPY, new Operador(g3d.getTipoFromVar(varActual), varActual), null, new Operador(tv, varIndice));
                        variablesIndice.add(varActual);//varIndice);
                    }
                    dim = dim.siguienteDimension;
                }
                if (ind < 0) {
                    errores.add("Se ha intentado acceder a mas dimensiones de las definidas en el array " + id);
                    indicarLocalizacion(dim);
                    error = true;
                }
                if (error) {
                    return null;
                }
                ArrayList<String> variablesDimension = da.getVariablesDimension();
                String varInd = variablesIndice.get(0);
                for (int i = 0; i < variablesIndice.size() - 1; i++) {
                    //String varIndice = variablesIndice.get(i);
                    String varDesplazamiento = variablesDimension.get(i);
                    String varTemp = g3d.nuevaVariable(TipoVariable.INT);
                    g3d.generarInstr(TipoInstr.MUL, new Operador(TipoVariable.INT, varInd), new Operador(TipoVariable.INT, varDesplazamiento), new Operador(TipoVariable.INT, varTemp));
                    String varInd2 = variablesIndice.get(i + 1);
                    varInd = g3d.nuevaVariable(TipoVariable.INT);
                    g3d.generarInstr(TipoInstr.ADD, new Operador(TipoVariable.INT, varInd2), new Operador(TipoVariable.INT, varTemp), new Operador(TipoVariable.INT, varInd));
                }
                // no hace falta calcular b porque nuestros arrays empiezan en 0
//                        String indexByte = g3d.nuevaVariable(TipoReferencia.var);
//                        g3d.generarInstr(TipoInstr.SUB, new Operador(varInd), new Operador(da.getOffsetTempsCompilacio()), new Operador(indexByte));
                String indexReal = g3d.nuevaVariable(TipoVariable.INT);
                TipoVariable t = TipoVariable.getTipo(da.tipoElementoDelArray, false);
                g3d.generarInstr(TipoInstr.MUL, new Operador(TipoVariable.INT, varInd), new Operador(TipoVariable.INT, t.bytes), new Operador(TipoVariable.INT, indexReal));

                TipoVariable tArr = g3d.getTipoFromVar(varArray);
                TipoVariable tNuevo = TipoVariable.getTipo(tipoDesplazado, tipoDesplazado.contains("[") || tipoDesplazado.startsWith(ParserSym.terminalNames[ParserSym.TUPLE]));
                // comprobacion de que el entero sea positivo???
                //Esto funcionaria unicamente para a = b[i], no para b[i] = a
                String nuevaVariable = g3d.nuevaVariable(tNuevo);
                g3d.generarInstr(TipoInstr.IND_VAL, new Operador(tArr, varArray), new Operador(TipoVariable.INT, indexReal), new Operador(tNuevo, nuevaVariable));
                varActual = nuevaVariable;
                // calcular desplazamiento? ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                return tipoDesplazado;
            }
            case MEMBER_ACCESS -> {
                SymbolOperand tupla = op.op;
                String tipoTupla = procesarOperando(tupla);
                String varTupla = varActual;
                if (tipoTupla == null) {
                    errores.add("Se realizan operaciones no validas (" + tupla + ") en la tupla de la cual se quiere coger un miembro");
                    indicarLocalizacion(tupla);
                    return null;
                } else if (tupla.atomicExp == null) {
                    errores.add("No se puede acceder al miembro de unas operaciones que no son un identificador");
                    indicarLocalizacion(tupla);
                    return null;
                }
                String nombre = tupla.value.toString();//tipoTupla.substring(tipoTupla.indexOf(" ") + 1);
                DescripcionSimbolo ds = tablaSimbolos.consulta(nombre);
                if (ds == null) {
                    if (nombre.contains(".")) {
                        errores.add("No se puede acceder al miembro de un miembro sin variables auxiliares por en medio (" + op + ")");
                        indicarLocalizacion(tupla);
                        return null;
                    }
                    errores.add("La tupla " + nombre + " no esta declarada");
                    indicarLocalizacion(tupla);
                    return null;
                } else if (!ds.isTipoTupla()) {
                    errores.add("Se ha intentado acceder a un miembro de una variable (" + nombre + ") no tupla");
                    indicarLocalizacion(tupla);
                    return null;
                }
                if (!ds.tieneValorAsignado()) {
                    errores.add("No se puede acceder a un miembro de la tupla " + nombre + " porque esta no ha sido inicializado con anterioridad");
                    indicarLocalizacion(tupla);
                    return null;
                }
                DescripcionSimbolo aux = tablaSimbolos.consulta(ds.getNombreTupla());
                if (aux == null) {
                    errores.add("Se ha intentado acceder a un miembro de una tupla (" + nombre + ") que es de un tipo no declarado anteriormente (" + ds.getNombreTupla() + ")");
                    indicarLocalizacion(tupla);
                    return null;
                }
                DescripcionDefinicionTupla dt = (DescripcionDefinicionTupla) aux;
                DefinicionMiembro miembro = dt.getMiembro(op.member);
                if (miembro == null) {
                    errores.add("El miembro " + op.member + " no existe en la tupla " + nombre);
                    indicarLocalizacion(tupla);
                    return null;
                }
                String varDesp = g3d.nuevaVariable(TipoVariable.INT);
                Integer valDesp = dt.getDesplazamiento(op.member);
                g3d.generarInstr(TipoInstr.COPY, new Operador(TipoVariable.INT, valDesp), null, new Operador(TipoVariable.INT, varDesp));
                TipoVariable tMiembro = TipoVariable.getTipo(miembro.tipo, miembro.tipo.contains("[") || miembro.tipo.startsWith(ParserSym.terminalNames[ParserSym.TUPLE]));
                TipoVariable tTupla = g3d.getTipoFromVar(varTupla);
                String nuevaVariable = g3d.nuevaVariable(tMiembro);
                g3d.generarInstr(TipoInstr.IND_VAL, new Operador(tTupla, varTupla), new Operador(TipoVariable.INT, varDesp), new Operador(tMiembro, nuevaVariable));
                varActual = nuevaVariable;
                return miembro.tipo;
            }
            case CASTING -> {
                SymbolOperand operando = op.op;
                String casting = op.casting.getTipo();
                String tipo = procesarOperando(operando);
                if (tipo == null) {
                    errores.add("Se realizan operaciones no validas (" + operando + ") en antes de aplicar el casting");
                    indicarLocalizacion(operando);
                    return null;
                }
                if (operando.atomicExp != null) {
                    DescripcionSimbolo ds = tablaSimbolos.consulta(operando.atomicExp.value.toString());
                    if (ds != null && !ds.tieneValorAsignado()) {
                        errores.add("No se puede realizar casting a la variable " + operando.atomicExp.value.toString() + " porque esta no ha sido incializada anteriormente");
                        indicarLocalizacion(operando.atomicExp);
                        return null;
                    }
                } else if (tipo.equals(casting)) { // si se hace casting de lo mismo no se trata nada
                    return casting;
                }
                String varCasting = varActual;
                boolean charAString = tipo.equals(ParserSym.terminalNames[ParserSym.CAR]) && casting.equals(ParserSym.terminalNames[ParserSym.CAR] + "[]");
                int idx1 = casting.indexOf(" "), idx2 = casting.lastIndexOf(" ");
                if (!charAString && idx1 >= 0 && idx2 >= 0) {
                    String castingPreEspacios = casting.substring(0, idx1);
                    String castingPostEspacios = casting.substring(idx2 + 1, casting.length());
                    casting = castingPreEspacios + " " + castingPostEspacios;
                    charAString = tipo.equals(ParserSym.terminalNames[ParserSym.CAR]) && casting.equals(ParserSym.terminalNames[ParserSym.CAR] + " []");
                }
                boolean opEsIntChar = tipo.equals(ParserSym.terminalNames[ParserSym.ENT]) || tipo.equals(ParserSym.terminalNames[ParserSym.CAR]); //|| tipo.equals(ParserSym.terminalNames[ParserSym.REAL]);
                boolean castingEsIntChar = casting.equals(ParserSym.terminalNames[ParserSym.ENT]) || casting.equals(ParserSym.terminalNames[ParserSym.CAR]);// || casting.equals(ParserSym.terminalNames[ParserSym.REAL]);
                // casting posible entre int <-> char <-> double <-> int y se hace solo esto: char -> string
                if (opEsIntChar && castingEsIntChar) {
                    TipoVariable t = g3d.getTipoFromVar(varCasting);
                    TipoVariable t2 = TipoVariable.getTipo(tipo, tipo.contains("["));
                    String nuevaVar = g3d.nuevaVariable(t2);
                    g3d.generarInstr(TipoInstr.COPY, new Operador(t, varCasting, t2), null, new Operador(t2, nuevaVar));
                    varActual = nuevaVar;
                    return casting;
                } else if (charAString) {
                    TipoVariable t = g3d.getTipoFromVar(varCasting);
                    TipoVariable t2 = TipoVariable.STRING;
                    String nuevaVar = g3d.nuevaVariable(t2);
                    g3d.generarInstr(TipoInstr.COPY, new Operador(t, varCasting, t2), null, new Operador(t2, nuevaVar));
                    varActual = nuevaVar;
                    return casting;
                }
                errores.add("Se ha intentado realizar un casting no permitido (" + op.toString() + ") de " + tipo + " a " + casting);
                indicarLocalizacion(operando);
                return null;
            }

        }
        throw new Exception("Error al procesar operando desconocido"); // error, no es ninguno de los casos
    }

}
