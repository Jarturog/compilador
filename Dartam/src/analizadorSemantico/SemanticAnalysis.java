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
import java.util.LinkedList;
import java.util.List;

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
    
    public SemanticAnalysis(SymbolScript script){
        tablaSimbolos = new TablaSimbolos();
        errors = new ArrayList<>();
        ArrayList<SymbolDecs> declaraciones = new ArrayList<>();
        ArrayList<SymbolScriptElemento> tuplas = new ArrayList<>();
        ArrayList<SymbolScriptElemento> metodos = new ArrayList<>();
        int idMidDecs = 0, idMidTuplas = 0, idMidMetodos = 0;
        SymbolScriptElemento elem = script.elemento;
        while (elem != null) {
            switch (elem.getTipo()) {
                case "d" -> { declaraciones.add(elem.declaraciones); idMidDecs++; }
                case "t" -> { tuplas.add(elem); idMidTuplas++; }
                case "m" -> { metodos.add(elem); idMidMetodos++; }
            }
            script = script.siguienteElemento;
            elem = script.elemento;
        }
        SymbolMain main = script.main;
        elem = main.elemento;
        while (elem != null) {
            switch (elem.getTipo()) {
                case "d" -> declaraciones.add(idMidDecs, elem.declaraciones);
                case "t" -> tuplas.add(idMidTuplas, elem);
                case "m" -> metodos.add(idMidMetodos, elem);
            }
            main = main.main;
            elem = main.elemento;
        }
        if (main.body == null) { // main vacío
            return;
        }
        for (SymbolDecs decs : declaraciones) {
            procesarDeclaraciones(decs);
        }
        for (SymbolScriptElemento tupla : tuplas) {
            procesarDeclaracionTupla(tupla);
        }
        for (SymbolScriptElemento metodo : metodos) {
            procesarDefinicionMetodo(metodo);
        }
        procesarMain(main.body);
    }
    
    private void procesarDeclaraciones(SymbolDecs decs) {
        DescripcionSimbolo d = new DescripcionSimbolo(); 
        // declaraciones variables y constantes
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
        
    private void procesarBody(SymbolBody body) {
        
    }

    private void procesarDefinicionMetodo(SymbolScriptElemento metodo) {
        DescripcionSimbolo d = new DescripcionSimbolo(); 
        d.changeValue(metodo.tipoRetorno);
        d.changeValue(metodo.parametros);
        d.changeValue(metodo.cuerpo); // está mal lo sé
        tablaSimbolos.insertVariable(metodo.idTuplaMetodo, d);
    }

    

    private void procesarDeclaracionTupla(SymbolScriptElemento tupla) {
        DescripcionSimbolo d = new DescripcionSimbolo(); 
        d.changeValue(tupla.miembrosTupla);
        tablaSimbolos.insertVariable(tupla.idTuplaMetodo, d);
    }
    
    private void procesarMain(SymbolBody body) {
        // tratamiento
        procesarBody(body);
    }

}