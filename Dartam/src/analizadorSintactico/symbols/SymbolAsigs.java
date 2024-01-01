
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informatica 
 * Itinerari: Inteligencia Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;



/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramatica.
 * 
 * ASIGS ::= ID_ASIG_LISTA:et ENDINSTR         {: RESULT = new SymbolAsigs(et, etxleft, etxright); :}
        ;
 */
public class SymbolAsigs extends SymbolBase {
    public final SymbolIDAsigLista idAL;

    public SymbolAsigs(SymbolIDAsigLista idAL, Location l, Location r) {
        super("asigs", 0, l , r);
        this.idAL = idAL;
    }

    public SymbolIDAsigLista getIdAL() {
        return idAL;
    }
    
    
    
}
