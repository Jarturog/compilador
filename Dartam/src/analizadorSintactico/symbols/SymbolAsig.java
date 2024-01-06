
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import analizadorSintactico.ParserSym;
import java_cup.runtime.ComplexSymbolFactory.Location;



/**
ASIG ::= ID:et ASIG_OP:aop OPERAND:val                                                  {: RESULT = new SymbolAsig(et, aop, val); :}
        | ID:et1 AUX_MEMBER LBRACKET OPERAND:et2 RBRACKET ASIG_OP:aop OPERAND:val  {: RESULT = new SymbolAsig(et1, et2, aop, val); :}
        | ID:et1 OP_MEMBER ID:et2 ASIG_OP:aop OPERAND:val                          {: RESULT = new SymbolAsig(et1, et2, aop, val); :}
        ;
 */
public class SymbolAsig extends SymbolBase {
    
    public final String id;
    public final SymbolAsigOp operacion;
    public final SymbolOperand valor;
    // array
    public final SymbolOperand idx;
    // tupla
    public final String miembro;
    private final Boolean isPost, isIncrement;
    
    public SymbolAsig(String id, SymbolAsigOp op, SymbolOperand valor, Location l, Location r) {
        super("asig", l, r);
        this.id = id;
        this.valor = valor;
        this.idx = null;
        this.miembro = null;
        operacion = op;
        this.isPost = null;
        this.isIncrement = null;
    }

    public SymbolAsig(String id, SymbolOperand dimensiones, SymbolAsigOp op, SymbolOperand valor, Location l, Location r) {
        super("asig", l, r);
        this.id = id;
        this.valor = valor;
        this.idx = dimensiones;
        this.miembro = null;
        operacion = op;
        this.isPost = null;
        this.isIncrement = null;
    }

    public SymbolAsig(String id, String miembro, SymbolAsigOp op, SymbolOperand valor, Location l, Location r) {
        super("asig", l, r);
        this.id = id;
        this.valor = valor;
        this.idx = null;
        this.miembro = miembro;
        operacion = op;
        this.isPost = null;
        this.isIncrement = null;
    }
    
    // post/pre-incremento/decremento
    public SymbolAsig(boolean postOperation, int numOperacion, String id, Object v, Location l, Location r) {
        super("asig", l, r);
        this.id = id;
        this.idx = null;
        this.miembro = null;
        this.isIncrement = numOperacion == ParserSym.OP_INC;
        operacion = new SymbolAsigOp(isIncrement ? ParserSym.AS_ADDA : ParserSym.AS_SUBA, v, l, r);
        valor = new SymbolOperand(new SymbolAtomicExpression((Integer)1, l, r), l, r);
        this.isPost = postOperation;
        
    }
    
    public static enum TIPO {
        PRIMITIVA, ARRAY, TUPLA
    }

    /**
     * 
     */
    public TIPO getTipo() {
        if (idx == null && miembro == null) {
            return TIPO.PRIMITIVA;
        } else if (idx != null) {
            return TIPO.ARRAY;
        } else {
            return TIPO.TUPLA;
        }
    }
    
    public Boolean isPost(){
        return isPost;
    }
    
    public Boolean isIncrement(){
        return isIncrement;
    }

}
