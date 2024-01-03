

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
DECS ::= KW_CONST:et1 TIPO:et2 DEC_ASIG_LISTA:et3 ENDINSTR     {: RESULT = new SymbolDecs(true,et2,et3, et1xleft, et1xright); :}
        | TIPO:et1 DEC_ASIG_LISTA:et2 ENDINSTR                 {: RESULT = new SymbolDecs(false, et1,et2, et1xleft, et1xright); :}
        ; 
 */
public class SymbolDecs extends SymbolBase {
    
    // compartido por primitivas, arrays y tuplas
    public final SymbolDecAsigLista iddecslista;
    public final SymbolTipo tipo;
    // primitiva
    public final boolean isConst;
    
    // primitiva
    public SymbolDecs(boolean constante, SymbolTipo tipo, SymbolDecAsigLista iddecslista, Location l, Location r) {
        super("decPrim", l, r);
        isConst = constante;
        this.tipo = tipo;
        this.iddecslista = iddecslista;
    }

}
