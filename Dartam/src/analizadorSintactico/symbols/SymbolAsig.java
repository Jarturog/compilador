
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
ASIG ::= ID:et ASIG_OP:aop OPERAND:val                                                  {: RESULT = new SymbolAsig(et, aop, val); :}
        | OPERAND:et1 AUX_MEMBER LBRACKET OPERAND:et2 RBRACKET ASIG_OP:aop OPERAND:val  {: RESULT = new SymbolAsig(et1, et2, aop, val); :}
        | OPERAND:et1 OP_MEMBER ID:et2 ASIG_OP:aop OPERAND:val                          {: RESULT = new SymbolAsig(et1, et2, aop, val); :}
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
