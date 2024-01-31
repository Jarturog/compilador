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
    public final Boolean continuaNotPara;

    public SymbolInstr(SymbolFCall fcall, Location l, Location r) {
        super("instruccion", l, r);
        this.fcall = fcall;
        this.decs = null;
        this.asigs = null;
        this.swap = null;
        this.ret = null;
        this.continuaNotPara = null;
    }

    public SymbolInstr(SymbolDecs decs, Location l, Location r) {
        super("instruccion", l, r);
        this.decs = decs;
        this.fcall = null;
        this.asigs = null;
        this.swap = null;
        this.ret = null;
        this.continuaNotPara = null;
    }

    public SymbolInstr(SymbolAsigs asigs, Location l, Location r) {
        super("instruccion", l, r);
        this.asigs = asigs;
        this.decs = null;
        this.fcall = null;
        this.swap = null;
        this.ret = null;
        this.continuaNotPara = null;
    }

    public SymbolInstr(SymbolSwap swap, Location l, Location r) {
        super("instruccion", l, r);
        this.swap = swap;
        this.decs = null;
        this.fcall = null;
        this.asigs = null;
        this.ret = null;
        this.continuaNotPara = null;
    }

    public SymbolInstr(SymbolReturn et, Location l, Location r) {
        super("instruccion", l, r);
        this.decs = null;
        this.fcall = null;
        this.asigs = null;
        this.swap = null;
        this.ret = et;
        this.continuaNotPara = null;
    }

    public SymbolInstr(boolean continua, Location l, Location r) {
        super("instruccion", l, r);
        this.decs = null;
        this.fcall = null;
        this.asigs = null;
        this.swap = null;
        this.ret = null;
        this.continuaNotPara = continua;
    }

    public static enum TIPO {
        FCALL, DECS, ASIGS, SWAP, RET, CONTINUE, BREAK
    }

    public TIPO getTipo() {
        if (fcall != null) {
            return TIPO.FCALL;
        } else if (decs != null) {
            return TIPO.DECS;
        } else if (asigs != null) {
            return TIPO.ASIGS;
        } else if (swap != null) {
            return TIPO.SWAP;
        } else if (ret != null) {
            return TIPO.RET;
        } else if (continuaNotPara) {
            return TIPO.CONTINUE;
        } else {
            return TIPO.BREAK;
        }
    }
}
