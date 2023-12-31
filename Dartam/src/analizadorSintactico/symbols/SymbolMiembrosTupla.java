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

    public final SymbolDecs decs;
    public final SymbolMiembrosTupla miembrosTupla;
    
    public SymbolMiembrosTupla(){
        super("miembrosTupla");
        decs = null;
        miembrosTupla = null;
    }
    
    public SymbolMiembrosTupla(SymbolDecs d, SymbolMiembrosTupla m, int l, int c){
        super("miembrosTupla", l, c);
        decs = d;
        miembrosTupla = m;
    }

}
