/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico;

/**
 *
 * @author dasad
 */
public class VData {
    
    private String nombre;
    private TipoReferencia tipoVariable;
    
    private String idProcedimiento;
    private int bytes;
    private Tipo tipo;
    private boolean isArray;
    private boolean isTupla;
    private int offset;
    
   
    
    public VData(String nombre, TipoReferencia tv, Tipo tipo, String idp, boolean isArray, boolean isTupla){
        this.nombre = nombre;
        this.tipoVariable = tv;
        this.tipo = tipo;
        this.idProcedimiento = idp;
        
        switch (tipo){
            case INT -> this.bytes = Integer.BYTES;
            case CHAR -> this.bytes = Character.BYTES;
            case BOOL -> this.bytes = 1;
            case STRING -> this.bytes = 128;
            case DOUBLE -> this.bytes = Double.BYTES;
        }
        
        this.isArray = isArray;
        this.isTupla = isTupla;
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

    public Tipo getTipo() {
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
    
}
