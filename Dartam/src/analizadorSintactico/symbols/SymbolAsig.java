
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
 * ASIG ::= AS_ASSIGN OPERAND:et   {: RESULT = new SymbolAsig(et, etleft, etright); :}
        |                       {: RESULT = new SymbolAsig(); :}
        ;
 */
public class SymbolAsig extends SymbolBase {
    private static int id = 0;
    private SymbolOperand operando;
    
    public SymbolAsig(SymbolOperand operando, int l, int r){
        super("asig",0, l, r);
        this.operando = operando;
    }
    
    public SymbolAsig() {
        super("asig", 0);
    }

    public SymbolOperand getOperando() {
        return operando;
    }
    
    
    
}
