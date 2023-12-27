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
    public SymbolE(int valorT, SymbolEp valorEp) {
        super("E", 0); // Crear instància amb un valor fals
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
        this.value = valor;
    }
}
