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
 * Reglas:
 * 
 * SCRIPT_ELEMENTO ::= KW_METHOD TIPO_RETORNO:et1 ID:et2 LPAREN PARAMS:et3 RPAREN LKEY BODY:et4 RKEY                  {: RESULT = new SymbolScriptElemento(et1, et1left, et1right); :}
        | DECS:et                       {: RESULT = new SymbolScriptElemento(et, etleft, etright); :}
        | KW_TUPLE:et1 ID:et2 LKEY MIEMBROS_TUPLA:et3 RKEY ENDINSTR {: RESULT = new Symbol(et1,et2,et3, et1left, et1right); :}
        ;
 */
public class SymbolScriptElemento extends SymbolBase{
    
    // metodo y tupla
    public final String idTuplaMetodo;
    // metodo
    public final SymbolTipoRetorno tipoRetorno;
    public final SymbolParams parametros;
    public final SymbolBody cuerpo;
    // tupla
    public final SymbolMiembrosTupla miembrosTupla;
    // declaraciones
    public final SymbolDecs declaraciones;
    
    public SymbolScriptElemento(SymbolTipoRetorno tipo, String id, SymbolParams p, SymbolBody b, int l, int c) {
        super("scriptElemento", l, c);
        tipoRetorno = tipo;
        idTuplaMetodo = id;
        parametros = p;
        cuerpo = b;
        miembrosTupla = null;
        declaraciones = null;
    }
    
    public SymbolScriptElemento(String id, SymbolMiembrosTupla m, int l, int c) {
        super("scriptElemento", l, c);
        tipoRetorno = null;
        idTuplaMetodo = id;
        parametros = null;
        cuerpo = null;
        miembrosTupla = m;
        declaraciones = null;
    }
    
    public SymbolScriptElemento(SymbolDecs decs, int l, int c){
        super("scriptElemento", l, c);
        tipoRetorno = null;
        idTuplaMetodo = null;
        parametros = null;
        cuerpo = null;
        miembrosTupla = null;
        declaraciones = decs;
    }
}
