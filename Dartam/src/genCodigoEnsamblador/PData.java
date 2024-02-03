/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package genCodigoEnsamblador;

import java.util.ArrayList;
import jflex.base.Pair;


public class PData {
//        private final int profundidad; //Nivel de profundidad
        private final String etiqueta;
        private final ArrayList<Pair<String, String>> parametros;
        private final int bytesRetorno;
        
        public PData(String etiqueta, ArrayList<Pair<String, String>> params, int bytesValorRetorno){
            this.etiqueta = etiqueta;
            this.parametros = params;
            this.bytesRetorno = bytesValorRetorno;
        }
        
        //Recibimos la profundidad del procedimiento
//        public int getProfundidad(){
//            return this.profundidad;
//        }
        
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
        
        public int getBytesRetorno() {
            return bytesRetorno;
        }
}
