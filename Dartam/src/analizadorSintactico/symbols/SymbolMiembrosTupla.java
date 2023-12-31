/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSintactico.symbols;

/**
 *
 * @author jartu
 * 
 * 
 * 
MIEMBROS_TUPLA ::= DECS:et1 MIEMBROS_TUPLA:et2         {:  RESULT = new SymbolMiembrosTupla(et1,et2, et1left, et1right); :}
        |                                              {:  RESULT = new SymbolMiembrosTupla(); :}
        ;
 */
public class SymbolMiembrosTupla extends SymbolBase {
    private static int id = 0;
    private SymbolDecs decs;
    private SymbolMiembrosTupla miembrosTupla;
    
    public SymbolMiembrosTupla(){
        super("miembrosTupla", 0);
    }
    
    public SymbolMiembrosTupla(SymbolDecs decs, SymbolMiembrosTupla miembrosTupla, int l, int r){
        super("miembrosTupla", 0, l,r);
        this.decs = decs;
        this.miembrosTupla = miembrosTupla;
    }
    
    
    
    /*public SymbolMiembrosTupla (String variable, Double valor) {
        super(variable, valor);
    }*/

    public SymbolDecs getDecs() {
        return decs;
    }

    public SymbolMiembrosTupla getMiembrosTupla() {
        return miembrosTupla;
    }
    
}
