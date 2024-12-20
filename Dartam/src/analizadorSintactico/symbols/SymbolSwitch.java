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
 * SWITCH ::= KW_SWITCH OPERAND:et1 RKEY CASO:et2 PRED:et3 LKEY {: RESULT = new SymbolSwitch(et1, et2, et3, et1xleft, et1xright); :}
        ;
 */
public class SymbolSwitch extends SymbolBase {
    public final SymbolOperand cond;
    public final SymbolCaso caso;
    public final SymbolPred pred;
    

    public SymbolSwitch(SymbolOperand op, SymbolCaso caso, SymbolPred pred, Location l, Location r) {
        super("switch", l, r);
        this.cond = op;
        this.caso = caso;
        this.pred = pred;
        
    }
    
}
