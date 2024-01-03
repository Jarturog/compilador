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

import analizadorSintactico.symbols.SymbolBase;
import analizadorSintactico.symbols.SymbolBody;
import analizadorSintactico.symbols.SymbolDecs;
import analizadorSintactico.symbols.SymbolElse;
import analizadorSintactico.symbols.SymbolIf;
import analizadorSintactico.symbols.SymbolInstr;
import analizadorSintactico.symbols.SymbolLoop;
import analizadorSintactico.symbols.SymbolMain;
import analizadorSintactico.symbols.SymbolOperand;
import analizadorSintactico.symbols.SymbolReturn;
import analizadorSintactico.symbols.SymbolScript;
import analizadorSintactico.symbols.SymbolSwap;
import analizadorSintactico.symbols.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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

    public String getErrors(){
        String s = "";
        for(String e : errors){
            s += e + "\n";
        }
        return s;
    }

    private void reportError(String errorMessage, int line, int column){
        thereIsError = true;
        errorMessage = " !! Semantic error: " + errorMessage + " at position [line: " + line + ", column: " + column + "]";
        System.err.println(errorMessage);
        errors.add(errorMessage);
    }
    
    public SemanticAnalysis(SymbolScript scriptElementosAntesDeMain){
        tablaSimbolos = new TablaSimbolos();
        errors = new ArrayList<>();
        ArrayList<SymbolDecs> declaraciones = new ArrayList<>();
        ArrayList<SymbolScriptElemento> tuplas = new ArrayList<>();
        ArrayList<SymbolScriptElemento> metodos = new ArrayList<>();
        int idMidDecs = 0, idMidTuplas = 0, idMidMetodos = 0;
        SymbolScriptElemento elem = scriptElementosAntesDeMain.elemento;
        while (elem != null) {
            switch (elem.getTipo()) {
                case SymbolScriptElemento.DECS -> { declaraciones.add(elem.declaraciones); idMidDecs++; }
                case SymbolScriptElemento.TUPLA -> { tuplas.add(elem); idMidTuplas++; }
                case SymbolScriptElemento.METODO -> { metodos.add(elem); idMidMetodos++; }
            }
            scriptElementosAntesDeMain = scriptElementosAntesDeMain.siguienteElemento;
            elem = scriptElementosAntesDeMain.elemento;
        }
        SymbolMain scriptMainYElementos = scriptElementosAntesDeMain.main;
        elem = scriptMainYElementos.elemento;
        while (elem != null) {
            switch (elem.getTipo()) {
                case SymbolScriptElemento.DECS -> declaraciones.add(idMidDecs, elem.declaraciones);
                case SymbolScriptElemento.TUPLA -> tuplas.add(idMidTuplas, elem);
                case SymbolScriptElemento.METODO -> metodos.add(idMidMetodos, elem);
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
            DescripcionSimbolo d = new DescripcionSimbolo(metodo.tipoRetorno.tipo);
            tablaSimbolos.poner(metodo.id, d);
        }
        for (SymbolScriptElemento metodo : metodos) {
            procesarDefinicionMetodo(metodo);
        }
        DescripcionSimbolo d = new DescripcionSimbolo();
        tablaSimbolos.poner(ParserSym.terminalNames[ParserSym.KW_MAIN], d);
        procesarMain(scriptMainYElementos.main);
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
        if (tipo.isArray()) {
            if (isConst) {
                // error
            }
            if (valorAsignado != null) {
                // error
            }
            // array...
        } else if (tipo.isTupla()) {
            if (isConst) {
                // error
            }
            if (valorAsignado != null) {
                // error
            }
            // tupla...
        } else {
            if (tablaSimbolos.contains(id)) {
                // error
            }
            Object tipoValor = procesarOperando(valorAsignado);
            if (tipoValor == null){
                // error
            } else if (tipoValor != tipo) {
                // error
            }
            DescripcionSimbolo d = new DescripcionSimbolo(tipo, isConst, valorAsignado != null);
            tablaSimbolos.poner(id, d);
        }
    }
        
    private void procesarBody(SymbolBody body) {
        while (body != null) {
            SymbolMetodoElemento elem = body.metodo;
            
            switch (elem.getTipo()) {
                case SymbolMetodoElemento.INSTR -> procesarInstruccion(elem.instruccion);
                case SymbolMetodoElemento.IF -> procesarIf(elem.iff);
                case SymbolMetodoElemento.LOOP -> procesarLoop(elem.loop);
                case SymbolMetodoElemento.SWITCH -> procesarSwitch(elem.sw);
            }
            body = body.siguienteMetodo;
        }
    }

    private void procesarInstruccion(SymbolInstr instr) {
        switch (instr.getTipo()) {
            case SymbolInstr.ASIGS -> procesarAsignaciones(instr.asigs);
            case SymbolInstr.DECS -> procesarDeclaraciones(instr.decs);
            case SymbolInstr.FCALL -> procesarLlamadaFuncion(instr.fcall);
            case SymbolInstr.RET -> procesarReturn(instr.ret);
            case SymbolInstr.SWAP -> procesarSwap(instr.swap);
        }
    }
    
    private void procesarAsignaciones(SymbolAsigs asigs) {

        do {
            SymbolAsig asig = asigs.asig;
            DescripcionSimbolo d = tablaSimbolos.getDescription(asig.id);
            if (d == null) {
                // error, no encontrado
            } else if (d.isArray() || d.isFunction() || d.isTupla()) {
                // error, asignación no permitida
            }
            Object valor = procesarOperando(asig.valor);
            if (valor == null){
                // error
            }
            switch (asig.getTipo()) {
                case PRIMITIVA -> {
                    if ((Object)d.getTipo() != valor) {
                        // error se asigna tipo diferente
                    }
                    
                }
                case ARRAY -> {
                
                }
                case TUPLA -> {
                    //HashMap<DescripcionSimbolo.Parametro, Boolean> tiposMiembros = d.getTiposMiembros();
                }
            }
            if (!asig.operacion.isBasicAsig() ){//&& d.getValor() == null) {
                // error operando con variable sin valor
            }
            
            asigs = asigs.siguienteAsig;
        } while (asigs != null);
        
    }
    
    private void procesarLlamadaFuncion(SymbolFCall fcall) {
        String nombre = (String) fcall.methodName.value;
        DescripcionSimbolo ds = tablaSimbolos.getDescription(nombre);
        if (!ds.isFunction()) {
            // error
        }
        ArrayList<DescripcionSimbolo.Parametro> params = ds.getTiposParametros();
        SymbolOperandsLista opLista = fcall.operandsLista;
        for (DescripcionSimbolo.Parametro tipoParam : params) {
            if (opLista == null) {
                // error, hay más operandos que parámetros
            }
            SymbolOperand op = opLista.operand;
            Object tipoOp = procesarOperando(op);
            
            if (tipoOp != tipoParam) {
                // error
            }
            opLista = opLista.operandsLista;
        }
        if (opLista != null) {
            // error, hay más parámetros que operandos
        }
        
    }
    
    private void procesarReturn(SymbolReturn ret) {
        SymbolTipo tipo = metodoActualmenteSiendoTratado.getTipoRetorno();
        SymbolOperand op = ret.op;
        if (op == null) {
            if (tipo.tipo == null) {
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
        
        DescripcionSimbolo ds1 = tablaSimbolos.getDescription(swap.op1);
        if (ds1 == null) {
            
        }
        DescripcionSimbolo ds2 = tablaSimbolos.getDescription(swap.op2);
        if (ds2 == null) {
            
        }
        //if (ds1.getValor() != ds2.getValor()) {
            
        //}
    }
    
    /**
     * Comprobar que las condiciones del if y elifs respeten los tipos y sus cuerpos
     * @param cond 
     */
    private void procesarIf(SymbolIf cond) {
        if (procesarOperando(cond.cond) == null) {

        }
        procesarBody(cond.cuerpo);
        SymbolElifs elifs = cond.elifs;
        while (elifs != null) {
            SymbolElif elif = elifs.elif;
            if (procesarOperando(elif.cond) == null) {
                
            }
            procesarBody(elif.cuerpo);
            elifs = elifs.elifs;
        }
        if (cond.els != null) {
            procesarBody(cond.els.cuerpo);
        }
    }
    
    private void procesarLoop(SymbolLoop loop) {
        // no importa que sea while o do while
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
    }
    
    private void procesarSwitch(SymbolSwitch sw) {
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
    }

    private void procesarDefinicionMetodo(SymbolScriptElemento metodo) {
        metodoActualmenteSiendoTratado = tablaSimbolos.getDescription(metodo.id);
        DescripcionSimbolo d = new DescripcionSimbolo(); 
//        d.setValor(metodo.tipoRetorno);
//        d.setValor(metodo.parametros);
//        d.setValor(metodo.cuerpo); // está mal lo sé
        tablaSimbolos.poner(metodo.id, d);
        procesarBody(metodo.cuerpo);
    }

    

    private void procesarDeclaracionTupla(SymbolScriptElemento tupla) {
        DescripcionSimbolo d = new DescripcionSimbolo(); 
//        d.setValor(tupla.miembrosTupla);
        tablaSimbolos.poner(tupla.id, d);
    }
    
    private void procesarMain(SymbolBody body) {
        // tratamiento
        procesarBody(body);
    }
    
    /**
     * Devuelve el tipo, array o struct al que pertenece el operando. Null si no se respetan los tipos.
     * @param op
     * @return 
     */
    private Object procesarOperando(SymbolOperand op) {
        return null; // DescripcionSimbolo a; a.getValor();
    }

    

    
}