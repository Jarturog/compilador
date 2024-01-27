/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package genCodigoIntermedio;

/**
 *
 * @author dasad
 */
public enum TipoInstruccion {
    ASIGN("asign"), 
    ADD("add"), 
    SUB("sub"), 
    MUL("mul"),
    DIV("div"),
    MOD("mod"),
    AND("and"),
    OR("or"),
    NOT("not"),
    SKIP("skip"),
    GOTO("goto"),
    IFLT("iflt"),
    IFLE("ifle"),
    IFGT("ifgt"),
    IFGE("ifge"),
    IFEQ("ifeq"),
    IFNE("ifne"),
    CALL("call"),
    RETURN("return"),
    READ("read"),
    WRITE("write"),
    SCAN("scan"),
    PRINT("print"),
    SWAP("swap");
    INIT("init");
    
    public final String tipo;
    
    TipoInstruccion(String t){
        this.tipo = t;
    }
    String getDescripcion() {
        return this.tipo;
    }
    
    
}
