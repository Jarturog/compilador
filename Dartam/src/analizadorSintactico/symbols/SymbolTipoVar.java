
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
public class SymbolTipoVar extends SymbolBase {
    
    public int tipo;
    public SymbolTipoVar tipoBase;
    private int profundidadArray;
   
    public int tamañoArray = -1;
    
   
    public SymbolTipoVar(){
        super("tipoVar", 0);
        this.tipo = -1; //VOID
        this.tipoBase = null;
        this.profundidadArray = 0;
    }
     
    public SymbolTipoVar(Integer tipo, SymbolTipoVar tipoBase) {
        super("tipoVar", 0);
        this.tipo = tipo;
        this.tipoBase = tipoBase;
        //Añadir tipo array
        //if(tipo != ParserSym.TIPO_ARRAY){
        //    
        //}
        this.profundidadArray = 0;
    }
    
    public SymbolTipoVar(int tipo){
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
    
    public void setTipoBase(SymbolTipoVar tipoBase){
        //if(this.tipo != Constantes.TIPO_ARRAY){
        //    throw new Exception("Error");
        //}
        this.tipoBase = tipoBase;
        this.tamañoArray = tipoBase.profundidadArray + 1;
    }
    
    public SymbolTipoVar getTipoBase(){
        return this.tipoBase;
    }
    
    
}
