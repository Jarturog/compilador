
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
 * 
BINARY_EXPRESSION ::= OPERAND:et1 BINARY_OPERATOR:et2 OPERAND:et3        {: RESULT = new SymbolBinaryExpression(et1, et2, et3, et1xleft, et1xright); :}
        ;

 */
public class SymbolBinaryExpression extends SymbolBase {
    public final SymbolOperand op1;
    public final SymbolBinaryOperator bop;
    public final SymbolOperand op2;

    /*public SymbolBinaryExpression(String variable, Double valor) {
        super(variable);
    }*/

    public SymbolBinaryExpression(SymbolOperand op1, SymbolBinaryOperator bop,  SymbolOperand op2, Location l , Location r) {
        super("binaryExpression", 0 , l , r);
        this.op1 = op1;
        this.bop = bop;
        this.op2 = op2;
    }

    public SymbolOperand getOp1() {
        return op1;
    }

    public SymbolBinaryOperator getBop() {
        return bop;
    }

    public SymbolOperand getOp2() {
        return op2;
    }
    
    
    
}
