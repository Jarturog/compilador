
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informatica 
 * Itinerari: Inteligencia Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramatica.
 * 
 * Basicament conte un valor enter
 */
public class SymbolBinaryOperator extends ComplexSymbol {
    private static int id = 0;

    public SymbolBinaryOperator(String variable, Double valor) {
        super(variable, id++, valor);
    }
    
}
