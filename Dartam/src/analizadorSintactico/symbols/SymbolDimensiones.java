
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
DIMENSIONES ::= LBRACKET OPERAND:et1 RBRACKET DIMENSIONES:et2   {: RESULT = new SymbolDimensiones(et1,et2, et1xleft, et1xright); :}
        | LBRACKET OPERAND:et1 RBRACKET                         {: RESULT = new SymbolDimensiones(et1, et1xleft, et1xright); :}
        ;
        
        .
        .
        .
        
DIMENSIONES ::= [OPERAND] DIMENSIONES
        | [OPERAND]
        ;
 */
public class SymbolDimensiones extends SymbolBase {
    
    public final SymbolOperand operando;
    public final SymbolDimensiones siguienteDimension;
            
    public SymbolDimensiones(SymbolOperand operando, Location l, Location r){
        super("dimensiones", l, r);
        this.operando = operando;
        this.siguienteDimension = null;
    }
    
    public SymbolDimensiones(SymbolOperand operando, SymbolDimensiones dimensiones, Location l, Location r) {
        super("dimensiones", l, r);
        this.operando = operando;
        this.siguienteDimension = dimensiones;
    }
    
    @Override
    public String toString() {
        String sig = (siguienteDimension != null) ? siguienteDimension.toString() : "";
        return "[" + operando.value + "]" + sig;
    }

}
