/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico;

/**
 *
 * @author dasad
 */
public class Operador {
    private String nombre;
    private Object valor;
    private TipoCast tipoCasting;
    private Tipo tipo;
    private int referencia;
    
    public static enum Tipo{
        literal, referencia
    }
    
    public static enum TipoCast {
        INT, CHAR, BOOL, STRING, DOUBLE
    }
    
    public Operador(String nombre){
        this.nombre = nombre;
    }
    
    public Operador(int referencia){
        this.tipo = Tipo.referencia;
        this.referencia = referencia;
    }
    
    public Operador(TipoCast tipo, Object valor){
        this.tipoCasting = tipo;
        this.valor = valor;
        this.tipo = Tipo.literal;
    }

    public String getNombre() {
        return nombre;
    }

    public Object getValor() {
        return valor;
    }

    public TipoCast getTipoCasting() {
        return tipoCasting;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getReferencia() {
        return referencia;
    }
    
    
    @Override
    public String toString(){
        if(this.nombre != null){ //O es una referencia o un valor
            if(this.tipo == Tipo.literal){ //valor
                return "Valor( " + this.valor  + ")";
            }else{ //Es una referencia a otro valor
                return "Referencia( " + this.referencia + ")";
            }
        }else{ //Variable
            return "Variable( " + this.nombre + ")"; 
        }
    }
    
    
    
}
