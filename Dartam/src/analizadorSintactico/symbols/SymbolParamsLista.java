/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;

import java_cup.runtime.ComplexSymbolFactory;


/**
 *
 * @author jartu
 * 
 * PARAMSLISTA ::= PARAM:et1 COMMA PARAMSLISTA:et3           {: RESULT = new ParamsLista(et1, et2, et1left, et1right); :}
        | PARAM:et                           {: RESULT = new ParamsLista(et, etleft, etright); :}
        ;
 */
public class SymbolParamsLista extends SymbolBase {
    
    public final SymbolTipo param;
    public final SymbolParamsLista pl;
    public int numParametros;
    
    public SymbolParamsLista(SymbolTipo param, Location l, Location r) {
        super("paramsLista" , l , r);
        this.param = param;
        this.pl = null;
        this.numParametros += 1;
        
    }
    
    public SymbolParamsLista(SymbolTipo param, SymbolParamsLista pl, Location l, Location r) {
        super("paramsLista" , l , r);
        this.param = param;
        this.pl = pl;
        this.numParametros +=1 ; 
    }

    public SymbolTipo getParam() {
        return param;
    }

    public SymbolParamsLista getPl() {
        return pl;
    }
    
    
}
