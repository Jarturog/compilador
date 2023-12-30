/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en InformÃ tica 
 * Itinerari: IntelÂ·ligÃ¨ncia Artificial i ComputaciÃ³
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramÃ tica.
 * 
 * BÃ sicament contÃ© un valor enter
 */
public class SymbolBase extends ComplexSymbol {
    private static int id = 0;

    public SymbolBase(String name, Object value) {
        super(name, id++, value);
    }
    
}
