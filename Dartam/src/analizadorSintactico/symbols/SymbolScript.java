
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
 * SCRIPT ::= SCRIPT_ELEMENTO:et1 SCRIPT:et2       {: RESULT = new SymbolScript(et1, et2, et1left, et1right); :}
        | MAIN:et                               {: RESULT = new SymbolScript(et, etleft, etright); :}
        ;
 */
public class SymbolScript extends SymbolBase {

    public final SymbolScriptElemento elemento;
    public final SymbolScript siguienteElemento; // o main
    
    public final SymbolMain main;
    
    public SymbolScript(SymbolScriptElemento e, SymbolScript s, Location l, Location r) {
        super("script", l, r);
        main = null;
        elemento = e;
        siguienteElemento = s;
    }
    
    public SymbolScript(SymbolMain m, Location l, Location r) {
        super("script", l, r);
        main = m;
        elemento = null;
        siguienteElemento = null;
    }

    public SymbolScriptElemento getElemento() {
        return elemento;
    }

    public SymbolScript getSiguienteElemento() {
        return siguienteElemento;
    }

    public SymbolMain getMain() {
        return main;
    }
    
    
    
}
