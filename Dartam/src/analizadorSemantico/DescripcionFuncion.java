package analizadorSemantico;

import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolOperand;
import analizadorSintactico.symbols.SymbolTipo;
import java.util.ArrayList;
import jflex.base.Pair;

public class DescripcionFuncion extends DescripcionSimbolo {

    private ArrayList<DefinicionParametro> parametros;
    private final String nfuncion;
    private boolean tieneReturnObligatorio = false; // que siempre se ejecuta independientemente de los ifs o loops que haya
    
    public DescripcionFuncion(String tipoRetorno, String var) {
        super(tipoRetorno, false, false, null, null, var);
        parametros = new ArrayList<>();
        this.nfuncion = setNFuncion();
    }
    
    public DescripcionFuncion(String tipoRetorno, ArrayList<DefinicionParametro> params, String var) {
        super(tipoRetorno, false, false, null, null, var);
        parametros = params;
        this.nfuncion = setNFuncion();
    }
    
    public DescripcionFuncion(String tipoRetorno, String idParam, String tipoParam, String var) {
        super(tipoRetorno, false, false, null, null, var);
        parametros = new ArrayList<>();
        parametros.add(new DefinicionParametro(idParam, tipoParam));
        this.nfuncion = setNFuncion();
    }
    
    public DescripcionFuncion(String tipoRetorno, String idParam1, String tipoParam1, String idParam2, String tipoParam2, String var) {
        super(tipoRetorno, false, false, null, null, var);
        parametros = new ArrayList<>();
        parametros.add(new DefinicionParametro(idParam1, tipoParam1));
        parametros.add(new DefinicionParametro(idParam2, tipoParam2));
        this.nfuncion = setNFuncion();
    }
    
    private final String setNFuncion() {
        return "n"+variableAsociada.substring(1); // le quito la 'e' y le pongo la n
    }

    public String getNFuncion() {
        return nfuncion;
    }

    public void cambiarTipo(int t) {
        if (t == ParserSym.KW_METHOD) {
            //this.tipo = new SymbolTipo();
            this.parametros = new ArrayList<>();
        }
    }

    //Funciones
    public void anyadirParametro(DefinicionParametro d) {
        parametros.add(d);
    }

//    public int getNumeroParametros() {
//        return parametros.size();
//    }
    public ArrayList<String> getTiposParametros() {
        ArrayList<String> tipos = new ArrayList<>();
        for (DefinicionParametro p : parametros) {
            tipos.add(p.tipo);
        }
        return tipos;
    }
    
    public ArrayList<DefinicionParametro> getParametros() {
        return parametros;
    }

//    public HashMap<String, DescripcionSimbolo> getTiposMiembros() {
//        return new HashMap<>(miembros);
//    }
//
//    public void setTipoRetorno(String tv) throws Exception {
//        if (!this.isFunction()) {
//            throw new Exception("No es una función");
//        } else {
//            this.tipo = tv;
//        }
//    }
    @Override
    public String toString() {

        String params = "";
        for (DefinicionParametro param : parametros) {
            params += param + ", ";
        }
        params = params.length() > 0 ? "con argumentos" + params.substring(0, params.length() - 2) : "sin argumentos";

        return "Función de tipo '" + tipo + "'  " + params + " declarado en el nivel " + nivel;
    }

    public boolean necesitaReturnStatement() {
        return !tipo.equals(ParserSym.terminalNames[ParserSym.KW_VOID]);
    }
    
    public boolean devuelveValor() {
        return necesitaReturnStatement();
    }

    public boolean tieneReturnStatement() {
        return tieneReturnObligatorio;
    }

    /**
     * Si un if tiene un return y su else también, no cuenta para el método
     * porque tienen que estar con un nivel de diferencia
     * @param profundidad 
     */
    public void asignarReturn(int profundidad) {
        tieneReturnObligatorio = nivel == profundidad - 1;
    }
    
    
    
    public static class DefinicionParametro {
        protected final String id;
        protected final String tipo;
        public DefinicionParametro(String i, String t) {
            id = i;
            tipo = t;
        }
        @Override
        public String toString() {
            return tipo + " " + id;
        }
    }
}
