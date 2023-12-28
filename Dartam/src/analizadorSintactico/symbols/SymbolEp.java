/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Professor: Pere Palmer
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

import compiler.sintactic.ParserSym;
/**
 * Classe que implementa la variable Ep de la gramàtica
 * 
 * @author Pere Palmer
 */
public class SymbolEp extends SymbolBase {
    private int operacio;
    
    /**
     * construeix una variable Ep buida (en una derivació a lambda)
     */
    public SymbolEp() {
        super("Ep", 0.0); // Crear instància amb un valor fals
    }
    
    /**
     * construeix una variable Ep a partir del valor de una T i una Ep 
     * (què pot ser buida). Els valors es combinen segons quina sigui l'operació
     * que conté valor2 (què és la operació que es troba entre els dos valors
     * @param operacio quina operació representa aquesta reducció?
     * @param valorT
     * @param valorEp 
     */
    public SymbolEp(int operacio, double valorT, SymbolEp valorEp) {
        super("Ep", 0.0); // Crear instància amb un valor fals
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
        this.operacio = operacio;
        this.value = valor;
    }

    /**
     * Mètode que permet determinar si quina operació ha de combinar el valor de
     * la variable amb l'anterior variable T
     * @return 
     */
    public int getOperacio() {
        return this.operacio;
    }
    
   
}
