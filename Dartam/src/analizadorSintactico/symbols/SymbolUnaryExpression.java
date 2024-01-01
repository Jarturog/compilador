
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
 * UNARY_EXPRESSION ::= L_UNARY_OPERATOR:et1 OPERAND:et2                       {: RESULT = new SymbolUnaryExpression(et1, et2, et1xleft, et1xright); :}
        | OPERAND:et1 R_UNARY_OPERATOR:et2                                  {: RESULT = new SymbolUnaryExpression(et1, et2, et1xleft, et1xright); :}
        ;
 */
public class SymbolUnaryExpression extends SymbolBase {
    public final SymbolLUnaryOperator luo;
    public final SymbolRUnaryOperator ruo;
    public final SymbolOperand op;
    
    public SymbolUnaryExpression(SymbolLUnaryOperator luo, SymbolOperand op, Location l, Location r) {
        super("unaryExpression", 0 , l , r);
        this.luo = luo;
        this.op = op;
        this.ruo = null;
    }

    public SymbolUnaryExpression(SymbolOperand op, SymbolRUnaryOperator ruo, Location l, Location r) {
        super("unaryExpression", 0 , l , r);
        this.ruo = ruo;
        this.op = op;
        this.luo = null;
    }

    
}
