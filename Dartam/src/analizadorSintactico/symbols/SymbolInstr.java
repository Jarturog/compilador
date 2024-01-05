
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;



/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * REglas:
 * INSTR ::= FCALL:et ENDINSTR    {: RESULT = new SymbolInstr(et); :}
        | RETURN:et            {: RESULT = new SymbolInstr(et); :}
        | DECS:et              {: RESULT = new SymbolInstr(et); :}
        | ASIGS:et             {: RESULT = new SymbolInstr(et); :}
        | SWAP:et              {: RESULT = new SymbolInstr(et); :}
        ;
 */
public class SymbolInstr extends SymbolBase {
    
    public final SymbolFCall fcall;
    public final SymbolDecs decs;
    public final SymbolAsigs asigs;
    public final SymbolSwap swap;
    public final SymbolReturn ret;
    
    public SymbolInstr(SymbolFCall fcall, Location l, Location r) {
        super("instruccion", 0, l,r);
        this.fcall = fcall;
        this.decs = null;
        this.asigs = null;
        this.swap = null;
        this.ret = null;
    }
    
    public SymbolInstr(SymbolDecs decs,  Location l, Location r) {
        super("instruccion", 0, l, r);
        this.decs = decs;
        this.fcall = null;
        this.asigs = null;
        this.swap = null;
        this.ret = null;
    }
    
    public SymbolInstr(SymbolAsigs asigs,  Location l, Location r) {
        super("instruccion", 0 , l , r);
        this.asigs = asigs;
        this.decs = null;
        this.fcall = null;
        this.swap = null;
        this.ret = null;
    }
    
    public SymbolInstr(SymbolSwap swap,  Location l, Location r) {
        super("instruccion", 0 , l , r);
        this.swap = swap;
        this.decs = null;
        this.fcall = null;
        this.asigs = null;
        this.ret = null;
    }

    public SymbolInstr(SymbolReturn et, Location l, Location r) {
        super("instruccion", 0 ,l ,r);
        this.decs = null;
        this.fcall = null;
        this.asigs = null;
        this.swap = null;
        this.ret = et;
    }
    
    public static final String FCALL = "f", DECS = "d", ASIGS = "a", SWAP = "s", RET = "r";
    
    /**
     * fcall, decs, asigs, swap, return
     * @return f, d, a, s or r
     */
    public String getTipo() {
        if (fcall != null) {
            return FCALL;
        } else if (decs != null) {
            return DECS;
        } else if (asigs != null) {
            return ASIGS;
        } else if (swap != null) {
            return SWAP;
        } else {
            return RET;
        }
    }
}
