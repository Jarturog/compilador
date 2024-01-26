package genCodigoIntermedio;

import java.util.ArrayList;
import jflex.base.Pair;

/**
 *
 * @author dasad
 */
public class VData {
    private int indiceTablaVariables;
    private String nombre;
    private TipoReferencia tipoVariable;
    
    private String idProcedimiento;
    private int bytes;
    private String tipo;
    private boolean isArray, isTupla;
    private int offset;
    private ArrayList<Pair<Integer, Tipo>> parametrosTupla; //Elementos de la tupla
    
   
    public VData(String nombre, TipoReferencia tv, String tipo, String idp, boolean isArray, boolean isTupla){
        this.nombre = nombre;
        this.tipoVariable = tv;
        this.tipo = tipo;
        this.idProcedimiento = idp;
        this.parametrosTupla = new ArrayList<>();
        
        switch (tipo){
            case "ent" -> this.bytes = Integer.BYTES;
            case "car" -> this.bytes = Character.BYTES;
            case "prop" -> this.bytes = 1;
            case "string" -> this.bytes = 128;
            case "real" -> this.bytes = Double.BYTES;
            default -> this.bytes = -1; //En caso de tupla y array calcularemos diferente
        }
        
        this.isArray = isArray;
        this.isTupla = isTupla;
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

    public String getTipo() {
        return tipo;
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
