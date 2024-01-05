/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;


/**
PARAMSLISTA ::= TIPO:et1 ID:id COMMA PARAMSLISTA:sig           {: RESULT = new SymbolParamsLista(et1, id, sig, et1xleft, et1xright); :}
        | TIPO:et ID:id                          {: RESULT = new SymbolParamsLista(et, id, etxleft, etxright); :}
        ;
 */
public class SymbolParamsLista extends SymbolBase {
    
    public final SymbolTipo param;
    public final String id;
    public final SymbolParamsLista siguienteParam;
    
    public SymbolParamsLista(SymbolTipo param, String id, Location l, Location r) {
        super("paramsLista" , l , r);
        this.param = param;
        this.id = id;
        this.siguienteParam = null;
    }
    
    public SymbolParamsLista(SymbolTipo param, String id, SymbolParamsLista pl, Location l, Location r) {
        super("paramsLista" , l , r);
        this.param = param;
        this.id = id;
        this.siguienteParam = pl;
    }

}
