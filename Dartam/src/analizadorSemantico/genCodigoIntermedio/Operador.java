/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package genCodigoIntermedio;

/**
 *Clase Operador, objecto referente a un operando de
 *  las 3 direcciones, ya sea op1 | op2 | 
 * @author dasad
 */
public class Operador {
    private String nombre; //Nombre de la variable / campo
    private Object valor; //Valor
    private Tipo tipoCasting; //Que tipo de dato es: BOOLEAN, STRING...
    private TipoReferencia tipo;
    private int referencia; //Referencia de la tabla de datos o procedimientos
    
    public static enum TipoReferencia{
        literal, referencia
    }
    
    public Operador(String nombre){
        this.nombre = nombre;
    }
    
    public Operador(int referencia){
        this.tipo = TipoReferencia.referencia;
        this.referencia = referencia;
    }
    
    public Operador(Tipo tipo, Object valor){
        this.tipoCasting = tipo;
        this.valor = valor;
        this.tipo = TipoReferencia.literal;
    }

    public String getNombre() {
        return nombre;
    }

    public Object getValor() {
        return valor;
    }

    public Tipo getTipoCasting() {
        return tipoCasting;
    }

    public TipoReferencia getTipo() {
        return tipo;
    }

    public int getReferencia() {
        return referencia;
    }
    
    
    @Override
    public String toString(){
        if(this.nombre != null){ //O es una referencia o un valor
            if(this.tipo == TipoReferencia.literal){ //valor
                return "Valor( " + this.valor  + ")";
            }else{ //Es una referencia a otro valor
                return "Referencia( " + this.referencia + ")";
            }
        }else{ //Variable
            return "Variable( " + this.nombre + ")"; 
        }
    }
    
    
    
}
