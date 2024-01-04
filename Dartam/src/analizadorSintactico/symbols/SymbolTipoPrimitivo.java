
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
TIPO_PRIMITIVO ::= KW_BOOL:et              {: RESULT = new SymbolTipoPrimitivo(ParserSym.KW_BOOL, etxleft, etxright); :}
        | KW_INT:et                        {: RESULT = new SymbolTipoPrimitivo(ParserSym.KW_INT, etxleft, etxright); :}
        | KW_DOUBLE:et                     {: RESULT = new SymbolTipoPrimitivo(ParserSym.KW_DOUBLE, etxleft, etxright); :}
        | KW_CHAR:et                       {: RESULT = new SymbolTipoPrimitivo(ParserSym.KW_CHAR, etxleft, etxright); :}
        | KW_STRING:et                     {: RESULT = new SymbolTipoPrimitivo(ParserSym.KW_STRING, etxleft, etxright); :}
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
    
}
