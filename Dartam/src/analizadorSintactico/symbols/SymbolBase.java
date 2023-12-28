/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Professor: Pere Palmer
 */
package analizadorSintactico.Symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les varaibles de la gramàtica.
 * 
 * Bàsicament conté un valor enter
 * 
 * @author Pere Palmer
 */
public class SymbolBase extends ComplexSymbol {
    private static int idAutoIncrement = 0;
    protected boolean empty;
    
    public SymbolBase(String variable, Double valor) {
        super(variable, idAutoIncrement++, valor);
        this.empty = false;
    }
    
    /**
     * Constructor per crear una instància buida, com a conseqüència d'un error
     * o una produció que deriva lambda.
     */
    public SymbolBase() {
        super("", idAutoIncrement++);
        empty = true;
    }
    
    /**
     * Mètode que permet determinar si la variable és buida (lambda) o bé perquè
     * hi ha un error semàntic.
     * @return true si és lambda/error false altrement
     */
    public boolean isEmpty() {
        return empty;
    }
    
 }
