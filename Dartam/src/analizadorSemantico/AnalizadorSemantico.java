/**
* Assignatura 21780 - Compiladors
* Estudis: Grau en Enginyeria Informàtica 
* Itinerari: Intel·ligència Artificial i Computacio
*
* Equipo: Arturo, Dani y Marta
*/
package analizadorSemantico;

import analizadorSintactico.ParserSym;
import java.util.ArrayList;
import java.util.Stack;

import analizadorSintactico.symbols.*;
import java.util.HashMap;
import java.util.List;
import jflex.base.Pair;

public class AnalizadorSemantico {

    public TablaSimbolos tablaSimbolos;
    
    // Description to the function in which we are currently.
    private Pair<String, DescripcionSimbolo> metodoActualmenteSiendoTratado;
    // When checking if a function's parameters are correct, we use this stack to store the declared function's types.
    // We use a stack because we will be taking elements out every time we process them.
    private List<String> errores, symbols;
    private static final boolean DEBUG = true;
    
    public String getErrors() {
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
        // metodos especiales
        DescripcionSimbolo dEnter = new DescripcionFuncion(ParserSym.terminalNames[ParserSym.ENT]);
        tablaSimbolos.poner(ParserSym.terminalNames[ParserSym.ENTER], dEnter);
        DescripcionSimbolo dShow = new DescripcionFuncion(ParserSym.terminalNames[ParserSym.ENT]);
        tablaSimbolos.poner(ParserSym.terminalNames[ParserSym.SHOW], dShow);
        DescripcionSimbolo dFrom = new DescripcionFuncion(ParserSym.terminalNames[ParserSym.ENT]);
        tablaSimbolos.poner(ParserSym.terminalNames[ParserSym.FROM], dFrom);
        DescripcionSimbolo dInto = new DescripcionFuncion(ParserSym.terminalNames[ParserSym.ENT]);
        tablaSimbolos.poner(ParserSym.terminalNames[ParserSym.INTO], dInto);
        // tuplas
        for (SymbolScriptElemento tupla : tuplas) {
            DescripcionSimbolo d = new DescripcionDefinicionTupla(tupla.id, new HashMap<String, DescripcionSimbolo>());
            tablaSimbolos.poner(tupla.id, d);
        }
        for (SymbolScriptElemento tupla : tuplas) {
            procesarDeclaracionTupla(tupla);
        }
        // declaraciones
        for (SymbolDecs decs : declaraciones) {
            procesarDeclaraciones(decs, null);
        }
        // metodos
        for (SymbolScriptElemento metodo : metodos) {
            DescripcionSimbolo d = new DescripcionFuncion(metodo.tipoRetorno.getTipo());
            tablaSimbolos.poner(metodo.id, d);
        }
        for (SymbolScriptElemento metodo : metodos) {
            procesarDefinicionMetodo(metodo);
        }
        // main
        DescripcionSimbolo d = new DescripcionSimbolo();
        tablaSimbolos.poner(scriptMainYElementos.nombreMain, d);
        procesarMain(scriptMainYElementos);
    }

