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
    public SymbolT(double valorF, SymbolTp valorTp) {
        super("T", 0.0); // Crear instància amb un valor fals
        Double valor;
        
        if ((valorTp == null) || (valorTp.isEmpty())) {
            valor = valorF;
        } else {
            switch (valorTp.getOperacio()) {
                case ParserSym.MUL:
                    valor = valorF * (Double)valorTp.value;
                    break;
                case ParserSym.DIV:
                    valor = valorF / (Double)valorTp.value;
                    break;
                case ParserSym.MOD:
                    valor = valorF % (Double)valorTp.value;
                    break;
                default:
                    valor = 0.0;
            }
        }
        this.value = valor;
    }
    
    public SymbolT() {
        super();
    }
}
