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
 * Reglas:
 * 
 *METODO_ELEMENTO ::= INSTR:et    {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | LOOP:et               {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | IF:et                 {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | SWITCH:et             {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | BLOQUE
 */
public class SymbolMetodoElemento extends SymbolBase {
    
    public final SymbolInstr  instruccion;
    public final SymbolLoop   loop;
    public final SymbolIf     iff;
    public final SymbolSwitch sw;
    public final SymbolBloque block;
    
    public SymbolMetodoElemento(SymbolInstr instruccion, Location l, Location r) {
        super("elementoMetodo");
        this.instruccion = instruccion;
        this.loop = null;
        this.iff = null;
        this.sw = null;
        this.block = null;
    }
    
    public SymbolMetodoElemento(SymbolLoop insLoop, Location l, Location r) {
        super("elementoMetodo");
        this.loop = insLoop;
        this.instruccion = null;
        this.iff = null;
        this.sw = null;
        this.block = null;
    }
    
    public SymbolMetodoElemento(SymbolIf insIf, Location l, Location r) {
        super("elementoMetodo");
        this.iff = insIf;
        this.instruccion = null;
        this.loop = null;
        this.sw = null;
        this.block = null;
    }
    
    public SymbolMetodoElemento(SymbolSwitch insSwitch, Location l, Location r) {
        super("elementoMetodo");
        this.sw = insSwitch;
        this.instruccion = null;
        this.loop = null;
        this.iff = null;
        this.block = null;
    }
    
    public SymbolMetodoElemento(SymbolBloque b, Location l, Location r) {
        super("elementoMetodo");
        this.sw = null;
        this.instruccion = null;
        this.loop = null;
        this.iff = null;
        this.block = b;
    }

    public static enum TIPO {
        INSTR, IF, LOOP, SWITCH, BLOQUE;
    }

    public TIPO getTipo() {
        if (instruccion != null) {
            return TIPO.INSTR;
        } else if (iff != null) {
            return TIPO.IF;
        } else if (loop != null) {
            return TIPO.LOOP;
        } else if (sw != null) {
            return TIPO.SWITCH;
        } else {
            return TIPO.BLOQUE;
        }
    }
    
}
