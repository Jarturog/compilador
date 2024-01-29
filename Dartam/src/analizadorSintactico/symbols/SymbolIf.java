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
IF ::= KW_IF OPERAND:et1 LKEY BODY:et2 RKEY ELIFS:et3 ELSE:et4          
    {: RESULT = new SymbolIf(et1, et2, et3, et4, et1xleft, et1xright); :}
        ;
 */
public class SymbolIf extends SymbolBase {
    public final SymbolOperand cond;
    public final SymbolBody cuerpo;
    public final SymbolElifs elifs;
    public final SymbolElse els;

    public SymbolIf(SymbolOperand op, SymbolBody cuerpo, SymbolElifs elifs, SymbolElse els, Location l, Location r) {
        super("if" ,l , r);
        this.cond = op;
        this.cuerpo = cuerpo;
        this.elifs = elifs;
        this.els = els;
    }
    
}
