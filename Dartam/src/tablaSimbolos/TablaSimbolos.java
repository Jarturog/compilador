/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tablaSimbolos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author dasad
 */
public class TablaSimbolos {
    private HashMap<String, SimboloDescripcion> td; //Nuestra tabla de simbolos
    private int n; //Nivel actual
    private ArrayList<Integer> ta; //Tabla de ambitos
    private ArrayList<Entrada>  te;
    
    public TablaSimbolos(){
        n = 0;
        td = new HashMap();
        ta = new ArrayList();
        te = new ArrayList();
        ta.add(n, 0);
    }
    //Clase entrada usado para la tabla de expansión!
    public class Entrada{
            public String nombreVariable;
            public SimboloDescripcion descripcion;
            public Entrada(String n, SimboloDescripcion d){
                this.nombreVariable = n;
                this.descripcion = d;
            }
    }
    
    /**
     * Vaciamos las tablas
     */
    public void vaciar(){
        this.n = 0;
        this.td = new HashMap();
        this.ta = new ArrayList();
        this.te = new ArrayList();
        //ta.add(n, 0);
        //this.n += 1;
        //ta.add(n,0);
    }
    
    public void poner(String id, SimboloDescripcion d) throws Exception{
        //Comprobamos si existe dentro de la tabla de descriptores
        SimboloDescripcion sd = td.get(id);
        if(sd != null){ //Existe actualmente
            if(sd.getNivel() == n){ //Error
                throw new Exception("Error!"); //Cambiar luego
            }
            //Si no estan declaradas al mismo nivel
            int indice = ta.get(n) + 1;
            ta.set(n,indice);
            te.add(new Entrada(id, sd)); //Ya que existia de antes, pero ahora a otro nivel
        }
        d.setNivel(n);
        td.put(id, d); //Actualizamos la tabla de descriciones
    }
    
    public void entraBloque(){
        this.n += 1;
        int valor = ta.get(n-1);
        ta.add(valor);
        
    }
    
    public void salirBloque() throws Exception{
       if(this.n == 0){ //Error grave del compilador
           throw new Exception("Error grave del compilador"); //Cambiar mas adelante
       }else{
           int lini = ta.get(this.n);
           ta.remove(this.n); //Esto revisarlo
           this.n -= 1;
           int lfi = ta.get(this.n);
           
           //Recorremos la tabla de expansión y replazamos dentro de la tabla de descripciones
           for(Entrada entrada: te.subList(lini, lini)){
               td.replace(entrada.nombreVariable, entrada.descripcion);
           }
           te.subList(lini, lini).clear(); //Las eleminimos ya que las metimos dentro de la td
           
           Iterator<HashMap.Entry<String, SimboloDescripcion>> iterador = td.entrySet().iterator();
           while(iterador.hasNext()){
               if(iterador.next().getValue().getNivel() > n){
                   iterador.remove();
               }
           }
       }
      
    }
    
    
    
}
