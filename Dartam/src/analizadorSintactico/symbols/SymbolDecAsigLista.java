
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;



/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * Reglas
 * 
 * ID_DECS_LISTA ::= ID:et1 ASIG:et2 COMMA ID_DECS_LISTA:et3       {: RESULT = new SymbolIDDecsLista(et1,et2,et3, et1left, et1right); :}
        | ID:et1 ASIG:et2                                   {: RESULT = new SymbolIDDecsLista(et1,et2, et1left, et1right); :}
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
    }
    
    public SymbolDecAsigLista(String identificador, SymbolAsigBasico asignacion, SymbolDecAsigLista iddecslista, Location l, Location r){
        super("iddecslista", l, r);
        id = identificador;
        this.asignacion = asignacion;
        siguienteDeclaracion = iddecslista;
    }
    
}
