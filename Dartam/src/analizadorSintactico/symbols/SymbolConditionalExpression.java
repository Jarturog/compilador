
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
 * CONDITIONAL_EXPRESSION ::= OPERAND:et1 OP_COND OPERAND:et2 ARROW OPERAND:et3  
 * {: RESULT = new SymbolConditionalExpression(et1, et2, et3, et1xleft, et1xright);; :}
        ; 

 */
public class SymbolConditionalExpression extends SymbolBase {
    private SymbolOperand op1;
    private SymbolOperand op2;
    private SymbolOperand op3;

    /*public SymbolConditionalExpression(String variable, Double valor) {
        super(variable);
    }*/

    public SymbolConditionalExpression(SymbolOperand op1, SymbolOperand op3,  SymbolOperand op2, Location l, Location r) {
        super("conditionalExpression", 0 , l ,r);
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
    }

    public SymbolOperand getOp1() {
        return op1;
    }

    public SymbolOperand getOp2() {
        return op2;
    }

    public SymbolOperand getOp3() {
        return op3;
    }
    
    
    
}
