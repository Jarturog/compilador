/**
 * Assignatura 21742 - Compiladors I 
 * Estudis: Grau en Informàtica 
 * Itinerari: Computació 
 * Curs: 2018-2019
 *
 * Professor: Pere Palmer
 */
package jlex_cup_example.compiler_components.cup.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import jlex_cup_example.compiler_components.cup.ParserSym;

/**
 * Classe que implementa la variable Ep de la gramàtica
 * 
 * @author Pere Palmer
 */
public class SymbolEp extends SymbolBase {
    private int operacio;
    private boolean empty;
    
    /**
     * construeix una variable Ep buida (en una derivació a lambda)
     */
    public SymbolEp() {
        super("Ep", 0); // Crear instància amb un valor fals
        this.empty = true;
    }
    
    /**
     * construeix una variable Ep a partir del valor de una T i una Ep 
     * (què pot ser buida). Els valors es combinen segons quina sigui l'operació
     * que conté valor2 (què és la operació que es troba entre els dos valors
     * @param operacio quina operació representa aquesta reducció?
     * @param valorT
     * @param valorEp 
     */
    public SymbolEp(int operacio, int valorT, SymbolEp valorEp) {
        super("Ep", 0); // Crear instància amb un valor fals
        this.empty = false;
        Integer valor;
        
        if ((valorEp == null) || (valorEp.isEmpty())) {
            valor = valorT;
        } else {
            switch (valorEp.getOperacio()) {
                case ParserSym.ADD:
                    valor = valorT + (Integer)valorEp.value;
                    break;
                case ParserSym.SUB:
                    valor = valorT - (Integer)valorEp.value;
                    break;
                default:
                    valor = 0;
            }
        }
        this.operacio = operacio;
        this.value = valor;
    }

    /**
     * Mètode que permet determinar si la variable és buida (lambda)
     * @return 
     */
    public boolean isEmpty() {
        return this.empty;
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
