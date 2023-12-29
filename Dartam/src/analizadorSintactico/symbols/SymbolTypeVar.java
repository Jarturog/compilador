
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Inform�tica 
 * Itinerari: Intel�lig�ncia Artificial i Computaci�
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les varaibles de la gram�tica.
 * 
 * B�sicament cont� un valor enter
 */
public class SymbolTypeVar extends ComplexSymbol {
    private static int id = 0;

    public SymbolTypeVar(String variable, Double valor) {
        super(variable, id++, valor);
    }
    
}
