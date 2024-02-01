/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica 
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import analizadorSintactico.ParserSym;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
ASIG ::= ID:et ASIG_OP:aop OPERAND:val                                  {: RESULT = new SymbolAsig(et, aop, val, etxleft, etxright); :}
        | ID:et1 LBRACKET OPERAND:et2 RBRACKET ASIG_OP:aop OPERAND:val  {: RESULT = new SymbolAsig(et1, et2, aop, val, et1xleft, et1xright); :}
        | ID:et1 OP_MEMBER ID:et2 ASIG_OP:aop OPERAND:val               {: RESULT = new SymbolAsig(et1, et2, aop, val, et1xleft, et1xright); :}
        | ID:et1 OP_INC:et2             {: RESULT = new SymbolAsig(true, ParserSym.OP_INC, et1, et2, et1xleft, et1xright); :}  %prec PREC_R_U_EXP
        | ID:et1 OP_DEC:et2             {: RESULT = new SymbolAsig(true, ParserSym.OP_DEC, et1, et2, et1xleft, et1xright); :}  %prec PREC_R_U_EXP
        | OP_INC:et1 ID:et2             {: RESULT = new SymbolAsig(false, ParserSym.OP_INC, et2, et1, et1xleft, et1xright); :} %prec PREC_L_U_EXP
        | OP_DEC:et1 ID:et2             {: RESULT = new SymbolAsig(false, ParserSym.OP_DEC, et2, et1, et1xleft, et1xright); :} %prec PREC_L_U_EXP
        ;
 */
public class SymbolAsig extends SymbolBase {
    
    public final String id;
    public final SymbolAsigOp operacion;
    public final SymbolOperand valor;
    // array
    public final SymbolDimensiones dim;
    // tupla
    public final String miembro;
    private final Boolean isPost, isIncrement;
    
    // asig normal
    public SymbolAsig(String id, SymbolAsigOp op, SymbolOperand valor, Location l, Location r) {
        super("asig", l, r);
        this.id = id;
        this.valor = valor;
        this.dim = null;
        this.miembro = null;
        operacion = op;
        this.isPost = null;
        this.isIncrement = null;
    }

    // elemento array
    public SymbolAsig(String id, SymbolDimensiones dimensiones, SymbolAsigOp op, SymbolOperand valor, Location l, Location r) {
        super("asig", l, r);
        this.id = id;
        this.valor = valor;
        this.dim = dimensiones;
        this.miembro = null;
        operacion = op;
        this.isPost = null;
        this.isIncrement = null;
    }

    // miembro
    public SymbolAsig(String id, String miembro, SymbolAsigOp op, SymbolOperand valor, Location l, Location r) {
        super("asig", l, r);
        this.id = id;
        this.valor = valor;
        this.dim = null;
        this.miembro = miembro;
        operacion = op;
        this.isPost = null;
        this.isIncrement = null;
    }
    
    // post/pre-incremento/decremento
    public SymbolAsig(boolean postOperation, int numOperacion, String id, Object v, Location l, Location r) {
        super("asig", l, r);
        this.id = id;
        this.dim = null;
        this.miembro = null;
        this.isIncrement = numOperacion == ParserSym.OP_INC;
        operacion = new SymbolAsigOp(isIncrement ? ParserSym.AS_ADDA : ParserSym.AS_SUBA, v, l, r);
        valor = new SymbolOperand(new SymbolAtomicExpression((Integer)1, l, r), l, r);
        this.isPost = postOperation;
    }
    
    public static enum TIPO {
        ID, ELEM_ARRAY, MIEMBRO
    }

    public TIPO getTipo() {
        if (dim == null && miembro == null) {
            return TIPO.ID;
        } else if (dim != null) {
            return TIPO.ELEM_ARRAY;
        } else {
            return TIPO.MIEMBRO;
        }
    }
    
    public Boolean isPost(){
        return isPost;
    }
    
    public Boolean isIncrement(){
        return isIncrement;
    }

}
