/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico;

import java.util.ArrayList;

/**
 *
 * @author dasad
 */
public class Generador3Direcciones {
    private TablaVariables tablaVariables;
    private TablaProcedimientos tablaProcedimientos;
    private ArrayList<String> listaProcedimientos; //Funcionara como una pila
    private ArrayList<Instruccion3Direcciones> instrucciones;
    private int et;
    
    public Generador3Direcciones(TablaVariables tv, TablaProcedimientos tp){
        this.tablaVariables = tv;
        this.tablaProcedimientos = tp;
        this.listaProcedimientos = new ArrayList<>();
        this.instrucciones = new ArrayList<>();
        this.et = 0;
    }
    
    //Crecion de una etiqueta: Sintaxi etX donde X es el contador de etiquetas puestas
    public String nuevaEtiqueta(){
        this.et++;
        return "et"+ this.et;
    }
    
    //Le pasaremos si es una variable o un parametro, de que tipo, y si es un array o una tupla
    public int nuevaVariable(TipoReferencia tipoVariable, Tipo tipo, boolean isArray, boolean isTupla){
        int numeroVariable = this.tablaVariables.getContador();
        String nombre = "var" + numeroVariable;
        VData data = new VData(nombre, tipoVariable, tipo, funcionActual(), isArray, isTupla);
        this.tablaVariables.put(data);
        return numeroVariable;
    }
    
    //Devuelve la función que esta en el topo de la pila
    public String funcionActual(){
        return this.listaProcedimientos.isEmpty() ? null : this.listaProcedimientos.get(listaProcedimientos.size() - 1);
    }
    
    //Eliminar funcion
    public String eliminarFuncion(){
        String nombre = this.listaProcedimientos.get(listaProcedimientos.size() -1);
        
        //Eliminamos el elemento
        this.listaProcedimientos.remove(listaProcedimientos.size() - 1);
        return nombre;
    }
    
    public void añadirFuncion(String id){
        this.listaProcedimientos.add(id);
    }
    
    public int nuevoProcedimiento(String id, int profundidad, String etiqueta){
        int contador = this.tablaProcedimientos.getContador();
        PData data = new PData(profundida, etiqueta, -1, -1);
        this.tablaProcedimientos.put(data);
        return contador;
    }
    
    public PData getProcedimeinto(String id){
        return this.tablaProcedimientos.get(id);
    }
    
    public void generarInstruccion(String instruccion, Operador op1, Operador op2, Operador dst){
        Instruccion3Direcciones ins = new Instruccion3Direcciones(instruccion, op1, op2, dst);
        this.instrucciones.add(ins);
    }
    
    public ArrayList<Instruccion3Direcciones> instrucciones(){
        return this.instrucciones;
    }
    
}
