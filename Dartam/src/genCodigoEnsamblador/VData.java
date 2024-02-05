package genCodigoEnsamblador;

import analizadorSemantico.genCodigoIntermedio.Tipo;
import analizadorSemantico.genCodigoIntermedio.Tipo.TipoReferencia;
import java.util.ArrayList;
import jflex.base.Pair;

public class VData {

    private Tipo tipo;
    private Object initCodigoIntermedio = null;
    private boolean initCodigoEnsamblador = false;
    private Integer bytesEstructura = null;
    private ArrayList<Pair<Integer, Tipo>> parametrosTupla; //Elementos de la tupla

    public VData(Tipo t) throws Exception {
        tipo = t;
        if(t == null) {
            throw new Exception("No puede haber una variable sin tipo");
        }
        this.parametrosTupla = new ArrayList<>();
    }

    public boolean estaInicializadaEnCodigoIntermedio() {
        return initCodigoIntermedio != null;
    }

    public boolean estaInicializadaEnCodigoEnsamblador() {
        return initCodigoEnsamblador;
    }

    public void inicializar(Object o) {
        initCodigoIntermedio = o;
    }

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
        parametrosTupla = d.parametrosTupla;
    }

    public Tipo tipo() {
        return tipo;
    }
}
