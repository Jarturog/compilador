package analizadorSemantico;

import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolOperand;
import analizadorSintactico.symbols.SymbolTipo;
import java.util.ArrayList;
import jflex.base.Pair;

public class DescripcionFuncion extends DescripcionSimbolo {

    private ArrayList<DefinicionParametro> parametros;
    
    public DescripcionFuncion(String tipoRetorno) {
        super(tipoRetorno, false, false, null, null);
        parametros = new ArrayList<>();
    }
    
    public DescripcionFuncion(String tipoRetorno, ArrayList<DefinicionParametro> params) {
        super(tipoRetorno, false, false, null, null);
        parametros = params;
    }
    
    public DescripcionFuncion(String tipoRetorno, String idParam, String tipoParam) {
        super(tipoRetorno, false, false, null, null);
        parametros = new ArrayList<>();
        parametros.add(new DefinicionParametro(idParam, tipoParam));
    }
    
    public DescripcionFuncion(String tipoRetorno, String idParam1, String tipoParam1, String idParam2, String tipoParam2) {
        super(tipoRetorno, false, false, null, null);
        parametros = new ArrayList<>();
        parametros.add(new DefinicionParametro(idParam1, tipoParam1));
        parametros.add(new DefinicionParametro(idParam2, tipoParam2));
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
