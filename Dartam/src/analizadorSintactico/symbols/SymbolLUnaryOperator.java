
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
L_UNARY_OPERATOR ::= OP_NOT:et  {: RESULT = new SymbolLUnaryOperator(ParserSym.OP_NOT, et, etxleft, etxright); :}
        | OP_INC:et             {: RESULT = new SymbolLUnaryOperator(ParserSym.OP_INC, et, etxleft, etxright); :}
        | OP_DEC:et             {: RESULT = new SymbolLUnaryOperator(ParserSym.OP_DEC, et, etxleft, etxright); :}
        | OP_ADD:et             {: RESULT = new SymbolLUnaryOperator(ParserSym.OP_ADD, et, etxleft, etxright); :}
        | OP_SUB:et             {: RESULT = new SymbolLUnaryOperator(ParserSym.OP_SUB, et, etxleft, etxright); :}
        ;
 */
public class SymbolLUnaryOperator extends SymbolBase {
    public final int unaryOperator;

    public SymbolLUnaryOperator(int op, Object et, Location l , Location r) {
        super("unaryOperator", et, l , r);
        this.unaryOperator = op;
    }
    
}
