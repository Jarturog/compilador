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
BINARY_EXPRESSION ::= OPERAND:et1 BINARY_OPERATOR:et2 OPERAND:et3        {: RESULT = new SymbolBinaryExpression(et1, et2, et3, et1xleft, et1xright); :}
        ;

 */
public class SymbolBinaryExpression extends SymbolBase {
    public final SymbolOperand op1, op2;
    public final SymbolBinaryOperator bop;

    public SymbolBinaryExpression(SymbolOperand op1, SymbolBinaryOperator bop, SymbolOperand op2, Location l , Location r) {
        super("binaryExpression", l , r);
        this.op1 = op1;
        this.bop = bop;
        this.op2 = op2;
        value = toString();
    }
    
    @Override
    public final String toString(){
        return op1.value + " " + bop.value + " " + op2.value;
    }

}
