package genCodigoEnsamblador;

import analizadorSemantico.DescripcionDefinicionTupla;
import analizadorSemantico.genCodigoIntermedio.TipoVariable;

public class VData {

    private TipoVariable tipo;
    private Object initCodigoIntermedio = null;
    private boolean initCodigoEnsamblador = false;
    private Integer bytesEstructura = null;
    private DescripcionDefinicionTupla tupla = null;

    public VData(VData v) throws Exception {
        tipo = v.tipo;
        initCodigoIntermedio = v.initCodigoIntermedio;
        initCodigoEnsamblador = v.initCodigoEnsamblador;
        bytesEstructura = v.bytesEstructura;
        tupla = v.tupla;
    }

    public VData(TipoVariable t) throws Exception {
        tipo = t;
        if (t == null) {
            throw new Exception("No puede haber una variable sin tipo");
        }
    }

    public void setTupla(DescripcionDefinicionTupla t) {
        tupla = t;
    }

    public DescripcionDefinicionTupla getTupla() {
        return tupla;
    }

    public boolean estaInicializadaEnCodigoIntermedio() {
        return initCodigoIntermedio != null;
    }

    public boolean estaInicializadaEnCodigoEnsamblador() {
        return initCodigoEnsamblador;
    }

    /**
     * Inicializar en código intermedio
     */
    public void inicializar(Object o) {
        initCodigoIntermedio = o;
    }

    /**
     * Inicializar en ensamblador
     */
    public void inicializar() {
        initCodigoEnsamblador = true;
    }

    public void setBytesEstructura(int n) {
        bytesEstructura = n;
    }

    public Integer getBytesEstructura() {
        if(tipo.equals(TipoVariable.STRING)) {
            return TipoVariable.STRING.bytes;
        }
        return bytesEstructura;
    }

    public void sustituirPor(VData d) {
        tipo = d.tipo;
        initCodigoIntermedio = d.initCodigoIntermedio;
        initCodigoEnsamblador = d.initCodigoEnsamblador;
        bytesEstructura = d.bytesEstructura;
        tupla = d.tupla;
    }

    public TipoVariable tipo() {
        return tipo;
    }
    
    @Override
    public String toString() {
        String init = "", bytes = "", tupl = "";
        if (initCodigoIntermedio != null) {
            init = " inicializado a " + initCodigoIntermedio;
        }
        if (bytesEstructura != null) {
            bytes = " almacenando una estructura de datos de " + bytesEstructura + " bytes";
        }
        if (tupla != null) {
            tupl = " de tipo " + tupla.tipo;
        }
        return "tipo " + tipo + init + bytes + tupl;
    }
}
