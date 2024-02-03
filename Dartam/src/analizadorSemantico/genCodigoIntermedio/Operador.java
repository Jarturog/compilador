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
    private String nombre; //Nombre de la variable / campo
    private Object valor; //Valor
    public final Tipo tipoValor; //Que tipo de dato es: BOOLEAN, STRING...

    public Operador(String nombre){
        this.nombre = nombre;
        this.tipoValor = null;
    }
    
    public Operador(Tipo tipo, Object valor){
        this.tipoValor = tipo;
        this.valor = valor;
    }

    @Override
    public String toString(){
        if(this.nombre == null){ //O es una variable o un valor
            return ""+valor;
        }else{ //Variable
            return nombre; 
        }
    }
    
    public String getOperadorEnsamblador() {
        if(this.nombre == null){ //O es una referencia o un valor
            return "#"+valor;
        }else{ //Variable
            return nombre; 
        }
    }
    
    public boolean isLiteral() {
        return valor != null;
    }
    
}
