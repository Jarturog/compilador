/**
 * Asignatura: 21780 - Compiladores
 * Miembros:
 * 	- Korn, Andreas Manuel
 * 	- Román Colom, Marc
 * 	- Vilella Candía, Joan
 */
package analizadorSemantico;

import analizadorSintactico.ParserSym;
import java.util.ArrayList;
import java.util.Stack;

import analizadorSintactico.symbols.*;
import java.util.HashMap;
import java.util.List;
import jflex.base.Pair;

public class SemanticAnalysis {

    public TablaSimbolos tablaSimbolos;
    
    // Description to the function in which we are currently.
    private Pair<String, DescripcionSimbolo> metodoActualmenteSiendoTratado;
    // When checking if a function's parameters are correct, we use this stack to store the declared function's types.
    // We use a stack because we will be taking elements out every time we process them.
    private Stack<SymbolTipoPrimitivo> currentArgs;
    private List<String> errores;
    
    public String getErrors() {
        String s = "";
        for (String e : errores) {
            s += e + "\n";
        }
        return s;
    }
    
    private void indicarLocalizacion(SymbolBase s) {
        String loc = "ERROR: Desde la linea " + s.xleft.getLine() + " y columna " + s.xleft.getColumn() + " hasta la linea " + s.xright.getLine() + " y columna " + s.xright.getColumn() + ": ";
        int idx = errores.size() - 1;
        errores.set(idx, loc + errores.get(idx));
    }

