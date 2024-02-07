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
    private final TipoVariable tipoValor; //Que tipo de dato es: BOOLEAN, STRING...
    private final TipoVariable casting; // si hace casting, hacia qué tipo lo hace

    public Operador(Operador o){
        nombre = o.nombre;
        tipoValor = o.tipoValor;
        valor = o.valor;
        casting = o.casting;
    }
    
    public Operador(String etiqueta){
        nombre = etiqueta;
        tipoValor = null;
        valor = null;
        casting = null;
    }
    
    public Operador(TipoVariable tipo, String id){
        tipoValor = tipo;
        nombre = id;
        valor = null;
        casting = null;
    }
    
    public Operador(TipoVariable tipo, String id, TipoVariable casting){
        tipoValor = tipo;
        nombre = id;
        valor = null;
        this.casting = casting;
    }
    
    public Operador(TipoVariable tipo, Object v){
        tipoValor = tipo;
        valor = v;
        nombre = null;
        casting = null;
    }

    @Override
    public String toString() {
        if(isLiteral()) {
            return valor.toString();
        }
        return nombre;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public boolean isLiteral() {
        return valor != null;
    }
    
    public boolean isPuntero() {
        return tipoValor.equals(TipoVariable.PUNTERO);
    }
    
    public boolean isArray() {
        return tipoValor.equals(TipoVariable.ARRAY);
    }
    
    public boolean isEstructura() {
        return tipoValor.equals(TipoVariable.TUPLA);
    }
    
    public boolean isString() {
        return tipoValor.equals(TipoVariable.STRING);
    }
    
    public Object getValor() throws Exception {
        if(valor == null) {
            throw new Exception("Se ha intentado acceder a un valor inexistente");
        }
        return valor;
    }
    
    public String toAssembly() throws Exception {
        if(isLiteral()) {
            if (tipoValor.equals(TipoVariable.STRING)) {
                return ""+valor;
            }
            if (tipoValor.equals(TipoVariable.CHAR)) {
                return "#'"+valor+"'";
            }
            return "#"+valor;
        }
//        if(valor instanceof String) {
//            return -1;
//        } 
        return nombre;
    }
    
    public TipoVariable tipo() {
        return tipoValor;
    }
    
    public TipoVariable getCasting() {
        return this.casting;
    }
    
}
