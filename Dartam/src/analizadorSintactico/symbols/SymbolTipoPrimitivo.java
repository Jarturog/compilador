/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import analizadorSintactico.ParserSym;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
TIPO_PRIMITIVO ::= KW_BOOL:et              {: RESULT = new SymbolTipoPrimitivo(et, etxleft, etxright); :}
        | KW_INT:et                        {: RESULT = new SymbolTipoPrimitivo(et, etxleft, etxright); :}
        | KW_DOUBLE:et                     {: RESULT = new SymbolTipoPrimitivo(et, etxleft, etxright); :}
        | KW_CHAR:et                       {: RESULT = new SymbolTipoPrimitivo(et, etxleft, etxright); :}
        | KW_STRING:et                     {: RESULT = new SymbolTipoPrimitivo(et, etxleft, etxright); :}
        ;
 */
public class SymbolTipoPrimitivo extends SymbolBase {

    public final String tipo;

    public SymbolTipoPrimitivo(Object tipo, Location l, Location r) {
        super("tipoVar", tipo, l, r);
        this.tipo = ((String) tipo).toUpperCase();
    }

    public String getTipo() {
        return tipo;
    }

    public static boolean isTipoPrimitivo(String t) {
        return t.equals(ParserSym.terminalNames[ParserSym.PROP])
                || t.equals(ParserSym.terminalNames[ParserSym.ENT])
                || t.equals(ParserSym.terminalNames[ParserSym.REAL])
                || t.equals(ParserSym.terminalNames[ParserSym.CAR])
                || t.equals(ParserSym.terminalNames[ParserSym.STRING]);
    }

    public static final String STRING = "STRING", CAR = "CAR", ENT = "ENT", PROP = "PROP", REAL = "REAL";
    
}
