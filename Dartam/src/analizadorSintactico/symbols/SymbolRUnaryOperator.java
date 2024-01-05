
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
R_UNARY_OPERATOR ::= OP_PCT:et  {: RESULT = new SymbolRUnaryOperator(ParserSym.OP_PCT, et, etxleft, etxright); :}
        | OP_INC:et             {: RESULT = new SymbolRUnaryOperator(ParserSym.OP_INC, et, etxleft, etxright); :}
        | OP_DEC:et             {: RESULT = new SymbolRUnaryOperator(ParserSym.OP_DEC, et, etxleft, etxright); :}
        ;
 */
public class SymbolRUnaryOperator extends SymbolBase {
    public final int unaryOperator;

    public SymbolRUnaryOperator(int op, Object et, Location l , Location r) {
        super("unaryOperator", et, l , r);
        this.unaryOperator = op;
    }
    
}
