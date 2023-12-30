

/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informatica 
 * Itinerari: Inteligencia Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramatica.
 * 
 * Reglas:
 * 
 * DECS ::= KW_CONST:et1 TIPO_VAR:et2 IDDECSLISTA:et3 ENDINSTR     {: RESULT = new SymbolDecs(true,et2,et3); :}
        | TIPO_VAR:et1 IDDECSLISTA:et2 ENDINSTR                 {: RESULT = new SymbolDecs(et1,et2); :}
        | KW_CONST:et1 TIPO_VAR:et2 DIMENSIONES:et3 IDDECSLISTA:et4 ENDINSTR {: RESULT = new SymbolDecs(true, et2, et3, et4); :}
        | TIPO_VAR:et1 DIMENSIONES:et2 IDDECSLISTA:et3 ENDINSTR {: RESULT = new SymbolDecs(false, et1, et2, et3); :}
        | KW_TUPLE:et1 ID:et2 LKEY VARIOS_IDS:et3 RKEY ENDINSTR {: RESULT = new SymbolDecs(et1, et2, et3); :}
        ;
 */
public class SymbolDecs extends ComplexSymbol {
    private static int id = 0;
    private boolean isConstante;
    private SymbolTipoVar tipo;
    private SymbolIDDecsLista iddecslista;
    private SymbolDimensiones dimensiones;
    private String identificador;
    private boolean isTupla;
    
    public SymbolDecs(boolean constante, SymbolTipoVar tipo, SymbolIDDecsLista iddecslista) {
        super("decs", id++, 0);
        this.isConstante = constante;
        this.isTupla = false;
        this.tipo = tipo;
        this.iddecslista = iddecslista;
    }
    
    //KW_CONST:et1 TIPO_VAR:et2 DIMENSIONES:et3 IDDECSLISTA:et4 
    public SymbolDecs(boolean constante, SymbolTipoVar tipo, SymbolDimensiones dimensiones, SymbolIDDecsLista iddecslista){
        super("decs", id++, 0);
        this.isConstante = constante;
        this.isTupla = false;
        this.tipo = tipo;
        this.dimensiones = dimensiones;
        this.iddecslista = iddecslista;
    }
    
    // | KW_TUPLE:et1 ID:et2 LKEY VARIOS_IDS:et3 RKEY ENDINSTR {: RESULT = new SymbolDecs(et1, et2, et3); :}
    //public SymbolDecs(){
    //    
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
