
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
 * Reglas:
 * 
 * 
FCALL ::= METODO_NOMBRE:et1 LPAREN OPERANDS_LISTA:et2 RPAREN   {: RESULT = new SymbolFCall(et1, et2, et1xleft, et1xright); :}
        | METODO_NOMBRE:et1 LPAREN RPAREN   {: RESULT = new SymbolFCall(et1, et1xleft, et1xright); :}
        ;
        ;
 */
public class SymbolFCall extends SymbolBase {
   
    private SymbolMetodoNombre methodName;
    private SymbolOperandsLista operandsLista;
    
    public SymbolFCall(SymbolMetodoNombre methodName, Location l, Location r) {
        super("fcall", 0 ,l ,r);
        this.methodName = methodName;
    }

    public SymbolFCall(SymbolMetodoNombre et1, SymbolOperandsLista et2, Location l, Location r) {
        super("fcall", 0, l ,r );
        this.methodName = et1;
        this.operandsLista = et2;
    }

    public SymbolMetodoNombre getMethodName() {
        return methodName;
    }

    public SymbolOperandsLista getOperandsLista() {
        return operandsLista;
    }
    
    
    
}
