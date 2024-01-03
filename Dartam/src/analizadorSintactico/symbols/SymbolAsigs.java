
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
 * ASIGS ::= ID:et1 ASIG_OP OPERAND:et2 COMMA ASIGS:et3    {: RESULT = new SymbolAsigs(et1, et2, et3, et1xleft, et1xright); :}
        | ID:et1 ASIG_OP OPERAND:et2 ENDINSTR                                    {: RESULT = new SymbolAsigs(et1, et2, et1xleft, et1xright); :}
        ;

 */
public class SymbolAsigs extends SymbolBase {
    
    public final String id;
    public final SymbolAsigOp operacion;
    public final SymbolOperand valor;
    public final SymbolAsigs siguienteAsig;

    public SymbolAsigs(String id, SymbolAsigOp o, SymbolOperand op, SymbolAsigs al, Location l, Location r) {
        super("idAsigLista", l ,r);
        this.id=  id;
        this.valor = op;
        this.siguienteAsig = al;
        operacion = o;
    }
    
    public SymbolAsigs(String id, SymbolAsigOp o, SymbolOperand op, Location l , Location r){
        super("idAsigLista", l, r);
        this.id= id;
        this.valor = op;
        this.siguienteAsig = null;
        operacion = o;
    }
    
    
    
}
