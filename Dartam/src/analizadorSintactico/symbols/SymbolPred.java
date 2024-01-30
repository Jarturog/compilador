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
 * PRED ::= KW_CASE KW_DEFAULT ARROW BODY:et            {: RESULT = new Symbol(et, etxleft, etxright); :}
        ;
 */
public class SymbolPred extends SymbolBase {
    public final SymbolBody cuerpo;

    public SymbolPred(SymbolBody b, Location l, Location r) {
        super("pred", l ,r );
        this.cuerpo = b;
    }
}
