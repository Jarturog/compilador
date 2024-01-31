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
    private Tipo tipoValor; //Que tipo de dato es: BOOLEAN, STRING...
    private TipoReferencia tipoReferencia;
    private int referencia; //Referencia de la tabla de datos o procedimientos
    
    public static enum TipoReferencia{
        literal, referencia
    }
    
    public Operador(String nombre){
        this.nombre = nombre;
    }
    
    public Operador(int referencia){
        this.tipoReferencia = TipoReferencia.referencia;
        this.referencia = referencia;
    }
    
    public Operador(Tipo tipo, Object valor){
        this.tipoValor = tipo;
        this.valor = valor;
        this.tipoReferencia = TipoReferencia.literal;
    }

    public String getNombre() {
        return nombre;
    }

    public Object getValor() {
        return valor;
    }

    public Tipo getTipoCasting() {
        return tipoValor;
    }

    public TipoReferencia getTipo() {
        return tipoReferencia;
    }

    public int getReferencia() {
        return referencia;
    }
    
    
    @Override
    public String toString(){
        if(this.nombre != null){ //O es una referencia o un valor
            if(this.tipoReferencia == TipoReferencia.literal){ //valor
                return "Valor( " + this.valor  + ")";
            }else{ //Es una referencia a otro valor
                return "Referencia( " + this.referencia + ")";
            }
        }else{ //Variable
            return "Variable( " + this.nombre + ")"; 
        }
    }
    
    
    
}
