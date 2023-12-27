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

/**
 * Classe que implementa la variable F de la gramàtica. De moment no fa res més
 * que extendre la classe base SymbolBase
 * 
 * @author Pere Palmer
 */
public class SymbolF extends SymbolBase {
    public SymbolF(int valor) {
        super("F", valor);
    }
}
