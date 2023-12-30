
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * Reglas:
 * 
 * 
FCALL ::= METHOD_NAME:et1 LPAREN SETPARAMS:et2 RPAREN   {: RESULT = new SymbolFCall(et1,et2); :}
        ;
 */
public class SymbolFCall extends ComplexSymbol {
    private static int id = 0;
    private SymbolMethodName methodName;
    private SymbolSetParams setparams;
    
    public SymbolFCall(SymbolMethodName methodName, SymbolSetParams setparams ) {
        super("fcall", id++, 0);
        this.methodName = methodName;
        this.setparams = setparams;
    }

    public SymbolMethodName getMethodName() {
        return methodName;
    }

    public SymbolSetParams getSetparams() {
        return setparams;
    }
    
    
    
}
