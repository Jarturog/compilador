/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Professor: Pere Palmer
 */
package analizadorSintactico.Symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la variable F de la gramàtica. De moment no fa res més
 * que extendre la classe base SymbolBase
 * 
 * @author Pere Palmer
 */
public class SymbolF extends SymbolBase {
    public SymbolF(double valor) {
        super("F", valor);
    }
    
    public SymbolF() {
        super();
    }
}
