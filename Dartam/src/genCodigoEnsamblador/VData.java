package genCodigoEnsamblador;

import analizadorSemantico.DescripcionDefinicionTupla;
import analizadorSemantico.genCodigoIntermedio.Tipo;
import analizadorSemantico.genCodigoIntermedio.Tipo.TipoReferencia;
import java.util.ArrayList;
import jflex.base.Pair;

public class VData {

    private Tipo tipo;
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

    public VData(Tipo t) throws Exception {
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
        return bytesEstructura;
    }

    public void sustituirPor(VData d) {
        tipo = d.tipo;
        initCodigoIntermedio = d.initCodigoIntermedio;
        initCodigoEnsamblador = d.initCodigoEnsamblador;
        bytesEstructura = d.bytesEstructura;
        tupla = d.tupla;
    }

    public Tipo tipo() {
        return tipo;
    }
}
