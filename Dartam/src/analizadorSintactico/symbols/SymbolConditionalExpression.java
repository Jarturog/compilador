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
CONDITIONAL_EXPRESSION ::= OPERAND:et1 OP_COND OPERAND:et2 ARROW OPERAND:et3   {: RESULT = new SymbolConditionalExpression(et1, et2, et3, et1xleft, et1xright);; :}
        ; 
 */
public class SymbolConditionalExpression extends SymbolBase {
    public final SymbolOperand cond, caseTrue, caseFalse;

    public SymbolConditionalExpression(SymbolOperand op1, SymbolOperand op2, SymbolOperand op3, Location l, Location r) {
        super("conditionalExpression", l ,r);
        this.cond = op1;
        this.caseTrue = op2;
        this.caseFalse = op3;
        value = toString();
    }
    
    @Override
    public String toString() {
        return "(" + cond.value + " ? " + caseTrue.value + " -> " + caseFalse.value + ")";
    }
    
}
