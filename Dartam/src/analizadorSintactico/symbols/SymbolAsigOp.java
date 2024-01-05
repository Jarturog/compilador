
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
ASIG_OP ::= AS_ASSIGN:et                {: RESULT = new SymbolAsigOp(ParserSym.AS_ASSIGN, et, etxleft, etxright); :}
        | AS_ADDA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_ADDA, et, etxleft, etxright); :}
        | AS_SUBA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_SUBA, et, etxleft, etxright); :}
        | AS_MULA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_MULA, et, etxleft, etxright); :}
        | AS_DIVA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_DIVA, et, etxleft, etxright); :}
        | AS_POTA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_POTA, et, etxleft, etxright); :}
        | AS_MODA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_MODA, et, etxleft, etxright); :}
        | AS_ANDA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_ANDA, et, etxleft, etxright); :}
        | AS_ORA:et                     {: RESULT = new SymbolAsigOp(ParserSym.AS_ORA, et, etxleft, etxright); :}
        ;
 */
public class SymbolAsigOp extends SymbolBase {
    private final int operacion;

    public SymbolAsigOp(int tipo, Object et, Location l, Location r) {
        super("asigOp", 0 , l , r);
        this.operacion = tipo;
    }
    
    public boolean isBasicAsig() {
        return operacion == ParserSym.AS_ASSIGN;
    }
    
    public boolean doesOperationResultInSameType(String type) {
        if (type.equals(ParserSym.terminalNames[ParserSym.BOOL])){
            return isBasicAsig() ||
                    operacion == ParserSym.AS_ANDA ||
                    operacion == ParserSym.AS_ORA;
        } else if (type.equals(ParserSym.terminalNames[ParserSym.CHAR])
                || type.equals(ParserSym.terminalNames[ParserSym.INT])){
            return isBasicAsig() ||
                    operacion == ParserSym.AS_ADDA ||
                    operacion == ParserSym.AS_SUBA ||
                    operacion == ParserSym.AS_MULA ||
                    operacion == ParserSym.AS_DIVA ||
                    operacion == ParserSym.AS_MODA ||
                    operacion == ParserSym.AS_POTA;
        } else if (type.equals(ParserSym.terminalNames[ParserSym.DOUBLE])){
            return isBasicAsig() ||
                    operacion == ParserSym.AS_ADDA ||
                    operacion == ParserSym.AS_SUBA ||
                    operacion == ParserSym.AS_MULA ||
                    operacion == ParserSym.AS_DIVA;
        } else if (type.equals(ParserSym.terminalNames[ParserSym.STRING])){
            return isBasicAsig() ||
                    operacion == ParserSym.AS_ADDA;
        }
        return false;
    }

}
