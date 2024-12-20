/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica 
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

/**
 * 
 * Reglas:
 * 
 * METHOD ::= KW_METHOD TIPO_RETORNO:et1 ID:et2 LPAREN GETPARAMS:et3 RPAREN LKEY BODY:et4 RKEY
 */
public class SymbolMetodo extends SymbolBase {
    
    public final SymbolTipoRetorno tipo;
    private String nombreMetodo;
//    public final SymbolGetParams parametros;
    public final SymbolBody cuerpo;
    
    public SymbolMetodo(SymbolTipoRetorno tipo, String nombreMetodo, Object parametros, SymbolBody cuerpo) {
        super("method");
        this.tipo = tipo;
        this.nombreMetodo = nombreMetodo;
        //this.parametros = parametros;
        this.cuerpo = cuerpo;
    }

    public SymbolTipoRetorno getTipo() {
        return tipo;
    }

    public String getNombreMetodo() {
        return nombreMetodo;
    }

    public SymbolBody getCuerpo() {
        return cuerpo;
    }
    
}
