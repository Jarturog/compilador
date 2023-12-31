
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
 * REGLAS: 
 * DIMENSIONES ::= LBRACKET OPERAND:et1 RBRACKET DIMENSIONES:et2   {: RESULT = new SymbolDimensiones(et1,et2, et1left, et1right); :}
        | LBRACKET OPERAND:et1 RBRACKET                         {: RESULT = new SymbolDimensiones(et1, et1left, et1right); :}
        ;
 *
 */
public class SymbolDimensiones extends SymbolBase {
    
    private SymbolOperand operando;
    private SymbolDimensiones dimensiones;
            
    public SymbolDimensiones(SymbolOperand operando, Location l, Location r){
        super("dimensiones", l, r);
        this.operando = operando;
    }
    
    public SymbolDimensiones(SymbolOperand operando, SymbolDimensiones dimensiones, Location l, Location r) {
        super("dimensiones", l, r);
        this.operando = operando;
        this.dimensiones = dimensiones;
    }

    public SymbolOperand getOperando() {
        return operando;
    }

    public SymbolDimensiones getDimensiones() {
        return dimensiones;
    }
    
    
    
}
