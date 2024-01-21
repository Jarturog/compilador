
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;



/**
DEC_ASIG_LISTA ::= ID:et1 ASIG_BASICO:et2 COMMA DEC_ASIG_LISTA:et3       {: RESULT = new SymbolDecAsigLista(et1,et2,et3, et1xleft, et1xright); :}
        | ID:et1 ASIG_BASICO:et2                                   {: RESULT = new SymbolDecAsigLista(et1,et2, et1xleft, et1xright); :}
        ;
 */
public class SymbolDecAsigLista extends SymbolBase {
    
    public final String id;
    public final SymbolAsigBasico asignacion;
    public final SymbolDecAsigLista siguienteDeclaracion;
    
    public SymbolDecAsigLista(String identificador, SymbolAsigBasico asignacion, Location l, Location r){
        super("iddecslista", l, r);
        id = identificador;
        this.asignacion = asignacion;
        siguienteDeclaracion = null;
        if (asignacion != null)
            value = asignacion.value;
    }
    
    public SymbolDecAsigLista(String identificador, SymbolAsigBasico asignacion, SymbolDecAsigLista iddecslista, Location l, Location r){
        super("iddecslista", l, r);
        id = identificador;
        this.asignacion = asignacion;
        siguienteDeclaracion = iddecslista;
        if (asignacion != null)
            value = asignacion.value;
    }
    
}
