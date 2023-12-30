
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * Bàsicament conté un valor enter
 */
public class SymbolScript extends SymbolBase {

    private SymbolScriptElemento elemento;
    private SymbolScript siguienteElemento; // o main
    
    private SymbolMain main;
    
    public SymbolScript(SymbolScriptElemento e, SymbolScript s, int l, int c) {
        super("script", 0, l, c);
        elemento = e;
        siguienteElemento = s;
    }
    
    public SymbolScript(SymbolMain m, int l, int c) {
        super("script", l, c);
        main = m;
    }
    
}
