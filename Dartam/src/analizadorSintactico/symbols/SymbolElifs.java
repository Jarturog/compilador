
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
 * 
ELIFS ::= ELIF:et1 ELIFS:et2                        {: RESULT = new SymbolElifs(et1, et2, et1xleft, et1xright); :}
        |                                           {: RESULT = new SymbolElifs(); :}
        ;
 */
public class SymbolElifs extends SymbolBase {
    private SymbolElif elif;
    private SymbolElifs elifs;

    public SymbolElifs() {
        super("elif");
    }
    
    public SymbolElifs(SymbolElif elif, SymbolElifs elifs, Location l, Location r){
        super("elif", 0, l, r);
        this.elif = elif;
        this.elifs = elifs;
    }

    public SymbolElif getElif() {
        return elif;
    }

    public SymbolElifs getElifs() {
        return elifs;
    }
    
    
}
