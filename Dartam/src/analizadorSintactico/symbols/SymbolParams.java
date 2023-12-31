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
    private static int id = 0;
    private SymbolParamsLista pl;
    
    public SymbolParams(SymbolParamsLista pl, int l, int r) {
        super("params", 0 , l , r);
        this.pl = pl;
    }
    
    public SymbolParams(){
        super("params", 0);
    }

    /*public SymbolParams(SymbolParamsLista et) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }*/

    public SymbolParamsLista getPl() {
        return pl;
    }
    
    
}
