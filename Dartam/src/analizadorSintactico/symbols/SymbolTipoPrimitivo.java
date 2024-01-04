/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica
 * Itinerari: Intel·ligència Artificial i Computació
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

    private final String tipo;

    public SymbolTipoPrimitivo(Object tipo, Location l, Location r) {
        super("tipoVar", tipo, l, r);
        this.tipo = (String) tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public static boolean isTipoPrimitivo(String t) {
        return t.equals(ParserSym.terminalNames[ParserSym.BOOL])
                || t.equals(ParserSym.terminalNames[ParserSym.INT])
                || t.equals(ParserSym.terminalNames[ParserSym.DOUBLE])
                || t.equals(ParserSym.terminalNames[ParserSym.CHAR])
                || t.equals(ParserSym.terminalNames[ParserSym.STRING]);
    }

}
