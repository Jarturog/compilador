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
 * UNARY_EXPRESSION ::= L_UNARY_OPERATOR:et1 OPERAND:et2                       {: RESULT = new SymbolUnaryExpression(et1, et2, et1xleft, et1xright); :}
        | OPERAND:et1 R_UNARY_OPERATOR:et2                                  {: RESULT = new SymbolUnaryExpression(et1, et2, et1xleft, et1xright); :}
        ;
 */
public class SymbolUnaryExpression extends SymbolBase {
    public final SymbolLUnaryOperator leftOp;
    public final SymbolRUnaryOperator rightOp;
    public final SymbolOperand op;
    
    public SymbolUnaryExpression(SymbolLUnaryOperator luo, SymbolOperand op, Location l, Location r) {
        super("unaryExpression", l , r);
        this.leftOp = luo;
        this.op = op;
        this.rightOp = null;
        value = toString();
    }

    public SymbolUnaryExpression(SymbolOperand op, SymbolRUnaryOperator ruo, Location l, Location r) {
        super("unaryExpression", l , r);
        this.rightOp = ruo;
        this.op = op;
        this.leftOp = null;
        value = toString();
    }

    public boolean isLeftUnaryOperator() {
        return leftOp != null;
    }
    
    @Override
    public String toString(){
        return (isLeftUnaryOperator() ? (leftOp.value + "" + op.value) : (op.value + "" + rightOp.value));
    }
}
