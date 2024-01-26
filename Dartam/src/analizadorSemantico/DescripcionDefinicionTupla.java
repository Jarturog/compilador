package analizadorSemantico;

import java.util.HashMap;

public class DescripcionDefinicionTupla extends DescripcionSimbolo {
    /**
     * Tupla
     */
    public DescripcionSimbolo(String nombre, HashMap<String, DescripcionSimbolo> m) {
        miembros = (HashMap<String, DescripcionSimbolo>) m;
        tipo = nombre;
    }
}
