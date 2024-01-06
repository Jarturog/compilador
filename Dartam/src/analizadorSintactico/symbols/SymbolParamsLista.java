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
    public int numParametros;
    
    public SymbolParamsLista(SymbolTipo param, String id, Location l, Location r) {
        super("paramsLista" , l , r);
        this.param = param;
        this.id = id;
        this.siguienteParam = null;
        
        if(siguienteParam != null){
            numParametros = siguienteParam.getNumeroParametros() + 1;
        }else{
            numParametros = 1;
        }
    }
    
    public SymbolParamsLista(SymbolTipo param, String id, SymbolParamsLista pl, Location l, Location r) {
        super("paramsLista" , l , r);
        this.param = param;
        this.id = id;
        this.siguienteParam = pl;
        this.numParametros = 1; //Revisar probando que no pase de una lista de params a solo 1
    }
    
    
    public int getNumeroParametros(){
        return numParametros;
    }

}
