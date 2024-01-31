/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico.genCodigoIntermedio;

import analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * 
 */
public class Generador3Direcciones {

    private final HashMap<String, VData> tablaVariables;
    private final HashMap<String, Integer> numEtiquetasOVariablesConId;
    private final TablaProcedimientos tablaProcedimientos;
//    private final ArrayList<String> listaProcedimientos; //Funcionara como una pila
    private final ArrayList<Instruccion> instrucciones;
    private int nEtiquetaConId = 1, nEtiqueta = 1;
    private int nVariableConId = 1, nVariableTemporal = 1, nVariableDimensiones = 1;

    public Generador3Direcciones() {
        this.tablaVariables = new HashMap<>();
        this.tablaProcedimientos = new TablaProcedimientos();
//        this.listaProcedimientos = new ArrayList<>();
        this.instrucciones = new ArrayList<>();
        numEtiquetasOVariablesConId = new HashMap<>();
    }
    
    public Generador3Direcciones(HashMap<String, VData> tv, TablaProcedimientos tp) {
        this.tablaVariables = tv;
        this.tablaProcedimientos = tp;
//        this.listaProcedimientos = new ArrayList<>();
        this.instrucciones = new ArrayList<>();
        numEtiquetasOVariablesConId = new HashMap<>();
    }
    
    public String nuevoN(String nombre) {
        String nom = "n" + nombre;
        Integer num = numEtiquetasOVariablesConId.get(nom);
        boolean hayRepe = num != null;
        numEtiquetasOVariablesConId.put(nom, hayRepe ? 1 + num : 1);
        return nom + (hayRepe ? num : "");
    }

    //Crecion de una etiqueta: Sintaxi etX donde X es el contador de etiquetas puestas
    public String nuevaEtiqueta() {
        return "et" + nEtiqueta++;
    }

    public String nuevaEtiqueta(String nombre) {
        Integer num = numEtiquetasOVariablesConId.get(nombre);
        boolean hayRepe = num != null;
        numEtiquetasOVariablesConId.put(nombre, hayRepe ? 1 + num : 1);
        nEtiquetaConId++;
        return "et" + nombre + (hayRepe ? num : "");
    }

    public String nuevaVariable() {
        return nuevaVariable(TipoReferencia.var, null);
    }
    
    public String nuevaVariable(String id) {
        return nuevaVariable(id, TipoReferencia.var, null);
    }
    
    //Le pasaremos si es una variable o un parametro, de que tipo, y si es un array o una tupla
    public String nuevaVariable(TipoReferencia tipoVariable, String procedimientoActual) {
        int numeroVariable = nVariableTemporal++;
        String nombre = "t" + numeroVariable;
        VData data = new VData(nombre, tipoVariable, procedimientoActual);
        tablaVariables.put(nombre, data);
        return nombre;
    }

    //Le pasaremos si es una variable o un parametro, de que tipo, y si es un array o una tupla
    public String nuevaVariable(String id, TipoReferencia tipoVariable, String procedimientoActual) {
        Integer num = numEtiquetasOVariablesConId.get(id);
        boolean hayRepe = num != null;
        numEtiquetasOVariablesConId.put(id, hayRepe ? 1 + num : 1);
        nVariableConId++;
        id += (hayRepe ? num : "");
        VData data = new VData(id, tipoVariable, procedimientoActual);
        tablaVariables.put(id, data);
        return id;
    }
    
    public String nuevaDimension(String idArray, TipoReferencia tipoVariable, String procedimientoActual) {
        int numeroVariable = nVariableDimensiones++;
        String nombre = "d" + numeroVariable + idArray;
        VData data = new VData(nombre, tipoVariable, procedimientoActual);
        tablaVariables.put(nombre, data);
        return nombre;
    }

//    //Devuelve la función que esta en el top de la pila
//    public String getFuncionActual(){
//        return listaProcedimientos.isEmpty() ? null : listaProcedimientos.get(listaProcedimientos.size() - 1);
//    }
//    
//    //Eliminar funcion
//    public String eliminarFuncion(){
//        return listaProcedimientos.remove(listaProcedimientos.size() - 1);
//    }
//    
//    //Permite añadir una nueva funcion
//    public void añadirFuncion(String id){
//        listaProcedimientos.add(id);
//    }
    //Permite crear un nuevo procedimiento y añadirlo a la tabla
    public int nuevoProcedimiento(String id, int profundidad, String etiqueta) {
        int contador = this.tablaProcedimientos.getContador();
        PData data = new PData(profundidad, etiqueta, -1, -1);
        this.tablaProcedimientos.put(id, data);
        return contador;
    }

    //Recibimos los datos de un procedimiento con el nombre pasado por parametro
    public PData getProcedimeinto(String id) {
        return this.tablaProcedimientos.get(id);
    }

    //Crearemos la instruccion de 3 direcciones y almacenaremos en el array list
    public void generarInstr(TipoInstr instruccion, Operador op1, Operador op2, Operador dst) {
        Instruccion ins = new Instruccion(instruccion, op1, op2, dst);
        this.instrucciones.add(ins);
    }
    
    public void generarInstr(TipoInstr instruccion, String op1, String op2, String dst) {
        Instruccion ins = new Instruccion(instruccion, op1 == null ? null : new Operador(op1), op2 == null ? null : new Operador(op2), dst == null ? null : new Operador(dst));
        this.instrucciones.add(ins);
    }

//    //Metodo que nos devolvera el arraylist con los objetos de 3 direcciones 
//    public ArrayList<Instruccion3Direcciones> instrucciones(){
//        return this.instrucciones;
//    }

    public ArrayList<Instruccion> getInstrucciones() {
        return instrucciones;
    }
    
}
