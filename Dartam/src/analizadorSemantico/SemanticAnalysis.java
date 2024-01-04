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
import jflex.base.Pair;

public class SemanticAnalysis {

    public TablaSimbolos tablaSimbolos;

    // Description to the function in which we are currently.
    private DescripcionSimbolo metodoActualmenteSiendoTratado;
    // When checking if a function's parameters are correct, we use this stack to store the declared function's types.
    // We use a stack because we will be taking elements out every time we process them.
    private Stack<SymbolTipoPrimitivo> currentArgs;
    private boolean returnFound;

    private ArrayList<String> errors;
    public boolean thereIsError = false;

    public String getErrors() {
        String s = "";
        for (String e : errors) {
            s += e + "\n";
        }
        return s;
    }

    private void reportError(String errorMessage, int line, int column) {
        thereIsError = true;
        errorMessage = " !! Semantic error: " + errorMessage + " at position [line: " + line + ", column: " + column + "]";
        System.err.println(errorMessage);
        errors.add(errorMessage);
    }

    public SemanticAnalysis(SymbolScript scriptElementosAntesDeMain) {
        tablaSimbolos = new TablaSimbolos();
        errors = new ArrayList<>();
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
            procesarDeclaracion(dec.id,
                    (dec.asignacion == null) ? null : dec.asignacion.operando,
                    decs.isConst,
                    decs.tipo);
            dec = dec.siguienteDeclaracion;
        }
    }

    private void procesarDeclaracion(String id, SymbolOperand valorAsignado, boolean isConst, SymbolTipo tipo) {
        if (tablaSimbolos.contains(id)) {
            // error
            return;
        }
        if (tipo.isArray() || tipo.isTupla()) {
            if (isConst) {
                // error
                return;
            }
            if (valorAsignado != null) {
                // error
                return;
            }
        } else {
            String tipoValor = null;
            if (valorAsignado != null) {
                tipoValor = procesarOperando(valorAsignado);
                if (tipoValor == null) {
                    // error
                    return;
                } else if (!tipoValor.equals(tipo.getTipo())) {
                    // error
                    return;
                }
            }
        }
        DescripcionSimbolo d = new DescripcionSimbolo(tipo.getTipo(), isConst, valorAsignado != null);
        tablaSimbolos.poner(id, d);
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

    private void procesarAsignaciones(SymbolAsigs asigs) {

        do {
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
        } while (asigs != null);

    }

    private void procesarLlamadaFuncion(SymbolFCall fcall) {
        String nombre = (String) fcall.methodName.value;
        DescripcionSimbolo ds = tablaSimbolos.consulta(nombre);
        if (!ds.isFunction()) {
            // error
        }
        ArrayList<Pair<String, DescripcionSimbolo>> params = ds.getTiposParametros();
        SymbolOperandsLista opLista = fcall.operandsLista;
        for (Pair<String, DescripcionSimbolo> tipoParam : params) {
            if (opLista == null) {
                // error, hay más operandos que parámetros
            }
            SymbolOperand op = opLista.operand;
            Object tipoOp = procesarOperando(op);

            if (tipoOp != tipoParam.snd.getTipo()) {
                // error
                return;
            }
            opLista = opLista.operandsLista;
        }
        if (opLista != null) {
            // error, hay más parámetros que operandos
        }

    }

    private void procesarReturn(SymbolReturn ret) {
        String tipo = metodoActualmenteSiendoTratado.getTipoRetorno();
        SymbolOperand op = ret.op;
        if (op == null) {
            if (tipo == null) {
                return;
            }
            // error
        }
        Object tipoOp = procesarOperando(op);
        if (tipoOp == null) {
            // error
        }
        if (tipoOp != tipo) {
            // error
        }
    }

    private void procesarSwap(SymbolSwap swap) {

        DescripcionSimbolo ds1 = tablaSimbolos.consulta(swap.op1);
        if (ds1 == null) {

        }
        DescripcionSimbolo ds2 = tablaSimbolos.consulta(swap.op2);
        if (ds2 == null) {

        }
        //if (ds1.getValor() != ds2.getValor()) {

        //}
    }

    /**
     * Comprobar que las condiciones del if y elifs respeten los tipos y sus
     * cuerpos
     *
     * @param cond
     */
    private void procesarIf(SymbolIf cond) {
        if (procesarOperando(cond.cond) == null) {

        }
        tablaSimbolos.entraBloque();
        procesarBody(cond.cuerpo);
        tablaSimbolos.salirBloque();
        SymbolElifs elifs = cond.elifs;
        while (elifs != null) {
            SymbolElif elif = elifs.elif;
            if (procesarOperando(elif.cond) == null) {

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
        if (procesarOperando(loopCond.cond) == null) {

        }
        if (loopCond.op2 != null && procesarOperando(loopCond.op2) == null) {
            // error
        }
        procesarBody(loop.cuerpo);
        tablaSimbolos.salirBloque();
    }

    private void procesarSwitch(SymbolSwitch sw) {
        tablaSimbolos.entraBloque();
        Object tipo1 = procesarOperando(sw.cond);
        if (tipo1 == null) {

        }
        SymbolCaso caso = sw.caso;
        while (caso != null) {
            Object tipo2 = procesarOperando(caso.cond);
            if (tipo2 == null) {

            }
            if (tipo1 != tipo2) {

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
                SymbolMetodoNombre nombre = fcall.methodName;
                if (nombre.isSpecialMethod()) {
                    return ParserSym.terminalNames[ParserSym.INT]; // los metodos especiales devuelven enteros
                }
                DescripcionSimbolo ds = tablaSimbolos.consulta((String) nombre.value);
                if (ds == null) {
                    // error, función que se llama no se ha encontrado
                    return null;
                }
                return ds.getTipo();
            }
            case OP_BETWEEN_PAREN -> {
                return procesarOperando(op.op);
            }
            case UNARY_EXPRESSION -> {
                SymbolUnaryExpression exp = op.unaryExp;
                String tipo = procesarOperando(exp.op);
                if (tipo == null) {
                    return null;
                }
                if (exp.isLeftUnaryOperator()) {
                    if (!tipo.equals(ParserSym.terminalNames[ParserSym.BOOL]) && !tipo.equals(ParserSym.terminalNames[ParserSym.INT])) {
                        // error, no se puede operar si no es int ni bool
                        return null;
                    }
                    SymbolLUnaryOperator operator = exp.leftOp;
                    int operation = operator.unaryOperator;
                    if (tipo.equals(ParserSym.terminalNames[ParserSym.BOOL]) && ParserSym.OP_NOT != operation) {
                        // no se puede operar booleano con inc/dec
                        return null;
                    } else if (tipo.equals(ParserSym.terminalNames[ParserSym.INT]) && (ParserSym.OP_INC != operation || ParserSym.OP_DEC != operation)) {
                        // no se puede operar entero con not
                        return null;
                    }
                    return tipo;
                }
                if (!tipo.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && !tipo.equals(ParserSym.terminalNames[ParserSym.INT])) {
                    // error, no se puede operar si no es int ni double
                    return null;
                }
                SymbolRUnaryOperator operator = exp.rightOp;
                int operation = operator.unaryOperator;
                if (tipo.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && ParserSym.OP_PCT != operation) {
                    // no se puede operar double con inc/dec
                    return null;
                } else if (tipo.equals(ParserSym.terminalNames[ParserSym.INT]) && (ParserSym.OP_INC != operation || ParserSym.OP_DEC != operation)) {
                    // !!! se puede operar entero con OP_PCT
                    return ParserSym.terminalNames[ParserSym.DOUBLE];
                }
                return tipo;
            }
            case BINARY_EXPRESSION -> {
                SymbolBinaryExpression exp = op.binaryExp;
                String tipo1 = procesarOperando(exp.op1);
                if (tipo1 == null) {
                    // error, operación no permitida
                    return null;
                }
                String tipo2 = procesarOperando(exp.op2);
                if (tipo2 == null) {
                    // error, operación no permitida
                    return null;
                }
                boolean unoIntOtroDouble = (tipo1.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && tipo2.equals(ParserSym.terminalNames[ParserSym.INT]))
                        || (tipo2.equals(ParserSym.terminalNames[ParserSym.DOUBLE]) && tipo1.equals(ParserSym.terminalNames[ParserSym.INT]));
                if (!tipo1.equals(tipo2) && !unoIntOtroDouble) {
                    // error, no se puede operar con tipos diferentes (excepto int y double)
                    return null;
                } else if (!SymbolTipoPrimitivo.isTipoPrimitivo(tipo1)) {
                    // error, no se puede operar con tuplas y arrays
                    return null;
                }
                SymbolBinaryOperator operator = exp.bop;
                if (unoIntOtroDouble) {
                    if (operator.isForOperandsOfType(ParserSym.terminalNames[ParserSym.DOUBLE])) {
                        return ParserSym.terminalNames[ParserSym.DOUBLE];
                    }
                    // error, operando no válido para operar con ints y doubles
                    return null;
                }
                if (!operator.isForOperandsOfType(tipo1)) {
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
                    // error, operación no permitida
                    return null;
                } else if (!tipoCond.equals(ParserSym.terminalNames[ParserSym.BOOL])) {
                    // error, no se puede utilizar de condición algo que no sea una proposición
                    return null;
                }
                String tipo1 = procesarOperando(exp.caseTrue);
                if (tipo1 == null) {
                    // error, operación no permitida
                    return null;
                }
                String tipo2 = procesarOperando(exp.caseFalse);
                if (tipo2 == null) {
                    // error, operación no permitida
                    return null;
                }
                if (!tipo1.equals(tipo2)) {
                    // error, no se puede asignar con tipos diferentes
                    return null;
                }
                return tipo1;
            }
            case IDX_ARRAY -> {
                SymbolOperand arr = op.op;
                String tipoArr = procesarOperando(arr);
                if (tipoArr == null) {
                    // error, operación no permitida
                    return null;
                }
                if (!tipoArr.endsWith(ParserSym.terminalNames[ParserSym.RBRACKET])) {
                    // operador a la izquierda no termina siendo un array
                    return null;
                }
                tipoArr = tipoArr.substring(0, tipoArr.lastIndexOf(" "));
                if (tipoArr.startsWith(ParserSym.terminalNames[ParserSym.KW_TUPLE])) {
                    String tipoPrimitivo = tipoArr.substring(tipoArr.indexOf(" ") + 1);
                    if (!SymbolTipoPrimitivo.isTipoPrimitivo(tipoPrimitivo)) {
                        DescripcionSimbolo ds = tablaSimbolos.consulta(tipoPrimitivo);
                        if (ds == null) {
                            // error, tupla no encontrada
                            return null;
                        }
                    }
                }
                SymbolOperand idx = op.idxArr;
                String tipoIdx = procesarOperando(idx);
                if (tipoIdx == null) {
                    // error, operación no permitida
                    return null;
                }
                if (!tipoIdx.equals(ParserSym.terminalNames[ParserSym.INT])) {
                    // indice es otra cosa que un entero
                    return null;
                }
                // comprobación de que el entero sea positivo???
                return tipoArr;
            }
            case MEMBER_ACCESS -> {
                SymbolOperand tupla = op.op;
                String tipoTupla = procesarOperando(tupla);
                if (tipoTupla == null) {
                    // error, operación no permitida
                    return null;
                }
                DescripcionSimbolo ds = tablaSimbolos.consulta(tipoTupla.substring(tipoTupla.indexOf(" ") + 1));
                if (ds == null) {
                    // error, tupla no encontrada
                    return null;
                }
                if (!ds.isTupla()) {
                    // error
                    return null;
                }
                DescripcionSimbolo miembro = ds.getMember(op.member);
                if (miembro == null){
                    // error, miembro no encontrado
                    return null;
                }
                // comprobación de que el entero sea positivo???
                return miembro.getTipo();
            }
        }
        return null; // error, no es ninguno de los casos
    }

}
