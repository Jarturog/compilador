
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
 * LOOP_COND ::= OPERAND:et                                                {: RESULT = new SymbolLoopCond(et, etxleft, etxright); :}
        | DECS:et1 ENDINSTR OPERAND:et2 ENDINSTR OPERAND:et3            {: RESULT = new SymbolLoopCond(et1, et2, et3, et1xleft, et1xright); :}
        ;

 */
public class SymbolLoopCond extends SymbolBase {
    public final SymbolOperand op1;
    public final SymbolDecs decs;
    public final SymbolOperand op2;

    public SymbolLoopCond(SymbolOperand op1, Location l, Location r) {
        super("loopCond", 0 ,l ,r);
        this.op1 = op1;
        this.decs = null;
        this.op2 = null;
    }
    
    public SymbolLoopCond(SymbolDecs decs, SymbolOperand op1, SymbolOperand op2, Location l, Location r) {
        super("loopCond", 0 ,l ,r);
        this.op1 = op1;
        this.op2 = op2;
        this.decs = decs;
    }

    public SymbolOperand getOp1() {
        return op1;
    }

    public SymbolDecs getDecs() {
        return decs;
    }

    public SymbolOperand getOp2() {
        return op2;
    }
    
    
    
    
    
}
