package analizadorSemantico;

import java.util.ArrayList;

public class DescripcionDefinicionTupla extends DescripcionSimbolo {

    protected final ArrayList<DefinicionMiembro> miembros;

    /**
     * Tupla
     */
    public DescripcionDefinicionTupla(String nombre, ArrayList<DefinicionMiembro> m) {
        super(nombre, false, false, null, null);
        miembros = m;
    }

    public void anyadirMiembro(DefinicionMiembro dm) {
        miembros.add(dm);
    }

    public static class DefinicionMiembro {

        protected final String nombre, tipo;
        protected final boolean isConst, valorAsignado;
        protected final DescripcionDefinicionTupla tipoTupla;

        public DefinicionMiembro(String nombre, String tipo, boolean isConst, boolean valorAsignado, DescripcionDefinicionTupla tipoTupla) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.isConst = isConst;
            this.valorAsignado = valorAsignado;
            this.tipoTupla = tipoTupla;
        }

        @Override
        public String toString() {
            return isConst ? "constante " : "" + tipo + " " + nombre;
        }

    }

    @Override
    public String toString() {
        String m = "";
        for (DefinicionMiembro miembro : miembros) {
            m += miembro + " ";
        }
        m = m.length() > 0 ? "con miembros" + m.substring(0, m.length() - 1) : "sin miembros";
        return "Tupla " + m + " declarado en el nivel " + nivel;
    }
}
