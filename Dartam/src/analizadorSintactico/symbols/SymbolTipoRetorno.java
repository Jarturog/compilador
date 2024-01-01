
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
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * TIPO_RETORNO ::= TIPO_VAR:et            {: RESULT = new SymbolTipoRetorno(et, etleft, etright); :}
        | KW_VOID:et                    {: RESULT = new SymbolTipoRetorno(true, etleft, etright); :} //Revisar
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

}
