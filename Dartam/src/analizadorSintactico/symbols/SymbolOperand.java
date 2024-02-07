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
    public final SymbolOperand op;
    public final SymbolDimensiones dim;
    public final SymbolUnaryExpression unaryExp;
    public final SymbolBinaryExpression binaryExp;
    public final SymbolConditionalExpression conditionalExp;
    public final String member;
    public final SymbolTipoCasting casting;

    // atomic expression
    public SymbolOperand(SymbolAtomicExpression et, Location l, Location r) {
        super("operand", et.value, l , r);
        this.atomicExp = et;
        this.fcall = null;
        this.op = null;
        this.dim = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        this.casting = null;
    }
    
    // fcall
    public SymbolOperand(SymbolFCall et, Location l, Location r) {
        super("operand", et.value, l , r);
        this.fcall = et;
        this.atomicExp = null;
        this.op = null;
        this.dim = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        this.casting = null;
    }
    
    // parentesis
    public SymbolOperand(SymbolOperand et, Location l, Location r) {
        super("operand", "("+et.value+")", l , r);
        this.op = et;
        this.atomicExp = null;
        this.fcall = null;
        this.dim = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        this.casting = null;
    }
    
    // unary expression
    public SymbolOperand(SymbolUnaryExpression et, Location l, Location r) {
        super("operand", et.value,l , r);
        this.unaryExp = et;
        this.atomicExp = null;
        this.fcall = null;
        this.op = null;
        this.dim = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        this.casting = null;
    }

    // binary expression
    public SymbolOperand(SymbolBinaryExpression et, Location l , Location r) {
        super("operand", et.value,l , r);
        this.binaryExp = et;
        this.atomicExp = null;
        this.fcall = null;
        this.op = null;
        this.dim = null;
        this.unaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        this.casting = null;
    }
    
    // conditional expression
    public SymbolOperand(SymbolConditionalExpression et, Location l , Location r) {
        super("operand", et.value, l ,r);
        this.conditionalExp = et;
        this.atomicExp = null;
        this.fcall = null;
        this.op = null;
        this.dim = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.member = null;
        this.casting = null;
    }

    // array operation
    public SymbolOperand(SymbolOperand arr, SymbolDimensiones dim, Location l, Location r) {
        super("operand",l ,r );
        this.op = arr;
        this.dim = dim;
        this.atomicExp = null;
        this.fcall = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.member = null;
        value = arrayToString();
        this.casting = null;
    }

    // tupla operation
    public SymbolOperand(SymbolOperand tuple, String member, Location l, Location r) {
        super("operand", l, r );
        this.op = tuple;
        this.member = member;
        this.atomicExp = null;
        this.fcall = null;
        this.dim = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        value = tuplaToString();
        this.casting = null;
    }

    public SymbolOperand(SymbolTipoPrimitivo t, SymbolOperand op, Object lParen, Object rParen, Location l, Location r) {
        super("operand", (String)lParen + (t != null ? t.value : (ParserSym.terminalNames[ParserSym.CAR].toLowerCase() + " []")) + rParen + op.value, l, r );
        this.op = op;
        this.member = null;
        this.atomicExp = null;
        this.fcall = null;
        this.dim = null;
        this.unaryExp = null;
        this.binaryExp = null;
        this.conditionalExp = null;
        this.casting = t != null ? t : new SymbolStringCasting(ParserSym.terminalNames[ParserSym.CAR] + " []");
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
        } else if (casting != null) {
            return TIPO.CASTING;
        } else if (dim != null) {
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
            return TIPO.OP_BETWEEN_PAREN;
        }
    }

    @Override
    public String toString(){
        return value.toString();
    }
    
    private String arrayToString(){
        return op.value+"["+dim.value+"]";
    }
    
    private String tuplaToString(){
        return op.value+"."+member;
    }
}
