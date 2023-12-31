/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSintactico.symbols;

/**
 *
 * @author jartu
 * 
 * PARAMS ::= PARAMSLISTA:et                             {: RESULT = new SymbolParams(et, etleft, etright); :}   
        |                                             {: RESULT = new SymbolParams(); :}
        ;

 */
public class SymbolParams extends SymbolBase {
    
    private SymbolParamsLista pl;
    
    public SymbolParams(SymbolParamsLista pl, int l, int r) {
        super("params" , l , r);
        this.pl = pl;
    }
    
    public SymbolParams(){
        super("");
    }

    /*public SymbolParams(SymbolParamsLista et) {
        super("");
    }*/

    public SymbolParamsLista getPl() {
        return pl;
    }
    
    
}
