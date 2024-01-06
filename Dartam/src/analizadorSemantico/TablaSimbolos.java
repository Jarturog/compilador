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
    private int n; //Nivel actual
    private HashMap<String, DescripcionSimbolo> td; //Nuestra tabla de simbolos
    private ArrayList<Integer> ta; //Tabla de ambitos
    private ArrayList<Entrada>  te; //Table de expansion
    
    public TablaSimbolos(){
        n = 0;
        td = new HashMap();
        ta = new ArrayList();
        te = new ArrayList();
        ta.add(n, 0);
    }
    
    //Clase entrada usado para la tabla de expansión!
    public class Entrada{

            public String nombreVariable, idcamp; //Identificador
            public DescripcionSimbolo descripcion;
            public int d, next, np;
            public Entrada(String n, DescripcionSimbolo d, int np){
                this.nombreVariable = n;
                this.descripcion = d;
                this.np = np;
            }
    }
    
    /**
     * Vaciamos todas las tablas y ponemos a n = 1
     */
//    public void vaciar(){
//        this.n = 0; //Nivel actual
//        this.td = new HashMap();
//        this.ta = new ArrayList();
//        this.te = new ArrayList();
//        
//        //ta.add(n, 0);
//        //this.n += 1;
//        //ta.add(n,0);
//    }
    

    /**
     * Ponemos un elemento dentro de la tabla de simbolos 
     */
    public void poner(String id, DescripcionSimbolo d) {
        //Comprobamos si existe dentro de la tabla de descriptores
        
        DescripcionSimbolo sd = td.get(id);
        
        if(sd != null){ //Existe actualmente
            if(sd.getNivel() == this.n){ //Si ya hay uno al mismo nivel error
                //throw new Exception("Ya existe una entrada con el mismo nombre en el mismo nivel");
            }
            
            //Si no estan declaradas al mismo nivel
            int indice = ta.get(this.n) + 1;
            ta.set(n,indice);
            te.add(new Entrada(id, sd, n)); //Ya que existia de antes, pero ahora a otro nivel
        }
        
        //Ya sea si no existia el simbolo, como si ha sido movido el actual de td
        //Actualizamos la tabla de descriciones
        d.setNivel(this.n);
        td.put(id, d);
    }
    
    /**
     * Entramos a un nuevo bloque de codigo 
     * EJ:
     *      int a = 0;
     *      ->
     *      if(){...}
     * -----------------
     *      int a = 0;
     *      if(){ 
     *          ->
     *      }
     */
    public void entraBloque(){
        this.n += 1; //Actualizamos el nivel
        int valor = ta.get(n-1);
        
        //ta valdrá lo mismo que la entrada anterior
        //A medida que se añadan simbolos este valor ta variara
        ta.add(valor);
    }
    
    public void salirBloque() throws Exception {

        if(this.n == 0){ //Error grave del compilador
            throw new Exception("Error grave del compilador, contacta con los desarrolladores por favor"); //Cambiar mas adelante
        }
        int lini = ta.get(this.n);
        ta.remove(this.n); //Esto revisarlo
        this.n -= 1;
        int lfi = ta.get(this.n);
        
        //Pasamos todas las declaraciones anteriores a la td
        for(Entrada entrada: te.subList(lfi, lini)){
            if(entrada.descripcion.getNivel() != -1){ //Si es -1, es una entrada que no se mete en la tabla de descriptores
                td.replace(entrada.nombreVariable, entrada.descripcion);
            }
        }
        te.subList(lini, lini).clear(); //Las eleminimos ya que las metimos dentro de la td
        
        //Vaciamos entradas del nivel del bloque del que salimos
        Iterator<HashMap.Entry<String, DescripcionSimbolo>> iterador = td.entrySet().iterator();
        while(iterador.hasNext()){
            //Si son de un nivel de profundidad superior, se quita
            if(iterador.next().getValue().getNivel() > this.n){
                iterador.remove();
            }
        }


    }
    
    //idr es la tupla
    //idc es el campo de la tupla
//    public void ponerCampo(String idr, String idc, DescripcionSimbolo dCamp) throws Exception {
//        DescripcionSimbolo d = td.get(idr);
//        if(!d.isTupla()){
//            throw new Exception("Error, no es una tupla!");
//        }
//        int i = d.first;
//        
//        //Buscamos dentro una variable con el mismo nombre dentro de la tupla
//        while(i != 0 && !te.get(i).nombreVariable.equals(idc)){
//            i = te.get(i).descripcion.next;
//        }
//        
//        //Si hemos salido o porque no hay mas variables, o hemos encontrado una con el mismo nombre
//        if(i != 0){
//            throw new Exception("Ya hay un campo con el mismo identificador");
//        }
//        
//        int idxe = ta.get(this.n) + 1;
//        ta.set(this.n, idxe); //Actualizamos tabla de ambitos porque añadimos un nuevo parametro
//        
//        Entrada e = new Entrada(idc, dCamp); // Nueva entrada
//        e.descripcion.setNivel(-1); //No se copiará al hacer el salir bloque, es unicamente un indicador
//        e.descripcion.next = td.get(idr).first;
//        e.descripcion.first = idxe;
//        td.get(idr).next = idxe; //Referenciamos al añadido para crear una lista
//        te.add(e); //Ahora la añadimos a la tabla de expansion
//    }
   
    /*
        Vamos a buscar mediante el identificadar de la tupla
        el una entrada de esta misma. Devolvera la entrada
        que contiene tanto nombre + DescripcionSimbolo
    */
