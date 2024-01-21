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
        vaciarVariables();
        calcularDesplazamientos();
    }

    //Calcularemos los desplazamientos de los diferentes parametros
    private void calcularDesplazamientos(){
        EntradaVariable entrada;

        // We calculate the displacements of the parameters. They are positive.
        int paramDisplacement = TAMAÑO_REGISTRO * 2; // We reserve space for DISP and BP and for the return
        for(String s : parametros){
            entrada = tablaVariables.get(s);
            entrada.displacement = paramDisplacement;
            paramDisplacement += TAMAÑO_REGISTRO;
        }

        int localVarDisplacement = 0;
        for (String s : tablaVariables.keySet()) {
            entrada = tablaVariables.get(s);
            if(entrada.displacement == 0 && entrada.getOccupation() != VAL_DESCONOCIDO){
                localVarDisplacement -= entrada.getOccupation();
                entrada.displacement = localVarDisplacement;
            }
        }
    }

    //Vaciado de la tabla de variables
    private void vaciarVariables(){
        HashMap<String, EntradaVariable> cleanVariableTable = new HashMap<>();
        for (String s : tablaVariables.keySet()) {
            EntradaVariable vte = tablaVariables.get(s);
            cleanVariableTable.put(vte.tName, vte);
        }
        tablaVariables = cleanVariableTable;
    }

    @Override
    public String toString(){
        return "[profundidad: " + this.profundidad + ", eInicio: " + this.eInicio + ", eFin: " + this.eFin + ", numeroParametros: " + this.numeroParametros + ", varsOccup: " + this.getOcupacionVariables()+ "]";
    }
}