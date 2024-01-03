
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
 * ASIG_OP ::= AS_ASSIGN:et                {: RESULT = new SymbolAsigOp(ParserSym.AS_ASSIGN, et, etxleft, etxright); :}
        | AS_ADDA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_ADDA, et, etxleft, etxright); :}
        | AS_SUBA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_SUBA, et, etxleft, etxright); :}
        | AS_MULA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_MULA, et, etxleft, etxright); :}
        | AS_DIVA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_DIVA, et, etxleft, etxright); :}
        | AS_POTA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_POTA, et, etxleft, etxright); :}
        | AS_ANDA:et                    {: RESULT = new SymbolAsigOp(ParserSym.AS_ANDA, et, etxleft, etxright); :}
        | AS_ORA:et                     {: RESULT = new SymbolAsigOp(ParserSym.AS_ORA, et, etxleft, etxright); :}
        ;
 */
public class SymbolAsigOp extends SymbolBase {
    public final int operacion;

    public SymbolAsigOp(int tipo, Object et, Location l, Location r) {
        super("asigOp", 0 , l , r);
        this.operacion = tipo;
    }
    
    public boolean isBasicAsig() {
        return operacion == ParserSym.AS_ASSIGN;
    }
    
}
