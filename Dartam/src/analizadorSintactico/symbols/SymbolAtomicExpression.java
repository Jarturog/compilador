/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import analizadorSemantico.genCodigoIntermedio.TipoVariable;
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

    public static final int LIMIT_INF = Integer.MIN_VALUE, LIMIT_SUP = Integer.MAX_VALUE;
    public final String tipo;

    public SymbolAtomicExpression(boolean isID, String et, Location l, Location r) {
        super("literal", et, l, r); // isID ? et : et.substring(1, et.length() - 1), l, r);
        if (isID) {
            tipo = ParserSym.terminalNames[ParserSym.ID];
        } else {
            tipo = ParserSym.terminalNames[ParserSym.CAR] + " []";
        }
        if(value.toString().equals("b: 8")) {
            System.out.println("");
        }
    }

    public SymbolAtomicExpression(Character et, Location l, Location r) {
        super("literal", et, l, r);
        tipo = ParserSym.terminalNames[ParserSym.CAR];
    }

//    public SymbolAtomicExpression(Double et, Location l, Location r) {
//        super("literal", et, l, r);
//        tipo = ParserSym.terminalNames[ParserSym.REAL];
//    }

    public SymbolAtomicExpression(Integer et, Location l, Location r) {
        super("literal", et, l, r);
        tipo = ParserSym.terminalNames[ParserSym.ENT];
    }

    public SymbolAtomicExpression(Boolean et, Location l, Location r) {
        super("literal", et, l, r);
        tipo = ParserSym.terminalNames[ParserSym.PROP];
    }

    public boolean esNumerico() {
        return tipo.equals(ParserSym.terminalNames[ParserSym.CAR])
                || tipo.equals(ParserSym.terminalNames[ParserSym.ENT]);
//                || tipo.equals(ParserSym.terminalNames[ParserSym.REAL]);
    }

    public Object getValorCodigoIntermedio() {
        if (tipo.equals(ParserSym.terminalNames[ParserSym.CAR])) {
            return (Character)value;
        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.ENT])) {
            return (Integer)value;
//        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.REAL])) {
//            return (Double)value;
        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.PROP])) {
            return (Integer)((Boolean) value ? TipoVariable.TRUE : TipoVariable.FALSE);
        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.ID])) {
            return value.toString();
        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.CAR] + " []")) {
            return value.toString();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return value.toString();
    }

}
