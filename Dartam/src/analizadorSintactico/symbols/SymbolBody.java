
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
 * Reglas:
 * 
 * BODY ::= ELEMENTO_METODO:et1 BODY:et2
 *        |
 *        ;
 */
public class SymbolBody extends ComplexSymbol {
    private static int id = 0;
    private SymbolElementoMetodo metodo;
    private SymbolBody  siguienteMetodo; 

    public SymbolBody() {
        super("body", id++, 0);
    }
    
    public SymbolBody(SymbolElementoMetodo sem, SymbolBody sb) {
        super("body", id++, 0);
        this.metodo = sem;
        this.siguienteMetodo = sb;
    }

    public SymbolElementoMetodo getMetodo() {
        return metodo;
    }

    public SymbolBody getSiguienteMetodo() {
        return siguienteMetodo;
    }

    
    
    
    
}
