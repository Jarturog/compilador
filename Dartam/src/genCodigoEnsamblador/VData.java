package genCodigoEnsamblador;

import analizadorSemantico.genCodigoIntermedio.Tipo;
import analizadorSemantico.genCodigoIntermedio.Tipo.TipoReferencia;
import java.util.ArrayList;
import jflex.base.Pair;

public class VData {

    private int indiceTablaVariables;
    private final TipoReferencia tipoVariable;

    private String idProcedimiento;
    private int bytes;
    private boolean isArray, isTupla;
    private int offset;
    private ArrayList<Pair<Integer, Tipo>> parametrosTupla; //Elementos de la tupla

    public VData(TipoReferencia tv) {
        this.tipoVariable = tv;
//        this.idProcedimiento = idp;
        this.parametrosTupla = new ArrayList<>();
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
    /**
     * !!!!!!!!!! los bytes deberían influir en el tamaño ----------------------------------------------------------------------------------------------------------------------------------------
     * @param inicializacion
     * @return
     * @throws Exception 
     */
    public String getDeclaracionEnsamblador(Object inicializacion) throws Exception {
        if (inicializacion == null) {
            return "DS.L 1";
        } else if (!(inicializacion instanceof String)) {
            return "DC.L " + inicializacion.toString();
        } else if (inicializacion instanceof String) {
            return "DC.B '" + inicializacion.toString() + "',0";
        }
        throw new Exception("Valor de inicialización " + inicializacion.toString() + " inválido");
    }
}
