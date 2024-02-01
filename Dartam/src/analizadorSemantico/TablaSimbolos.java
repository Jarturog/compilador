/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSemantico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TablaSimbolos {

    private int n; //Nivel actual
    private HashMap<String, DescripcionSimbolo> td; //Nuestra tabla de simbolos
    private ArrayList<Integer> ta; //Tabla de ambitos
    private HashMap<Integer, Entrada> te; //Table de expansion

    public TablaSimbolos() {
        vaciar();
    }

    //Clase entrada usado para la tabla de expansión!
    public class Entrada {

        /**
         * Nombre
         */
        public final String id;

        protected String idcamp; //Identificador

        /**
         * Descripción
         */
        private final DescripcionSimbolo d;
        protected int dAntigua, next;
        /**
         * Nivel
         */
        protected int np;

        public Entrada(String n, DescripcionSimbolo ds, int np) {
            this.id = n;
            this.d = ds;
            this.np = np;
        }

        public Entrada(String n, int np, DescripcionSimbolo ds) {
            this.id = n;
            this.d = ds;
            this.np = np;
        }

        @Override
        public String toString() {
            return "Variable: '" + id + "'\t Descripción: " + d + "\n";
        }

        public int getNivel() {
            return np;
        }
    }

    /**
     * Vaciamos todas las tablas y ponemos a n = 1
     */
    public final void vaciar() {
        this.n = 0; //Nivel actual
        this.td = new HashMap();
        this.ta = new ArrayList();
        this.te = new HashMap();

        ta.add(n, 0);
        this.n++;
        ta.add(n, 0);
    }

    /**
     * Ponemos un elemento dentro de la tabla de simbolos
     */
    public DescripcionSimbolo poner(String id, DescripcionSimbolo d) throws Exception {
        //Comprobamos si existe dentro de la tabla de descriptores

        DescripcionSimbolo sd = td.get(id); // = consulta(id);

        if (sd != null) { //Existe actualmente
            if (sd.getNivel() == this.n) { //Si ya hay uno al mismo nivel error
                throw new Exception("Ya existe una entrada con el mismo nombre en el mismo nivel");
            }

            //Si no estan declaradas al mismo nivel
            int indice = ta.get(n) + 1;
            if (n == ta.size()) {
                ta.add(n, indice);
            } else {
                ta.set(n, indice);
            }
            te.put(indice, new Entrada(id, sd.getNivel(), sd)); //Ya que existia de antes, pero ahora a otro nivel

        }

        //Ya sea si no existia el simbolo, como si ha sido movido el actual de td
        //Actualizamos la tabla de descriciones
        d.setNivel(this.n);
        td.put(id, d);
        return sd;
    }
    
    public DescripcionSimbolo sustituir(String id, DescripcionSimbolo d) throws Exception{
        DescripcionSimbolo ds = td.remove(id);
        if (ds == null) {
            throw new Exception("Se ha intentado sustituir una entrada de la tabla de símbolos que no existe");
        }
        return poner(id, d);
    }

    /**
     * Entramos a un nuevo bloque de codigo EJ: int a = 0; -> if(){...}
     * ----------------- int a = 0; if(){ -> }
     */
    public void entraBloque() throws Exception {
        n++; //Actualizamos el nivel
        //ta valdrá lo mismo que la entrada anterior
        //A medida que se añadan simbolos este valor ta variara
        if (n == ta.size()) {
            ta.add(n, ta.get(n - 1));
        } else if (n < ta.size()) {
            ta.set(n, ta.get(n - 1));
        } else {
            throw new Exception("Error grave del compilador, contacta con los desarrolladores por favor\n");
        }
    }

    public void salirBloque() throws Exception {
        if (this.n <= 0) { //Error grave del compilador
            throw new Exception("Error grave del compilador, contacta con los desarrolladores por favor\n");
        }
        int lini = ta.get(this.n);
        //ta.remove(this.n); //Esto revisarlo
        n--;
        int lfi = ta.get(this.n);

        //Pasamos todas las declaraciones anteriores a la td
        for (int i = lini; i > lfi; i--) { // <= ?
            Entrada entrada = te.remove(i);//te.get(i);
            DescripcionSimbolo ds = td.put(entrada.id, entrada.d);
            ds.setNivel(entrada.getNivel()); // sobra -------------------------------------
        }
        //Vaciamos entradas del nivel del bloque del que salimos
        for (Iterator<Map.Entry<String, DescripcionSimbolo>> it = td.entrySet().iterator(); it.hasNext();) {
            HashMap.Entry<String, DescripcionSimbolo> e = it.next();
            if (e.getValue().getNivel() > n) { //Si son de un nivel de profundidad superior, se quita
                it.remove();//td.remove(e.getKey());
            }
        }
    }

    //idr es la tupla
    //idc es el campo de la tupla
    public void ponerCampo(String idr, String idc, DescripcionSimbolo dCamp) throws Exception {
//        DescripcionSimbolo d = td.get(idr);
//        if (!d.isTipoTupla()) {
//            throw new Exception("Error, no es una tupla!");
//        }
//        int i = d.first;
//
//        //Buscamos dentro una variable con el mismo nombre dentro de la tupla
//        while (i != 0 && !te.get(i).id.equals(idc)) {
//            i = te.get(i).d.next;
//        }
//
//        //Si hemos salido o porque no hay mas variables, o hemos encontrado una con el mismo nombre
//        if (i != 0) {
//            throw new Exception("Ya hay un campo con el mismo identificador");
//        }
//
//        int idxe = ta.get(this.n) + 1;
//        ta.set(this.n, idxe); //Actualizamos tabla de ambitos porque añadimos un nuevo parametro
//
//        Entrada e = new Entrada(idc, dCamp, n); // Nueva entrada
//        e.d.setNivel(-1); //No se copiará al hacer el salir bloque, es unicamente un indicador
//        e.d.next = td.get(idr).first;
//        e.d.first = idxe;
//        td.get(idr).next = idxe; //Referenciamos al añadido para crear una lista
//        te.put(idxe, e); //Ahora la añadimos a la tabla de expansion 
    }
//
//    /*
//        Vamos a buscar mediante el identificadar de la tupla
//        el una entrada de esta misma. Devolvera la entrada
//        que contiene tanto nombre + DescripcionSimbolo
//     */
//    public Entrada consultaCamp(String idr, String idc) throws Exception {
//        DescripcionSimbolo d = td.get(idr); //Buscamos la descripcion de la tupla
//        if (!d.isTipoTupla()) {
//            throw new Exception("No es una tupla!");
//        }
//
//        int i = d.first;
//        while (i != 0 && !te.get(i).id.equals(idc)) {
//            i = te.get(i).d.next;
//        }
//
//        if (i != 0) {
//            return te.get(i);
//        } else {
//            return null;
//        }
//
//    }
//
//    /*
//      Vamos a meter un elemento de una tabla dentro de la tabla de simbolos
//      El id hace referencia al id del array, y descripcionSimbolo contendra lo necesirio
//        para identificar al propio dato a insertar
//     */
//    public void ponerIndice(String id, DescripcionSimbolo d) throws Exception {
//        DescripcionSimbolo da = td.get(id);
//        if (!da.isArray()) {
//            throw new Exception("No es un array");
//        }
//
//        int idxe = da.first;
//        int idxep = 0;
//
//        while (idxe != 0) {
//            idxep = idxe;
//            idxe = te.get(idxe).next;
//        }
//
//        idxe = ta.get(this.n) + 1;
//        ta.set(n, idxe); //Elemento nuevo dentro de la tabla de expansion, actualizamos contador
//
//        Entrada ent = new Entrada("", d, n); //No tienen nombre las entradas de valores de indices
//
//        ent.idcamp = ""; //No tiene identificador ya que será un valor unicamente o referencia a un valor
//        ent.np = -1;    //No se copiará en la tabla de descriptores al salir del bloque
//        ent.next = 0;   //Si es el primer elemento no tendrá siguiente
//
//        if (idxep == 0) { //Si es el primer indice
//            td.get(id).first = idxe;
//        } else {  //En el caso de que haya mas indices, es decir mas elementeos lo actualizamos
//            te.get(idxep).next = idxe; //Actualizamos el anterior para que apunte al nuevo
//        }
//        te.put(idxe, ent); //Finalmente añadimos la entrada a la tabla de expansion
//    }
//
//    /*
//        Método auxiliar por si necesitamos saber si es el primer indice
//     */
//    private Integer first(String id) throws Exception {
//        DescripcionSimbolo sd = td.get(id);
//        if (!sd.isArray()) {
//            throw new Exception("No es un array el elemento");
//        }
//        return sd.first;
//    }

//    /*
//        Método auxiliar para saber el siguiente indice
//     */
//    private Integer next(int idx) throws Exception {
//        int ent = te.get(idx).next;
//        if (ent == 0) {
//            throw new Exception("Error al conseguir la sigueinte dimension");
//        }
//        return ent;
//    }
//
//    /*
//        Metodo auxiliar para saber si es el ultimo indice
//     */
//    private boolean last(int idx) {
//        return te.get(idx).next == 0;
//    }
//    /*
//        Metodo para consultar una indice dentro de un array, en teoría mandara
//        la descripcion de dicho elemento
//     */
//    public DescripcionSimbolo consulta(int idx) {
//        return te.get(idx).descripcion;
//    }
    //Consulatamos una variable ya incorporada
    public DescripcionSimbolo consulta(String id) {
        return td.get(id);
    }

    /**
     * Comprueba si ya ha estado declarado y es visible o si el id corresponde a una función o tupla
     * @param id
     * @return 
     */
    public String sePuedeDeclarar(String id) {
        DescripcionSimbolo ds = td.get(id);
        if (ds == null) {
            return "";
        }
        if (ds.isDefinicionTupla() || ds.isFunction()) {
            return "El identificador '" + id + "' ha intentado hacer override de una función o de una tupla";
        }
        if (ds.nivel != n) {
            return "";
        }
        return "El identificador '" + id + "' ya ha sido declarado con anterioridad";
    }

    /*
        Metodo para poner parametros de una función dentro del la tabla de simbolors
        idpr es el nombre de la funcion, idparam el nombre del parametro
        y necesitamos la descripcion de dicho parameto
     */
//    public void posaparam(String idpr, String idparam, DescripcionSimbolo d) throws Exception {
//        DescripcionSimbolo des = td.get(idpr);
//        if (!des.isFunction()) {
//            throw new Exception("Error, no es una funcion");
//        }
//
//        int idxe = td.get(idpr).first;
//        int idxep = 0;
//        while (idxe != 0 && te.get(idxe).id.equals(idparam)) {
//            idxep = idxe;
//            idxe = te.get(idxe).next;
//        }
//
//        if (idxe != 0) {
//            throw new Exception("Error, ya hay un parametro con el mismo nombre");
//        }
//
//        idxe = ta.get(this.n) + 1;
//        ta.set(this.n, idxe); //Hemos actualizado al tabla de expansion con una nueva entrada
//
//        Entrada ent = new Entrada(idparam, d, n);
//        ent.idcamp = idparam;
//        ent.np = -1;    //No se copiará en la tabla de descriptores al salir del bloque
//        ent.next = 0;
//
//        if (idxep == 0) {
//            td.get(idpr).first = idxe;
//        } else {
//            te.get(idxep).next = idxe; //Actualizamos el anterior para que apunte a este nuevo
//        }
//
//        te.put(idxe, ent);
//
//    }

    @Override
    public String toString() {
        int nChars = 13;
        String s = "";//"Tabla de símbolos:\n";
        for (HashMap.Entry<String, DescripcionSimbolo> e : td.entrySet()) {
            s += e.getKey() + ":" + calcularTabuladores(nChars + 1, e.getKey()) + e.getValue() + "\n";
        }
        //s += "\nTabla de ámbitos:" + ta.toString() + "\n\nTabla de expansión:" + te.toString() + "\n";
        return s;
    }

    private static String calcularTabuladores(int numChars, String s) {
        int charsPorTab = 4;
        int tabs = Math.max(0, (numChars - s.length()) / charsPorTab);
        StringBuilder tabuladores = new StringBuilder();
        for (int i = 0; i < tabs; i++) {
            tabuladores.append('\t');
        }
        return tabuladores.toString();
    }

    public int getProfundidad() {
        return this.n;
    }

}
