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
import java.util.List;
import jflex.base.Pair;

public class SemanticAnalysis {

    public TablaSimbolos tablaSimbolos;
    
    // Description to the function in which we are currently.
    private DescripcionSimbolo metodoActualmenteSiendoTratado;
    // When checking if a function's parameters are correct, we use this stack to store the declared function's types.
    // We use a stack because we will be taking elements out every time we process them.
    private Stack<SymbolTipoPrimitivo> currentArgs;
    private List<String> errores;
    public boolean thereIsError = false;

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
        SymbolScriptElemento elem = scriptElementosAntesDeMain.elemento;
        while (elem != null) {
            switch (elem.getTipo()) {
                case SymbolScriptElemento.DECS -> {
                    declaraciones.add(elem.declaraciones);
                    idMidDecs++;
                }
                case SymbolScriptElemento.TUPLA -> {
                    tuplas.add(elem);
                    idMidTuplas++;
                }
                case SymbolScriptElemento.METODO -> {
                    metodos.add(elem);
                    idMidMetodos++;
                }
            }
            scriptElementosAntesDeMain = scriptElementosAntesDeMain.siguienteElemento;
            elem = scriptElementosAntesDeMain.elemento;
        }
        SymbolMain scriptMainYElementos = scriptElementosAntesDeMain.main;
        elem = scriptMainYElementos.elemento;
        while (elem != null) {
            switch (elem.getTipo()) {
                case SymbolScriptElemento.DECS ->
                    declaraciones.add(idMidDecs, elem.declaraciones);
                case SymbolScriptElemento.TUPLA ->
                    tuplas.add(idMidTuplas, elem);
                case SymbolScriptElemento.METODO ->
                    metodos.add(idMidMetodos, elem);
            }
            scriptMainYElementos = scriptMainYElementos.siguienteElemento;
            elem = scriptMainYElementos.elemento;
        }
        if (scriptMainYElementos.main == null) { // main vacío
            return;
        }
        for (SymbolDecs decs : declaraciones) {
            procesarDeclaraciones(decs);
        }
        for (SymbolScriptElemento tupla : tuplas) {
            procesarDeclaracionTupla(tupla);
        }
        for (SymbolScriptElemento metodo : metodos) {
            DescripcionSimbolo d = new DescripcionSimbolo(metodo.tipoRetorno.getTipo());
            tablaSimbolos.poner(metodo.id, d);
        }
        for (SymbolScriptElemento metodo : metodos) {
            procesarDefinicionMetodo(metodo);
        }
        DescripcionSimbolo d = new DescripcionSimbolo();
        tablaSimbolos.poner(scriptMainYElementos.nombreMain, d);
        procesarMain(scriptMainYElementos);
    }

    private void procesarDeclaraciones(SymbolDecs decs) {
        SymbolDecAsigLista dec = decs.iddecslista;
        while (dec != null) {
            procesarDeclaracion(dec, decs.isConst, decs.tipo);
            dec = dec.siguienteDeclaracion;
        }
    }

    private void procesarDeclaracion(SymbolDecAsigLista dec, boolean isConst, SymbolTipo tipo) {
        boolean error = false;
        String id = dec.id;
        SymbolOperand valorAsignado = (dec.asignacion == null) ? null : dec.asignacion.operando;
        if (tablaSimbolos.contains(id)) {
            errores.add("El identificador " + id + " ya ha sido declarado con anterioridad");
            indicarLocalizacion(dec);
            error = true;
        }
        if (tipo.isArray() || tipo.isTupla()) {
            if (isConst) {
                errores.add(id + " se ha intentado declarar constante, cuando solo los tipos primitivos pueden serlo");
                indicarLocalizacion(dec);
                error = true;
            }
            if (valorAsignado != null) {
                errores.add("Se ha intentado asignar un valor a " + id + ", pero solo se les pueden asignar valores a los tipos primitivos");
                indicarLocalizacion(dec);
                error = true;
            }
        } else {
            String tipoValor = null;
            if (valorAsignado != null) {
                tipoValor = procesarOperando(valorAsignado);
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
        }
        if (!error) {
            DescripcionSimbolo d = new DescripcionSimbolo(tipo.getTipo(), isConst, valorAsignado != null);
            tablaSimbolos.poner(id, d);
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
                procesarDeclaraciones(instr.decs);
            case SymbolInstr.FCALL ->
                procesarLlamadaFuncion(instr.fcall);
            case SymbolInstr.RET ->
                procesarReturn(instr.ret);
            case SymbolInstr.SWAP ->
                procesarSwap(instr.swap);
        }
    }

    /**
     * pendiente
     */
    private void procesarAsignaciones(SymbolAsigs asigs) {

        while (asigs != null) {
            SymbolAsig asig = asigs.asig;
            DescripcionSimbolo d = tablaSimbolos.consulta(asig.id);
            if (d == null) {
                // error, no encontrado
            } else if (d.isArray() || d.isFunction() || d.isTupla()) {
                // error, asignación no permitida
            }
            Object valor = procesarOperando(asig.valor);
            if (valor == null) {
                // error
                return;
            }
            switch (asig.getTipo()) {
                case PRIMITIVA -> {
                    if ((Object) d.getTipo() != valor) {
                        // error se asigna tipo diferente
                    }

                }
                case ARRAY -> {

                }
                case TUPLA -> {
                    //HashMap<DescripcionSimbolo.Parametro, Boolean> tiposMiembros = d.getTiposMiembros();
                }
            }
            if (!asig.operacion.isBasicAsig()) {//&& d.getValor() == null) {
                // error operando con variable sin valor
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

            if (!tipoOp.equals(tipoParam.snd.getTipo())) {
                errores.add("Se ha intentado pasar por parámetro un operando de tipo " + tipoOp + " en el parámetro " + n + " que es de tipo " + tipoParam.snd.getTipo());
                indicarLocalizacion(fcall);
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
        String tipo = metodoActualmenteSiendoTratado.getTipoRetorno();
        SymbolOperand op = ret.op;
        if (op == null) {
            if (tipo == null) {
                return;
            }
            errores.add("Se ha realizado pop sin nada de una función que devuelve valores de tipo " + tipo);
            indicarLocalizacion(ret);
            return;
        }
        Object tipoOp = procesarOperando(op);
        if (tipoOp == null) {
            errores.add("Se ha intentado hacer pop de una operación no válida");
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
            procesarDeclaraciones(loopCond.decs);
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
        metodoActualmenteSiendoTratado = tablaSimbolos.consulta(metodo.id);
        DescripcionSimbolo d = new DescripcionSimbolo();
//        d.setValor(metodo.tipoRetorno);
//        d.setValor(metodo.parametros);
//        d.setValor(metodo.cuerpo); // está mal lo sé
        tablaSimbolos.poner(metodo.id, d);
        procesarBody(metodo.cuerpo);
        tablaSimbolos.salirBloque();
    }

    private void procesarDeclaracionTupla(SymbolScriptElemento tupla) {
        DescripcionSimbolo d = new DescripcionSimbolo();
//        d.setValor(tupla.miembrosTupla);
        tablaSimbolos.poner(tupla.id, d);
    }

    private void procesarMain(SymbolMain main) {
        SymbolBody body = main.main;
        tablaSimbolos.entraBloque();
        metodoActualmenteSiendoTratado = tablaSimbolos.consulta(main.nombreMain);
        String tipo = ParserSym.terminalNames[ParserSym.STRING] + " " + ParserSym.terminalNames[ParserSym.LBRACKET] + " "+ Integer.MAX_VALUE + " " + ParserSym.terminalNames[ParserSym.RBRACKET];
        DescripcionSimbolo d = new DescripcionSimbolo(tipo, false, true);
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
                    errores.add("Se realiza una operación no válida");
                    indicarLocalizacion(exp.op);
                    return null;
                }
                if (exp.isLeftUnaryOperator()) {
                    if (!tipo.equals(ParserSym.terminalNames[ParserSym.BOOL]) && !tipo.equals(ParserSym.terminalNames[ParserSym.INT])) {
                        errores.add("Se ha intentado realizar una operación no válida sobre un " + tipo); // error, no se puede operar si no es int ni bool
                        indicarLocalizacion(exp);
                        return null;
                    }
                    SymbolLUnaryOperator operator = exp.leftOp;
                    int operation = operator.unaryOperator;
                    if (tipo.equals(ParserSym.terminalNames[ParserSym.BOOL]) && ParserSym.OP_NOT != operation) {
                        errores.add("Se ha intentado realizar una operación no válida sobre un " + tipo); // no se puede operar booleano con inc/dec
                        indicarLocalizacion(exp);
                        return null;
                    } else if (tipo.equals(ParserSym.terminalNames[ParserSym.INT]) && (ParserSym.OP_INC != operation || ParserSym.OP_DEC != operation)) {
                        errores.add("Se ha intentado realizar una operación no válida sobre un " + tipo); // no se puede operar entero con not
                        indicarLocalizacion(exp);
                        return null;
                    }
                    return tipo;
                }
                if (!tipo.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && !tipo.equals(ParserSym.terminalNames[ParserSym.INT])) {
                    errores.add("Se ha intentado realizar una operación no válida sobre un " + tipo); // error, no se puede operar si no es int ni double
                    indicarLocalizacion(exp); 
                    return null;
                }
                SymbolRUnaryOperator operator = exp.rightOp;
                int operation = operator.unaryOperator;
                if (tipo.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && ParserSym.OP_PCT != operation) {
                    errores.add("Se ha intentado realizar una operación no válida sobre un " + tipo); // no se puede operar double con inc/dec
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
                    errores.add("El primer operando de la expresión binaria realiza una operación no válida");
                    indicarLocalizacion(exp);
                    error = true;
                }
                String tipo2 = procesarOperando(exp.op2);
                if (tipo2 == null) {
                    errores.add("El segundo operando de la expresión binaria realiza una operación no válida");
                    indicarLocalizacion(exp);
                    error = true;
                }
                if (error){
                    return null;
                }
                boolean unoIntOtroDouble = (tipo1.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && tipo2.equals(ParserSym.terminalNames[ParserSym.INT]))
                        || (tipo2.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && tipo1.equals(ParserSym.terminalNames[ParserSym.INT]));
                if (!tipo1.equals(tipo2) && !unoIntOtroDouble) {
                    errores.add("Se ha intentado realizar una operación ilegal entre "+tipo1 + " y "+tipo2+" en la expresión binaria");
                    indicarLocalizacion(exp);// error, no se puede operar con tipos diferentes (excepto int y double)
                    error = true;
                } else if (!SymbolTipoPrimitivo.isTipoPrimitivo(tipo1)) {
                    errores.add("Se ha intentado realizar una operación ilegal entre tipos no primitivos ("+tipo1 + ") en la expresión binaria");
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
                    errores.add("Se ha intentado realizar una operación ilegal entre tipos "+tipo1 + " y "+tipo2+" en la expresión binaria");
                    indicarLocalizacion(exp);// error, operando no válido para operar con ints y doubles
                    return null;
                }
                if (!operator.isForOperandsOfType(tipo1)) {
                    errores.add("Se ha intentado realizar una operación ilegal para los tipos "+tipo1 + " y "+tipo2+" en la expresión binaria");
                    indicarLocalizacion(exp);
                    return null; // error, operandos no pueden operar con operador
                }
                if (operator.doesOperationResultsInBoolean()) {
                    return ParserSym.terminalNames[ParserSym.BOOL];
                }
                return tipo1; // en caso contrario resulta en el mismo tipo
            }
            case CONDITIONAL_EXPRESSION -> {
                SymbolConditionalExpression exp = op.conditionalExp;
                String tipoCond = procesarOperando(exp.cond);
                if (tipoCond == null) {
                    errores.add("La condición de la expresión ternaria realiza una operación no válida");
                    indicarLocalizacion(exp.cond);
                } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.BOOL])) {
                    errores.add("La condición de la expresión ternaria no resulta en una proposición evualable como verdadera o falsa");
                    indicarLocalizacion(exp.cond); // error, no se puede utilizar de condición algo que no sea una proposición
                }
                boolean error = false;
                String tipo1 = procesarOperando(exp.caseTrue);
                if (tipo1 == null) {
                    errores.add("El operando a asignar en caso positivo realiza operaciones no válidas");
                    indicarLocalizacion(exp.caseTrue);
                    error = true;
                }
                String tipo2 = procesarOperando(exp.caseFalse);
                if (tipo2 == null) {
                    errores.add("El operando a asignar en caso negativo realiza operaciones no válidas");
                    indicarLocalizacion(exp.caseFalse);
                    error = true;
                }
                if (error) {
                    return null;
                }
                if (!tipo1.equals(tipo2)) {
                    errores.add("No se puede asignar con tipos diferentes en la operación ternaria");
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
                    errores.add("Se realizan operaciones no válidas en el array del cual se quiere coger un índice");
                    indicarLocalizacion(arr);
                    error = true;
                }
                if (!error && !tipoArr.endsWith(ParserSym.terminalNames[ParserSym.RBRACKET])) {
                    errores.add("El operador del cual se quiere coger un índice no es un array, es de tipo " + tipoArr);
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
                                errores.add("El tipo del array es de una tupla no declarada (" + tipo+")");
                                indicarLocalizacion(arr); // operador a la izquierda no termina siendo un array
                            }
                        }
                    }
                }
                SymbolOperand idx = op.idxArr;
                String tipoIdx = procesarOperando(idx);
                if (tipoIdx == null) {
                    errores.add("Se realizan operaciones no válidas en el cálculo del índice");
                    indicarLocalizacion(idx);
                    return null;
                }
                if (!tipoIdx.equals(ParserSym.terminalNames[ParserSym.INT])) {
                    errores.add("El operador que se quiere usar como índice no es un entero, es de tipo " + tipoIdx);
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
                    errores.add("Se realizan operaciones no válidas en en la tupla de la cual se quiere coger un miembro");
                    indicarLocalizacion(tupla);
                    return null;
                }
                String nombre = tipoTupla.substring(tipoTupla.indexOf(" ") + 1);
                DescripcionSimbolo ds = tablaSimbolos.consulta(nombre);
                if (ds == null) {
                    errores.add("La tupla "+nombre+" no está declarada");
                    indicarLocalizacion(tupla);
                    return null;
                }
                if (!ds.isTupla()) {
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
                // comprobación de que el entero sea positivo???
                return miembro.getTipo();
            }
        }
        return null; // error, no es ninguno de los casos
    }

}
