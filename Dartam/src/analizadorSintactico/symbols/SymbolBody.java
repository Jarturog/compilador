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
 * BODY ::= ELEMENTO_METODO:et1 BODY:et2
 *        |
 *        ;
 */
public class SymbolBody extends SymbolBase {
    
    public final SymbolMetodoElemento metodo;
    public final SymbolBody  siguienteMetodo; 

    public SymbolBody() {
        super("body");
        this.metodo = null;
        this.siguienteMetodo = null;
    }
    
    public SymbolBody(SymbolMetodoElemento sem, SymbolBody sb, Location l, Location r) {
        super("body", l, r);
        this.metodo = sem;
        this.siguienteMetodo = sb;
    }

    public SymbolMetodoElemento getMetodo() {
        return metodo;
    }

    public SymbolBody getSiguienteMetodo() {
        return siguienteMetodo;
    }
        
}
