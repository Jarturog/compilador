package analizadorSemantico;

import analizadorSintactico.symbols.SymbolOperand;
import java.util.ArrayList;
import jflex.base.Pair;

public class DescripcionArray extends DescripcionSimbolo {

    private ArrayList<SymbolOperand> dimensiones;
    public int dcamp; //Desplazamiento dentro del array

    /**
     * Array
     */
    public DescripcionArray(String t, ArrayList<SymbolOperand> dim, DescripcionSimbolo tupla, DescripcionDefinicionTupla tipoTupla) {
        super(t, false, false, tupla, tipoTupla);
        dimensiones = dim;
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
