
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
 * SWITCH ::= KW_SWITCH OPERAND:et1 RKEY CASO:et2 PRED:et3 LKEY {: RESULT = new SymbolSwitch(et1, et2, et3, et1xleft, et1xright); :}
        ;
 */
public class SymbolSwitch extends SymbolBase {
    public final SymbolOperand op;
    public final SymbolCaso caso;
    public final SymbolPred pred;
    

    public SymbolSwitch(SymbolOperand op, SymbolCaso caso, SymbolPred pred, Location l, Location r) {
        super("switch", 0, l, r);
        this.op = op;
        this.caso = caso;
        this.pred = pred;
        
    }

    public SymbolOperand getOp() {
        return op;
    }

    public SymbolCaso getCaso() {
        return caso;
    }

    public SymbolPred getPred() {
        return pred;
    }
    
}
