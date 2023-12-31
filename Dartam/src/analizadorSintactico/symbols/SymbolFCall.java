
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
 * Reglas:
 * 
 * 
FCALL ::= METHOD_NAME:et1 LPAREN SETPARAMS:et2 RPAREN   {: RESULT = new SymbolFCall(et1,et2); :}
        ;
 */
public class SymbolFCall extends SymbolBase {
    
    private SymbolMetodoNombre methodName;
    
    public SymbolFCall(SymbolMetodoNombre methodName) {
        super("fcall");
        this.methodName = methodName;
    }

    public SymbolFCall(SymbolMetodoNombre et1, SymbolOperandsLista et2) {
        super("");
    }

    public SymbolMetodoNombre getMethodName() {
        return methodName;
    }

    
    
}
