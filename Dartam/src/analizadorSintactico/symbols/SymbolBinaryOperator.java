/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica
 * Itinerari: Inteligencia Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import analizadorSintactico.ParserSym;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
BINARY_OPERATOR ::= OP_ADD:et   {: RESULT = new SymbolBinaryOperator(ParserSym.OP_ADD, et, etxleft, etxright); :}
        | OP_SUB:et             {: RESULT = new SymbolBinaryOperator(ParserSym.OP_SUB, et, etxleft, etxright); :}
        | OP_MUL:et             {: RESULT = new SymbolBinaryOperator(ParserSym.OP_MUL, et, etxleft, etxright); :}
        | OP_DIV:et             {: RESULT = new SymbolBinaryOperator(ParserSym.OP_DIV, et, etxleft, etxright); :}
        | OP_MOD:et             {: RESULT = new SymbolBinaryOperator(ParserSym.OP_MOD, et, etxleft, etxright); :}
        | OP_POT:et             {: RESULT = new SymbolBinaryOperator(ParserSym.OP_POT, et, etxleft, etxright); :}
        | OP_EQ:et              {: RESULT = new SymbolBinaryOperator(ParserSym.OP_EQ, et, etxleft, etxright); :}
        | OP_BEQ:et             {: RESULT = new SymbolBinaryOperator(ParserSym.OP_BEQ, et, etxleft, etxright); :}
        | OP_BT:et              {: RESULT = new SymbolBinaryOperator(ParserSym.OP_BT, et, etxleft, etxright); :}
        | OP_LEQ:et             {: RESULT = new SymbolBinaryOperator(ParserSym.OP_LEQ, et, etxleft, etxright); :}
        | OP_LT:et              {: RESULT = new SymbolBinaryOperator(ParserSym.OP_LT, et, etxleft, etxright); :}
        | OP_NEQ:et             {: RESULT = new SymbolBinaryOperator(ParserSym.OP_NEQ, et, etxleft, etxright); :}
        | OP_AND:et             {: RESULT = new SymbolBinaryOperator(ParserSym.OP_AND, et, etxleft, etxright); :}
        | OP_OR:et              {: RESULT = new SymbolBinaryOperator(ParserSym.OP_OR, et, etxleft, etxright); :}
        ;
 */
public class SymbolBinaryOperator extends SymbolBase {

    private final int operador;

    public SymbolBinaryOperator(int op, Object et, Location l, Location r) {
        super("binaryOperator", et, l, r);
        this.operador = op;
    }

    public boolean doesOperationResultInBoolean() {
        return operador == ParserSym.OP_EQ ||
            operador == ParserSym.OP_BEQ ||
            operador == ParserSym.OP_BT ||
            operador == ParserSym.OP_LEQ ||
            operador == ParserSym.OP_LT ||
            operador == ParserSym.OP_NEQ ||
            operador == ParserSym.OP_AND ||
            operador == ParserSym.OP_OR;
    }

    public boolean isForOperandsOfType(String type) {
        if (type.equals(ParserSym.terminalNames[ParserSym.PROP])) {
            return operador == ParserSym.OP_OR
                    || operador == ParserSym.OP_AND
                    || operador == ParserSym.OP_EQ
                    || operador == ParserSym.OP_NEQ;
        } else if (type.equals(ParserSym.terminalNames[ParserSym.ENT])) {
            return operador == ParserSym.OP_ADD
                    || operador == ParserSym.OP_SUB
                    || operador == ParserSym.OP_MUL
                    || operador == ParserSym.OP_DIV
                    || operador == ParserSym.OP_MOD
                    || operador == ParserSym.OP_POT
                    || operador == ParserSym.OP_EQ
                    || operador == ParserSym.OP_BEQ
                    || operador == ParserSym.OP_BT
                    || operador == ParserSym.OP_LEQ
                    || operador == ParserSym.OP_LT
                    || operador == ParserSym.OP_NEQ;
        } else if (type.equals(ParserSym.terminalNames[ParserSym.REAL])) {
            return operador == ParserSym.OP_ADD
                    || operador == ParserSym.OP_SUB
                    || operador == ParserSym.OP_MUL
                    || operador == ParserSym.OP_DIV
                    || operador == ParserSym.OP_EQ
                    || operador == ParserSym.OP_BEQ
                    || operador == ParserSym.OP_BT
                    || operador == ParserSym.OP_LEQ
                    || operador == ParserSym.OP_LT
                    || operador == ParserSym.OP_NEQ;
        } else if (type.equals(ParserSym.terminalNames[ParserSym.CAR])) {
            return operador == ParserSym.OP_ADD
                    || operador == ParserSym.OP_SUB
                    || operador == ParserSym.OP_MUL
                    || operador == ParserSym.OP_DIV
                    || operador == ParserSym.OP_MOD
                    || operador == ParserSym.OP_POT
                    || operador == ParserSym.OP_EQ
                    || operador == ParserSym.OP_BEQ
                    || operador == ParserSym.OP_BT
                    || operador == ParserSym.OP_LEQ
                    || operador == ParserSym.OP_LT
                    || operador == ParserSym.OP_NEQ;
        } else if (type.equals(ParserSym.terminalNames[ParserSym.STRING])) {
            return operador == ParserSym.OP_ADD
                    || operador == ParserSym.OP_EQ
                    || operador == ParserSym.OP_NEQ;
        }
        return false;
    }
    
    public String nombreOperador(){
        return ParserSym.terminalNames[this.operador];
    }

}
