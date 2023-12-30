
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
 * Reglas: 
 * 
 * SCRIPTBM ::= METHOD:et
 *              | DECS:et
 *              ;
 */
public class SymbolMethodsBeforeMain extends ComplexSymbol {
    private static int id = 0;
    private SymbolMethod metodo;
    private SymbolDecs declaraciones;
    
    public SymbolMethodsBeforeMain(SymbolMethod metodo){
        super("mbm", id++, 0);
        this.metodo = metodo;
    }
    
    public SymbolMethodsBeforeMain(SymbolDecs declaraciones){
        super("mbm", id++, 0);
        this.declaraciones = declaraciones;
    }
    
    public SymbolMethodsBeforeMain(String variable, Double valor) {
        super(variable, id++, valor);
    }
    
}
