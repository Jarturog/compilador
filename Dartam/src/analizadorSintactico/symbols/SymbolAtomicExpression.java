
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import analizadorSintactico.ParserSym;
import java_cup.runtime.ComplexSymbolFactory.Location;



/**
ATOMIC_EXPRESSION ::= ID:et     {: RESULT = new SymbolAtomicExpression(et, etxleft, etxright); :}
        | STRING:et             {: RESULT = new SymbolAtomicExpression(et, etxleft, etxright); :}
        | BOOL:et               {: RESULT = new SymbolAtomicExpression(et, etxleft, etxright); :}
        | INT:et                {: RESULT = new SymbolAtomicExpression(et, etxleft, etxright); :}
        | DOUBLE:et             {: RESULT = new SymbolAtomicExpression(et, etxleft, etxright); :}
        | CHAR:et               {: RESULT = new SymbolAtomicExpression(et, etxleft, etxright); :}
        ;
 */
public class SymbolAtomicExpression extends SymbolBase {
    
    public final String tipo;
    
    public SymbolAtomicExpression(boolean isID, String et, Location l, Location r) {
        super("literal", isID ? et : et.substring(1, et.length() - 1), l, r);
        if (isID) {
            tipo = ParserSym.terminalNames[ParserSym.ID];
        } else {
            tipo = ParserSym.terminalNames[ParserSym.STRING];
        }
    }

    public SymbolAtomicExpression(Character et, Location l, Location r) {
        super("literal", et, l, r);
        tipo = ParserSym.terminalNames[ParserSym.CAR];
    }

    public SymbolAtomicExpression(Double et, Location l, Location r) {
        super("literal", et, l, r);
        tipo = ParserSym.terminalNames[ParserSym.REAL];
    }

    public SymbolAtomicExpression(Integer et, Location l, Location r) {
        super("literal", et, l, r);
        tipo = ParserSym.terminalNames[ParserSym.ENT];
    }

    public SymbolAtomicExpression(Boolean et, Location l, Location r) {
        super("literal", et, l, r);
        tipo = ParserSym.terminalNames[ParserSym.PROP];
    }
    
}
