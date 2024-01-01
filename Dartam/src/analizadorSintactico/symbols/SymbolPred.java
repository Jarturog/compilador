
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
 * PRED ::= KW_CASE KW_DEFAULT ARROW BODY:et            {: RESULT = new Symbol(et, etxleft, etxright); :}
        ;
 */
public class SymbolPred extends SymbolBase {
    public final SymbolBody b;

    public SymbolPred(SymbolBody b, Location l, Location r) {
        super("pred", 0 , l ,r );
        this.b = b;
    }

    public SymbolBody getB() {
        return b;
    }
    
    
}
