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
 * 
 * TIPO_RETORNO ::= TIPO_VAR:et            {: RESULT = new SymbolTipoRetorno(et, etleft, etright); :}
        | VOID:et                    {: RESULT = new SymbolTipoRetorno(true, etleft, etright); :} //Revisar
        ;
 */
public class SymbolTipoRetorno extends SymbolBase {
    public final SymbolTipo tipo;
    
    public SymbolTipoRetorno(SymbolTipo t, Location l, Location r){
        super("tipoRetornoVar", l, r);
        tipo = t;
    }
    
    public SymbolTipoRetorno(Location l, Location r){
        super("tipoRetornoVoid", l , r);
        tipo = null;
    }

    public String getTipo() {
        if (tipo == null) {
            return ParserSym.terminalNames[ParserSym.VOID];
        }
        return tipo.getTipo();
    }
    
}
