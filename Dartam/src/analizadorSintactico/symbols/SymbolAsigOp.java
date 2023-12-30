
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

public class SymbolAsigOp extends SymbolBase {
    private static int id = 0;

    public SymbolAsigOp(String variable, Double valor) {
        super(variable, valor);
    }
    
}
