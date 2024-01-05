/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 *
 * @author jartu
 * 
 * PARAMS ::= PARAMSLISTA:et                             {: RESULT = new SymbolParams(et, etleft, etright); :}   
        |                                             {: RESULT = new SymbolParams(); :}
        ;

 */
public class SymbolParams extends SymbolBase {
    
    public final SymbolParamsLista paramsLista;
    
    public SymbolParams(SymbolParamsLista pl, Location l, Location r) {
        super("params" , l , r);
        this.paramsLista = pl;
    }
    
    public SymbolParams(){
        super("");
        this.paramsLista = null;
    }

    
}
