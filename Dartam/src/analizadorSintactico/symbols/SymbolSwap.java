
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
 * SWAP ::= OPERAND:et1 OP_SWAP OPERAND:et2 ENDINSTR           {: RESULT = new SymbolSwap(et1, et2, et1xleft, et1xright); :}
        ;

 */
public class SymbolSwap extends SymbolBase {
    
    public final SymbolOperand op1;
    public final SymbolOperand op2;

    public SymbolSwap(SymbolOperand op1, SymbolOperand op2, Location l, Location r) {
        super("swap", 0 , l ,r );
        this.op1 = op1;
        this.op2 = op2;
    }

    public SymbolOperand getOp1() {
        return op1;
    }

    public SymbolOperand getOp2() {
        return op2;
    }
    
    
    
}
