/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tablaSimbolos;

import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolTipoRetorno;
import java.util.ArrayList;

/**
 *
 * @author dasad
 */
public class SimboloDescripcion {
    
    private SymbolTipoRetorno tipo;
    private Object valor;
    
    private int nivel;
    private int isConstante;
    
    private boolean isFunction;
    private int nParametros;
    private ArrayList<Parametro> parametros;
    
    public boolean isTupla;
    public boolean isArray;
    
    public int first;
    public int next;
    public String idcamp;
    
    public int dcamp; //Desplazamiento dentro del array
    public class Parametro{
        public String nombre;
        public SymbolTipoRetorno tipo;
        
        public Parametro(String nombre, SymbolTipoRetorno tipo){
            this.nombre = nombre;
            this.tipo = tipo;
        }
    }
    
    public SimboloDescripcion(){
        this.tipo = new SymbolTipoRetorno();
    }
    
    public void cambiarTipo(SymbolTipoRetorno t){
        this.tipo = t;
    }
    
    public void cambiarTipo(int t){
        if(t == ParserSym.KW_METHOD){
            this.isFunction = true;
            this.nParametros = 0;
            this.tipo = new SymbolTipoRetorno();
            this.parametros = new ArrayList<>();
        }
    }
    
    public void setValor(Object v){
        this.valor = v;
    }
    
    public Object getValor(){
        return this.valor;
    }
    
    public int getTipo(){
        if(isFunction){
            return ParserSym.KW_METHOD;
        }else{
            return tipo.getTipo();
        }
    }
    
    //Array
    public void setTipoBase(SymbolTipoRetorno base){
        tipo.setTipoBase(base);
    }
    
    public SymbolTipoRetorno getTipoBase(){
        return tipo.getTipoBase();
    }
    
    public int getProfundidad(){
        return tipo.getProfundidadArray();
    }
    
    public void setTamañoArray(int n){
       tipo.setTamañoArray(n);
    }
    
    public int getNivel(){
        return this.nivel;
    }
    
    public void setNivel(int n){
        this.nivel = n;
    }
    
    //Funciones
    public void añadirParametro(String n, SymbolTipoRetorno t){
        parametros.add(new Parametro(n,t));
        this.nParametros += 1;
    }
    
    public int getNumeroParametros(){
        return this.nParametros;
    }
    
    public ArrayList<SymbolTipoRetorno> getTiposParametros(){
        ArrayList<SymbolTipoRetorno> al = new ArrayList<>();
        for(int i = 0; i< this.parametros.size(); i++){
            al.add(this.parametros.get(i).tipo);
        }
        return al;
    }
    
    public void setTipoRetorno(SymbolTipoRetorno tv) throws Exception{
        if(!this.isFunction){
           throw new Exception("Error");
        }else{
            this.tipo = tv;
        }
    }
    
    public SymbolTipoRetorno getTipoRetorno(){
        return this.tipo;
    }
    
}