//    public Entrada consultaCamp(String idr, String idc) throws Exception{
//        DescripcionSimbolo d = td.get(idr); //Buscamos la descripcion de la tupla
//        if (!d.isTupla()){
//            throw new Exception("No es una tupla!");
//        }
//        
//        int i = d.first;
//        while(i != 0 && !te.get(i).nombreVariable.equals(idc)){
//            i = te.get(i).descripcion.next;
//        }
//        
//        if(i != 0){
//            return te.get(i);
//        }else{
//            return null;
//        }
//        
//    }
    
   //Es parte del codigo del profesor, por si acaso se necesita
    //Igual esta puesto más arriba actualizado el salirBloque
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
    
    /*
      Vamos a meter un elemento de una tabla dentro de la tabla de simbolos
      El id hace referencia al id del array, y descripcionSimbolo contendra lo necesirio
        para identificar al propio dato a insertar
    */
//    public void ponerIndice(String id, DescripcionSimbolo d) {
//        DescripcionSimbolo da = td.get(id);
//        if(!da.isArray()){
//            //throw new Exception("No es un array");
//        }
//        
//        int idxe = da.first;
//        int idxep = 0;
//        
//        while(idxe != 0){
//            idxep = idxe;
//            idxe = te.get(idxe).next;
//        }
//        
//        idxe = ta.get(this.n) + 1; 
//        ta.set(n, idxe); //Elemento nuevo dentro de la tabla de expansion, actualizamos contador
//        
//        Entrada ent = new Entrada("", d); //No tienen nombre las entradas de valores de indices
//        
//        ent.idcamp = ""; //No tiene identificador ya que será un valor unicamente o referencia a un valor
//        ent.np = -1;    //No se copiará en la tabla de descriptores al salir del bloque
//        ent.next = 0;   //Si es el primer elemento no tendrá siguiente
//        
//        if(idxep == 0){ //Si es el primer indice
//            td.get(id).first = idxe;
//        }else{  //En el caso de que haya mas indices, es decir mas elementeos lo actualizamos
//            te.get(idxep).next = idxe; //Actualizamos el anterior para que apunte al nuevo
//        }
//        te.add(idxe, ent); //Finalmente añadimos la entrada a la tabla de expansion
//    }
    
    /*
        Método auxiliar por si necesitamos saber si es el primer indice
    */
//    private Integer first(String id) {//throws Exception{
//        DescripcionSimbolo sd = td.get(id);
//        if(!sd.isArray()){
//            return null;//throw new Exception("No es un array el elemento");
//        }
//        return sd.first;
//    }
    
    /*
        Método auxiliar para saber el siguiente indice
    */
//    private Integer next(int idx) {//throws Exception{
//        int ent = te.get(idx).next;
//        if(ent == 0){
//            return null;//throw new Exception("Error al conseguir la sigueinte dimension");
//        }
//        return ent;
//    }
    
    /*
        Metodo auxiliar para saber si es el ultimo indice
    */
//    private boolean last(int idx){
//        return te.get(idx).next == 0;
//    }
    
    /*
        Mñetodo para consultar una indice dentro de un array, en teoría mandara
        la descripcion de dicho elemento
    */
//    public DescripcionSimbolo consulta(int idx){
//        return te.get(idx).descripcion;
//    }
    
    //Consulatamos una variable ya incorporada
    public DescripcionSimbolo consulta(String s){
        DescripcionSimbolo e = td.get(s);
        if (e != null){ //Si es visible desde el bloque, estará aquí
            return e;
        }
        
        //No es necesario, ya que si esta oculto, usaremos la del bloque actual, que no este oculto significa
        //que en un nivel diferente no hay una variable con el mismo nombre, por lo que siempre estará en tabla de descriptores (visible) o no existira
        //En el caso de que no, recorrerá la tabla de expansión ()
        /*for (Entrada ent : te) {
            if (ent.nombreVariable.equals(s)) {
                return ent.descripcion;
            }
        }*/
        return null;
    }
    
    /*
        Metodo para poner parametros de una función dentro del la tabla de simbolors
        idpr es el nombre de la funcion, idparam el nombre del parametro
        y necesitamos la descripcion de dicho parameto
    */
//    public void posaparam(String idpr, String idparam, DescripcionSimbolo d) {
//        DescripcionSimbolo des = td.get(idpr);
//        if(!des.isFunction()){
//            //throw new Exception("Error, no es una funcion");
//        }
//        
//        int idxe = td.get(idpr).first;
//        int idxep = 0;
//        while(idxe != 0 && te.get(idxe).nombreVariable.equals(idparam)){
//            idxep = idxe;
//            idxe = te.get(idxe).next;
//        }
//        
//        if(idxe != 0){
//            //throw new Exception("Error, ya hay un parametro con el mismo nombre");
//        }
//        
//        idxe = ta.get(this.n) + 1;
//        ta.set(this.n, idxe); //Hemos actualizado al tabla de expansion con una nueva entrada
//        
//        Entrada ent = new Entrada(idparam, d);
//        ent.idcamp = idparam;
//        ent.np = -1;    //No se copiará en la tabla de descriptores al salir del bloque
//        ent.next = 0;
//        
//        if(idxep == 0){
//            td.get(idpr).first = idxe;
//        }else{
//            te.get(idxep).next = idxe; //Actualizamos el anterior para que apunte a este nuevo
//        }
//        
//        te.add(idxe, ent);
//    }

}