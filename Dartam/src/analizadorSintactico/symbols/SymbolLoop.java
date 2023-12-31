
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
 * LOOP ::= KW_LOOP:et1 LOOP_COND:et2 LKEY BODY:et3 RKEY                   {: RESULT = new SymbolLoop(et1, et2, et3, et1xleft, et1xright); :}
        | KW_DO:et1 LKEY BODY:et2 RKEY LOOP_COND:et3 ENDINSTR           {: RESULT = new SymbolLoop(et1, et2, et3, et1xleft, et1xright); :}
        ;
 */
public class SymbolLoop extends SymbolBase {
    private int tipoLoop;
    private SymbolLoopCond loopCond;
    private SymbolBody cuerpo;

    public SymbolLoop(int tipoLoop, SymbolLoopCond loopCond, SymbolBody b, Location l, Location r){
        super("loop", 0, l ,r);
        this.tipoLoop = tipoLoop;
        this.loopCond = loopCond;
        this.cuerpo = b;
    }
    
    public SymbolLoop(int tipoLoop,SymbolBody b,SymbolLoopCond loopCond,  Location l, Location r){
        super("loop", 0, l ,r);
        this.tipoLoop = tipoLoop;
        this.loopCond = loopCond;
        this.cuerpo = b;
    }

    public int getTipoLoop() {
        return tipoLoop;
    }

    public SymbolLoopCond getLoopCond() {
        return loopCond;
    }

    public SymbolBody getCuerpo() {
        return cuerpo;
    }
    

}
