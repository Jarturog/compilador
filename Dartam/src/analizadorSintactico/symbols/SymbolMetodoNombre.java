
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
METODO_NOMBRE ::= ID:et                 {: RESULT = new SymbolMetodoNombre(ParserSym.ID, et, etxleft, etxright); :}
        | KW_IN:et                      {: RESULT = new SymbolMetodoNombre(ParserSym.KW_IN, et, etxleft, etxright); :}
        | KW_OUT:et                     {: RESULT = new SymbolMetodoNombre(ParserSym.KW_OUT, et, etxleft, etxright); :}
        | KW_WRITE:et                   {: RESULT = new SymbolMetodoNombre(ParserSym.KW_WRITE, et, etxleft, etxright); :}
        | KW_READ:et                    {: RESULT = new SymbolMetodoNombre(ParserSym.KW_READ, et, etxleft, etxright); :}
        ;
 * 
 */
public class SymbolMetodoNombre extends SymbolBase {
    
    public final Integer specialMethod;

    public SymbolMetodoNombre(Integer methodName, Object et, Location l , Location r) {
        super("methodName", et, l,r);
        this.specialMethod = methodName;
    }
    
    public boolean isSpecialMethod() {
        return specialMethod != null;
    }
    
}
