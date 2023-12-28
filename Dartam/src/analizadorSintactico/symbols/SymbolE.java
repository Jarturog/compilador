/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Professor: Pere Palmer
 */
package analizadorSintactico.Symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

import analizadorSintactico.ParserSym;
/**
 * Classe que implementa la variable E de la gramàtica. De moment no fa res més
 * que extendre la classe base SymbolBase
 * 
 * @author Pere Palmer
 */
public class SymbolE extends SymbolBase {
    /**
     * construeix una variable E a partir del valor de una T i una Ep 
     * (què pot ser buida). Els valors es combinen segons quina sigui l'operació
     * que conté valor2 (què és la operació que es troba entre els dos valors
     * @param valorT
     * @param valorEp 
     */
    public SymbolE(double valorT, SymbolEp valorEp) {
        super("E", 0.0); // Crear instància amb un valor fals
        Double valor;
        
        if ((valorEp == null) || (valorEp.isEmpty())) {
            valor = valorT;
        } else {
            switch (valorEp.getOperacio()) {
                case ParserSym.ADD:
                    valor = valorT + (Double)valorEp.value;
                    break;
                case ParserSym.SUB:
                    valor = valorT - (Double)valorEp.value;
                    break;
                default:
                    valor = 0.0;
            }
        }
        this.value = valor;
    }
    
    /**
     * construeix una variable E a partir del valor que es rep. Servei pes 
     * cassos d'assignació
     * @param valor el valor que té
     */
    public SymbolE(Double valor) {
        super("E", valor); // Crear instància amb el valor rebut
    }
    
    
    /**
     * construeix una variable Ep buida (en una derivació a lambda)
     */
    public SymbolE() {
        super(); // Crear instància amb un valor fals
    }
}
