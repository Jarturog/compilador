package analizadorSemantico;

import analizadorSemantico.genCodigoIntermedio.Tipo;
import java.util.ArrayList;

public class DescripcionDefinicionTupla extends DescripcionSimbolo {

    protected final ArrayList<DefinicionMiembro> miembros;
    private Integer bytes = 0;
    /**
     * Tupla
     */
    public DescripcionDefinicionTupla(String nombre, ArrayList<DefinicionMiembro> m, String var) {
        super(nombre, false, false, null, var);
        miembros = m;
        for (DefinicionMiembro miem : miembros) {
            bytes += miem.bytes;
        }
    }

    public void anyadirMiembro(DefinicionMiembro dm) {
        miembros.add(dm);
        bytes += dm.bytes;
    }
    
    public DefinicionMiembro getMiembro(String nombre) {
        for (DefinicionMiembro m : miembros) {
            if (nombre.equals(m.nombre)) {
                return m;
            }
        }
        return null;
    }

    public boolean tieneMiembro(String nombre) {
        return getMiembro(nombre) != null;
    }
    
    public Integer getDesplazamiento(String miembro) {
        if (!tieneMiembro(miembro)) {
            return null;
        }
        int desp = 0;
        for (DefinicionMiembro m : miembros) {
            if (miembro.equals(m.nombre)) {
                return desp;
            }
            desp += m.bytes;
        }
        return null; // error
    }

    public static class DefinicionMiembro {

        private Integer bytes = null;
        protected final String nombre, tipo;
        protected final boolean isConst, valorAsignado;
        protected final DescripcionDefinicionTupla tipoTupla;

        public DefinicionMiembro(String nombre, String tipo, boolean isConst, boolean valorAsignado, DescripcionDefinicionTupla tipoTupla) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.isConst = isConst;
            this.valorAsignado = valorAsignado;
            this.tipoTupla = tipoTupla;
            bytes = Tipo.getTipo(tipo).bytes;
        }

        @Override
        public String toString() {
            return isConst ? "constante " : "" + tipo + " " + nombre;
        }
        
        public Integer getBytes() {
            return bytes;
        }
        
        public void setBytes(Integer b) {
            bytes = b;
        }

    }
    
    @Override
    public String getNombreTupla() throws Exception {
        return super.tipo;
    }

    @Override
    public String toString() {
        String m = "";
        for (DefinicionMiembro miembro : miembros) {
            m += miembro + " ";
        }
        m = m.length() > 0 ? "con miembros " + m.substring(0, m.length() - 1) : "sin miembros";
        return "Tupla " + m + " declarado en el nivel " + nivel;
    }
}
