/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 * 
 * ELSE ::= KW_ELSE LKEY BODY:et RKEY                  {: RESULT = new SymbolElse(et, etxleft, etxright); :}
        |                                           {: RESULT = new SymbolElse(); :}
        ;
 */
public class SymbolElse extends SymbolBase {
    public final SymbolBody cuerpo;

    public SymbolElse() {
        super("else");
        this.cuerpo = null;
    }
    
    public SymbolElse(SymbolBody b, Location l, Location r){
        super("else", l ,r);
        this.cuerpo = b;
        
    }
    
}
