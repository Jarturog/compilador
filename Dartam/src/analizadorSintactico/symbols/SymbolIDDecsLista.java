
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
 * IDDECSLISTA ::= ID:et1 ASIG:et2 COMMA IDDECSLISTA:et3       {: RESULT = new SymbolIDDecsLista(et1,et2,et3); :}
        | ID:et1 ASIG:et2                                   {: RESULT = new SymbolIDDecsLista(et1,et2); :}
        ;
 */
public class SymbolIDDecsLista extends ComplexSymbol {
    private static int id = 0;
    private String identificador;
    private SymbolAsig asignacion;
    private SymbolIDDecsLista iddecslista;
    
    public SymbolIDDecsLista(String identificador, SymbolAsig asignacion){
        super("iddecslista", id++, 0);
        this.identificador = identificador;
        this.asignacion = asignacion;
    }
    
    
    public SymbolIDDecsLista(String identificador, SymbolAsig asignacion, SymbolIDDecsLista iddecslista){
        super("iddecslista", id++, 0);
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
