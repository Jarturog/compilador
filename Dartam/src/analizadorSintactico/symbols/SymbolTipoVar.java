
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * Bàsicament conté un valor enter
 */
public class SymbolTipoVar extends ComplexSymbol {
    private static int id = 0;
    private int tipo;
    private int tipoBase;
    
    public SymbolTipoVar(int tipo) {
        super("type", id++, 0);
        this.tipo = tipo;
    }
    
    public SymbolTipoVar(int tipo, int tipoBase){
        super("type", id++, 0);
        this.tipo = tipo;
        this.tipoBase = tipoBase;
    }

    
    public boolean isTipo(int tipo){
           return this.tipo == tipo;
    }

    public int getTipo() {
        return tipo;
    }
    
    
}
