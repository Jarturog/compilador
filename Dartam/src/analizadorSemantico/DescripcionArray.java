package analizadorSemantico;

import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolOperand;
import java.util.ArrayList;
import jflex.base.Pair;

public class DescripcionArray extends DescripcionSimbolo {

    // o tiene dimensiones llenas o vacías, no las dos 
    private ArrayList<SymbolOperand> dimensiones;
    private Integer numDimensionesVacias;
    //
    private ArrayList<String> variablesDimension;
    public final String tipoElementoDelArray;
//    public int dcamp; //Desplazamiento dentro del array

    /**
     * Array
     */
    public DescripcionArray(String t, boolean isConst, ArrayList<SymbolOperand> dim, boolean valorAsignado, DescripcionDefinicionTupla tipoTupla, ArrayList<String> variablesDimension, String var) throws Exception {
        super(t, isConst, valorAsignado, tipoTupla, var);
        tipoElementoDelArray = t.substring(0, t.lastIndexOf(" "));
        dimensiones = dim;
        this.variablesDimension = variablesDimension;
    }

    public DescripcionArray(String t, boolean isConst, Integer num, boolean valorAsignado, DescripcionDefinicionTupla tipoTupla, ArrayList<String> variablesDimension, String var) throws Exception {
        super(t, isConst, valorAsignado, tipoTupla, var);
        tipoElementoDelArray = t.substring(0, t.lastIndexOf(" "));
        numDimensionesVacias = num;
        this.variablesDimension = variablesDimension;
    }

    public DescripcionArray(DescripcionArray d) {
        super(d);
        dimensiones = d.dimensiones;
        variablesDimension = d.variablesDimension;
        tipoElementoDelArray = d.tipoElementoDelArray;
        numDimensionesVacias = d.numDimensionesVacias;
    }

    public ArrayList<String> getVariablesDimension() {
        return variablesDimension;
    }

    //    //Array
//    public void setTipoBase(SymbolTipo base) {
//        //tipo.setTipoBase(base);
//    }
//
//    public String getTipoBase() {
//        return null;//tipo.getTipoBase();
//    }
//
//    public int getProfundidad() {
//        return -1;//tipo.getProfundidadArray();
//    }
//
//    public void setTamanyoArray(int n) {
//        //tipo.setTamanyoArray(n);
//    }
    @Override
    public String toString() {
        String dim = "";
        if (dimensiones != null) {
            for (SymbolOperand op : dimensiones) {
                dim += op + " ";
            }
        } else {
            for (int i = 0; i < numDimensionesVacias; i++) {
                dim += "[] ";
            }
        }
        dim = dim.substring(0, dim.length() - 1);
        String varDim = "";
        for (String s : variablesDimension) {
            varDim += s + ", ";
        }
        if (!varDim.isEmpty()) {
            varDim = varDim.substring(0, varDim.length() - 2);
        }
        String c = (isConstante() ? "constante " : "");
        String v = (tieneValorAsignado() ? " con valor asignado" : " sin valor asignado");
        String varA = variableAsociada == null ? "" : " siendo su variable de CI " + variableAsociada;
        String bytes = getBytes() == null ? "" : " ocupando " + getBytes() + " bytes";
        return "Array " + c + "tipo " + tipoElementoDelArray + v + " declarado en el nivel " + getNivel() + varA + bytes + " con dimensiones " + dim;
    }

    boolean isString() {
        return tipo.equalsIgnoreCase(ParserSym.terminalNames[ParserSym.CAR] + " []");
    }
}
