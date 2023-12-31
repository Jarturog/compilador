
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
public class SymbolMain extends SymbolBase {
    private static int id = 0;
    private SymbolBody sb;
    private SymbolMain sm;
    private SymbolScriptElemento se;

    public SymbolMain(SymbolBody sb, int l, int r) {
        super("sb", 0, r, r);
        this.sb = sb;
    }
    
    public SymbolMain() {
        super("sb",0);
    }

    public SymbolMain(SymbolMain et1, SymbolScriptElemento et2, int l, int r) {
        super("sb", 0, l, r);
        this.sm = et1;
        this.se = et2;
    }
    
}
