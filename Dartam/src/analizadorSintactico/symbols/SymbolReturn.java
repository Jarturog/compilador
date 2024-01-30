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
 * RETURN ::= KW_RETURN ENDINSTR                               {: :} //Con este que hacemos?
        | KW_RETURN OPERAND:et ENDINSTR                     {: RESULT = new SymbolReturn(et, etxleft, etxright) :}
        ;

 */
public class SymbolReturn extends SymbolBase {
    
    public final SymbolOperand op;
    
    public SymbolReturn(SymbolOperand op, Location l, Location r){
        super("operand", l , r);
        this.op = op;
    }

}
