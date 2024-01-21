package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 * Reglas:
 * 
 * SCRIPT_ELEMENTO ::= KW_METHOD TIPO_RETORNO:et1 ID:et2 LPAREN PARAMS:et3 RPAREN LKEY BODY:et4 RKEY                  {: RESULT = new SymbolScriptElemento(et1, et1left, et1right); :}
        | DECS:et                       {: RESULT = new SymbolScriptElemento(et, etleft, etright); :}
        | KW_TUPLE:et1 ID:et2 LKEY MIEMBROS_TUPLA:et3 RKEY ENDINSTR {: RESULT = new Symbol(et1,et2,et3, et1left, et1right); :}
        ;
 */
public class SymbolScriptElemento extends SymbolBase{
    
    // metodo y tupla
    public final String id;
    // metodo
    public final SymbolTipoRetorno tipoRetorno;
    public final SymbolParams parametros;
    public final SymbolBody cuerpo;
    // tupla
    public final SymbolMiembrosTupla miembrosTupla;
    // declaraciones
    public final SymbolDecs declaraciones;
    
    public SymbolScriptElemento(SymbolTipoRetorno tipo, String id, SymbolParams p, SymbolBody b, Location l, Location r) {
        super("scriptElementoMetodo", l, r);
        tipoRetorno = tipo;
        this.id = id;
        parametros = p;
        cuerpo = b;
        miembrosTupla = null;
        declaraciones = null;
    }
    
    public SymbolScriptElemento(String id, SymbolMiembrosTupla m, Location l, Location r) {
        super("scriptElementoTupla", l, r);
        tipoRetorno = null;
        this.id = id;
        parametros = null;
        cuerpo = null;
        miembrosTupla = m;
        declaraciones = null;
    }
    
    public SymbolScriptElemento(SymbolDecs decs, Location l, Location r){
        super("scriptElementoDeclaracion", l, r);
        tipoRetorno = null;
        id = null;
        parametros = null;
        cuerpo = null;
        miembrosTupla = null;
        declaraciones = decs;
    }

    public static enum TIPO { DECS, METODO, TUPLA }
    
    /**
     * 
     * @return d, m or t
     */
    public TIPO getTipo() {
        if (declaraciones != null) {
            return TIPO.DECS;
        } else if (tipoRetorno != null) {
            return TIPO.METODO;
        } else {
            return TIPO.TUPLA;
        }
    }
}
