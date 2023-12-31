
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
public class SymbolBinaryOperator extends SymbolBase {
    

    public SymbolBinaryOperator(String variable, Double valor) {
        super(variable);
    }

    public SymbolBinaryOperator(int OP_ADD, Object et) {
        super("");
    }
    
}
