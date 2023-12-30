
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
 * Reglas:
 * 
 * SCRIPTAM ::= MAIN:et 
 *              | SCRIPTAM:et1 SCRIPTBM:et2
 *              ;
 */
public class SymbolMetodoElemento extends ComplexSymbol {
    private static int id = 0;
    private SymbolMain main;
    private SymbolMetodoElemento mam;

    public SymbolMetodoElemento(SymbolMain sm) {
        super("sam", id++, 0);
        this.main = sm;
    }
    
    public SymbolMetodoElemento(SymbolMetodoElemento mam){
        super("sam", id++, 0);
        this.mam = mam;
    }

    public SymbolMetodoElemento(SymbolSwitch et) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public SymbolMetodoElemento(SymbolIf et) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public SymbolMetodoElemento(SymbolLoop et) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public SymbolMetodoElemento(SymbolInstr et) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
