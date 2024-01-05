
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
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
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
        super("else", 0 ,l ,r);
        this.cuerpo = b;
        
    }
    
}
