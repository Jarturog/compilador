/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico.genCodigoIntermedio;

import java.util.ArrayList;
import jflex.base.Pair;


/**
 *
 * 
 */
public class PData {
        private final int profundidad; //Nivel de profundidad
        private final String etiqueta;
        private final ArrayList<Pair<String, String>> parametros;
        
        public PData(int profundidad, String etiqueta, int totalBytes, int cantidadParametros){
            this.profundidad = profundidad;
            this.etiqueta = etiqueta;
            this.parametros = new ArrayList<>();
        }
        
        //Recibimos la profundidad del procedimiento
        public int getProfundidad(){
            return this.profundidad;
        }
        
        //Recibimos la etiqueta del procedimientos
        public String getEtiqueta(){
            return this.etiqueta;
        }
        
        //Metodo con el que podemos recuperar los parametros del procedimiento
        public ArrayList<Pair<String, String>> getParametros(){
            return parametros;
        }
        
        //Metodo que nos devolvera la cantidad de parametros que tiene
        public int getCantidadParametros(){
            return parametros.size();
        }
        
        //Metodo que permite añadir un nuevo parametro al procedimiento
        public void añadirParametro(String i, String t){
            Pair<String, String> param = new Pair(i,t);
            this.parametros.add(param);
        }
        
        //Metodo con el que podemos modificar la cantidad de parametros
        //public void setCantidadParametros(int i){
        //    this.cantidadParametros = i;
        //}
}
