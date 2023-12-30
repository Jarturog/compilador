
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
 * Reglas:
 * 
 * ELEMENTO_METODO ::= INSTR:et {: RESULT = new SymbolElementoMetodo(et); :}
        | LOOP:et               {: RESULT = new SymbolElementoMetodo(et); :}
        | IF:et                 {: RESULT = new SymbolElementoMetodo(et); :}
        | SWITCH:et             {: RESULT = new SymbolElementoMetodo(et); :}
        ;

 */
public class SymbolElementoMetodo extends ComplexSymbol {
    private static int id = 0;
    private SymbolInstr instruccion;
    private SymbolLoop insLoop;
    private SymbolIf    insIf;
    private SymbolSwitch insSwitch;
    
    public SymbolElementoMetodo(SymbolInstr instruccion) {
        super("elementoMetodo", id++, 0);
        this.instruccion = instruccion;
    }
    
    public SymbolElementoMetodo(SymbolLoop insLoop) {
        super("elementoMetodo", id++, 0);
        this.insLoop = insLoop;
    }
    
    public SymbolElementoMetodo(SymbolIf insIf) {
        super("elementoMetodo", id++, 0);
        this.insIf = insIf;
    }
    
    public SymbolElementoMetodo(SymbolSwitch insSwitch) {
        super("elementoMetodo", id++, 0);
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
