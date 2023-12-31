
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * TIPO_RETORNO ::= TIPO_VAR:et            {: RESULT = new SymbolTipoRetorno(et, etleft, etright); :}
        | KW_VOID:et                    {: RESULT = new SymbolTipoRetorno(true, etleft, etright); :} //Revisar
        ;
 */
public class SymbolTipoRetorno extends SymbolBase {
    public final SymbolTipoVar tipo;
    public final Integer voidNum;
    
    public SymbolTipoRetorno(SymbolTipoVar t, int l, int r){
        super("tipoRetorno", l, r);
        tipo = t;
        voidNum = null;
    }
    
    public SymbolTipoRetorno(int v, int l, int r){
        super("tipoRetorno", l , r);
        tipo = null;
        voidNum = v;
    }

}
