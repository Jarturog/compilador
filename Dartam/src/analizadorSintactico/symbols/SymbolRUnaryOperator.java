
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
 * R_UNARY_OPERATOR ::= OP_PCT:et  {: RESULT = new SymbolRUnaryOperator(ParserSym.OP_PCT, et, etxleft, etxright); :}
        | OP_INC:et             {: RESULT = new SymbolRUnaryOperator(ParserSym.OP_INC, et, etxleft, etxright); :}
        | OP_DEC:et             {: RESULT = new SymbolRUnaryOperator(ParserSym.OP_DEC, et, etxleft, etxright); :}
        ;

 */
public class SymbolRUnaryOperator extends SymbolBase {
    private int operador;
    
    public SymbolRUnaryOperator(String variable, Double valor) {
        super(variable);
    }

    public SymbolRUnaryOperator(int op, Object et, Location l , Location r) {
        super("rUnaryOperator", 0 ,l , r);
        this.operador = op;
    }

    public int getOperador() {
        return operador;
    }
    
    
}
