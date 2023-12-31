
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;



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
public class SymbolBody extends SymbolBase {
    
    private SymbolElementoMetodo metodo;
    private SymbolBody  siguienteMetodo; 

    public SymbolBody() {
        super("body");
    }
    
    public SymbolBody(SymbolElementoMetodo sem, SymbolBody sb, Location l, Location r) {
        super("body", l, r);
        this.metodo = sem;
        this.siguienteMetodo = sb;
    }

    public SymbolBody(SymbolMetodoElemento et1, SymbolBody et2, Location l, Location r) {
        super("");
    }

    /*
    public SymbolBody(SymbolMetodoElemento et1, SymbolBody et2) {
        super("");
    }*/

    public SymbolElementoMetodo getMetodo() {
        return metodo;
    }

    public SymbolBody getSiguienteMetodo() {
        return siguienteMetodo;
    }

    
    
    
    
}
