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
import jflex.base.Pair;

/**
 *
 * @author dasad
 */
public class DescripcionSimbolo {
    
    private String tipo;

    private int nivel;
    private boolean isConstante, valorAsignado;

    private ArrayList<Pair<String, DescripcionSimbolo>> parametros;
    private HashMap<String, DescripcionSimbolo> miembros;
    private ArrayList<Integer> dimensiones;


    public int first;
    public int next;    //Apuntador al siguiente campo de una tupla
    public String idcamp;   //Identificador del campo de la tupla
    
    public int dcamp; //Desplazamiento dentro del array
    
    /**
     * Main
     */
    public DescripcionSimbolo(){
        tipo = null;
    }
    
    /**
     * Primitiva
     */
    public DescripcionSimbolo(String t, boolean isConst){
        tipo = t;
        isConstante = isConst;
    }
    
    /**
     * Primitiva
     */
    public DescripcionSimbolo(String t, boolean isConst, boolean v){
        tipo = t;
        isConstante = isConst;
        valorAsignado = v;
    }
    
    /**
     * Array
     */
    public DescripcionSimbolo(String t, ArrayList<Integer> dim){
        tipo = t;
        dimensiones = dim;
    }
    
    /**
     * Tupla
     */
    public DescripcionSimbolo(Set<DescripcionSimbolo> m){
        miembros = (HashMap<String, DescripcionSimbolo>) m;
    }
    
    /**
     * Función
     */
    public DescripcionSimbolo(String tipoRetorno){
        tipo = tipoRetorno;
    }
    
    public void cambiarTipo(String t){
        this.tipo = t;
    }
    
    public void cambiarTipo(int t){
        if(t == ParserSym.KW_METHOD){
            //this.tipo = new SymbolTipo();
            this.parametros = new ArrayList<>();
        }
    }
    
    public String getTipo(){
        return tipo;
    }
    
    //Array
    public void setTipoBase(SymbolTipo base){
        //tipo.setTipoBase(base);
    }
    
    public String getTipoBase(){
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
    public void añadirParametro(String n, String t){
        DescripcionSimbolo ds = new DescripcionSimbolo(t);
        parametros.add(new Pair(n, ds));
    }
    
    public int getNumeroParametros(){
        return parametros.size();
    }
    
    public ArrayList<Pair<String, DescripcionSimbolo>> getTiposParametros(){
        return new ArrayList<>(parametros);
    }
    
    public HashMap<String, DescripcionSimbolo> getTiposMiembros() {
        return new HashMap<>(miembros);
    }
    
    public void setTipoRetorno(String tv) throws Exception{
        if(!this.isFunction()){
           throw new Exception("Error");
        }else{
            this.tipo = tv;
        }
    }
    
    public String getTipoRetorno(){
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
    
    public DescripcionSimbolo getMember(String id) {
        return miembros.get(id);
    }
    
}
