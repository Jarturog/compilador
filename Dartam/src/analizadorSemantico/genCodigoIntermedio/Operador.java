/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico.genCodigoIntermedio;

/**
 *Clase Operador, objecto referente a un operando de
 *  las 3 direcciones, ya sea op1 | op2 | 
 * 
 */
public class Operador {
    private final String nombre; //Nombre de la variable / campo
    private final Object valor; //Valor
    private final Tipo tipoValor; //Que tipo de dato es: BOOLEAN, STRING...

    public Operador(String etiqueta){
        nombre = etiqueta;
        tipoValor = null;
        valor = null;
    }
    
    public Operador(Tipo tipo, String id){
        tipoValor = tipo;
        nombre = id;
        valor = null;
    }
    
    public Operador(Tipo tipo, Object v){
        tipoValor = tipo;
        valor = v;
        nombre = null;
    }

    @Override
    public String toString() {
        if(isLiteral()) {
            return valor.toString();
        }
        return nombre;
    }
    
    public String getNombreEnsamblador() {
        return nombre;
    }
    
    public boolean isLiteral() {
        return valor != null;
    }
    
    public boolean isPuntero() {
        return tipoValor.equals(Tipo.PUNTERO) || tipoValor.equals(Tipo.STRING);
    }
    
    public Object getValor() throws Exception {
        if(valor == null) {
            throw new Exception("Se ha intentado acceder a un valor inexistente");
        }
        return valor;
    }
    
    public String toStringEnsamblador() throws Exception {
        if(isLiteral()) {
            return "#"+valor;
        }
//        if(valor instanceof String) {
//            return -1;
//        } 
        return nombre;
    }
    
    public Tipo getTipo() {
        return tipoValor;
    }
    
}
