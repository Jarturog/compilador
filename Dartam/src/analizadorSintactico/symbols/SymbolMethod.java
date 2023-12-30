
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
 * Reglas:
 * 
 * METHOD ::= KW_METHOD TIPO_RETORNO:et1 ID:et2 LPAREN GETPARAMS:et3 RPAREN LKEY BODY:et4 RKEY
 */
public class SymbolMethod extends ComplexSymbol {
    private static int id = 0;
    private SymbolTypeRetorno tipo;
    private String nombreMetodo;
    private SymbolGetParams parametros;
    private SymbolBody cuerpo;
    
    public SymbolMethod(SymbolTypeRetorno tipo, String nombreMetodo, SymbolGetParams parametros, SymbolBody cuerpo) {
        super("method", id++, 0);
        this.tipo = tipo;
        this.nombreMetodo = nombreMetodo;
        this.parametros = parametros;
        this.cuerpo = cuerpo;
    }

    public SymbolTypeRetorno getTipo() {
        return tipo;
    }

    public String getNombreMetodo() {
        return nombreMetodo;
    }

    public SymbolGetParams getParametros() {
        return parametros;
    }

    public SymbolBody getCuerpo() {
        return cuerpo;
    }
    
    public int getNumParametros(){
        if(this.parametros != null){
            return this.parametros.NumParametros();
        }else{
            return 0;
        }
    }
    
}
