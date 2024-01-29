package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;

/**
MIEMBROS_TUPLA ::= DECS:et1 MIEMBROS_TUPLA:et2         {:  RESULT = new SymbolMiembrosTupla(et1,et2, et1left, et1right); :}
        |                                              {:  RESULT = new SymbolMiembrosTupla(); :}
        ;
 */
public class SymbolMiembrosTupla extends SymbolBase {

    public final SymbolDecs decs;
    public final SymbolMiembrosTupla siguienteDeclaracion;
    
    public SymbolMiembrosTupla(Location l, Location r){
        super("miembrosTupla", l, r);
        decs = null;
        siguienteDeclaracion = null;
    }
    
    public SymbolMiembrosTupla(SymbolDecs d, SymbolMiembrosTupla m, Location l, Location r){
        super("miembrosTupla", l, r);
        decs = d;
        siguienteDeclaracion = m;
    }

}
