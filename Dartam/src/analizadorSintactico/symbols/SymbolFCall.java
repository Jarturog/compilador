
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
 * Reglas:
 * 
 * 
FCALL ::= METODO_NOMBRE:et1 LPAREN OPERANDS_LISTA:et2 RPAREN   {: RESULT = new SymbolFCall(et1, et2, et1xleft, et1xright); :}
        | METODO_NOMBRE:et1 LPAREN RPAREN   {: RESULT = new SymbolFCall(et1, et1xleft, et1xright); :}
        ;
        
 */
public class SymbolFCall extends SymbolBase {
   
    public final SymbolMetodoNombre methodName;
    public final SymbolOperandsLista operandsLista;
    
    public SymbolFCall(SymbolMetodoNombre methodName, Location l, Location r) {
        super("fcall", l ,r);
        this.methodName = methodName;
        this.operandsLista = null;
        value = toString();
    }

    public SymbolFCall(SymbolMetodoNombre et1, SymbolOperandsLista et2, Location l, Location r) {
        super("fcall", l ,r );
        this.methodName = et1;
        this.operandsLista = et2;
        value = toString();
    }

    @Override
    public final String toString() {
        String nombre = ((String)methodName.value) + "(";
        if (operandsLista == null) {
            return nombre + ")";
        }
        String ops = "";
        SymbolOperandsLista op = operandsLista;
        while (op != null) {
            ops += op.operand.toString() + ", ";
            op = op.siguienteOperando;
        }
        return ops.substring(0, ops.length() - 2) + ")";
    }

}
