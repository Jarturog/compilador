
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
 * Bàsicament conté un valor enter
 */
public class SymbolRUnaryOperator extends SymbolBase {
    

    public SymbolRUnaryOperator(String variable, Double valor) {
        super(variable);
    }

    public SymbolRUnaryOperator(int OP_PCT, Object et) {
        super("");
    }
    
}
