package analizadorSemantico;

import analizadorSintactico.symbols.SymbolOperand;
import java.util.ArrayList;

public class DescripcionArray extends DescripcionSimbolo {
    private ArrayList<SymbolOperand> dimensiones;
    public int dcamp; //Desplazamiento dentro del array
    /**
     * Array
     */
    public DescripcionSimbolo(String t, ArrayList<SymbolOperand> dim, DescripcionSimbolo tupla) {
        tipo = t;
        dimensiones = dim;
        memberOf = tupla; // miembro de una tupla
        miembros = null;
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
}
