package analizadorSemantico;

import analizadorSintactico.symbols.SymbolOperand;
import java.util.ArrayList;
import jflex.base.Pair;

public class DescripcionArray extends DescripcionSimbolo {

    // o tiene dimensiones llenas o vacías, no las dos 
    private ArrayList<SymbolOperand> dimensiones;
    private Integer numDimensionesVacias;
    //
    private ArrayList<String> variablesDimension;
    private Integer valorConegutEnTC;
    public final String tipoElementoDelArray;
//    public int dcamp; //Desplazamiento dentro del array

    /**
     * Array
     */
    public DescripcionArray(String t, boolean isConst, ArrayList<SymbolOperand> dim, boolean valorAsignado, DescripcionDefinicionTupla tipoTupla, ArrayList<String> variablesDimension, Integer valorConegutEnTC, String var) {
        super(t, isConst, valorAsignado, tipoTupla, var);
        tipoElementoDelArray = t.substring(0, t.lastIndexOf(" "));
        dimensiones = dim;
        this.variablesDimension = variablesDimension;
        this.valorConegutEnTC = valorConegutEnTC;
    }
    
    public DescripcionArray(String t, boolean isConst, Integer num, boolean valorAsignado, DescripcionDefinicionTupla tipoTupla, ArrayList<String> variablesDimension, Integer valorConegutEnTC, String var) {
        super(t, isConst, valorAsignado, tipoTupla, var);
        tipoElementoDelArray = t.substring(0, t.lastIndexOf(" "));
        numDimensionesVacias = num;
        this.variablesDimension = variablesDimension;
        this.valorConegutEnTC = valorConegutEnTC;
    }
    
    public DescripcionArray(DescripcionArray d){
        super(d);
        dimensiones = d.dimensiones;
        variablesDimension = d.variablesDimension;
        valorConegutEnTC = d.valorConegutEnTC;
        tipoElementoDelArray = d.tipoElementoDelArray;
        numDimensionesVacias = d.numDimensionesVacias;
    }
    
    public ArrayList<String> getVariablesDimension() {
        return variablesDimension;
    }
    
    public Integer getOffsetTempsCompilacio() {
        return valorConegutEnTC;
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
        String s = "Array de tipo '" + tipo + "' con dimensiones " + dim.substring(0, dim.length() - 1);

        return s + " declarado en el nivel " + getNivel();
    }
}
