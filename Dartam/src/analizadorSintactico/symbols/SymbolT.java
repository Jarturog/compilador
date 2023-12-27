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
 * Classe que implementa la variable T de la gramàtica
 * 
 * @author Pere Palmer
 */
public class SymbolT extends SymbolBase {  
    /**
     * construeix una variable T a partir del valor de una F i una Tp 
     * (què pot ser buida). Els valors es combinen segons quina sigui l'operació
     * que conté valor2 (què és la operació que es troba entre els dos valors
     * @param valorF
     * @param valorEp 
     */  
    public SymbolT(int valorF, SymbolTp valorTp) {
        super("T", 0); // Crear instància amb un valor fals
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
        this.value = valor;
    }
}
