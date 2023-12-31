
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;



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
    
    private SymbolFCall fcall;
    private SymbolDecs decs;
    private SymbolAsigs asigs;
    private SymbolSwap swap;
    
    public SymbolInstr(SymbolFCall fcall) {
        super("instruccion");
        this.fcall = fcall;
    }
    
    public SymbolInstr(SymbolDecs decs) {
        super("instruccion");
        this.decs = decs;
    }
    
    public SymbolInstr(SymbolAsigs asigs) {
        super("instruccion");
        this.asigs = asigs;
    }
    
    public SymbolInstr(SymbolSwap swap) {
        super("instruccion");
        this.swap = swap;
    }

    public SymbolInstr(SymbolReturn et) {
        super("");
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
