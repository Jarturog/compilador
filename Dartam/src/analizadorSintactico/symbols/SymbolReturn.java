
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
 * RETURN ::= KW_RETURN ENDINSTR                               {: :} //Con este que hacemos?
        | KW_RETURN OPERAND:et ENDINSTR                     {: RESULT = new SymbolReturn(et, etxleft, etxright) :}
        ;

 */
public class SymbolReturn extends SymbolBase {
    
    private SymbolOperand op;
    
    public SymbolReturn(SymbolOperand op, Location l, Location r){
        super("operand", 0, l , r);
        this.op = op;
    }

    public SymbolOperand getOp() {
        return op;
    }
    
    
    
}
