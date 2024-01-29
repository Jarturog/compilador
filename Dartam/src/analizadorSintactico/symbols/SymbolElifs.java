/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica 
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 * 
 * 
ELIFS ::= ELIF:et1 ELIFS:et2                        {: RESULT = new SymbolElifs(et1, et2, et1xleft, et1xright); :}
        |                                           {: RESULT = new SymbolElifs(); :}
        ;
 */
public class SymbolElifs extends SymbolBase {
    public final SymbolElif elif;
    public final SymbolElifs siguienteElif;
    
    public SymbolElifs(Location l, Location r) {
        super("elif", l, r);
        this.elif = null;
        this.siguienteElif = null;
    }
    
    public SymbolElifs(SymbolElif elif, SymbolElifs elifs, Location l, Location r){
        super("elif", l, r);
        this.elif = elif;
        this.siguienteElif = elifs;
    }

}