    private void procesarDeclaraciones(SymbolDecs decs, DescripcionSimbolo declaracionTipoTupla) throws Exception {
        SymbolDecAsigLista dec = decs.iddecslista;
        while (dec != null) {
            SymbolTipo tipo = decs.tipo;
            boolean error = false;
            DescripcionSimbolo descripcionTupla  = null;
            String id = dec.id;
            SymbolOperand valorAsignado = (dec.asignacion == null) ? null : dec.asignacion.operando;
            if (tablaSimbolos.consulta(id) != null) {
                errores.add("El identificador '" + id + "' ya ha sido declarado con anterioridad");
                indicarLocalizacion(dec);
                error = true;
            }
            if (tipo.isArray() || tipo.isTupla()) {
                if (decs.isConst) {
                    errores.add(id + " se ha intentado declarar constante, cuando solo los tipos primitivos pueden serlo");
                    indicarLocalizacion(dec);
                    error = true;
                }
                if (valorAsignado != null) {
                    errores.add("Se ha intentado asignar un valor a " + id + ", pero solo se les pueden asignar valores a los tipos primitivos");
                    indicarLocalizacion(dec);
                    error = true;
                }
                if (tipo.isTupla()) {
                    String tuplaName = tipo.getTipo();
                    tuplaName = tuplaName.substring(tuplaName.indexOf(" ") + 1);
                    descripcionTupla = tablaSimbolos.consulta(tuplaName);
                    if (descripcionTupla == null) {
                        errores.add("No se ha encontrado ninguna tupla con el identificador "+tuplaName);
                        indicarLocalizacion(tipo);
                        error = true;
                    }
                }
                if(tipo.isArray()){
                    SymbolDimensiones dim = tipo.dimArray;
                    int n = 0;
                    while(dim != null) {
                        n++;
                        String tipoIdx = procesarOperando(dim.operando);
                        if (tipoIdx == null) {
                            errores.add("Se realizan operaciones no validas para el calculo del indice "+n+" del array "+id);
                            indicarLocalizacion(dim.operando);
                            error = true;
                        } else if (!tipoIdx.equals(ParserSym.terminalNames[ParserSym.ENT])) {
                            errores.add("Las operaciones para el calculo del indice "+n+" del array " + id + " resultan en " + tipoIdx + ", cuando tendria que ser un entero");
                            indicarLocalizacion(dim.operando);
                            error = true;
                        }
                        dim = dim.siguienteDimension;
                    }
                }
            } else if (valorAsignado != null) {
                String tipoValor = procesarOperando(valorAsignado);
                if (tipoValor == null) {
                    errores.add("Los valores del operando "+valorAsignado+" no son compatibles");
                    indicarLocalizacion(valorAsignado);
                    error = true;
                } else if (!tipoValor.equals(tipo.getTipo())) {
                    errores.add("El tipo "+tipo.getTipo()+" con el que se ha declarado no es compatible con el que se esta intentado asignar a la variable ("+tipoValor+")");
                    indicarLocalizacion(valorAsignado);
                    error = true;
                }
            }
            if (!error) {
                DescripcionSimbolo d;
                if (tipo.isArray()) {
                    d = new DescripcionArray(tipo.getTipo() + " " + tipo.dimArray.getEmptyBrackets(), tipo.dimArray.getDimensiones(), descripcionTupla);
                } else {
                    d = new DescripcionSimbolo(tipo.getTipo(), decs.isConst, valorAsignado != null, descripcionTupla);
                }
                if (declaracionTipoTupla != null) {
                    declaracionTipoTupla.anyadirMiembro(id, d);
                } else {
                    tablaSimbolos.poner(id, d);
                }
            }
            dec = dec.siguienteDeclaracion;
        }
    }

    private void procesarBody(SymbolBody body) throws Exception {
        while (body != null) {
            SymbolMetodoElemento elem = body.metodo;

            switch (elem.getTipo()) {
                case SymbolMetodoElemento.INSTR ->
                    procesarInstruccion(elem.instruccion);
                case SymbolMetodoElemento.IF ->
                    procesarIf(elem.iff);
                case SymbolMetodoElemento.LOOP ->
                    procesarLoop(elem.loop);
                case SymbolMetodoElemento.SWITCH ->
                    procesarSwitch(elem.sw);
            }
            body = body.siguienteMetodo;
        }
    }

    private void procesarInstruccion(SymbolInstr instr) throws Exception {
        switch (instr.getTipo()) {
            case SymbolInstr.ASIGS ->
                procesarAsignaciones(instr.asigs);
            case SymbolInstr.DECS ->
                procesarDeclaraciones(instr.decs, null);
            case SymbolInstr.FCALL ->
                procesarLlamadaFuncion(instr.fcall);
            case SymbolInstr.RET ->
                procesarReturn(instr.ret);
            case SymbolInstr.SWAP ->
                procesarSwap(instr.swap);
        }
    }

