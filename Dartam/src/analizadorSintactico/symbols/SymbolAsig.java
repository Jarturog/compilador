
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
 * ASIG ::= AS_ASSIGN OPERAND:et   {: RESULT = new SymbolAsig(et, etleft, etright); :}
        |                       {: RESULT = new SymbolAsig(); :}
        ;
 */
public class SymbolAsig extends SymbolBase {
    
    public final SymbolOperand operando;
    
    public SymbolAsig(SymbolOperand operando, Location l, Location r){
        super("asig",0, l, r);
        this.operando = operando;
    }
    
    public SymbolAsig() {
        super("asig");
        operando = null;
    }

}
