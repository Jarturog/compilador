
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
 * Reglas:
 * 
 *METODO_ELEMENTO ::= INSTR:et    {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | LOOP:et               {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | IF:et                 {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | SWITCH:et             {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
 */
public class SymbolMetodoElemento extends SymbolBase {
    
    public final SymbolInstr instruccion;
    public final SymbolLoop insLoop;
    public final SymbolIf    insIf;
    public final SymbolSwitch insSwitch;
    
    public SymbolMetodoElemento(SymbolInstr instruccion, Location l, Location r) {
        super("elementoMetodo");
        this.instruccion = instruccion;
        this.insLoop = null;
        this.insIf = null;
        this.insSwitch = null;
    }
    
    public SymbolMetodoElemento(SymbolLoop insLoop, Location l, Location r) {
        super("elementoMetodo");
        this.insLoop = insLoop;
        this.instruccion = null;
        this.insIf = null;
        this.insSwitch = null;
    }
    
    public SymbolMetodoElemento(SymbolIf insIf, Location l, Location r) {
        super("elementoMetodo");
        this.insIf = insIf;
        this.instruccion = null;
        this.insLoop = null;
        this.insSwitch = null;
    }
    
    public SymbolMetodoElemento(SymbolSwitch insSwitch, Location l, Location r) {
        super("elementoMetodo");
        this.insSwitch = insSwitch;
        this.instruccion = null;
        this.insLoop = null;
        this.insIf = null;
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
