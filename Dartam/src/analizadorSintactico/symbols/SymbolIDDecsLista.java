
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * Reglas
 * 
 * ID_DECS_LISTA ::= ID:et1 ASIG:et2 COMMA ID_DECS_LISTA:et3       {: RESULT = new SymbolIDDecsLista(et1,et2,et3, et1left, et1right); :}
        | ID:et1 ASIG:et2                                   {: RESULT = new SymbolIDDecsLista(et1,et2, et1left, et1right); :}
        ;
 */
public class SymbolIDDecsLista extends SymbolBase {
    private static int id = 0;
    private String identificador;
    private SymbolAsig asignacion;
    private SymbolIDDecsLista iddecslista;
    
    public SymbolIDDecsLista(String identificador, SymbolAsig asignacion, int l , int r){
        super("iddecslista",0, l, r);
        this.identificador = identificador;
        this.asignacion = asignacion;
    }
    
    
    public SymbolIDDecsLista(String identificador, SymbolAsig asignacion, SymbolIDDecsLista iddecslista, int l, int r){
        super("iddecslista",0, l, r);
        this.identificador = identificador;
        this.asignacion = asignacion;
        this.iddecslista = iddecslista;
    }



    public String getIdentificador() {
        return identificador;
    }

    public SymbolIDDecsLista getIddecslista() {
        return iddecslista;
    }

    public SymbolAsig getAsignacion() {
        return asignacion;
    }
    
    
    
    
}
