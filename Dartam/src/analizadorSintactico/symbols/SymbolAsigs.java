
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informatica 
 * Itinerari: Inteligencia Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;



/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramatica.
 * 
 * Basicament conte un valor enter
 */
public class SymbolAsigs extends SymbolBase {
    

    public SymbolAsigs(String variable, Double valor) {
        super(variable);
    }
    
}
