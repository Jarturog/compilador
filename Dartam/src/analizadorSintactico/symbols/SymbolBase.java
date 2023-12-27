/**
 * Assignatura 21742 - Compiladors I 
 * Estudis: Grau en Informàtica 
 * Itinerari: Computació 
 * Curs: 2018-2019
 *
 * Professor: Pere Palmer
 */
package analizadorSintactico.symbols;


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
    
    public SymbolBase(String variable, Integer valor) {
        super(variable, idAutoIncrement++, valor);
    }
 }
