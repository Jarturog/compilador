
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
 * OPERAND ::= ATOMIC_EXPRESSION:et        {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | FCALL:et                      {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | LPAREN OPERAND:et RPAREN      {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | UNARY_EXPRESSION:et           {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | BINARY_EXPRESSION:et          {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | CONDITIONAL_EXPRESSION:et     {: RESULT = new SymbolOperand(et, etxleft, etxright); :}
        | OPERAND:et1 AUX_MEMBER LBRACKET OPERAND:et2 RBRACKET  {: RESULT = new SymbolOperand(et1, et2, et1xleft, et1xright); :}
        | OPERAND:et1 OP_MEMBER ID:et2                     {: RESULT = new SymbolOperand(et1, et2, et1xleft, et1xright); :}
        ;
 */
public class SymbolOperand extends SymbolBase {
    private SymbolAtomicExpression aex;
    private SymbolFCall fcall;
    private SymbolOperand op1;
    private SymbolOperand op2;
    private SymbolUnaryExpression uex;
    private SymbolBinaryExpression bex;
    private SymbolConditionalExpression cex;
    private String id;

    public SymbolOperand(String variable, Double valor) {
        super(variable);
    }

    public SymbolOperand(SymbolBinaryExpression et, Location l , Location r) {
        super("operand", 0 ,l , r);
        this.bex = et;
    }

    public SymbolOperand(SymbolOperand arr, SymbolOperand idx, Location l, Location r) {
        super("operand", 0 ,l ,r );
        this.op1 = arr;
        this.op2 = idx;
  
    }

    public SymbolOperand(SymbolOperand tuple, String member, Location l, Location r) {
        super("operand", 0 ,l ,r );
        this.op1 = tuple;
        this.id= member;
    }

    public SymbolOperand(SymbolUnaryExpression et, Location l, Location r) {
        super("operand", 0 ,l , r);
        this.uex = et;
    }
    

    public SymbolOperand(SymbolOperand et, Location l, Location r) {
        super("operand", 0 , l , r);
        this.op1 = et;
    }

    public SymbolOperand(SymbolFCall et, Location l, Location r) {
        super("operand", 0 , l , r);
        this.fcall = et;
    }

    public SymbolOperand(SymbolAtomicExpression et, Location l, Location r) {
        super("operand", 0, l , r);
        this.aex = et;
    }

    public SymbolOperand(SymbolConditionalExpression et, Location l , Location r) {
        super("operand", 0, l ,r);
        this.cex = et;
    }

    public SymbolAtomicExpression getAex() {
        return aex;
    }

    public SymbolFCall getFcall() {
        return fcall;
    }

    public SymbolOperand getOp1() {
        return op1;
    }

    public SymbolOperand getOp2() {
        return op2;
    }

    public SymbolUnaryExpression getUex() {
        return uex;
    }

    public SymbolBinaryExpression getBex() {
        return bex;
    }

    public SymbolConditionalExpression getCex() {
        return cex;
    }

    public String getId() {
        return id;
    }
    
    
}
