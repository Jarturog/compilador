/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico;

import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolTipo;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author dasad
 */
public class DescripcionSimbolo {
    
    private SymbolTipo tipo;
    private Object valor;
    
    private int nivel;
    private int isConstante;

    private ArrayList<Parametro> parametros;
    private HashMap<Parametro, Boolean> miembros;
    private ArrayList<Integer> dimensiones;

    public int first;   
    public int next;    //Apuntador al siguiente campo de una tupla
    public String idcamp;   //Identificador del campo de la tupla
    
    public int dcamp; //Desplazamiento dentro del array
    public class Parametro{
        public String nombre;
        public SymbolTipo tipo;
        
        public Parametro(String nombre, SymbolTipo tipo){
            this.nombre = nombre;
            this.tipo = tipo;
        }
    }
    
    public DescripcionSimbolo(){
        //this.tipo = new SymbolTipo();
    }
    
    public void cambiarTipo(SymbolTipo t){
        this.tipo = t;
    }
    
    public void cambiarTipo(int t){
        if(t == ParserSym.KW_METHOD){
            //this.tipo = new SymbolTipo();
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
        if(isFunction()){
            return ParserSym.KW_METHOD;
        }else{
            return -1;//tipo.getTipo();
        }
    }
    
    //Array
    public void setTipoBase(SymbolTipo base){
        //tipo.setTipoBase(base);
    }
    
    public SymbolTipo getTipoBase(){
        return null;//tipo.getTipoBase();
    }
    
    public int getProfundidad(){
        return -1;//tipo.getProfundidadArray();
    }
    
    public void setTamañoArray(int n){
       //tipo.setTamañoArray(n);
    }
    
    public int getNivel(){
        return this.nivel;
    }
    
    public void setNivel(int n){
        this.nivel = n;
    }
    
    //Funciones
    public void añadirParametro(String n, SymbolTipo t){
        parametros.add(new Parametro(n,t));
    }
    
    public int getNumeroParametros(){
        return parametros.size();
    }
    
    public ArrayList<Parametro> getTiposParametros(){
        return new ArrayList<>(parametros);
    }
    
    public HashMap<Parametro, Boolean> getTiposMiembros() {
        return new HashMap<>(miembros);
    }
    
    public void setTipoRetorno(SymbolTipo tv) throws Exception{
        if(!this.isFunction()){
           throw new Exception("Error");
        }else{
            this.tipo = tv;
        }
    }
    
    public SymbolTipo getTipoRetorno(){
        return this.tipo;
    }
    
    public boolean isFunction() {
        return parametros != null;
    }
    
    public boolean isArray() {
        return dimensiones != null;
    }
    
    public boolean isTupla() {
        return miembros != null;
    }
    
}
