
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;



/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * Bàsicament conté un valor enter
 */
public class SymbolMain extends SymbolBase {
    
    private SymbolBody sb;
    private SymbolMain sm;
    private SymbolScriptElemento se;

    public SymbolMain(SymbolBody sb, Location l, Location r) {
        super("sb", r, r);
        this.sb = sb;
    }
    
    public SymbolMain() {
        super("sb");
    }

    public SymbolMain(SymbolMain et1, SymbolScriptElemento et2, Location l, Location r) {
        super("sb", l, r);
        this.sm = et1;
        this.se = et2;
    }
    
}
