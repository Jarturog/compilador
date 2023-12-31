/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en InformÃ tica 
 * Itinerari: IntelÂ·ligÃ¨ncia Artificial i ComputaciÃ³
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramÃ tica.
 * 
 * BÃ sicament contÃ© un valor enter
 */
public abstract class SymbolBase extends ComplexSymbol {
    private static int id = 0;
    private int linea;
    private int columna;
    
    public SymbolBase(String name){
        super(name, id++);
    }
    
    public SymbolBase(String name, Object value, int linea, int columna) {
        super(name, id++, value);
        this.linea = linea;
        this.columna = columna;
    }
    
    public SymbolBase(String name, int linea, int columna) {
        super(name, id++);
        this.linea = linea;
        this.columna = columna;
    }
    
    public int getLinea() {
        return linea;
    }
    
    public int getColumna() {
        return columna;
    }
    
}
