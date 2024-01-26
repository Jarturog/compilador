package analizadorSemantico;

import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolOperand;
import java.util.ArrayList;
import jflex.base.Pair;

public class DescripcionFuncion extends DescripcionSimbolo {

    private ArrayList<Pair<String, DescripcionSimbolo>> parametros;

    /**
     * Función
     */
    public DescripcionFuncion(String tipoRetorno) {
        super(tipoRetorno, false, false, null, null);
        parametros = new ArrayList<>();
    }

    public void cambiarTipo(int t) {
        if (t == ParserSym.KW_METHOD) {
            //this.tipo = new SymbolTipo();
            this.parametros = new ArrayList<>();
        }
    }

    //Funciones
    public void anyadirParametro(String n, DescripcionSimbolo d) {
        parametros.add(new Pair(n, d));
    }

//    public int getNumeroParametros() {
//        return parametros.size();
//    }
    public ArrayList<Pair<String, DescripcionSimbolo>> getTiposParametros() {
        return new ArrayList<>(parametros);
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
        for (Pair<String, DescripcionSimbolo> param : parametros) {
            params += param.fst + " ";
        }
        params = params.length() > 0 ? "con argumentos" + params.substring(0, params.length() - 1) : "sin argumentos";

        return "Función de tipo '" + tipo + "'  " + params + " declarado en el nivel " + nivel;
    }
}