    private void procesarAsignaciones(SymbolAsigs asigs) throws Exception {
        while (asigs != null) {
            boolean error = false, errorOperandoInvalido = false;
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
            String tipoValor = procesarOperando(asig.valor);
            if (tipoValor == null) {
                errores.add("Se realizan operaciones no validas en el valor a asignar a '" + asig.id + "'");
                indicarLocalizacion(asig.valor);
                errorOperandoInvalido = true;
            }
            if (error) {
                asigs = asigs.siguienteAsig;
                continue;
            }
            error = errorOperandoInvalido;
            String tipoVariable = null;
            switch (asig.getTipo()) {
                case PRIMITIVA -> {
                    if (d.isArray() || d.isTupla()) {
                        String estructura = "una tupla";
                        if (d.isArray()) {
                            estructura = "un array";
                        }
                        errores.add("Se ha intentado asignar un valor a " + estructura + " en vez de a uno de sus elementos");
                        indicarLocalizacion(asig);
                        error = true;
                    }
                    tipoVariable = d.getTipo();
                }
                case ARRAY -> {
                    if (!d.isArray()) {
                        errores.add("Se ha intentado acceder a un elemento de la variable "+asig.id+" que no es un array (es de tipo "+d.getTipo()+")");
                        indicarLocalizacion(asig);
                        error = true;
                    }
                    String idx = procesarOperando(asig.idx);
                    if (tipoValor == null) {
                        errores.add("Se realizan operaciones no validas en el indice del array " + asig.id);
                        indicarLocalizacion(asig.idx);
                        error = true;
                    } else if (!idx.equals(ParserSym.terminalNames[ParserSym.ENT])) {
                        errores.add("El indice del array " + asig.id + " no resulta en un entero");
                        indicarLocalizacion(asig.idx);
                        error = true;
                    }
                    if (!error) {
                        tipoVariable = d.getTipo();
                    }
                    if (!asig.operacion.isBasicAsig()) {
                        errores.add("No se puede realizar asignacion compuesta si la variable '"+asig.id+"' es un array");
                        indicarLocalizacion(asig.operacion);
                        error = true;
                    }
                }
                case TUPLA -> {
                    if (!d.isTupla()) {
                        errores.add("Se ha intentado acceder a un miembro de la variable "+asig.id+" que no es una tupla (es de tipo "+d.getTipo()+")");
                        indicarLocalizacion(asig);
                        error = true;
                    } else {
                        DescripcionSimbolo miembro = d.getMember(asig.miembro);
                        if (miembro == null) {
                            errores.add("El miembro " + asig.miembro + " no ha sido encontrado en la tupla " + d.getNombreTupla());
                            indicarLocalizacion(asig);
                            error = true;
                        } else {
                            tipoVariable = miembro.getTipo();
                        }
                    }
                }
            }
            if (error) {
                asigs = asigs.siguienteAsig;
                continue;
            }
            if (!tipoValor.equals(tipoVariable)) {
                errores.add("Se esta intentado asignar un valor de tipo "+tipoValor+" a una variable de tipo " + tipoVariable);
                indicarLocalizacion(asig.valor);
                error = true;
            }
            if (!error && !asig.operacion.doesOperationResultInSameType(tipoValor)) {
                errores.add("Se esta intentado aplicar una operacion no valida entre los tipos "+tipoVariable+" y "+ tipoValor);
                indicarLocalizacion(asig);
                error = true;
            }
            if (error) {
                asigs = asigs.siguienteAsig;
                continue;
            }
            if (!asig.operacion.isBasicAsig() && !d.tieneValorAsignado()) {
                errores.add("No se puede realizar asignacion compuesta si la variable '"+asig.id+"' no ha sido asignada de forma simple anteriormente");
                indicarLocalizacion(asig.operacion);
                error = true;
            }
            switch (asig.getTipo()) {
                case PRIMITIVA -> {
                    if (d.tieneValorAsignado() && d.isConstante()) {
                        errores.add("Se esta intentado asignar un valor a la constante '"+ asig.id+"' que ya tenia valor");
                        indicarLocalizacion(asig);
                        error = true;
                    }
                    d.asignarValor();
                }
                case ARRAY -> {
                    tablaSimbolos.ponerIndice(asig.id, d);
                }
                case TUPLA -> {
                    DescripcionSimbolo miembro = d.getMember(asig.miembro);
                    if (miembro.tieneValorAsignado() && miembro.isConstante()) {
                        errores.add("Se esta intentado asignar un valor al miembro constante '"+ asig.miembro+"' de la variable '"+asig.id+"' de la tupla '"+d.getNombreTupla()+"', el cual ya tenia un valor asignado");
                        indicarLocalizacion(asig);
                        error = true;
                    }
                    tablaSimbolos.ponerCampo(asig.id, asig.miembro, miembro);
                    miembro.asignarValor();
                }
            }
            asigs = asigs.siguienteAsig;
        }

    }

    private void procesarLlamadaFuncion(SymbolFCall fcall) throws Exception {
        String nombre = (String) fcall.methodName.value;
        DescripcionSimbolo ds = tablaSimbolos.consulta(nombre);
        if (ds == null) {
            errores.add("La funcion " + nombre + " no ha sido declarada");
            indicarLocalizacion(fcall);
            return;
        }
        if (!ds.isFunction()) {
            errores.add("El identificador " + nombre + " pertenece a una variable, no una funcion");
            indicarLocalizacion(fcall);
            return;
        }
        ds = (DescripcionFuncion) ds;
        ArrayList<Pair<String, DescripcionSimbolo>> params = ds.getTiposParametros();
        SymbolOperandsLista opLista = fcall.operandsLista;
        int n = 0;
        for (Pair<String, DescripcionSimbolo> tipoParam : params) {
            n++;
            if (opLista == null) {
                errores.add("La funcion " + nombre + " tiene " + params.size() + " parametros, pero se han pasado mas al momento de llamarla");
                indicarLocalizacion(fcall);
                return;
            }
            SymbolOperand op = opLista.operand;
            String tipoOp = procesarOperando(op);
            if (tipoOp == null) {
                errores.add("Se ha intentado pasar por parametros operacions no validas en el parametro " + n + " de la funcion "+nombre);
                indicarLocalizacion(op);
            } else if (!tipoOp.equals(tipoParam.snd.getTipo())) {
                errores.add("Se ha intentado pasar por parametro un operando de tipo " + tipoOp + " en el parametro " + n + " de la funcion "+nombre+" que es de tipo " + tipoParam.snd.getTipo());
                indicarLocalizacion(op);
                return;
            }
            opLista = opLista.siguienteOperando;
        }
        if (opLista != null) {
            errores.add("La funcion " + nombre + " tiene " + params.size() + " parametros, pero se han pasado " + n);
            indicarLocalizacion(fcall);
        }
    }

