package analizadorSemantico.genCodigoIntermedio;

import java.util.ArrayList;
import analizadorSintactico.ParserSym;


public class EntradaVariable {
    public static final int TIPO_ARRAY = Integer.MAX_VALUE; //identificador de que es un array
    public static final int TYPE_FUNCTION = TIPO_ARRAY-1; //identificador de que es una funcion

    public static final int VAL_DESCONOCIDO = -1; //VALORE DESCONOCIDO

    public static final byte FALSE = 0;
    public static final byte TRUE = -1;

    public static final int BYTES_ENTERO = 4; //4 BYTES para un entero
    public static final int BYTES_CHAR = 1; //1 BYTE para un char
    public static final int BYTES_BOOLEAN = 1; //1 BYTE para un booleano
    
    public String tName;
    public int desplazamiento;
    public boolean isConstant;
    public int tipo;
    public int tipoSubyacente;
    public ArrayList<String> dimensiones;
    public String valorInicial = "0";
    public int tipoOperacion;
    
    //Constructor de una nueva variable
    public EntradaVariable(String nombre){
        //Nombre
        this.tName = nombre;
        
        //Tipos
        tipo = ParserSym.KW_INT;
        tipoSubyacente = ParserSym.KW_INT;
        
        //Dimensiones
        dimensiones = new ArrayList<>();
    }

//    //Clonaremos las dimensiones y los pasaremos a un nuevo array
//    private ArrayList<String> clonarDimensiones(){
//        ArrayList<String> rtn = new ArrayList<>();
//        for (String dim : dimensiones) {
//            rtn.add(dim);
//        }
//        return rtn;
//    }

    //Recibiremos cuanto ocupa la variable
    protected int getOcupacion(){
        int o;
        if(tipo == ParserSym.KW_INT){
            o = BYTES_ENTERO;
        }
        else o = BYTES_CHAR;
        for (String s : dimensiones) {
            try {
                o *= Integer.parseInt(s);
            } catch (Exception e){
                o = VAL_DESCONOCIDO;
            }
        }
        return o;
    }

    @Override
    public String toString(){
        String s = "[variable: " + this.tName + ", tipo: " + ParserSym.terminalNames[tipoSubyacente]  + ", desplazamiento: " + this.desplazamiento + ", ocupacion: " + this.getOcupacion();
        for (String d : this.dimensiones) {
            s += "[" + d + "]";
        }
        
        s += ", tipoSubyacente: " + ParserSym.terminalNames[tipoSubyacente] + "]";
        
        return s;
    }

}