
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
 * Reglas:
 * 
 * ELEMENTO_METODO ::= INSTR:et {: RESULT = new SymbolElementoMetodo(et); :}
        | LOOP:et               {: RESULT = new SymbolElementoMetodo(et); :}
        | IF:et                 {: RESULT = new SymbolElementoMetodo(et); :}
        | SWITCH:et             {: RESULT = new SymbolElementoMetodo(et); :}
        ;

 */
public class SymbolElementoMetodo extends SymbolBase {
    
    private SymbolInstr instruccion;
    private SymbolLoop insLoop;
    private SymbolIf    insIf;
    private SymbolSwitch insSwitch;
    
    public SymbolElementoMetodo(SymbolInstr instruccion) {
        super("elementoMetodo");
        this.instruccion = instruccion;
    }
    
    public SymbolElementoMetodo(SymbolLoop insLoop) {
        super("elementoMetodo");
        this.insLoop = insLoop;
    }
    
    public SymbolElementoMetodo(SymbolIf insIf) {
        super("elementoMetodo");
        this.insIf = insIf;
    }
    
    public SymbolElementoMetodo(SymbolSwitch insSwitch) {
        super("elementoMetodo");
        this.insSwitch = insSwitch;
    }

    public SymbolInstr getInstruccion() {
        return instruccion;
    }

    public SymbolLoop getInsLoop() {
        return insLoop;
    }

    public SymbolIf getInsIf() {
        return insIf;
    }

    public SymbolSwitch getInsSwitch() {
        return insSwitch;
    }
    
    
    
}
