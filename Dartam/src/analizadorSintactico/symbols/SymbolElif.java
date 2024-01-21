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
ELIF ::= KW_ELIF OPERAND:et1 LKEY BODY:et2 RKEY     {: RESULT = new SymbolElif(et1, et2, et1xleft, et1xright); :}
        ;
 */
public class SymbolElif extends SymbolBase {
    
    public final SymbolOperand cond;
    public final SymbolBody cuerpo;
    
    public SymbolElif(SymbolOperand op, SymbolBody b, Location l, Location r){
        super("elif", 0 ,l ,r);
        this.cond = op;
        this.cuerpo = b;
    }
    
}
