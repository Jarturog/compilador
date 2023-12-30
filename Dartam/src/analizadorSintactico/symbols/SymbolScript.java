
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
public class SymbolScript extends ComplexSymbol {
    private static int id = 0;

    public SymbolScript(SymbolMetodoElemento contenedorMain) {
        super("script", id++, 0);
    }

    public SymbolScript(SymbolScript script) {
        super("script", id++, 0);
    }
    
}
