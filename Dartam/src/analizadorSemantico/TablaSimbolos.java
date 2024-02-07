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
import jflex.base.Pair;

public class TablaSimbolos {

    private int n; //Nivel actual
    private HashMap<String, DescripcionSimbolo> td; //Nuestra tabla de simbolos
    private ArrayList<Integer> ta; //Tabla de ambitos
    private HashMap<Integer, Entrada> te; //Table de expansion
    private ArrayList<Pair<String, DescripcionSimbolo>> historialSimbolos; // para dejarlo en un fichero

    public TablaSimbolos() {
        vaciar();
    }

    //Clase entrada usado para la tabla de expansion!
    public class Entrada {

        /**
         * Nombre
         */
        public final String id;
        /**
         * Descripcion
         */
        private final DescripcionSimbolo d;
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
            return "Variable: '" + id + "'\t Descripcion: " + d + "\n";
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
        historialSimbolos = new ArrayList<>();
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
                throw new Exception("El identificador " + id + " se repite varias veces para la declaracion de elementos");
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
            throw new Exception("Se ha intentado sustituir una entrada de la tabla de simbolos que no existe");
        }
        return poner(id, d);
    }

    /**
     * Entramos a un nuevo bloque de codigo EJ: int a = 0; -> if(){...}
     * ----------------- int a = 0; if(){ -> }
     */
    public void entraBloque() throws Exception {
        n++; //Actualizamos el nivel
        //ta valdra lo mismo que la entrada anterior
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
                historialSimbolos.add(new Pair<>(e.getKey(), e.getValue()));
                it.remove();//td.remove(e.getKey());
            }
        }
    }

    //Consulatamos una variable ya incorporada
    public DescripcionSimbolo consulta(String id) {
        return td.get(id); 
    }

    /**
     * Comprueba si ya ha estado declarado y es visible o si el id corresponde a una funcion o tupla
     * @param id
     * @return 
     */
    public String sePuedeDeclarar(String id) {
        DescripcionSimbolo ds = td.get(id);
        if (ds == null) {
            return "";
        }
        if (ds.isDefinicionTupla() || ds.isFunction()) {
            return "El identificador '" + id + "' ha intentado hacer override de una funcion o de una tupla";
        }
        if (ds.getNivel() < n) { // !=
            return "";
        }
        return "El identificador '" + id + "' ya ha sido declarado con anterioridad";
    }

    @Override
    public String toString() {
        int nChars = 12;
        String s = "";//"Tabla de simbolos:\n";
        for (HashMap.Entry<String, DescripcionSimbolo> e : td.entrySet()) {
            s += e.getKey() + ":" + calcularTabuladores(nChars, e.getKey()) + e.getValue() + "\n";
        }
        for (Pair<String, DescripcionSimbolo> e : historialSimbolos) {
            s += e.fst + ":" + calcularTabuladores(nChars, e.fst) + e.snd + "\n";
        }
        //s += "\nTabla de ambitos:" + ta.toString() + "\n\nTabla de expansion:" + te.toString() + "\n";
        return s;
    }

    private static String calcularTabuladores(int numChars, String s) {
        int chars = Math.max(1, numChars - s.length());
        String res = "";
        for (int i = 0; i < chars; i++) {
            res += " ";
        }
        return res;
    }

    public int getProfundidad() {
        return this.n;
    }

}
