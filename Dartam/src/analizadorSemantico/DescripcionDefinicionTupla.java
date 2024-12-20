package analizadorSemantico;

import analizadorSemantico.genCodigoIntermedio.TipoVariable;
import java.util.ArrayList;

public class DescripcionDefinicionTupla extends DescripcionSimbolo {

    protected final ArrayList<DefinicionMiembro> miembros;
    private String etInit = null;

    /**
     * Tupla
     */
    public DescripcionDefinicionTupla(String nombre, ArrayList<DefinicionMiembro> m, String var) throws Exception {
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

    public Integer getDesplazamiento(String miembro) throws Exception {
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
        throw new Exception("Miembro " + miembro + " no encontrado"); // error
    }
    
//    @Override
//    public Integer getBytes() {
//        int b = 0;
//        for (DefinicionMiembro m : miembros) {
//            b += m.bytes;
//        }
//        return b;
//    }

    public ArrayList<DefinicionMiembro> getMiembros() {
        return miembros;
    }

    public static class DefinicionMiembro {

        private Integer bytes = null;
        public final String nombre, tipo;
        protected final boolean isConst;
        private boolean valorAsignado;
        protected final DescripcionDefinicionTupla tipoTupla;
        public final Object varInit;

        public DefinicionMiembro(String nombre, String tipo, boolean isConst, boolean valorAsignado, DescripcionDefinicionTupla tipoTupla, Object varInit) throws Exception {
            this.nombre = nombre;
            this.tipo = tipo;
            this.isConst = isConst;
            this.valorAsignado = valorAsignado;
            this.tipoTupla = tipoTupla;
            if (tipoTupla == null) {
                bytes = TipoVariable.getTipo(tipo, false).bytes; // false porque no pueden haber arrays o tuplas dentro de una tupla
            } else {
                bytes = tipoTupla.getBytes();
            }
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
        String varA = variableAsociada == null ? "" : " siendo su variable de CI " + variableAsociada;
        String bytes = getBytes() == null ? "" : " ocupando " + getBytes() + " bytes";
        return "Tupla" + m + " declarada en el nivel " + getNivel() + varA + bytes;
    }
    
    public void setEtInit(String et) {
        etInit = et;
    }
    
    public String getEtInit() {
        return etInit;
    }
}
