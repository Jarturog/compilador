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
 * LOOP ::= KW_LOOP:et1 LOOP_COND:et2 LKEY BODY:et3 RKEY                   {: RESULT = new SymbolLoop(et1, et2, et3, et1xleft, et1xright); :}
        | KW_DO:et1 LKEY BODY:et2 RKEY LOOP_COND:et3 ENDINSTR           {: RESULT = new SymbolLoop(et1, et2, et3, et1xleft, et1xright); :}
        ;
 */
public class SymbolLoop extends SymbolBase {

    public final SymbolLoopCond loopCond;
    public final SymbolBody cuerpo;
    private final boolean isDoWhile;

    public SymbolLoop(SymbolLoopCond loopCond, SymbolBody b, Location l, Location r){
        super("loop", l ,r);
        this.loopCond = loopCond;
        this.cuerpo = b;
        isDoWhile = false;
    }
    
    public SymbolLoop(SymbolBody b, SymbolLoopCond loopCond,  Location l, Location r){
        super("loop", l ,r);
        this.loopCond = loopCond;
        this.cuerpo = b;
        isDoWhile = true;
    }
    
    public boolean isDoWhile(){
        return isDoWhile;
    }
    
    public boolean isFor(){
        return loopCond.isFor();
    }
}
