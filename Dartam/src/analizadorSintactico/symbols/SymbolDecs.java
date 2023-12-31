

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
DECS ::= KW_CONST:et1 TIPO_VAR:et2 ID_DECS_LISTA:et3 ENDINSTR     {: RESULT = new SymbolDecs(true,et2,et3, et1xleft, et1xright); :}
        | TIPO_VAR:et1 ID_DECS_LISTA:et2 ENDINSTR                 {: RESULT = new SymbolDecs(false, et1,et2, et1xleft, et1xright); :}
        | KW_CONST:et1 TIPO_VAR:et2 DIMENSIONES:et3 ID_DECS_LISTA:et4 ENDINSTR {: RESULT = new SymbolDecs(true, et2, et3, et4, et1xleft, et1xright); :}
        | TIPO_VAR:et1 DIMENSIONES:et2 ID_DECS_LISTA:et3 ENDINSTR {: RESULT = new SymbolDecs(false, et1, et2, et3, et1xleft, et1xright); :}
        | KW_TUPLE:et1 ID:et2 ID_DECS_LISTA:et3 ENDINSTR          {: RESULT = new SymbolDecs(et1, et2, et3, true, et1xleft, et1xright); :} //REVISAR 
        ;
 */
public class SymbolDecs extends SymbolBase {
    
    // compartido por primitivas, arrays y tuplas
    public final SymbolIDDecsLista iddecslista;
    public final SymbolTipoVar tipo;
    // primitiva
    public final boolean isConst;
    // array
    public final SymbolDimensiones dimensiones;
    // tupla
    public final String idTupla;
    
    // primitiva
    public SymbolDecs(boolean constante, SymbolTipoVar tipo, SymbolIDDecsLista iddecslista, Location l, Location r) {
        super("decPrim", l, r);
        idTupla = null;
        isConst = constante;
        this.tipo = tipo;
        dimensiones = null;
        this.iddecslista = iddecslista;
    }
    
    // array
    public SymbolDecs(SymbolTipoVar tipo, SymbolDimensiones dim, SymbolIDDecsLista iddecslista, Location l, Location r){
        super("decArr", l, r);
        idTupla = null;
        isConst = false;
        this.tipo = tipo;
        dimensiones = dim;
        this.iddecslista = iddecslista;
    }

    // tupla
    public SymbolDecs(Object et1, String id, SymbolIDDecsLista et3, Location l, Location r) {
        super("decTupla", l, r);
        idTupla = id;
        isConst = false;
        tipo = null;
        dimensiones = null;
        iddecslista = null;
    }
    
}
