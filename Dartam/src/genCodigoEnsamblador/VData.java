package genCodigoEnsamblador;

import analizadorSemantico.genCodigoIntermedio.Tipo;
import analizadorSemantico.genCodigoIntermedio.Tipo.TipoReferencia;
import java.util.ArrayList;
import jflex.base.Pair;

public class VData {

    private int indiceTablaVariables;
//    private final TipoReferencia tipoVariable;
    public final Tipo tipo;
    private Object initCodigoIntermedio = null;
    private boolean initCodigoEnsamblador = false;
    private String idProcedimiento;
    private int bytes;
    private boolean isArray, isTupla;
    private int offset;
    private ArrayList<Pair<Integer, Tipo>> parametrosTupla; //Elementos de la tupla

    public VData(Tipo t) {
        tipo = t;
        // this.inicializacion = init;
//        this.idProcedimiento = idp;
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

//    public int getIndice(){
//        return this.indiceTablaVariables;
//    }
//
//    public TipoReferencia getTipoVariable() {
//        return tipoVariable;
//    }
//
//    public String getIdProcedimiento() {
//        return idProcedimiento;
//    }
//
//    public int getBytes() {
//        return bytes;
//    }
//    
//    public void setBytes(int b){
//        this.bytes = b;
//    }
//    public boolean isIsArray() {
//        return isArray;
//    }
//
//    public boolean isIsTupla() {
//        return isTupla;
//    }
//    public int getOffset() {
//        return offset;
//    }
//    
//    public void actualizarOffset(int o){
//        this.offset = o;
//    }
//    
//    //Metodo para añadir elementos a una tupla
//    public void añadirElementoTupla(Pair<Integer, Tipo> e){
//        this.parametrosTupla.add(e);
//    }

    

}
