
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informatica 
 * Itinerari: Inteligencia Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;



/**
    ASIGS ::= ASIG:et1 COMMA ASIGS:et2                              {: RESULT = new SymbolAsigs(et1, et2, et1xleft, et1xright); :}
            | ASIG:et ENDINSTR                                      {: RESULT = new SymbolAsigs(et, etxleft, etxright); :}
            ;
 */
public class SymbolAsigs extends SymbolBase {
    
    public final SymbolAsig asig;
    public final SymbolAsigs siguienteAsig;

    public SymbolAsigs(SymbolAsig asig, SymbolAsigs al, Location l, Location r) {
        super("idAsigLista", l ,r);
        this.asig = asig;
        this.siguienteAsig = al;
    }
    
    public SymbolAsigs(SymbolAsig asig, Location l , Location r){
        super("idAsigLista", l, r);
        this.asig = asig;
        this.siguienteAsig = null;
    }
    
    
    
}
