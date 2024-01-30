/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica 
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;

/**
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
        super("asig", l, r);
        this.operando = operando;
    }
    
    public SymbolAsigBasico(Location l, Location r) {
        super("asig", l, r);
        operando = null;
    }

}
