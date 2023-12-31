
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
 * L_UNARY_OPERATOR ::= OP_NOT:et    {: RESULT = new SymbolLUnaryOperator(ParserSym.OP_NOT, et , etxleft, etxright); :}
        | OP_INC:et             {: RESULT = new SymbolLUnaryOperator(ParserSym.OP_INC, et , etxleft, etxright); :}
        | OP_DEC:et             {: RESULT = new SymbolLUnaryOperator(ParserSym.OP_DEC, et , etxleft, etxright); :}
        ;
 */
public class SymbolLUnaryOperator extends SymbolBase {
    private int unaryOperator;

    public SymbolLUnaryOperator(String variable, Double valor) {
        super(variable);
    }

    public SymbolLUnaryOperator(int op, Object et, Location l , Location r) {
        super("unaryOperator", 0 , l , r);
        this.unaryOperator = op;
    }

    public int getUnaryOperator() {
        return unaryOperator;
    }
    
    
    
}
