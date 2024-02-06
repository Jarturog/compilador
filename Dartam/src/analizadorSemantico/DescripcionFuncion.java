package analizadorSemantico;

import analizadorSemantico.genCodigoIntermedio.TipoVariable;
import analizadorSintactico.ParserSym;
import java.util.ArrayList;

public class DescripcionFuncion extends DescripcionSimbolo {

    private ArrayList<Parametro> parametros;
    // return que siempre se ejecuta independientemente de los ifs o loops que haya 
    // (está solamente un nivel por debajo de la declaración de la función)
    private boolean tieneReturnObligatorio = false;
    private boolean isMain = false;

    public DescripcionFuncion(String tipoRetorno, String var, boolean isMain) throws Exception {
        super(tipoRetorno, false, false, null, var);
        parametros = new ArrayList<>();
        this.isMain = isMain;
    }

    public DescripcionFuncion(String tipoRetorno, String var) throws Exception {
        super(tipoRetorno, false, false, null, var);
        parametros = new ArrayList<>();
    }

    public DescripcionFuncion(String tipoRetorno, ArrayList<Parametro> params, String var) throws Exception {
        super(tipoRetorno, false, false, null, var);
        parametros = params;
    }

    /**
     * Para métodos especiales
     */
    public DescripcionFuncion(String tipoRetorno, String idParam, String tipoParam, String var) throws Exception {
        super(tipoRetorno, false, false, null, var);
        parametros = new ArrayList<>();
        parametros.add(new Parametro(idParam, null, tipoParam));
    }

    /**
     * Para métodos especiales
     */
    public DescripcionFuncion(String tipoRetorno, String idParam1, String tipoParam1, String idParam2, String tipoParam2, String var) throws Exception {
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
    public void anyadirParametro(String id, String var, String tipo) throws Exception {
        parametros.add(new Parametro(id, var, tipo));
    }

    public ArrayList<Parametro> getParametros() {
        return parametros;
    }

    @Override
    public String toString() {
        String params = "";
        for (Parametro param : parametros) {
            params += param + ", ";
        }
        params = params.length() > 0 ? "con parámetros " + params.substring(0, params.length() - 2) : "sin parámetros";
        String etA = variableAsociada == null ? "" : " con etiqueta " + variableAsociada;
        String bytes = getBytes() == null ? "" : " devolviendo " + getBytes() + " bytes";
        return "Función tipo " + tipo + " declarada en el nivel " + getNivel() + etA + " " + params;
    }

    public boolean necesitaReturnStatement() {
        return !tipo.equals(ParserSym.terminalNames[ParserSym.VOID]);
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
     *
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
        public final TipoVariable t;

        public Parametro(String id, String variable, String tipo) throws Exception {
            this.id = id;
            this.variable = variable;
            this.tipo = tipo;
            t = TipoVariable.getTipo(tipo, tipo.contains("[") || tipo.startsWith(ParserSym.terminalNames[ParserSym.TUPLE]));
        }

        @Override
        public String toString() {
            String v = "";
            if (variable != null) {
                v = " siendo su variable de CI " + variable;
            }
            return tipo + " " + id + v;
        }
    }

}
