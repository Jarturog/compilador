
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
public class SymbolAsigBasico extends SymbolBase {
    
    public final SymbolOperand operando;
    
    public SymbolAsigBasico(SymbolOperand operando, Location l, Location r){
        super("asig",0, l, r);
        this.operando = operando;
    }
    
    public SymbolAsigBasico() {
        super("asig");
        operando = null;
    }

}
