
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
 * 
ID_ASIG_LISTA ::= ID:et1 ASIG_OP OPERAND:et2 COMMA ID_ASIG_LISTA:et3    {: RESULT = new SymbolIDAsigLista(et1, et2, et3, et1xleft, et1xright); :}
        | ID:et1 ASIG_OP OPERAND:et2                                    {: RESULT = new SymbolIDAsigLista(et1, et2, et1xleft, et1xright); :}
        ;
 */
public class SymbolIDAsigLista extends SymbolBase {
    
    private String id;
    private SymbolOperand op;
    private SymbolIDAsigLista idAL;

    public SymbolIDAsigLista(String id, SymbolOperand op, SymbolIDAsigLista al, Location l, Location r) {
        super("idAsigLista", 0 , l ,r);
        this.id=  id;
        this.op = op;
        this.idAL = al;
    }
    
    public SymbolIDAsigLista(String id, SymbolOperand op, Location l , Location r){
        super("idAsigLista", 0, l, r);
        this.id= id;
        this.op = op;
    }
}
