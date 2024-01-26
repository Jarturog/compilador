package genCodigoIntermedio;

/**
 *
 * @author dasad
 */
public class Instruccion3Direcciones {
//    private static enum InstructionType {
//        copy, add, sub, prod, div, mod, neg, and, or, not, ind_val, ind_ass, 
//        skip, go_to, if_LT, if_LE, if_EQ, if_NE, if_GE, if_GT, pmb, call, rtn, 
//        param_s, param_c, in, out, point
//    }
    
    private String tipoInstruccion;
    public final Operador op1, op2, dst;
    
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
