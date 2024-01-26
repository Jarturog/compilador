/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package genCodigoIntermedio;

import java.util.ArrayList;
import jflex.base.Pair;


/**
 *
 * @author dasad
 */
public class PData {
        private int profundidad; //Nivel de profundidad
        private String etiqueta;
        private int cantidadParametros;
        private ArrayList<Pair<Integer, Tipo>> parametros;
        
        public PData(int profundidad, String etiqueta, int totalBytes, int cantidadParametros){
            this.profundidad = profundidad;
            this.etiqueta = etiqueta;
            this.cantidadParametros = 0;
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
        public ArrayList<Pair<Integer, Tipo>> getParametros(){
            return this.parametros;
        }
        
        //Metodo que nos devolvera la cantidad de parametros que tiene
        public int getCantidadParametros(){
            return this.cantidadParametros;
        }
        
        //Metodo que permite añadir un nuevo parametro al procedimiento
        public void añadirParametro(int i, Tipo t){
            Pair<Integer, Tipo> param = new Pair(i,t);
            this.parametros.add(param);
            this.cantidadParametros++;
        }
        
        //Metodo con el que podemos modificar la cantidad de parametros
        //public void setCantidadParametros(int i){
        //    this.cantidadParametros = i;
        //}
}
