
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;



/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * REGLAS:
 * 
PARAM ::= TIPO_VAR:et1 ID:et2                   {: RESULT = new SymbolParam(et1,et2, et1left, et1right); :}
        | TIPO_VAR:et1 DIMENSIONES:et2 ID:et3   {: RESULT = new SymbolParam(et1,et2,et3, et1left, et1right); :}
        | KW_TUPLE:et1 ID:et2 ID:et3            {: RESULT = new SymbolParam(true,et1,et2, et1left, et1right); :}
        ;
 */
public class SymbolParam extends SymbolBase {
    
    private SymbolTipoVar tipo;
    private SymbolDimensiones dim;
    private boolean isTuple;
    private String identificador1;
    private String identificador2;

    public SymbolParam(SymbolTipoVar tipo, String identificador, int l, int r){
        super("param", l,r);
        this.tipo = tipo;
        this.identificador1 = identificador;
    }
    
    public SymbolParam(SymbolTipoVar tipo, SymbolDimensiones dim,  String identificador, int l, int r){
        super("param", l,r);
        this.tipo = tipo;
        this.dim = dim;
        this.identificador1 = identificador;
    }
    
    public SymbolParam(boolean isTuple, String identificador1 ,String identificador2, int l, int r){
        super("param", l,r);
        this.isTuple = true;
        this.identificador1 = identificador1;
        this.identificador2 = identificador2;
    }

    public SymbolParam(boolean b, Object et1, String et2, int et1left, int et1right) {
    super("");}
    
    public SymbolTipoVar getTipo() {
        return tipo;
    }

    public SymbolDimensiones getDim() {
        return dim;
    }

    public boolean isIsTuple() {
        return isTuple;
    }

    public String getIdentificador1() {
        return identificador1;
    }

    public String getIdentificador2() {
        return identificador2;
    }
    
    
    
    
    
    
}
