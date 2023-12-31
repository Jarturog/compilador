
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class SymbolAsigOp extends SymbolBase {
    

    public SymbolAsigOp(String variable, Double valor) {
        super(variable);
    }

    public SymbolAsigOp(int AS_ORA, Object et) {
        super("");
    }
    
}
