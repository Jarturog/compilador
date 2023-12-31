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
    
    private SymbolTipoRetorno tipoRetorno;
    private String identificador;
    private SymbolParams parametros;
    private SymbolBody cuerpo;
    private SymbolDecs declaraciones;
    private boolean isTupla;
    private SymbolMiembrosTupla miembrosTupla;
    
    public SymbolScriptElemento(SymbolTipoRetorno tipoRetorno, String identificador, SymbolParams parametros, int l, int r){
        super("scriptElemento", 0, l,r);
        this.tipoRetorno = tipoRetorno;
        this.identificador = identificador;
        this.parametros = parametros;
    }
    
    /**
     * | DECS:et                       {: RESULT = new SymbolScriptElemento(et, etleft, etright); :}
        | KW_TUPLE:et1 ID:et2 LKEY MIEMBROS_TUPLA:et3 RKEY ENDINSTR {: RESULT = new Symbol(et1,et2,et3, et1left, et1right); :}
        
     */
    public SymbolScriptElemento(SymbolDecs decs, int l, int r){
        super("scriptElemento", 0 , l,r);
        this.declaraciones = decs;
    }
    
    public SymbolScriptElemento(boolean isTupla, String id, SymbolMiembrosTupla miem, int l, int r){
        super("scriptElemento", 0, l,r);
        this.identificador = id;
        this.isTupla = isTupla;
        this.miembrosTupla = miem;
    }
    
    
    
    /*
    public SymbolScriptElemento(SymbolDecs et) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public SymbolScriptElemento(SymbolTipoRetorno et1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    */

    public SymbolTipoRetorno getTipoRetorno() {
        return tipoRetorno;
    }

    public String getIdentificador() {
        return identificador;
    }

    public SymbolParams getParametros() {
        return parametros;
    }

    public SymbolBody getCuerpo() {
        return cuerpo;
    }

    public SymbolDecs getDeclaraciones() {
        return declaraciones;
    }

    public boolean isIsTupla() {
        return isTupla;
    }

    public SymbolMiembrosTupla getMiembrosTupla() {
        return miembrosTupla;
    }
}
