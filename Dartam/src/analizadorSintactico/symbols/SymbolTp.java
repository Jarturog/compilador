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
 * Classe que implementa la variable Tp de la gramàtica
 * 
 * @author Pere Palmer
 */
public class SymbolTp extends SymbolBase {    
    private int operacio;
    private boolean empty;
    
    /**
     * construeix una variable Ep buida (en una derivació a lambda)
     */
    public SymbolTp() {
        super("Tp", 0); // Crear instància amb un valor fals
        this.empty = true;
    }
    
    /**
     * construeix una variable T a partir del valor de una F i una Tp 
     * (què pot ser buida). Els valors es combinen segons quina sigui l'operació
     * que conté valor2 (què és la operació que es troba entre els dos valors
     * @param operacio quina operació representa aquesta reducció?
     * @param valorF
     * @param valorEp 
     */  
    public SymbolTp(int operacio, int valorF, SymbolTp valorTp) {
        super("Tp", 0); // Crear instància amb un valor fals
        
        this.empty = false;
        Integer valor;
        
        if ((valorTp == null) || (valorTp.isEmpty())) {
            valor = valorF;
        } else {
            switch (valorTp.getOperacio()) {
                case ParserSym.MUL:
                    valor = valorF * (Integer)valorTp.value;
                    break;
                case ParserSym.DIV:
                    valor = valorF / (Integer)valorTp.value;
                    break;
                case ParserSym.MOD:
                    valor = valorF % (Integer)valorTp.value;
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
     * la variable amb l'anterior variable F
     * @return 
     */
    public int getOperacio() {
        return this.operacio;
    }
}
