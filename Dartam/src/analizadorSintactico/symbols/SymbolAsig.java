
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
    public final SymbolOperand dimensiones;
    // tupla
    public final String miembro;

    public SymbolAsig(String id, SymbolAsigOp op, SymbolOperand valor, Location l, Location r) {
        super("asig", l, r);
        this.id = id;
        this.valor = valor;
        this.dimensiones = null;
        this.miembro = null;
        operacion = op;
    }

    public SymbolAsig(String id, SymbolOperand dimensiones, SymbolAsigOp op, SymbolOperand valor, Location l, Location r) {
        super("asig", l, r);
        this.id = id;
        this.valor = valor;
        this.dimensiones = dimensiones;
        this.miembro = null;
        operacion = op;
    }

    public SymbolAsig(String id, String miembro, SymbolAsigOp op, SymbolOperand valor, Location l, Location r) {
        super("asig", l, r);
        this.id = id;
        this.valor = valor;
        this.dimensiones = null;
        this.miembro = miembro;
        operacion = op;
    }
    
    public static enum TIPO {
        PRIMITIVA, ARRAY, TUPLA
    }

    /**
     * fcall, decs, asigs, swap, return
     * @return f, d, a, s or r
     */
    public TIPO getTipo() {
        if (dimensiones == null && miembro == null) {
            return TIPO.PRIMITIVA;
        } else if (dimensiones != null) {
            return TIPO.ARRAY;
        } else {
            return TIPO.TUPLA;
        }
    }

}
