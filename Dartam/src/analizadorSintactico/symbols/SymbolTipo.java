
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
TIPO ::= TIPO_PRIMITIVO:t                       {: RESULT = new SymbolTipo(t, txleft, txright); :}
        | TIPO_PRIMITIVO:t DIMENSIONES:d        {: RESULT = new SymbolTipo(t, d, txleft, txright); :}
        | KW_TUPLE:t ID:i                       {: RESULT = new SymbolTipo(t, i, txleft, txright); :}
        | KW_TUPLE:t ID:i DIMENSIONES:d         {: RESULT = new SymbolTipo(t, d, i, txleft, txright); :}
        ;
 */
public class SymbolTipo extends SymbolBase {
    
    public final SymbolTipoPrimitivo tipo;
    public final String idTupla;
    public final SymbolDimensiones dimArray;

    public SymbolTipo(SymbolTipoPrimitivo tipo, Location left, Location right) {
        super("tipo", left, right);
        this.tipo = tipo;
        this.idTupla = null;
        this.dimArray = null;
    }
    
    public SymbolTipo(SymbolTipoPrimitivo tipo, SymbolDimensiones dimArray, Location left, Location right) {
        super("tipo", left, right);
        this.tipo = tipo;
        this.idTupla = null;
        this.dimArray = dimArray;
    }
    
    public SymbolTipo(String idTupla, Location left, Location right) {
        super("tipo", left, right);
        this.tipo = null;
        this.idTupla = idTupla;
        this.dimArray = null;
    }
    
    public SymbolTipo(String idTupla, SymbolDimensiones dimArray, Location left, Location right) {
        super("tipo", left, right);
        this.tipo = null;
        this.idTupla = idTupla;
        this.dimArray = dimArray;
    }
    
    public boolean isTupla(){
        return idTupla != null;
    }
    
    public boolean isArray() {
        return dimArray != null;
    }

}
