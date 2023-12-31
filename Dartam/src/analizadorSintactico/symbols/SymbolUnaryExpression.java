
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * Bàsicament conté un valor enter
 */
public class SymbolUnaryExpression extends SymbolBase {
    

    public SymbolUnaryExpression(String variable, Double valor) {
        super(variable);
    }

    public SymbolUnaryExpression(SymbolLUnaryOperator et, SymbolOperand op) {
        super("");
    }

    
}
