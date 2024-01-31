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
CASO ::= CASO:et1 KW_CASE OPERAND:et2 ARROW BODY:et3 {: RESULT = new SymbolCaso(et1, et2, et3, et1xleft et1xright); :}
        |                                            {: RESULT = new SymbolCaso(); :}
        ;
 */
public class SymbolCaso extends SymbolBase {
    public final SymbolCaso siguienteCaso;
    public final SymbolOperand cond;
    public final SymbolBody cuerpo;

    public SymbolCaso(Location l, Location r) {
        super("caso", l, r);
        this.siguienteCaso = null;
        this.cond = null;
        this.cuerpo = null;
    }
    
    public SymbolCaso(SymbolCaso caso, SymbolOperand op, SymbolBody b, Location l , Location r){
        super("caso", l ,r );
        this.siguienteCaso = caso;
        this.cond = op;
        this.cuerpo = b;
    }

}
