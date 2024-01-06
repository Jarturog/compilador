
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
OPERAND ::= ATOMIC_EXPRESSION:et        {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | FCALL:et                      {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | LPAREN OPERAND:et RPAREN      {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | UNARY_EXPRESSION:et           {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | BINARY_EXPRESSION:et          {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | CONDITIONAL_EXPRESSION:et     {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | OPERAND:et1 AUX_MEMBER LBRACKET OPERAND:et2 RBRACKET  {: RESULT = new SymbolOperand(et1, et2, et1xleft, et1xright); :}
        | OPERAND:et1 OP_MEMBER ID:et2                          {: RESULT = new SymbolOperand(et1, et2, et1xleft, et1xright); :}
        ;
 */
public class SymbolOperand extends SymbolBase {
    public final SymbolAtomicExpression atomicExp;
    public final SymbolFCall fcall;
    public final SymbolOperand op, idxArr;
    public final SymbolUnaryExpression unaryExp;
    public final SymbolBinaryExpression binaryExp;
    public final SymbolConditionalExpression conditionalExp;
    public final String member;
    public final SymbolTipoPrimitivo casting;
    private final String lBracket, rBracket;

    // atomic expression
    public SymbolOperand(SymbolAtomicExpression et, Location l, Location r) {
        super("operand", et.value, l , r);
        this.atomicExp = et;
        this.fcall = null;
        this.op = null;
        this.idxArr = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        this.lBracket = null;
        this.rBracket = null;
        this.casting = null;
    }
    
    // fcall
    public SymbolOperand(SymbolFCall et, Location l, Location r) {
        super("operand", et.value, l , r);
        this.fcall = et;
        this.atomicExp = null;
        this.op = null;
        this.idxArr = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        this.lBracket = null;
        this.rBracket = null;
        this.casting = null;
    }
    
    // parentesis
    public SymbolOperand(SymbolOperand et, Location l, Location r) {
        super("operand", "("+et.value+")", l , r);
        this.op = et;
        this.atomicExp = null;
        this.fcall = null;
        this.idxArr = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        this.lBracket = null;
        this.rBracket = null;
        this.casting = null;
    }
    
    // unary expression
    public SymbolOperand(SymbolUnaryExpression et, Location l, Location r) {
        super("operand", et.value,l , r);
        this.unaryExp = et;
        this.atomicExp = null;
        this.fcall = null;
        this.op = null;
        this.idxArr = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        this.lBracket = null;
        this.rBracket = null;
        this.casting = null;
    }

    // binary expression
    public SymbolOperand(SymbolBinaryExpression et, Location l , Location r) {
        super("operand", et.value,l , r);
        this.binaryExp = et;
        this.atomicExp = null;
        this.fcall = null;
        this.op = null;
        this.idxArr = null;
        this.unaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        this.lBracket = null;
        this.rBracket = null;
        this.casting = null;
    }
    
    // conditional expression
    public SymbolOperand(SymbolConditionalExpression et, Location l , Location r) {
        super("operand", et.value, l ,r);
        this.conditionalExp = et;
        this.atomicExp = null;
        this.fcall = null;
        this.op = null;
        this.idxArr = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.member = null;
        this.lBracket = null;
        this.rBracket = null;
        this.casting = null;
    }

    // array operation
    public SymbolOperand(SymbolOperand arr, Object lb, SymbolOperand idx, Object lr, Location l, Location r) {
        super("operand",l ,r );
        this.op = arr;
        this.idxArr = idx;
        this.atomicExp = null;
        this.fcall = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        value = arrayToString();
        this.lBracket = (String)lb;
        this.rBracket = (String)lr;
        this.casting = null;
    }

    // tupla operation
    public SymbolOperand(SymbolOperand tuple, String member, Location l, Location r) {
        super("operand", l, r );
        this.op = tuple;
        this.member = member;
        this.atomicExp = null;
        this.fcall = null;
        this.idxArr = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        value = tuplaToString();
        this.lBracket = null;
        this.rBracket = null;
        this.casting = null;
    }

    public SymbolOperand(SymbolTipoPrimitivo t, SymbolOperand op, Object lParen, Object rParen, Location l, Location r) {
        super("operand", (String)lParen + t.value + rParen + op.value, l, r );
        this.op = op;
        this.member = null;
        this.atomicExp = null;
        this.fcall = null;
        this.idxArr = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.lBracket = null;
        this.rBracket = null;
        this.casting = t;
    }
    
    public static enum TIPO {
        ATOMIC_EXPRESSION,
        FCALL,
        OP_BETWEEN_PAREN,
        UNARY_EXPRESSION,
        BINARY_EXPRESSION,
        CONDITIONAL_EXPRESSION,
        IDX_ARRAY,
        MEMBER_ACCESS,
        CASTING
    }
    
    public TIPO getTipo() {
        if (atomicExp != null) {
            return TIPO.ATOMIC_EXPRESSION;
        } else if (fcall != null) {
            return TIPO.FCALL;
        } else if (op != null) {
            return TIPO.OP_BETWEEN_PAREN;
        } else if (idxArr != null) {
            return TIPO.IDX_ARRAY;
        } else if (unaryExp != null) {
            return TIPO.UNARY_EXPRESSION;
        } else if (binaryExp != null) {
            return TIPO.BINARY_EXPRESSION;
        } else if (conditionalExp != null) {
            return TIPO.CONDITIONAL_EXPRESSION;
        } else if (member != null) {
            return TIPO.MEMBER_ACCESS;
        } else {
            return TIPO.CASTING;
        }
    }

    @Override
    public String toString(){
        return (String) value;
    }
    
    private String arrayToString(){
        return "("+op.value+lBracket+idxArr.value+rBracket+")";
    }
    
    private String tuplaToString(){
        return "("+op.value+"."+member+")";
    }
}
