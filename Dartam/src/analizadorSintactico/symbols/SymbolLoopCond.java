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
 * LOOP_COND ::= OPERAND:et                                                {: RESULT = new SymbolLoopCond(et, etxleft, etxright); :}
        | DECS:et1 ENDINSTR OPERAND:et2 ENDINSTR OPERAND:et3            {: RESULT = new SymbolLoopCond(et1, et2, et3, et1xleft, et1xright); :}
        ;

 */
public class SymbolLoopCond extends SymbolBase {
    public final SymbolOperand cond;
    public final SymbolDecs decs;
    public final SymbolAsigs asig;
    private final boolean isFor;

    public SymbolLoopCond(SymbolOperand op1, Location l, Location r) {
        super("loopCond", 0 ,l ,r);
        this.cond = op1;
        this.decs = null;
        this.asig = null;
        isFor = false;
    }
    
    public SymbolLoopCond(SymbolDecs decs, SymbolOperand op1, SymbolAsigs op2, Location l, Location r) {
        super("loopCond", 0 ,l ,r);
        this.cond = op1;
        this.asig = op2;
        this.decs = decs;
        isFor = true;
    }

    public boolean isFor() {
        return isFor;
    }
    
}
