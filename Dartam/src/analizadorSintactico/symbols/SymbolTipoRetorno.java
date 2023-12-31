
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * TIPO_RETORNO ::= TIPO_VAR:et            {: RESULT = new SymbolTipoRetorno(et, etleft, etright); :}
        | KW_VOID:et                    {: RESULT = new SymbolTipoRetorno(true, etleft, etright); :} //Revisar
        ;
 */
public class SymbolTipoRetorno extends SymbolBase {
    private static int id = 0;
    private SymbolTipoVar tipo;
    private boolean isVoid;
    
    public SymbolTipoRetorno(SymbolTipoVar tipo, int l, int r){
        super("tipoRetorno", 0, l, r);
        this.tipo = tipo;
    }
    
    public SymbolTipoRetorno(boolean isVoid, int l, int r){
        super("tipoRetorno", 0 , l , r);
        this.isVoid = isVoid;
    }

    public SymbolTipoVar getTipo() {
        return tipo;
    }

    public boolean isIsVoid() {
        return isVoid;
    }
    
    
    
   
    
}
