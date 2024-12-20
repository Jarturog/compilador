package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;

/**
PARAMSLISTA ::= TIPO:et1 ID:id COMMA PARAMSLISTA:sig           {: RESULT = new SymbolParamsLista(et1, id, sig, et1xleft, et1xright); :}
        | TIPO:et ID:id                          {: RESULT = new SymbolParamsLista(et, id, etxleft, etxright); :}
        ;
 */
public class SymbolParamsLista extends SymbolBase {
    
    public final SymbolTipo tipoParam;
    public final String idParam;
    public final SymbolParamsLista siguienteParam;
    private final int numParametros;
    
    public SymbolParamsLista(SymbolTipo param, String id, Location l, Location r) {
        super("paramsLista" , l , r);
        this.tipoParam = param;
        this.idParam = id;
        this.siguienteParam = null;
        
        if(siguienteParam != null){
            numParametros = siguienteParam.getNumeroParametros() + 1;
        }else{
            numParametros = 1;
        }
    }
    
    public SymbolParamsLista(SymbolTipo param, String id, SymbolParamsLista pl, Location l, Location r) {
        super("paramsLista" , l , r);
        this.tipoParam = param;
        this.idParam = id;
        this.siguienteParam = pl;
        this.numParametros = 1; //Revisar probando que no pase de una lista de params a solo 1
    }

    public int getNumeroParametros(){
        return numParametros;
    }

}
