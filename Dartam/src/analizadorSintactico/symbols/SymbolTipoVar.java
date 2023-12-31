
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
 * Bàsicament conté un valor enter
 */
public class SymbolTipoVar extends SymbolBase {
    
    public final int tipo;

    public SymbolTipoVar(Integer tipo, int l, int c) {
        super("tipo", tipo, l, c);
        this.tipo = tipo;
    }
}
