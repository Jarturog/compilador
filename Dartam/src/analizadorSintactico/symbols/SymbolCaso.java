
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
CASO ::= CASO:et1 KW_CASE OPERAND:et2 ARROW BODY:et3 {: RESULT = new SymbolCaso(et1, et2, et3, et1xleft et1xright); :}
        |                                            {: RESULT = new SymbolCaso(); :}
        ;
 */
public class SymbolCaso extends SymbolBase {
    public final SymbolCaso caso;
    public final SymbolOperand op;
    public final SymbolBody b;

    public SymbolCaso() {
        super("caso");
        this.caso = null;
        this.op = null;
        this.b = null;
    }
    
    public SymbolCaso(SymbolCaso caso, SymbolOperand op, SymbolBody b, Location l , Location r){
        super("caso",0 , l ,r );
        this.caso = caso;
        this.op = op;
        this.b = b;
    }

    public SymbolCaso getCaso() {
        return caso;
    }

    public SymbolOperand getOp() {
        return op;
    }

    public SymbolBody getB() {
        return b;
    }
    
    
}
