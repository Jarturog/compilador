/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author dasad
 */
public class TablaSimbolos {
    private HashMap<String, DescripcionSimbolo> td; //Nuestra tabla de simbolos
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
            public DescripcionSimbolo descripcion;
            public int next;
            public int np;
            public int d;
            public String idcamp;
            public Entrada(String n, DescripcionSimbolo d){
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
    
    public void poner(String id, DescripcionSimbolo d) {
        //Comprobamos si existe dentro de la tabla de descriptores
        DescripcionSimbolo sd = td.get(id);
        if(sd != null){ //Existe actualmente
            if(sd.getNivel() == n){ //Error
                //throw new Exception("Error!"); //Cambiar luego
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
           
           Iterator<HashMap.Entry<String, DescripcionSimbolo>> iterador = td.entrySet().iterator();
           while(iterador.hasNext()){
               if(iterador.next().getValue().getNivel() > n){
                   iterador.remove();
               }
           }
       }  
    }
    
    public void ponerCampo(String idr, String idc, int dCamp) throws Exception {
        DescripcionSimbolo d = td.get(idr);
        if(!d.isTupla()){
            throw new Exception("Error, no es una tupla!");
        }else{
            int i = d.first;
            while(i != 0 && !te.get(i).nombreVariable.equals(idc)){
                i = te.get(i).next;
            }
            
            if(i != 0){
                throw new Exception("Ya hay un campo con el mismo error");
            }
            
            int idxe = ta.get(this.n);
            idxe += 1;
            ta.set(this.n, idxe);
            te.get(idxe).np = -1;
            te.get(idxe).d = dCamp;
            te.get(idxe).next = td.get(idr).first;
            td.get(idr).first = idxe;
        }
        
    }
    
    public Entrada consultaCamp(String idr, String idc) throws Exception{
        DescripcionSimbolo d = td.get(idr);
        if (!d.isTupla()){
            throw new Exception("No es una tupla");
        }else{
            int i = d.first;
            while(i != 0 && !te.get(i).nombreVariable.equals(idc)){
                i = te.get(i).next;
            }
            
            if(i != 0){
                return te.get(i);
            }else{
                return null;
            }
        }
    }
    
    
/*    public void salirBloque(){
        int idxi = ta.get(this.n);
        idxi -= 1;
        int idxf = ta.get(this.n);
        while(idxi > idxf){
            if(te.get(idxi).np != -1){
                String id = te.get(idxi).nombreVariable;
                SimboloDescripcion sd = td.get(id);
                sd.setNivel(te.get(idxi).np);
                //Poner toda la descripcion en td?
                sd.first = te.get(idxi).next;
            }
            idxi = idxi - 1;
        }
            
    }*/
    
    public void ponerIndice(String id, DescripcionSimbolo d) throws Exception{
        DescripcionSimbolo da = td.get(id);
        if(!da.isArray()){
            throw new Exception("No es un array");
        }
        
        int idxe = da.first;
        int idxep = 0;
        while(idxe != 0){
            idxep = idxe;
            idxe = te.get(idxe).next;
        }
        
        idxe = ta.get(this.n);
        idxe += 1;
        ta.set(n, idxe);
        Entrada ent = te.get(idxe);
        ent.idcamp = "";
        ent.np = -1;
        ent.descripcion = d;
        if(idxep == 0){
            td.get(id).first = idxe;
        }else{
            te.get(idxep).next = idxe;
        }
    }
    
    
    public int first(String id) throws Exception{
        DescripcionSimbolo sd = td.get(id);
        if(!sd.isArray()){
            throw new Exception("No es un ");
        }
        return sd.first;
    }
    
    public int next(int idx) throws Exception{
        int ent = te.get(idx).next;
        if(ent == 0){
            throw new Exception("Error al conseguir la sigueinte dimension");
        }
        return ent;
    }
    
    public boolean last(int idx){
        return te.get(idx).next == 0;
    }
    
    
    public DescripcionSimbolo consulta(int idx){
        return te.get(idx).descripcion;
    }
    
    public void posaparam(String idpr, String idparam, DescripcionSimbolo d) throws Exception{
        DescripcionSimbolo des = td.get(idpr);
        if(!des.isFunction()){
            throw new Exception("Error, no es una funcion");
        }
        
        int idxe = td.get(idpr).first;
        int idxep = 0;
        while(idxe != 0 && te.get(idxe).nombreVariable.equals(idparam)){
            idxep = idxe;
            idxe = te.get(idxe).next;
        }
        
        if(idxe != 0){
            throw new Exception("Error, ya hay un parametro con el mismo nombre");
        }
        
        idxe = ta.get(this.n);
        idxe = idxe + 1;
        ta.set(this.n, idxe);
        Entrada ent = te.get(idxe);
        ent.idcamp = idparam;
        ent.np = -1;
        ent.descripcion = d;
        ent.next = 0;
        if(idxep == 0){
            td.get(idpr).first = idxe;
        }else{
            te.get(idxep).next = idxe;
        }
         
    }

    public DescripcionSimbolo getDescription(String variable) {
        return td.get(variable);
    }

    
}
