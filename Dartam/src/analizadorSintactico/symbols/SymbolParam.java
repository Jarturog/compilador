
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
 * Bàsicament conté un valor enter
 */
public class SymbolParam extends ComplexSymbol {
    private static int id = 0;

    public SymbolParam(String variable, Double valor) {
        super(variable, id++, valor);
    }
    
    public SymbolParam(Object o) {
        super("", id++, 0);
    }
    
    public SymbolParam(Object o, Object o1) {
        super("", id++, 0);
    }
    
}
