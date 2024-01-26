package analizadorSemantico;

import analizadorSintactico.ParserSym;
import java.util.ArrayList;
import jflex.base.Pair;

public class DescripcionFuncion extends DescripcionSimbolo {
    private ArrayList<Pair<String, DescripcionSimbolo>> parametros;
    
    /**
     * Función
     */
    public DescripcionFuncion(String tipoRetorno) {
        super(tipoRetorno);
        parametros = new ArrayList<>();
    }
    
    public void cambiarTipo(int t) {
        if (t == ParserSym.KW_METHOD) {
            //this.tipo = new SymbolTipo();
            this.parametros = new ArrayList<>();
        }
    }

    public String getTipo() {
        return super.tipo;
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
}