    private void procesarReturn(SymbolReturn ret) throws Exception {
        String tipo = metodoActualmenteSiendoTratado.snd.getTipo();
        SymbolOperand op = ret.op;
        if (op == null) {
            if (tipo == null) {
                return;
            }
            errores.add("Se ha realizado pop sin nada de la funcion "+metodoActualmenteSiendoTratado.fst+" que devuelve valores de tipo " + tipo);
            indicarLocalizacion(ret);
            return;
        }
        Object tipoOp = procesarOperando(op);
        if (tipoOp == null) {
            errores.add("Se ha intentado hacer pop de una operacion no valida en la funcion " + metodoActualmenteSiendoTratado.fst);
            indicarLocalizacion(ret);
        } else if (!tipoOp.equals(tipo)) {
            errores.add("Se ha intentado hacer pop de un tipo (" + tipoOp + ") diferente al que devuelve la funcion (" + tipo + ")");
            indicarLocalizacion(ret);
        }
    }

    private void procesarSwap(SymbolSwap swap) {
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
            errores.add("La variable " + swap.op1 + " y la variable " + swap.op2 + " son de tipos diferentes ("+ds1.getTipo()+", "+ds2.getTipo()+"), no se puede realizar el swap");
            indicarLocalizacion(swap);
        }
    }

    private void procesarIf(SymbolIf cond) throws Exception {
        String tipoCond = procesarOperando(cond.cond);
        if (tipoCond == null) {
            errores.add("La condicion del 'si' realiza una operacion no valida");
            indicarLocalizacion(cond.cond);
        } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.PROP])){
            errores.add("La condicion del 'si' no resulta en una proposicion evualable como verdadera o falsa");
            indicarLocalizacion(cond.cond);
        }
        tablaSimbolos.entraBloque();
        procesarBody(cond.cuerpo);
        tablaSimbolos.salirBloque();
        SymbolElifs elifs = cond.elifs;
        while (elifs != null) {
            SymbolElif elif = elifs.elif;
            tipoCond = procesarOperando(elif.cond);
            if (tipoCond == null) {
                errores.add("La condicion del 'sino' realiza una operacion no valida");
                indicarLocalizacion(elif.cond);
            } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.PROP])){
                errores.add("La condicion del 'sino' no resulta en una proposicion evualable como verdadera o falsa");
                indicarLocalizacion(elif.cond);
            }
            tablaSimbolos.entraBloque();
            procesarBody(elif.cuerpo);
            tablaSimbolos.salirBloque();
            elifs = elifs.elifs;
        }
        if (cond.els != null) {
            tablaSimbolos.entraBloque();
            procesarBody(cond.els.cuerpo);
            tablaSimbolos.salirBloque();
        }
    }

    private void procesarLoop(SymbolLoop loop) throws Exception {
        // no importa que sea while o do while
        tablaSimbolos.entraBloque();
        SymbolLoopCond loopCond = loop.loopCond;
        if (loopCond.decs != null) {
            procesarDeclaraciones(loopCond.decs, null);
        }
        String tipoCond = procesarOperando(loopCond.cond);
        if (tipoCond == null) {
            errores.add("La condicion del 'loop' realiza una operacion no valida");
            indicarLocalizacion(loopCond.cond);
        } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.PROP])){
            errores.add("La condicion del 'loop' no resulta en una proposicion evualable como verdadera o falsa");
            indicarLocalizacion(loopCond.cond);
        }
        if (loopCond.asig != null) {
            procesarAsignaciones(loopCond.asig);
        }
        procesarBody(loop.cuerpo);
        tablaSimbolos.salirBloque();
    }

    private void procesarSwitch(SymbolSwitch sw) throws Exception {
        tablaSimbolos.entraBloque();
        Object tipo1 = procesarOperando(sw.cond);
        if (tipo1 == null) {
            errores.add("La operacion del 'select' realiza una operacion no valida");
            indicarLocalizacion(sw.cond);
        }
        SymbolCaso caso = sw.caso;
        int n = 0;
        while (caso != null) {
            n++;
            Object tipo2 = procesarOperando(caso.cond);
            if (tipo2 == null) {
                errores.add("La operacion del 'caso' realiza una operacion no valida");
                indicarLocalizacion(caso.cond);
            } else if (tipo1 != null && tipo1 != tipo2) {
                errores.add("Los tipos de la condicion ("+tipo1+") del select y del caso "+n+" ("+tipo2+") no coinciden");
                indicarLocalizacion(caso.cond);
            }
            procesarBody(caso.cuerpo);
            caso = caso.caso;
        }
        if (sw.pred != null) {
            procesarBody(sw.pred.cuerpo);
        }
        tablaSimbolos.salirBloque();
    }

    private void procesarDefinicionMetodo(SymbolScriptElemento metodo) throws Exception {
        tablaSimbolos.entraBloque();
        metodoActualmenteSiendoTratado = new Pair(metodo.id, tablaSimbolos.consulta(metodo.id));
        procesarParametros(metodo.parametros.paramsLista);
        procesarBody(metodo.cuerpo);
        tablaSimbolos.salirBloque();
    }
    
    private void procesarParametros(SymbolParamsLista params) {
        while (params != null) {
            
                String nombreParam = params.id;
                //if (tablaSimbolos.contains(id)) { // imposible
                if (nombreParam.equals(metodoActualmenteSiendoTratado.fst)) {
                    errores.add("El parametro " + nombreParam + " tiene el mismo nombre que el metodo en el que esta siendo declarado");
                    indicarLocalizacion(params);
                    params = params.siguienteParam;
                    continue;
                } else if (tablaSimbolos.consulta(nombreParam) != null) {
                    errores.add("El identificador " + nombreParam + " ya está declarado en este ámbito y por lo tanto no se puede llamar así al parámetro");
                    indicarLocalizacion(params);
                    params = params.siguienteParam;
                    continue;
                }
                
                DescripcionSimbolo dFuncion = metodoActualmenteSiendoTratado.snd;
                DescripcionSimbolo tupla = null;
                if (params.param.isTupla()) {
                    tupla = tablaSimbolos.consulta(params.param.idTupla);
                }
                DescripcionSimbolo dParam = new DescripcionSimbolo(params.param.getTipo(), false, false, tupla);
            try {
                //tablaSimbolos.posaparam(metodoActualmenteSiendoTratado.fst, nombreParam, dParam);
                tablaSimbolos.poner(nombreParam, dParam);
                dFuncion.anyadirParametro(nombreParam, dParam);
            } catch (Exception ex) {
                errores.add(ex.getMessage());
            }
            params = params.siguienteParam;
        }
    }
    
    private void procesarDeclaracionTupla(SymbolScriptElemento tupla) throws Exception {
        SymbolMiembrosTupla miembros = tupla.miembrosTupla;
        DescripcionSimbolo d = tablaSimbolos.consulta(tupla.id);
        while (miembros != null) {
            procesarDeclaraciones(miembros.decs, d);
            miembros = miembros.siguienteDeclaracion;
        }
    }

    private void procesarMain(SymbolMain main) throws Exception {
        SymbolBody body = main.main;
        tablaSimbolos.entraBloque();
        metodoActualmenteSiendoTratado = new Pair(main.nombreMain, tablaSimbolos.consulta(main.nombreMain));
        String tipo = ParserSym.terminalNames[ParserSym.STRING] + " " + main.lBracket + main.rBracket;
        DescripcionSimbolo d = new DescripcionSimbolo(tipo, false, true, null);
        tablaSimbolos.poner(main.nombreArgumentos, d);
        procesarBody(body);
        tablaSimbolos.salirBloque();
    }

    /**
     * Devuelve el tipo, array o struct al que pertenece el operando. Null si no
     * se respetan los tipos.
     *
     * @param op
     * @return
     */
    private String procesarOperando(SymbolOperand op) throws Exception {
        if (DEBUG) {
            //System.out.println(op.toString());
        }
        switch (op.getTipo()) {
            case ATOMIC_EXPRESSION -> {
                SymbolAtomicExpression literal = op.atomicExp;
                String tipo = literal.tipo;
                if (!tipo.equals(ParserSym.terminalNames[ParserSym.ID])){
                    return tipo; // si no es ID
                }
                String nombreID = (String)literal.value;
                DescripcionSimbolo ds = tablaSimbolos.consulta(nombreID);
                if (ds == null) {
                    errores.add("No se ha declarado con anterioridad la variable " + nombreID);
                    indicarLocalizacion(literal);
                    return null;
                }
                return ds.getTipo();
            }
            case FCALL -> {
                SymbolFCall fcall = op.fcall;
                int nErrores = errores.size();
                procesarLlamadaFuncion(fcall);
                if (nErrores != errores.size()) { // si han habido errores
                    return null;
                }
                DescripcionSimbolo ds = tablaSimbolos.consulta((String) fcall.methodName.value);
                return ds.getTipo();
            }
            case OP_BETWEEN_PAREN -> {
                return procesarOperando(op.op);
            }
            case UNARY_EXPRESSION -> {
                SymbolUnaryExpression exp = op.unaryExp;
                String tipo = procesarOperando(exp.op);
                if (tipo == null) {
                    errores.add("Se realizan operaciones no validas en " + exp.op);
                    indicarLocalizacion(exp.op);
                    return null;
                }
                int operation;
                if (exp.isLeftUnaryOperator()) {
                    SymbolLUnaryOperator operator = exp.leftOp;
                    operation = operator.unaryOperator;
                } else {
                    SymbolRUnaryOperator operator = exp.rightOp;
                    operation = operator.unaryOperator;
                }
                if (ParserSym.OP_INC == operation || ParserSym.OP_DEC == operation) {
                    String s = "incremento";
                    if (ParserSym.OP_DEC == operation) s = "decremento";
                    if (exp.op.atomicExp == null || !exp.op.atomicExp.tipo.equals(ParserSym.terminalNames[ParserSym.ID])) {
                        errores.add("No se puede realizar un "+s+" sobre un operando diferente a una variable primitiva ("+exp+")");
                        indicarLocalizacion(exp);
                        return null;
                    }
                    int nErrors = errores.size();
                    procesarAsignaciones(new SymbolAsigs(new SymbolAsig(!exp.isLeftUnaryOperator(), operation, exp.op.toString(), exp.value, exp.xleft, exp.xright), exp.xleft, exp.xright));
                    if (errores.size() != nErrors) {
                        errores.add("No se ha podido realizar la operacion de "+s+" "+exp);
                        indicarLocalizacion(exp);
                        return null;
                    }
                }
                if (exp.isLeftUnaryOperator()) {
                    if (!tipo.equals(ParserSym.terminalNames[ParserSym.PROP]) && !tipo.equals(ParserSym.terminalNames[ParserSym.ENT]) && !tipo.equals(ParserSym.terminalNames[ParserSym.REAL])) {
                        errores.add("Se ha intentado realizar una operacion "+exp+" no valida sobre un " + tipo); // error, no se puede operar si no es int ni bool
                        indicarLocalizacion(exp);
                        return null;
                    } else if (tipo.equals(ParserSym.terminalNames[ParserSym.PROP]) && ParserSym.OP_NOT != operation) {
                        errores.add("Se ha intentado realizar una operacion "+exp+" no valida sobre un " + tipo); // no se puede operar booleano con inc/dec
                        indicarLocalizacion(exp);
                        return null;
                    } else if (tipo.equals(ParserSym.terminalNames[ParserSym.REAL]) && (ParserSym.OP_ADD != operation && ParserSym.OP_SUB != operation)) {
                        errores.add("Se ha intentado realizar una operacion "+exp+" no valida sobre un " + tipo); // no se puede operar booleano con signo +/-
                        indicarLocalizacion(exp);
                        return null;
                    } else if (tipo.equals(ParserSym.terminalNames[ParserSym.ENT]) && (ParserSym.OP_INC != operation && ParserSym.OP_DEC != operation && ParserSym.OP_ADD != operation && ParserSym.OP_SUB != operation)) {
                        errores.add("Se ha intentado realizar una operacion "+exp+" no valida sobre un " + tipo); // no se puede operar entero con not
                        indicarLocalizacion(exp);
                        return null;
                    }
                    return tipo;
                }
                if (!tipo.equals(ParserSym.terminalNames[ParserSym.REAL]) && !tipo.equals(ParserSym.terminalNames[ParserSym.ENT])) {
                    errores.add("Se ha intentado realizar una operacion "+exp+" no valida sobre un " + tipo); // error, no se puede operar si no es int ni double
                    indicarLocalizacion(exp); 
                    return null;
                } else if (tipo.equals(ParserSym.terminalNames[ParserSym.REAL]) && ParserSym.OP_PCT != operation) {
                    errores.add("Se ha intentado realizar una operacion "+exp+" no valida sobre un " + tipo); // no se puede operar double con inc/dec
                    indicarLocalizacion(exp);
                    return null;
                } else if (tipo.equals(ParserSym.terminalNames[ParserSym.ENT]) && (ParserSym.OP_INC != operation && ParserSym.OP_DEC != operation)) {
                    return ParserSym.terminalNames[ParserSym.REAL]; // !!! se puede operar entero con OP_PCT
                }
                return tipo;
            }
            case BINARY_EXPRESSION -> {
                SymbolBinaryExpression exp = op.binaryExp;
                String tipo1 = procesarOperando(exp.op1);
                boolean error = false;
                if (tipo1 == null) {
                    errores.add("El primer operando de la expresion binaria realiza una operacion "+exp.op1+" no valida");
                    indicarLocalizacion(exp);
                    error = true;
                }
                String tipo2 = procesarOperando(exp.op2);
                if (tipo2 == null) {
                    errores.add("El segundo operando de la expresion binaria realiza una operacion "+exp.op2+" no valida");
                    indicarLocalizacion(exp);
                    error = true;
                }
                if (error){
                    return null;
                }
                boolean unoIntOtroDouble = (tipo1.equals(ParserSym.terminalNames[ParserSym.REAL]) && tipo2.equals(ParserSym.terminalNames[ParserSym.ENT]))
                        || (tipo2.equals(ParserSym.terminalNames[ParserSym.REAL]) && tipo1.equals(ParserSym.terminalNames[ParserSym.ENT]));
                if (!tipo1.equals(tipo2) && !unoIntOtroDouble) {
                    errores.add("Se ha intentado realizar una operacion ilegal "+exp.bop+" entre "+tipo1 + " y "+tipo2+" en la expresion binaria "+exp);
                    indicarLocalizacion(exp);// error, no se puede operar con tipos diferentes (excepto int y double)
                    error = true;
                } else if (!SymbolTipoPrimitivo.isTipoPrimitivo(tipo1)) {
                    errores.add("Se ha intentado realizar una operacion ilegal "+exp.bop+" entre tipos no primitivos ("+tipo1 + ") en la expresion binaria "+exp);
                    indicarLocalizacion(exp);// error, no se puede operar con tuplas y arrays
                    error = true;
                }
                if (error){
                    return null;
                }
                SymbolBinaryOperator operator = exp.bop;
                if (unoIntOtroDouble) {
                    if (operator.isForOperandsOfType(ParserSym.terminalNames[ParserSym.REAL])) {
                        return ParserSym.terminalNames[ParserSym.REAL];
                    }
                    errores.add("Se ha intentado realizar una operacion ilegal "+exp.bop+" entre tipos "+tipo1 + " y "+tipo2+" en la expresion binaria "+exp);
                    indicarLocalizacion(exp);// error, operando no valido para operar con ints y doubles
                    return null;
                }
                if (!operator.isForOperandsOfType(tipo1)) {
                    errores.add("Se ha intentado realizar una operacion ilegal "+exp.bop+" para los tipos "+tipo1 + " y "+tipo2+" en la expresion binaria "+exp);
                    indicarLocalizacion(exp);
                    return null; // error, operandos no pueden operar con operador
                }
                if (operator.doesOperationResultInBoolean()) {
                    return ParserSym.terminalNames[ParserSym.PROP];
                }
                return tipo1; // en caso contrario resulta en el mismo tipo
            }
            case CONDITIONAL_EXPRESSION -> {
                SymbolConditionalExpression exp = op.conditionalExp;
                String tipoCond = procesarOperando(exp.cond);
                if (tipoCond == null) {
                    errores.add("La condicion "+exp.cond+" de la expresion ternaria "+exp+" realiza una operacion no valida");
                    indicarLocalizacion(exp.cond);
                } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.PROP])) {
                    errores.add("La condicion "+exp.cond+" de la expresion ternaria "+exp+" no resulta en una proposicion evualable como verdadera o falsa, sino en " + tipoCond);
                    indicarLocalizacion(exp.cond); // error, no se puede utilizar de condicion algo que no sea una proposicion
                }
                boolean error = false;
                String tipo1 = procesarOperando(exp.caseTrue);
                if (tipo1 == null) {
                    errores.add("El operando a asignar en caso positivo "+exp.caseTrue+" de la expresion ternaria "+exp+" realiza operaciones no validas");
                    indicarLocalizacion(exp.caseTrue);
                    error = true;
                }
                String tipo2 = procesarOperando(exp.caseFalse);
                if (tipo2 == null) {
                    errores.add("El operando a asignar en caso negativo "+exp.caseFalse+" de la expresion ternaria "+exp+" realiza operaciones no validas");
                    indicarLocalizacion(exp.caseFalse);
                    error = true;
                }
                if (error) {
                    return null;
                }
                if (!tipo1.equals(tipo2)) {
                    errores.add("No se puede operar con tipos diferentes (" + tipo1 + ", " +tipo2+ ") en la operacion ternaria "+exp);
                    indicarLocalizacion(exp);
                    return null;
                }
                return tipo1;
            }
            case IDX_ARRAY -> {
                SymbolOperand arr = op.op;
                String tipoArr = procesarOperando(arr);
                boolean error = false;
                if (tipoArr == null) {
                    errores.add("Se realizan operaciones no validas en el array "+arr+" del cual se quiere coger un indice");
                    indicarLocalizacion(arr);
                    error = true;
                }
                String aux = tipoArr;
                if (aux != null && aux.startsWith(ParserSym.terminalNames[ParserSym.KW_TUPLE])) {
                    aux = aux.substring(aux.indexOf(" ") + 1);
                }
                aux = aux.substring(aux.indexOf(" ") + 1);
                if (!error && (aux == null || aux.length() < 2)){// !tipoArr.endsWith("]")) {
                    errores.add("El operador "+arr+" del cual se quiere coger un indice no es un array, es de tipo " + tipoArr);
                    indicarLocalizacion(arr); // operador a la izquierda no termina siendo un array
                    error = true;
                }
                if (!error) {
                    tipoArr = tipoArr.substring(0, tipoArr.length() - 3);
                    if (tipoArr.startsWith(ParserSym.terminalNames[ParserSym.KW_TUPLE])) {
                        String tipo = tipoArr.substring(tipoArr.indexOf(" ") + 1);
                        if (!SymbolTipoPrimitivo.isTipoPrimitivo(tipo)) {
                            DescripcionSimbolo ds = tablaSimbolos.consulta(tipo);
                            if (ds == null) {
                                // error, tupla no encontrada
                                errores.add("El tipo del array "+arr+" es de una tupla no declarada (" + tipo+")");
                                indicarLocalizacion(arr); // operador a la izquierda no termina siendo un array
                            }
                        }
                    }
                }
                SymbolOperand idx = op.idxArr;
                String tipoIdx = procesarOperando(idx);
                if (tipoIdx == null) {
                    errores.add("Se realizan operaciones no validas ("+idx+") en el calculo del indice");
                    indicarLocalizacion(idx);
                    return null;
                }
                if (!tipoIdx.equals(ParserSym.terminalNames[ParserSym.ENT])) {
                    errores.add("El operador que se quiere usar como indice ("+idx+") no es un entero, es de tipo " + tipoIdx);
                    indicarLocalizacion(idx); // indice es otra cosa que un entero
                    return null;
                }
                if (error) {
                    return null;
                }
                // comprobacion de que el entero sea positivo???
                return tipoArr;
            }
            case MEMBER_ACCESS -> {
                SymbolOperand tupla = op.op;
                String tipoTupla = procesarOperando(tupla);
                if (tipoTupla == null) {
                    errores.add("Se realizan operaciones no validas ("+tupla+") en la tupla de la cual se quiere coger un miembro");
                    indicarLocalizacion(tupla);
                    return null;
                }
                String nombre = (String)tupla.value;//tipoTupla.substring(tipoTupla.indexOf(" ") + 1);
                DescripcionSimbolo ds = tablaSimbolos.consulta(nombre);
                if (ds == null) {
                    errores.add("La tupla "+nombre+" no esta declarada");
                    indicarLocalizacion(tupla);
                    return null;
                } else if (!ds.isTupla()) {
                    errores.add("Se ha intentado acceder a un miembro de una variable ("+nombre+") no tupla");
                    indicarLocalizacion(tupla);
                    return null;
                }
                DescripcionSimbolo miembro = ds.getMember(op.member);
                if (miembro == null){
                    errores.add("El miembro "+op.member+" no existe en la tupla "+nombre);
                    indicarLocalizacion(tupla);
                    return null;
                }
                return miembro.getTipo();
            }
            case CASTING -> {
                SymbolOperand operando = op.op;
                String casting = op.casting.getTipo();
                String tipo = procesarOperando(operando);
                if (tipo == null) {
                    errores.add("Se realizan operaciones no validas ("+operando+") en antes de aplicar el casting");
                    indicarLocalizacion(operando);
                    return null;
                }
                boolean opIntCharDouble = tipo.equals(ParserSym.terminalNames[ParserSym.ENT]) || tipo.equals(ParserSym.terminalNames[ParserSym.CAR]) ||tipo.equals(ParserSym.terminalNames[ParserSym.REAL]);
                boolean castIntCharDouble = casting.equals(ParserSym.terminalNames[ParserSym.ENT]) || casting.equals(ParserSym.terminalNames[ParserSym.CAR]) ||casting.equals(ParserSym.terminalNames[ParserSym.REAL]);
                if ((opIntCharDouble && castIntCharDouble) || (casting.equals(ParserSym.terminalNames[ParserSym.STRING]) && tipo.equals(ParserSym.terminalNames[ParserSym.CAR]))){ 
                    return casting; // casting posible entre int <-> char <-> double <-> int y de char -> string
                }
                errores.add("Se ha intentado realizar un casting no permitido ("+op.toString()+") de "+tipo+" a "+casting);
                indicarLocalizacion(operando);
                return null;
            }
        }
        return null; // error, no es ninguno de los casos
    }

}
