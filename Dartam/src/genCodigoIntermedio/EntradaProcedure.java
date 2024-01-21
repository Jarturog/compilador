package genCodigoIntermedio;

import java.util.ArrayList;

import analizadorSintactico.ParserSym;
import java.util.HashMap;


public class EntradaProcedure {
    public static final int TAMAÑO_REGISTRO = 8; // Los registros son de 8 Bytes
    public static final int VAL_DESCONOCIDO = -1;
    public int profundidad; //Total de profundidad
    public String eInicio; //Etiqueta de inicio
    public String eFin; //Etiqueta de fin
    public int numeroParametros; //Número de parametros
    
    public ArrayList<String> parametros; //Lista de parametros
    public HashMap<String, EntradaVariable> tablaVariables; //Lista de variables asociadas

    //Constructor de un procedure
    public EntradaProcedure(){
        //Inicializamos variables
        profundidad = 1;
        numeroParametros = 0;
        tablaVariables = new HashMap<>();
        parametros = new ArrayList<>();
    }

    //Total de la ocupacion de la variables, en caso de haber una desconocida se devuelve -1
    public int getOcupacionVariables(){
        int tamaño = 0;
        for (EntradaVariable vte : tablaVariables.values()) {
            int val = vte.getOccupation();
            if(val == VAL_DESCONOCIDO) return VAL_DESCONOCIDO;
            tamaño += val;
        }
        return tamaño;
    }

    //Preparacion para codigo maquina
    public void preaparacionCodigoMaquina(){
        limpiarVariables();
        calcularDesplazamientos();
    }

    //Calcularemos los desplazamientos de los diferentes parametros
    private void calcularDesplazamientos(){
        EntradaVariable entrada;

        //Tamaño inicial del desplazamiento: 16
        int desplazamiento = TAMAÑO_REGISTRO * 2;
        
        //Ahora para cada parametro cogeremos su desplazamiento y lo sumaremos al total
        for(String s : parametros){
            entrada = tablaVariables.get(s);
            entrada.displacement = desplazamiento;
            desplazamiento += TAMAÑO_REGISTRO;
        }

        //Haremos lo mismo pero para variables
        int desplazamientoL = 0;
        for (String s : tablaVariables.keySet()) {
            entrada = tablaVariables.get(s);
            if(entrada.displacement == 0 && entrada.getOccupation()!= VAL_DESCONOCIDO){
                desplazamientoL -= entrada.getOccupation();
                entrada.displacement = desplazamientoL;
            }
        }
    }

    //Vaciado de la tabla de variables
    private void limpiarVariables(){
        HashMap<String, EntradaVariable> limpio = new HashMap<>();
        for (String s : tablaVariables.keySet()) {
            EntradaVariable entrada = tablaVariables.get(s);
            limpio.put(entrada.tName, entrada);
        }
        tablaVariables = limpio;
    }

    @Override
    public String toString(){
        return "[profundidad: " + this.profundidad + ", eInicio: " + this.eInicio + ", eFin: " + this.eFin + ", numeroParametros: " + this.numeroParametros + ", varsOccup: " + this.getOcupacionVariables()+ "]";
    }
}