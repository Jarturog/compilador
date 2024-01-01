/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en InformÃ tica 
 * Itinerari: IntelÂ·ligÃ¨ncia Artificial i ComputaciÃ³
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;

import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramÃ tica.
 * 
 * BÃ sicament contÃ© un valor enter
 */
public abstract class SymbolBase extends ComplexSymbol {
    private static int id = 0;
    public final Location leftPos;
    public final Location rightPos;
    
    public SymbolBase(String name){
        super(name, id++);
        leftPos = null;
        rightPos = null;
    }
    
    public SymbolBase(String name, Object value){
        super(name, id++, value);
        leftPos = null;
        rightPos = null;
    }
    
    public SymbolBase(String name, Object value, Location left, Location right) {
        super(name, id++, value);
        leftPos = left;
        rightPos = right;
    }
    
    public SymbolBase(String name, Location left, Location right) {
        super(name, id++);
        leftPos = left;
        rightPos = right;
    }
}
