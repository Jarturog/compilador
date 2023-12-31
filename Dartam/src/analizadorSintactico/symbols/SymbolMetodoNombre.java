
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
 * METHOD_NAME ::= ID                   {: RESULT = new SymbolMethodName(ParserSym.ID); :}
        | KW_IN:et                      {: RESULT = new SymbolMethodName(ParserSym.KW_IN); :}
        | KW_OUT:et                     {: RESULT = new SymbolMethodName(ParserSym.KW_OUT); :}
        | KW_WRITE:et                   {: RESULT = new SymbolMethodName(ParserSym.KW_WRITE); :}
        | KW_READ:et                    {: RESULT = new SymbolMethodName(ParserSym.KW_READ); :}
        ;
 * 
 */
public class SymbolMetodoNombre extends SymbolBase {
    
    private int methodName;

    public SymbolMetodoNombre(int methodName, Location l , Location r) {
        super("methodName", 0, l,r);
        this.methodName = methodName;
    }

    public int getMethodName() {
        return methodName;
    }
    
    
    
}
