package analizadorSemantico;

/**
 *
 * @author dasad
 */
public class Instruccion3Direcciones {
    private String tipoInstruccion;
    private Operador op1;
    private Operador op2;
    private Operador dst;
    
    public Instruccion3Direcciones(String tipoInstruccion, Operador op1, Operador op2, Operador dst){
        this.tipoInstruccion = tipoInstruccion;
        this.op1 = op1;
        this.op2 = op2;
        this.dst = dst; 
    }
    
    //Devuelve todos los operadores de la instruccion
    public Operador[] operadores(){
        Operador[] ops = {this.op1, this.op2, this.dst};
        return ops;
    }
    
    //Tipo de instruccion
    public String tipo(){
        return this.tipoInstruccion;
    }
    
    @Override
    public String toString(){
        String cadena = "op1: " +  this.op1 + " | " + this.op2 + " | " + this.dst;  
        return cadena;
    }
    
    
}
