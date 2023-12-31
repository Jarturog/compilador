

/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informatica 
 * Itinerari: Inteligencia Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;



/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramatica.
 * 
 * Reglas:
 * 
 *
DECS ::= KW_CONST:et1 TIPO_VAR:et2 ID_DECS_LISTA:et3 ENDINSTR     {: RESULT = new SymbolDecs(true,et2,et3, et1left, et1right); :}
        | TIPO_VAR:et1 ID_DECS_LISTA:et2 ENDINSTR                 {: RESULT = new SymbolDecs(false, et1,et2, et1left, et1right); :}
        | KW_CONST:et1 TIPO_VAR:et2 DIMENSIONES:et3 ID_DECS_LISTA:et4 ENDINSTR {: RESULT = new SymbolDecs(true, et2, et3, et4, et1left, et1right); :}
        | TIPO_VAR:et1 DIMENSIONES:et2 ID_DECS_LISTA:et3 ENDINSTR {: RESULT = new SymbolDecs(false, et1, et2, et3, et1left, et1right); :}
        | KW_TUPLE:et1 ID:et2 ID_DECS_LISTA:et3 ENDINSTR          {: RESULT = new SymbolDecs(et1, et2, et3, true, et1left, et1right); :} // 
        ;
 */
public class SymbolDecs extends SymbolBase {
    
    private boolean isConstante;
    private SymbolTipoVar tipo;
    private SymbolIDDecsLista iddecslista;
    private SymbolDimensiones dimensiones;
    private String identificador;
    private boolean isTupla;
    
    public SymbolDecs(boolean constante, SymbolTipoVar tipo, SymbolIDDecsLista iddecslista, Location l, Location r) {
        super("decs", l, r);
        this.isConstante = constante;
        this.isTupla = false;
        this.tipo = tipo;
        this.iddecslista = iddecslista;
    }
    
    //KW_CONST:et1 TIPO_VAR:et2 DIMENSIONES:et3 IDDECSLISTA:et4 
    public SymbolDecs(boolean constante, SymbolTipoVar tipo, SymbolDimensiones dimensiones, SymbolIDDecsLista iddecslista, Location l, Location r){
        super("decs", l,r);
        this.isConstante = constante;
        this.isTupla = false;
        this.tipo = tipo;
        this.dimensiones = dimensiones;
        this.iddecslista = iddecslista;
    }

    public SymbolDecs(Object et1, String et2, SymbolIDDecsLista et3, boolean b, Location l, Location r) {
        super("");
    }
    
    // | KW_TUPLE:et1 ID:et2 LKEY VARIOS_IDS:et3 RKEY ENDINSTR {: RESULT = new SymbolDecs(et1, et2, et3); :}
    //public SymbolDecs(String id,){
    //    super("decs" , l, r);
    //    this.
    //}

    public boolean isIsConstante() {
        return isConstante;
    }

    public SymbolTipoVar getTipo() {
        return tipo;
    }

    public SymbolIDDecsLista getIddecslista() {
        return iddecslista;
    }

    public SymbolDimensiones getDimensiones() {
        return dimensiones;
    }

    public String getIdentificador() {
        return identificador;
    }

    public boolean isIsTupla() {
        return isTupla;
    }
    
    
    
}
