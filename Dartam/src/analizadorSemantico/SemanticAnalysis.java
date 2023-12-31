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

public class SemanticAnalysis {
    
    public TablaSimbolos tablaSimbolos;

    // Description to the function in which we are currently.
    private DescripcionSimbolo currentFunction;
    // When checking if a function's parameters are correct, we use this stack to store the declared function's types.
    // We use a stack because we will be taking elements out every time we process them.
    private Stack<SymbolTipoVar> currentArgs;
    private boolean returnFound;

    private ArrayList<String> errors;
    public boolean thereIsError = false;

    public SemanticAnalysis(SymbolScript script){
        tablaSimbolos = new TablaSimbolos();
        errors = new ArrayList<>();
        ArrayList<SymbolScriptElemento> elementosScript = new ArrayList<>();
        int idxMain;
        
        for (idxMain = 0; script.elemento != null; idxMain++) {
            elementosScript.add(script.elemento);
            script = script.siguienteElemento;
        }
        SymbolMain main = script.main;
        while (main.elemento != null) {
            elementosScript.add(idxMain, main.elemento);
            main = main.main;
        }
        for (SymbolScriptElemento s : elementosScript) {
            procesarElementoScript(s);
        }
        // main.procesar();
        
        /*SymbolDecs decs = body.getDeclarations();
        if(decs != null) manage(decs);
        SymbolMain main = body.getMain();
        manage(main);*/
    }
    
    private void procesarElementoScript(SymbolScriptElemento s){
        DescripcionSimbolo d = new DescripcionSimbolo(); 
        if (s.tipoRetorno != null) { // definición metodo
            d.changeValue(s.tipoRetorno);
            d.changeValue(s.parametros);
            d.changeValue(s.cuerpo);
            tablaSimbolos.insertVariable(s.idTuplaMetodo, d);
            return;
        }
        if (s.idTuplaMetodo != null) { // declaración tupla
            d.changeValue(s.miembrosTupla);
            tablaSimbolos.insertVariable(s.idTuplaMetodo, d);
            return;
        }
        // declaraciones variables y constantes
        SymbolDecs decs = s.declaraciones;
        if (decs.idTupla != null) { // variable tipo tupla
            tablaSimbolos.insertVariable(decs.idTupla, d);
            return;
        }
        // variables tipo primitivas y array
        SymbolIDDecsLista declaracion = decs.iddecslista;
        while (declaracion.siguienteDeclaracion != null) {
            procesarDeclaracion(declaracion);
            declaracion = declaracion.siguienteDeclaracion;
        }
        procesarDeclaracion(declaracion);
    }
    
    private void procesarDeclaracion(SymbolIDDecsLista dec) {
        DescripcionSimbolo d = new DescripcionSimbolo(); 
        if (dec.asignacion != null) {
            d.changeValue(dec.asignacion.operando);
        }
        tablaSimbolos.insertVariable(dec.id, d);
    }

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

}