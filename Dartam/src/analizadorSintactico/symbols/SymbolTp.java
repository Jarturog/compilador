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
 * Classe que implementa la variable Tp de la gramàtica
 * 
 * @author Pere Palmer
 */
public class SymbolTp extends SymbolBase {    
    private int operacio;
    
    /**
     * construeix una variable Ep buida (en una derivació a lambda)
     */
    public SymbolTp() {
        super(); // Crear instància amb un valor fals
    }
    
    /**
     * construeix una variable T a partir del valor de una F i una Tp 
     * (què pot ser buida). Els valors es combinen segons quina sigui l'operació
     * que conté valor2 (què és la operació que es troba entre els dos valors
     * @param operacio quina operació representa aquesta reducció?
     * @param valorF
     * @param valorEp 
     */  
    public SymbolTp(int operacio, double valorF, SymbolTp valorTp) {
        super("Tp", 0.0); // Crear instància amb un valor fals
        
        this.empty = false;
        Double valor;
        
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
                    valor = 0.0;
            }
        }
        this.operacio = operacio;
        this.value = valor;
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
