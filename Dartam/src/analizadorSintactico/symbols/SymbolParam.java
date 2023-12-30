
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
 * REGLAS:
 * 
 * PARAM ::= TIPO_VAR:et1 ID:et2  
 *         ;
 */
public class SymbolParam extends ComplexSymbol {
    private static int id = 0;
    private SymbolTipoVar tipo;
    private String identificador;

    public SymbolParam(SymbolTipoVar tipo, String identificador){
        super("param", id++, 0);
        this.tipo = tipo;
        this.identificador = identificador;
    }

    public SymbolTipoVar getTipo() {
        return tipo;
    }

    public String getIdentificador() {
        return identificador;
    }
    
    
    
    
}
