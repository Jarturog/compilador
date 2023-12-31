
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
 * MAIN ::= KW_METHOD KW_VOID KW_MAIN LPAREN KW_STRING LBRACKET RBRACKET KW_ARGS RPAREN LKEY BODY:et RKEY  
        | MAIN:et1 SCRIPT_ELEMENTO:et2     
        ;
 */
public class SymbolMain extends SymbolBase {
    
    // body main
    public final SymbolBody body;
    // hay más elementos
    public final SymbolMain main;
    public final SymbolScriptElemento elemento;

    public SymbolMain(SymbolBody b, Location l, Location r) {
        super("main", r, r);
        body = b;
        main = null;
        elemento = null;
    }

    public SymbolMain(SymbolMain m, SymbolScriptElemento e, Location l, Location r) {
        super("elementoDespuesDeMain", l, r);
        body = null;
        main = m;
        elemento = e;
    }
}
