package analizadorSemantico;

import analizadorSintactico.symbols.SymbolOperand;
import java.util.ArrayList;
import jflex.base.Pair;

public class DescripcionArray extends DescripcionSimbolo {

    private ArrayList<SymbolOperand> dimensiones;
    private ArrayList<String> variablesDimension;
    private Integer valorConegutEnTC;
//    public int dcamp; //Desplazamiento dentro del array

    /**
     * Array
     */
    public DescripcionArray(String t, ArrayList<SymbolOperand> dim, DescripcionSimbolo tupla, DescripcionDefinicionTupla tipoTupla, ArrayList<String> variablesDimension, Integer valorConegutEnTC) {
        super(t, false, false, tupla, tipoTupla);
        dimensiones = dim;
        this.variablesDimension = variablesDimension;
        this.valorConegutEnTC = valorConegutEnTC;
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
        for (SymbolOperand op : dimensiones) {
            dim += op + " ";
        }
        String s = "Array de tipo '" + tipo + "' con dimensiones " + dim.substring(0, dim.length() - 1);

        return s + " declarado en el nivel " + nivel;
    }
}
