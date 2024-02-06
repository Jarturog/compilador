package analizadorSemantico;

import analizadorSemantico.genCodigoIntermedio.Tipo;
import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolOperand;
import analizadorSintactico.symbols.SymbolTipo;
import java.util.ArrayList;
import jflex.base.Pair;

public class DescripcionFuncion extends DescripcionSimbolo {

    private ArrayList<Parametro> parametros;
    private boolean tieneReturnObligatorio = false; // que siempre se ejecuta independientemente de los ifs o loops que haya
    private boolean isMain = false;
    
    public DescripcionFuncion(String tipoRetorno, String var, boolean isMain) {
        super(tipoRetorno, false, false, null, var);
        parametros = new ArrayList<>();
        this.isMain = isMain;
    }
    
    public DescripcionFuncion(String tipoRetorno, String var) {
        super(tipoRetorno, false, false, null, var);
        parametros = new ArrayList<>();
    }
    
    public DescripcionFuncion(String tipoRetorno, ArrayList<Parametro> params, String var) {
        super(tipoRetorno, false, false, null, var);
        parametros = params;
    }
    
    /**
     * Para métodos especiales
     */
    public DescripcionFuncion(String tipoRetorno, String idParam, String tipoParam, String var) {
        super(tipoRetorno, false, false, null, var);
        parametros = new ArrayList<>();
        parametros.add(new Parametro(idParam, null, tipoParam));
    }
    
    /**
     * Para métodos especiales
     */
    public DescripcionFuncion(String tipoRetorno, String idParam1, String tipoParam1, String idParam2, String tipoParam2, String var) {
        super(tipoRetorno, false, false, null, var);
        parametros = new ArrayList<>();
        parametros.add(new Parametro(idParam1, null, tipoParam1));
        parametros.add(new Parametro(idParam2, null, tipoParam2));
    }

    public void cambiarTipo(int t) {
        if (t == ParserSym.KW_METHOD) {
            //this.tipo = new SymbolTipo();
            this.parametros = new ArrayList<>();
        }
    }

    //Funciones
    public void anyadirParametro(String id, String var, String tipo) {
        parametros.add(new Parametro(id, var, tipo));
    }

//    public int getNumeroParametros() {
//        return parametros.size();
//    }
//    public ArrayList<String> getTiposParametros() {
//        ArrayList<String> tipos = new ArrayList<>();
//        for (Parametro p : parametros) {
//            tipos.add(p.snd);
//        }
//        return tipos;
//    }
    
    public ArrayList<Parametro> getParametros() {
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
        for (Parametro param : parametros) {
            params += param.tipo + " " + param.id + ", ";
        }
        params = params.length() > 0 ? "con argumentos" + params.substring(0, params.length() - 2) : "sin argumentos";

        return "Función de tipo '" + tipo + "'  " + params + " declarado en el nivel " + getNivel();
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
        tieneReturnObligatorio = getNivel() == profundidad - 1;
    }

    boolean isMain() {
        return isMain;
    }
    
    public static class Parametro {
        public final String id, variable, tipo;

        public Parametro(String id, String variable, String tipo) {
            this.id = id;
            this.variable = variable;
            this.tipo = tipo;
        }
    }
    
}
