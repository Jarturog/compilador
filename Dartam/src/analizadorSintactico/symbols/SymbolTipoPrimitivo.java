
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import analizadorSintactico.ParserSym;
import java_cup.runtime.ComplexSymbolFactory.Location;

/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * Bàsicament conté un valor enter
 */
public class SymbolTipoPrimitivo extends SymbolBase {
    
    public int tipo;
    public SymbolTipoPrimitivo tipoBase;
    private int profundidadArray;
   
    public int tamañoArray = -1;
    
   
    public SymbolTipoPrimitivo(){
        super("tipoVar", 0);
        this.tipo = -1; //VOID
        this.tipoBase = null;
        this.profundidadArray = 0;
    }
     
    public SymbolTipoPrimitivo(Integer tipo, SymbolTipoPrimitivo tipoBase) {
        super("tipoVar", 0);
        this.tipo = tipo;
        this.tipoBase = tipoBase;
        //Añadir tipo array
        //if(tipo != ParserSym.TIPO_ARRAY){
        //    
        //}
        this.profundidadArray = 0;
    }
    
    public SymbolTipoPrimitivo(Integer tipo, Location l, Location r) {
        super("tipoVar", l, r);
        this.tipo = tipo;
        this.tipoBase = tipoBase;
        //Añadir tipo array
        //if(tipo != ParserSym.TIPO_ARRAY){
        //    
        //}
        this.profundidadArray = 0;
    }
    
    public SymbolTipoPrimitivo(int tipo){
        super("tipoVar", 0);
        this.tipo = tipo;
        this.profundidadArray = 0;
    }

    public int getProfundidadArray(){
        return this.profundidadArray;
    }
    
    public void setProfundidadArray(int pa){
        this.profundidadArray = pa;
    }
    
    
    public int getTipo() {
        return tipo;
    }
    
    public boolean isTipo(int t){
        return t == this.tipo;
    }
    
    public int getTamañoArray(){
        return this.tamañoArray;
    }
    
    public void setTamañoArray(int t){
        this.tamañoArray  = t;
    }
    
    public void setTipoBase(SymbolTipoPrimitivo tipoBase){
        //if(this.tipo != Constantes.TIPO_ARRAY){
        //    throw new Exception("Error");
        //}
        this.tipoBase = tipoBase;
        this.tamañoArray = tipoBase.profundidadArray + 1;
    }
    
    public SymbolTipoPrimitivo getTipoBase(){
        return this.tipoBase;
    }
    
    
}
