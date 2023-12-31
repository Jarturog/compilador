
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
 *METODO_ELEMENTO ::= INSTR:et    {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | LOOP:et               {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | IF:et                 {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
        | SWITCH:et             {: RESULT = new SymbolMetodoElemento(et, etleft, etright); :}
 */
public class SymbolMetodoElemento extends SymbolBase {
    
    private SymbolMain main;
    private SymbolMetodoElemento mam;

    public SymbolMetodoElemento(SymbolMain sm) {
        super("sam");
        this.main = sm;
    }
    
    public SymbolMetodoElemento(SymbolMetodoElemento mam){
        super("sam");
        this.mam = mam;
    }

    public SymbolMetodoElemento(SymbolSwitch et) {
        super("");
    }

    public SymbolMetodoElemento(SymbolIf et) {
        super("");
    }

    public SymbolMetodoElemento(SymbolLoop et) {
        super("");
    }

    public SymbolMetodoElemento(SymbolInstr et) {
        super("");
    }

    public SymbolMetodoElemento(SymbolInstr et, int etleft, int etright) {
        super("");
    }

    public SymbolMetodoElemento(SymbolLoop et, int etleft, int etright) {
        super("");
    }

    public SymbolMetodoElemento(SymbolIf et, int etleft, int etright) {
        super("");
    }

    public SymbolMetodoElemento(SymbolSwitch et, int etleft, int etright) {
        super("");
    }
    
}
