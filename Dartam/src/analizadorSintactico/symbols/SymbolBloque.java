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
BLOQUE ::= LKEY BODY:et RKEY    {: RESULT = new SymbolBloque(et, etxleft, etxright); :}
        ;
 */
public class SymbolBloque extends SymbolBase {

    public final SymbolBody cuerpo;

    public SymbolBloque(SymbolBody b,  Location l, Location r){
        super("bloque", l ,r);
        this.cuerpo = b;
    }

}
