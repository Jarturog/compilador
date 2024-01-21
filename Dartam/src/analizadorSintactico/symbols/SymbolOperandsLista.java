/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica 
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 * 
 * OPERANDS_LISTA ::= OPERAND:et COMMA OPERANDS_LISTA:ol     {: RESULT = new SymbolOperandsLista(et, ol, etxleft, etxright); :}
        | OPERAND:et                                    {: RESULT = new SymbolOperandsLista(et, etxleft, etxright); :}
        ;
 */
public class SymbolOperandsLista extends SymbolBase {
    
    public final SymbolOperand operand;
    public final SymbolOperandsLista siguienteOperando;
    
    public SymbolOperandsLista(SymbolOperand op, SymbolOperandsLista opl, Location l, Location r) {
        super("operandsLista",0, l, r);
        this.operand = op;
        this.siguienteOperando = opl;
    }

    public SymbolOperandsLista(SymbolOperand et, Location l, Location r) {
        super("operandsLista", 0 , l , r);
        this.operand = et;
        this.siguienteOperando = null;
    }
    
}
