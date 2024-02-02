package analizadorSemantico;

import analizadorSemantico.genCodigoIntermedio.Tipo;
import java.util.ArrayList;

public class DescripcionDefinicionTupla extends DescripcionSimbolo {

    protected final ArrayList<DefinicionMiembro> miembros;
    /**
     * Tupla
     */
    public DescripcionDefinicionTupla(String nombre, ArrayList<DefinicionMiembro> m, String var) {
        super(nombre, false, false, null, var);
        miembros = m;
        int b = 0;
        for (DefinicionMiembro miem : miembros) {
            b += miem.bytes;
        }
        setBytes(b);
    }

    public void anyadirMiembro(DefinicionMiembro dm) {
        miembros.add(dm);
        setBytes(getBytes() + dm.bytes);
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
        protected final boolean isConst;
        private boolean valorAsignado;
        protected final DescripcionDefinicionTupla tipoTupla;
        protected final String varInit;

        public DefinicionMiembro(String nombre, String tipo, boolean isConst, boolean valorAsignado, DescripcionDefinicionTupla tipoTupla, String varInit) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.isConst = isConst;
            this.valorAsignado = valorAsignado;
            this.tipoTupla = tipoTupla;
            bytes = Tipo.getTipo(tipo).bytes;
            this.varInit = varInit;
        }

        @Override
        public String toString() {
            return isConst ? "constante " : "" + tipo + " " + nombre;
        }
        
        public Integer getBytes() {
            return bytes;
        }
        
        public boolean tieneValorAsignado() {
            return valorAsignado;
        }
        
        public void asignarValor() {
            valorAsignado = true;
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
        return "Tupla " + m + " declarado en el nivel " + getNivel();
    }
}
