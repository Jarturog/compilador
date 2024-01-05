
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
 * MAIN ::= KW_METHOD KW_VOID KW_MAIN LPAREN KW_STRING LBRACKET RBRACKET KW_ARGS RPAREN LKEY BODY:et RKEY  
        | MAIN:et1 SCRIPT_ELEMENTO:et2     
        ;
 */
public class SymbolMain extends SymbolBase {
    
    // body main
    public final String nombreMain, nombreArgumentos, lBracket, rBracket;
    public final SymbolBody main;
    // hay mas elementos
    public final SymbolMain siguienteElemento;
    public final SymbolScriptElemento elemento;

    public SymbolMain(Object m, Object a, Object lb, Object rb, SymbolBody b, Location l, Location r) {
        super("main", r, r);
        main = b;
        siguienteElemento = null;
        elemento = null;
        nombreMain = (String)m;
        nombreArgumentos = (String)a;
        lBracket = (String)lb;
        rBracket = (String)rb;
    }

    public SymbolMain(SymbolMain m, SymbolScriptElemento e, Location l, Location r) {
        super("elementoDespuesDeMain", l, r);
        main = null;
        siguienteElemento = m;
        elemento = e;
        nombreMain = null;
        nombreArgumentos = null;
        lBracket = null;
        rBracket = null;
    }
}
