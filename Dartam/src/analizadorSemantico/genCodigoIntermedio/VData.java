package analizadorSemantico.genCodigoIntermedio;

import java.util.ArrayList;
import jflex.base.Pair;

/**
 *
 * 
 */
public class VData {
    private int indiceTablaVariables;
    private String nombre;
    private TipoReferencia tipoVariable;
    
    private String idProcedimiento;
    private int bytes;
    private boolean isArray, isTupla;
    private int offset;
    private ArrayList<Pair<Integer, Tipo>> parametrosTupla; //Elementos de la tupla
    
   
    public VData(String nombre, TipoReferencia tv, String idp){
        this.nombre = nombre;
        this.tipoVariable = tv;
        this.idProcedimiento = idp;
        this.parametrosTupla = new ArrayList<>();
    }
    
    public int getIndice(){
        return this.indiceTablaVariables;
    }

    public String getNombre() {
        return nombre;
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
    
}
