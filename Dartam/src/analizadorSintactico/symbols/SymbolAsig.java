
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
 * ASIG ::= AS_ASSIGN OPERAND:et   {: RESULT = new SymbolAsig(et); :}
        |                       {: RESULT = new SymbolAsig(); :}
        ;
 */
public class SymbolAsig extends ComplexSymbol {
    private static int id = 0;
    private SymbolOperand operando;
    
    public SymbolAsig(SymbolOperand operando){
        super("asig", id++, 0);
        this.operando = operando;
    }
    
    public SymbolAsig() {
        super("", id++, 0);
    }

    public SymbolOperand getOperando() {
        return operando;
    }
    
    
    
}
