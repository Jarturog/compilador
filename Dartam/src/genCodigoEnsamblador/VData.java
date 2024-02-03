package genCodigoEnsamblador;

import analizadorSemantico.genCodigoIntermedio.Tipo;
import analizadorSemantico.genCodigoIntermedio.Tipo.TipoReferencia;
import java.util.ArrayList;
import jflex.base.Pair;

public class VData {
    private int indiceTablaVariables;
    private TipoReferencia tipoVariable;
    
    private String idProcedimiento;
    private int bytes;
    private boolean isArray, isTupla;
    private int offset;
    private ArrayList<Pair<Integer, Tipo>> parametrosTupla; //Elementos de la tupla
    
   
    public VData(TipoReferencia tv){
        this.tipoVariable = tv;
//        this.idProcedimiento = idp;
        this.parametrosTupla = new ArrayList<>();
    }
    
    public int getIndice(){
        return this.indiceTablaVariables;
    }

    public TipoReferencia getTipoVariable() {
        return tipoVariable;
    }

    public String getIdProcedimiento() {
        return idProcedimiento;
    }

    public int getBytes() {
        return bytes;
    }
    
    public void setBytes(int b){
        this.bytes = b;
    }

    public boolean isIsArray() {
        return isArray;
    }

    public boolean isIsTupla() {
        return isTupla;
    }

    public int getOffset() {
        return offset;
    }
    
    public void actualizarOffset(int o){
        this.offset = o;
    }
    
    //Metodo para añadir elementos a una tupla
    public void añadirElementoTupla(Pair<Integer, Tipo> e){
        this.parametrosTupla.add(e);
    }

    public String getDeclaracionEnsamblador(String inicializacion) {
        if (inicializacion != null) {
            return "DC.B '" + inicializacion + "',0";
        }
        return "DS.L 1";
    }
    
    
}
