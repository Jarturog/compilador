
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
    private int operacion;

    /*public SymbolAsigOp(String variable, Double valor, Location l , Location r) {
        super(variable);
       
    }*/

    public SymbolAsigOp(int AS_ORA, Object et, Location l, Location r) {
        super("asigOp", 0 , l , r);
        this.operacion = AS_ORA;
    }

    public int getOperacion() {
        return operacion;
    }
    
    
    
}
