
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
 * Reglas:
 * 
 *METODO_ELEMENTO ::= INSTR:et    {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | LOOP:et               {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | IF:et                 {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | SWITCH:et             {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
 */
public class SymbolMetodoElemento extends SymbolBase {
    
    public final SymbolInstr  instruccion;
    public final SymbolLoop   loop;
    public final SymbolIf     iff;
    public final SymbolSwitch sw;
    
    public SymbolMetodoElemento(SymbolInstr instruccion, Location l, Location r) {
        super("elementoMetodo");
        this.instruccion = instruccion;
        this.loop = null;
        this.iff = null;
        this.sw = null;
    }
    
    public SymbolMetodoElemento(SymbolLoop insLoop, Location l, Location r) {
        super("elementoMetodo");
        this.loop = insLoop;
        this.instruccion = null;
        this.iff = null;
        this.sw = null;
    }
    
    public SymbolMetodoElemento(SymbolIf insIf, Location l, Location r) {
        super("elementoMetodo");
        this.iff = insIf;
        this.instruccion = null;
        this.loop = null;
        this.sw = null;
    }
    
    public SymbolMetodoElemento(SymbolSwitch insSwitch, Location l, Location r) {
        super("elementoMetodo");
        this.sw = insSwitch;
        this.instruccion = null;
        this.loop = null;
        this.iff = null;
    }

    public static final String INSTR = "i", IF = "c", LOOP = "l", SWITCH = "s";

    /**
     * instruccion, if, loop, switch
     * @return i, c, l or s
     */
    public String getTipo() {
        if (instruccion != null) {
            return INSTR;
        } else if (iff != null) {
            return IF;
        } else if (loop != null) {
            return LOOP;
        } else {
            return SWITCH;
        }
    }
    
}
