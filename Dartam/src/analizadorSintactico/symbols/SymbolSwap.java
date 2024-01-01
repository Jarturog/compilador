
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;



/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * SWAP ::= OPERAND:et1 OP_SWAP OPERAND:et2 ENDINSTR           {: RESULT = new SymbolSwap(et1, et2, et1xleft, et1xright); :}
        ;

 */
public class SymbolSwap extends SymbolBase {
    
    public final String op1, op2;

    public SymbolSwap(String op1, String op2, Location l, Location r) {
        super("swap", 0 , l ,r );
        this.op1 = op1;
        this.op2 = op2;
    }

}
