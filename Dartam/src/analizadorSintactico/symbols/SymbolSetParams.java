
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
 * REglas:
 * 
 * SETPARAMS ::= OPERANDSLISTA:et          {: RESULT = new SymbolSetParams(et); :}
        |                               {: RESULT = new SymbolSetParams(); :}
        ;
 */
public class SymbolSetParams extends ComplexSymbol {
    private static int id = 0;
    private SymbolOperandsLista operandsLista;
    
    public SymbolSetParams(SymbolOperandsLista operandsLista) {
        super("operandsLista", id++, 0);
        this.operandsLista = operandsLista;
    }

    public SymbolOperandsLista getOperandsLista() {
        return operandsLista;
    }
    
    
    
}
