/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico;

import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolTipo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author dasad
 */
public class DescripcionSimbolo {
    
    private SymbolTipo tipo;

    private int nivel;
    private boolean isConstante, valorAsignado;

    private ArrayList<Parametro> parametros;
    private HashMap<DescripcionSimbolo, Boolean> miembros;
    private ArrayList<Integer> dimensiones;

    public int first;
    public int next;
    public String idcamp;
    
    public int dcamp; //Desplazamiento dentro del array
    public class Parametro{
        public String nombre;
        public SymbolTipo tipo;
        
        public Parametro(String nombre, SymbolTipo tipo){
            this.nombre = nombre;
            this.tipo = tipo;
        }
    }
    
    /**
     * Main
     */
    public DescripcionSimbolo(){
        tipo = null;
    }
    
    /**
     * Primitiva
     */
    public DescripcionSimbolo(SymbolTipo t, boolean isConst){
        tipo = t;
        isConstante = isConst;
    }
    
    /**
     * Primitiva
     */
    public DescripcionSimbolo(SymbolTipo t, boolean isConst, boolean v){
        tipo = t;
        isConstante = isConst;
        valorAsignado = v;
    }
    
    /**
     * Array
     */
    public DescripcionSimbolo(SymbolTipo t, ArrayList<Integer> dim){
        tipo = t;
        dimensiones = dim;
    }
    
    /**
     * Tupla
     */
    public DescripcionSimbolo(Set<DescripcionSimbolo> m){
        miembros = (HashMap<DescripcionSimbolo, Boolean>) m;
    }
    
    /**
     * Función
     */
    public DescripcionSimbolo(SymbolTipo tipoRetorno){
        tipo = tipoRetorno;
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
    
    public HashMap<DescripcionSimbolo, Boolean> getTiposMiembros() {
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
