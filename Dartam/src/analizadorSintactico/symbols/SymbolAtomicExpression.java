
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
public class SymbolAtomicExpression extends SymbolBase {
    

    public SymbolAtomicExpression(String variable, Double valor) {
        super(variable);
    }

    public SymbolAtomicExpression(String et, Location l, Location r) {
        super("");
    }

    public SymbolAtomicExpression(Character et, Location l, Location r) {
        super("");
    }

    public SymbolAtomicExpression(Double et, Location l, Location r) {
        super("");
    }

    public SymbolAtomicExpression(Integer et, Location l, Location r) {
        super("");
    }

    public SymbolAtomicExpression(Boolean et, Location l, Location r) {
        super("");
    }
    
}