    public SemanticAnalysis(SymbolScript scriptElementosAntesDeMain) {
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
        // elementos después del main
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
        // tuplas
        for (SymbolScriptElemento tupla : tuplas) {
            DescripcionSimbolo d = new DescripcionSimbolo(new HashMap<String, DescripcionSimbolo>());
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
            DescripcionSimbolo d = new DescripcionSimbolo(metodo.tipoRetorno.getTipo());
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

    private void procesarDeclaraciones(SymbolDecs decs, DescripcionSimbolo tupla) {
        SymbolDecAsigLista dec = decs.iddecslista;
        while (dec != null) {
            SymbolTipo tipo = decs.tipo;
            boolean error = false;
            
            String id = dec.id;
            SymbolOperand valorAsignado = (dec.asignacion == null) ? null : dec.asignacion.operando;
            if (tablaSimbolos.contains(id)) {
                errores.add("El identificador " + id + " ya ha sido declarado con anterioridad");
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
                if(tipo.isArray()){
                    SymbolDimensiones dim = tipo.dimArray;
                    int n = 0;
                    while(dim != null) {
                        n++;
                        String tipoIdx = procesarOperando(dim.operando);
                        if (tipoIdx == null) {
                            errores.add("Se realizan operaciones no válidas para el cálculo del índice "+n+" del array "+id);
                            indicarLocalizacion(dim.operando);
                            error = true;
                        } else if (!tipoIdx.equals(ParserSym.terminalNames[ParserSym.INT])) {
                            errores.add("Las operaciones para el cálculo del índice "+n+" del array " + id + " resultan en " + tipoIdx + ", cuando tendría que ser un entero");
                            indicarLocalizacion(dim.operando);
                            error = true;
                        }
                        dim = dim.siguienteDimension;
                    }
                }
            } else if (valorAsignado != null) {
                String tipoValor = procesarOperando(valorAsignado);
                if (tipoValor == null) {
                    errores.add("Los valores del operando no son compatibles");
                    indicarLocalizacion(valorAsignado);
                    error = true;
                } else if (!tipoValor.equals(tipo.getTipo())) {
                    errores.add("El tipo con el que se ha declarado no es compatible con el que se está intentado asignar a la variable");
                    indicarLocalizacion(valorAsignado);
                    error = true;
                }
            }
            if (!error) {
                DescripcionSimbolo d = new DescripcionSimbolo(tipo.getTipo(), decs.isConst, valorAsignado != null, tupla != null);
                tablaSimbolos.poner(id, d);
                if (tupla != null) {
                    tupla.añadirMiembro(id, d);
                }
            }
            dec = dec.siguienteDeclaracion;
        }
    }

    private void procesarBody(SymbolBody body) {
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

    private void procesarInstruccion(SymbolInstr instr) {
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

    private void procesarAsignaciones(SymbolAsigs asigs) {
        while (asigs != null) {
            boolean error = false, errorOperandoInvalido = false;
            SymbolAsig asig = asigs.asig;
            DescripcionSimbolo d = tablaSimbolos.consulta(asig.id);
            if (d == null) {
                errores.add("La variable " + asig.id + " no ha sido declarado con anterioridad");
                indicarLocalizacion(asig);
                error = true;
            } else if (d.isFunction()) {
                errores.add("La variable " + asig.id + " corresponde a una función y no a una variable");
                indicarLocalizacion(asig);
                error = true;
            }
            String tipoValor = procesarOperando(asig.valor);
            if (tipoValor == null) {
                errores.add("Se realizan operaciones no válidas en el valor a asignar a " + asig.id);
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
                        errores.add("Se realizan operaciones no válidas en el índice del array " + asig.id);
                        indicarLocalizacion(asig.idx);
                        error = true;
                    } else if (!idx.equals(ParserSym.terminalNames[ParserSym.INT])) {
                        errores.add("El índice del array " + asig.id + " no resulta en un entero");
                        indicarLocalizacion(asig.idx);
                        error = true;
                    }
                    if (!error) {
                        tipoVariable = d.getTipoArray();
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
                            errores.add("El miembro " + asig.miembro + " no ha sido encontrado en la tupla " + asig.id);
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
                errores.add("Se está intentado asignar un valor de tipo "+tipoValor+" a una variable de tipo " + tipoVariable);
                indicarLocalizacion(asig.valor);
                error = true;
            }
            if (!error && !asig.operacion.doesOperationResultInSameType(tipoValor)) {
                errores.add("Se está intentado aplicar una operación no válida entre los tipos "+tipoVariable+" y "+ tipoValor);
                indicarLocalizacion(asig);
                error = true;
            }
            if (error) {
                asigs = asigs.siguienteAsig;
                continue;
            }
            switch (asig.getTipo()) {
                case PRIMITIVA -> {
                    if (d.tieneValorAsignado() && d.isConstante()) {
                        errores.add("Se está intentado asignar un valor a la constante "+ asig.id+" que ya tenía valor");
                        indicarLocalizacion(asig);
                        error = true;
                    }
                    d.asignarValor();
                }
                case ARRAY -> {
                    // do nothing
                }
                case TUPLA -> {
                    DescripcionSimbolo miembro = d.getMember(asig.miembro);
                    if (miembro.tieneValorAsignado() && miembro.isConstante()) {
                        errores.add("Se está intentado asignar un valor al miembro constante "+ asig.miembro+" de la tupla "+asig.id+", el cual ya tenía un valor asignado");
                        indicarLocalizacion(asig);
                        error = true;
                    }
                    miembro.asignarValor();
                }
            }
            asigs = asigs.siguienteAsig;
        }

    }

    private void procesarLlamadaFuncion(SymbolFCall fcall) {
        String nombre = (String) fcall.methodName.value;
        DescripcionSimbolo ds = tablaSimbolos.consulta(nombre);
        if (ds == null) {
            errores.add("La función " + nombre + " no ha sido declarada");
            indicarLocalizacion(fcall);
            return;
        }
        if (!ds.isFunction()) {
            errores.add("El identificador " + nombre + " pertenece a una variable, no una función");
            indicarLocalizacion(fcall);
            return;
        }
        ArrayList<Pair<String, DescripcionSimbolo>> params = ds.getTiposParametros();
        SymbolOperandsLista opLista = fcall.operandsLista;
        int n = 0;
        for (Pair<String, DescripcionSimbolo> tipoParam : params) {
            n++;
            if (opLista == null) {
                errores.add("La función " + nombre + " tiene " + params.size() + " parámetros, pero se han pasado más al momento de llamarla");
                indicarLocalizacion(fcall);
                return;
            }
            SymbolOperand op = opLista.operand;
            String tipoOp = procesarOperando(op);
            if (tipoOp == null) {
                errores.add("Se ha intentado pasar por parámetros operacions no válidas en el parámetro " + n + " de la función "+nombre);
                indicarLocalizacion(op);
            } else if (!tipoOp.equals(tipoParam.snd.getTipo())) {
                errores.add("Se ha intentado pasar por parámetro un operando de tipo " + tipoOp + " en el parámetro " + n + " de la función "+nombre+" que es de tipo " + tipoParam.snd.getTipo());
                indicarLocalizacion(op);
                return;
            }
            opLista = opLista.siguienteOperando;
        }
        if (opLista != null) {
            errores.add("La función " + nombre + " tiene " + params.size() + " parámetros, pero se han pasado " + n);
            indicarLocalizacion(fcall);
        }
    }

    private void procesarReturn(SymbolReturn ret) {
        String tipo = metodoActualmenteSiendoTratado.snd.getTipoRetorno();
        SymbolOperand op = ret.op;
        if (op == null) {
            if (tipo == null) {
                return;
            }
            errores.add("Se ha realizado pop sin nada de la función "+metodoActualmenteSiendoTratado.fst+" que devuelve valores de tipo " + tipo);
            indicarLocalizacion(ret);
            return;
        }
        Object tipoOp = procesarOperando(op);
        if (tipoOp == null) {
            errores.add("Se ha intentado hacer pop de una operación no válida en la función " + metodoActualmenteSiendoTratado.fst);
            indicarLocalizacion(ret);
        } else if (tipoOp != tipo) {
            errores.add("Se ha intentado hacer pop de un tipo (" + tipoOp + ") diferente al que devuelve la función (" + tipo + ")");
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

    private void procesarIf(SymbolIf cond) {
        String tipoCond = procesarOperando(cond.cond);
        if (tipoCond == null) {
            errores.add("La condición del 'si' realiza una operación no válida");
            indicarLocalizacion(cond.cond);
        } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.BOOL])){
            errores.add("La condición del 'si' no resulta en una proposición evualable como verdadera o falsa");
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
                errores.add("La condición del 'sino' realiza una operación no válida");
                indicarLocalizacion(elif.cond);
            } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.BOOL])){
                errores.add("La condición del 'sino' no resulta en una proposición evualable como verdadera o falsa");
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

    private void procesarLoop(SymbolLoop loop) {
        // no importa que sea while o do while
        tablaSimbolos.entraBloque();
        SymbolLoopCond loopCond = loop.loopCond;
        if (loopCond.decs != null) {
            procesarDeclaraciones(loopCond.decs, null);
        }
        String tipoCond = procesarOperando(loopCond.cond);
        if (tipoCond == null) {
            errores.add("La condición del 'loop' realiza una operación no válida");
            indicarLocalizacion(loopCond.cond);
        } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.BOOL])){
            errores.add("La condición del 'loop' no resulta en una proposición evualable como verdadera o falsa");
            indicarLocalizacion(loopCond.cond);
        }
        if (loopCond.asig != null) {
            procesarAsignaciones(loopCond.asig);
        }
        procesarBody(loop.cuerpo);
        tablaSimbolos.salirBloque();
    }

    private void procesarSwitch(SymbolSwitch sw) {
        tablaSimbolos.entraBloque();
        Object tipo1 = procesarOperando(sw.cond);
        if (tipo1 == null) {
            errores.add("La operación del 'select' realiza una operación no válida");
            indicarLocalizacion(sw.cond);
        }
        SymbolCaso caso = sw.caso;
        int n = 0;
        while (caso != null) {
            n++;
            Object tipo2 = procesarOperando(caso.cond);
            if (tipo2 == null) {
                errores.add("La operación del 'caso' realiza una operación no válida");
                indicarLocalizacion(caso.cond);
            } else if (tipo1 != null && tipo1 != tipo2) {
                errores.add("Los tipos de la condición ("+tipo1+") del select y del caso "+n+" ("+tipo2+") no coinciden");
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

    private void procesarDefinicionMetodo(SymbolScriptElemento metodo) {
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
                errores.add("El parámetro " + nombreParam + " tiene el mismo nombre que el método en el que está siendo declarado");
                indicarLocalizacion(params);
                params = params.siguienteParam;
                continue;
            }
            DescripcionSimbolo dFuncion = metodoActualmenteSiendoTratado.snd;
            DescripcionSimbolo dParam = new DescripcionSimbolo(params.param.getTipo(), false, false, false);
            dFuncion.añadirParametro(nombreParam, dParam);
            tablaSimbolos.poner(nombreParam, dParam);
            
            params = params.siguienteParam;
        }
    }
    
    private void procesarDeclaracionTupla(SymbolScriptElemento tupla) {
        SymbolMiembrosTupla miembros = tupla.miembrosTupla;
        DescripcionSimbolo d = tablaSimbolos.consulta(tupla.id);
        while (miembros != null) {
            procesarDeclaraciones(miembros.decs, d);
            miembros = miembros.siguienteDeclaracion;
        }
    }

    private void procesarMain(SymbolMain main) {
        SymbolBody body = main.main;
        tablaSimbolos.entraBloque();
        metodoActualmenteSiendoTratado = new Pair(main.nombreMain, tablaSimbolos.consulta(main.nombreMain));
        String tipo = ParserSym.terminalNames[ParserSym.STRING] + " " + main.lBracket + " "+ Integer.MAX_VALUE + " " + main.rBracket;
        DescripcionSimbolo d = new DescripcionSimbolo(tipo, false, true, false);
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
    private String procesarOperando(SymbolOperand op) {
        switch (op.getTipo()) {
            case ATOMIC_EXPRESSION -> {
                SymbolAtomicExpression literal = op.atomicExp;
                return literal.tipo;
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
                    errores.add("Se realizan operaciones no válidas en " + exp.op);
                    indicarLocalizacion(exp.op);
                    return null;
                }
                if (exp.isLeftUnaryOperator()) {
                    if (!tipo.equals(ParserSym.terminalNames[ParserSym.BOOL]) && !tipo.equals(ParserSym.terminalNames[ParserSym.INT])) {
                        errores.add("Se ha intentado realizar una operación "+exp.op+" no válida sobre un " + tipo); // error, no se puede operar si no es int ni bool
                        indicarLocalizacion(exp);
                        return null;
                    }
                    SymbolLUnaryOperator operator = exp.leftOp;
                    int operation = operator.unaryOperator;
                    if (tipo.equals(ParserSym.terminalNames[ParserSym.BOOL]) && ParserSym.OP_NOT != operation) {
                        errores.add("Se ha intentado realizar una operación "+exp.op+" no válida sobre un " + tipo); // no se puede operar booleano con inc/dec
                        indicarLocalizacion(exp);
                        return null;
                    } else if (tipo.equals(ParserSym.terminalNames[ParserSym.INT]) && (ParserSym.OP_INC != operation || ParserSym.OP_DEC != operation)) {
                        errores.add("Se ha intentado realizar una operación "+exp.op+" no válida sobre un " + tipo); // no se puede operar entero con not
                        indicarLocalizacion(exp);
                        return null;
                    }
                    return tipo;
                }
                if (!tipo.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && !tipo.equals(ParserSym.terminalNames[ParserSym.INT])) {
                    errores.add("Se ha intentado realizar una operación "+exp.op+" no válida sobre un " + tipo); // error, no se puede operar si no es int ni double
                    indicarLocalizacion(exp); 
                    return null;
                }
                SymbolRUnaryOperator operator = exp.rightOp;
                int operation = operator.unaryOperator;
                if (tipo.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && ParserSym.OP_PCT != operation) {
                    errores.add("Se ha intentado realizar una operación "+exp.op+" no válida sobre un " + tipo); // no se puede operar double con inc/dec
                    indicarLocalizacion(exp);
                    return null;
                } else if (tipo.equals(ParserSym.terminalNames[ParserSym.INT]) && (ParserSym.OP_INC != operation || ParserSym.OP_DEC != operation)) {
                    return ParserSym.terminalNames[ParserSym.DOUBLE]; // !!! se puede operar entero con OP_PCT
                }
                return tipo;
            }
            case BINARY_EXPRESSION -> {
                SymbolBinaryExpression exp = op.binaryExp;
                String tipo1 = procesarOperando(exp.op1);
                boolean error = false;
                if (tipo1 == null) {
                    errores.add("El primer operando de la expresión binaria realiza una operación "+exp.op1+" no válida");
                    indicarLocalizacion(exp);
                    error = true;
                }
                String tipo2 = procesarOperando(exp.op2);
                if (tipo2 == null) {
                    errores.add("El segundo operando de la expresión binaria realiza una operación "+exp.op2+" no válida");
                    indicarLocalizacion(exp);
                    error = true;
                }
                if (error){
                    return null;
                }
                boolean unoIntOtroDouble = (tipo1.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && tipo2.equals(ParserSym.terminalNames[ParserSym.INT]))
                        || (tipo2.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && tipo1.equals(ParserSym.terminalNames[ParserSym.INT]));
                if (!tipo1.equals(tipo2) && !unoIntOtroDouble) {
                    errores.add("Se ha intentado realizar una operación ilegal "+exp.bop.value+" entre "+tipo1 + " y "+tipo2+" en la expresión binaria "+exp);
                    indicarLocalizacion(exp);// error, no se puede operar con tipos diferentes (excepto int y double)
                    error = true;
                } else if (!SymbolTipoPrimitivo.isTipoPrimitivo(tipo1)) {
                    errores.add("Se ha intentado realizar una operación ilegal "+exp.bop.value+" entre tipos no primitivos ("+tipo1 + ") en la expresión binaria "+exp);
                    indicarLocalizacion(exp);// error, no se puede operar con tuplas y arrays
                    error = true;
                }
                if (error){
                    return null;
                }
                SymbolBinaryOperator operator = exp.bop;
                if (unoIntOtroDouble) {
                    if (operator.isForOperandsOfType(ParserSym.terminalNames[ParserSym.DOUBLE])) {
                        return ParserSym.terminalNames[ParserSym.DOUBLE];
                    }
                    errores.add("Se ha intentado realizar una operación ilegal "+exp.bop.value+" entre tipos "+tipo1 + " y "+tipo2+" en la expresión binaria "+exp);
                    indicarLocalizacion(exp);// error, operando no válido para operar con ints y doubles
                    return null;
                }
                if (!operator.isForOperandsOfType(tipo1)) {
                    errores.add("Se ha intentado realizar una operación ilegal "+exp.bop.value+" para los tipos "+tipo1 + " y "+tipo2+" en la expresión binaria "+exp);
                    indicarLocalizacion(exp);
                    return null; // error, operandos no pueden operar con operador
                }
                if (operator.doesOperationResultInBoolean()) {
                    return ParserSym.terminalNames[ParserSym.BOOL];
                }
                return tipo1; // en caso contrario resulta en el mismo tipo
            }
            case CONDITIONAL_EXPRESSION -> {
                SymbolConditionalExpression exp = op.conditionalExp;
                String tipoCond = procesarOperando(exp.cond);
                if (tipoCond == null) {
                    errores.add("La condición "+exp.cond+" de la expresión ternaria "+exp+" realiza una operación no válida");
                    indicarLocalizacion(exp.cond);
                } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.BOOL])) {
                    errores.add("La condición "+exp.cond+" de la expresión ternaria "+exp+" no resulta en una proposición evualable como verdadera o falsa, sino en " + tipoCond);
                    indicarLocalizacion(exp.cond); // error, no se puede utilizar de condición algo que no sea una proposición
                }
                boolean error = false;
                String tipo1 = procesarOperando(exp.caseTrue);
                if (tipo1 == null) {
                    errores.add("El operando a asignar en caso positivo "+exp.caseTrue+" de la expresión ternaria "+exp+" realiza operaciones no válidas");
                    indicarLocalizacion(exp.caseTrue);
                    error = true;
                }
                String tipo2 = procesarOperando(exp.caseFalse);
                if (tipo2 == null) {
                    errores.add("El operando a asignar en caso negativo "+exp.caseFalse+" de la expresión ternaria "+exp+" realiza operaciones no válidas");
                    indicarLocalizacion(exp.caseFalse);
                    error = true;
                }
                if (error) {
                    return null;
                }
                if (!tipo1.equals(tipo2)) {
                    errores.add("No se puede operar con tipos diferentes (" + tipo1 + ", " +tipo2+ ") en la operación ternaria "+exp);
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
                    errores.add("Se realizan operaciones no válidas en el array "+arr+" del cual se quiere coger un índice");
                    indicarLocalizacion(arr);
                    error = true;
                }
                if (!error && !tipoArr.endsWith(ParserSym.terminalNames[ParserSym.RBRACKET])) {
                    errores.add("El operador "+arr+" del cual se quiere coger un índice no es un array, es de tipo " + tipoArr);
                    indicarLocalizacion(arr); // operador a la izquierda no termina siendo un array
                    error = true;
                }
                if (!error) {
                    tipoArr = tipoArr.substring(0, tipoArr.lastIndexOf(" "));
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
                    errores.add("Se realizan operaciones no válidas ("+idx+") en el cálculo del índice");
                    indicarLocalizacion(idx);
                    return null;
                }
                if (!tipoIdx.equals(ParserSym.terminalNames[ParserSym.INT])) {
                    errores.add("El operador que se quiere usar como índice ("+idx+") no es un entero, es de tipo " + tipoIdx);
                    indicarLocalizacion(idx); // indice es otra cosa que un entero
                    return null;
                }
                if (error) {
                    return null;
                }
                // comprobación de que el entero sea positivo???
                return tipoArr;
            }
            case MEMBER_ACCESS -> {
                SymbolOperand tupla = op.op;
                String tipoTupla = procesarOperando(tupla);
                if (tipoTupla == null) {
                    errores.add("Se realizan operaciones no válidas ("+tupla+") en en la tupla de la cual se quiere coger un miembro");
                    indicarLocalizacion(tupla);
                    return null;
                }
                String nombre = tipoTupla.substring(tipoTupla.indexOf(" ") + 1);
                DescripcionSimbolo ds = tablaSimbolos.consulta(nombre);
                if (ds == null) {
                    errores.add("La tupla "+nombre+" no está declarada");
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
        }
        return null; // error, no es ninguno de los casos
    }

}
