
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

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
public class SymbolInstr extends ComplexSymbol {
    private static int id = 0;
    private SymbolFCall fcall;
    private SymbolDecs decs;
    private SymbolAsigs asigs;
    private SymbolSwap swap;
    
    public SymbolInstr(SymbolFCall fcall) {
        super("instruccion", id++, 0);
        this.fcall = fcall;
    }
    
    public SymbolInstr(SymbolDecs decs) {
        super("instruccion", id++, 0);
        this.decs = decs;
    }
    
    public SymbolInstr(SymbolAsigs asigs) {
        super("instruccion", id++, 0);
        this.asigs = asigs;
    }
    
    public SymbolInstr(SymbolSwap swap) {
        super("instruccion", id++, 0);
        this.swap = swap;
    }

    public SymbolFCall getFcall() {
        return fcall;
    }

    public SymbolDecs getDecs() {
        return decs;
    }

    public SymbolAsigs getAsigs() {
        return asigs;
    }

    public SymbolSwap getSwap() {
        return swap;
    }
    
    
}
