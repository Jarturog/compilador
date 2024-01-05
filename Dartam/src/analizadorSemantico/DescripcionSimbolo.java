/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico;

import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolParamsLista;
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
    private boolean isConstante, valorAsignado, isMember;

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
     * Variable
     */
    public DescripcionSimbolo(String t, boolean isConst, boolean v, boolean isMiembro){
        tipo = t;
        isConstante = isConst;
        valorAsignado = v;
        isMember = isMiembro; // miembro de una tupla
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
    public DescripcionSimbolo(HashMap<String, DescripcionSimbolo> m){
        miembros = (HashMap<String, DescripcionSimbolo>) m;
    }
    
    /**
     * Función
     * @param tipoRetorno
     */
    public DescripcionSimbolo(String tipoRetorno){
        tipo = tipoRetorno;
        parametros = new ArrayList<>();
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
    
    public void setTamanyoArray(int n){
       //tipo.setTamanyoArray(n);
    }
    
    public int getNivel(){
        return this.nivel;
    }
    
    public void setNivel(int n){
        this.nivel = n;
    }
    
    //Funciones
    public void anyadirParametro(String n, DescripcionSimbolo d){
        parametros.add(new Pair(n, d));
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
    
    public void anyadirMiembro(String id, DescripcionSimbolo ds) {
        miembros.put(id, ds);
    }
    
    public String getTipoArray() {
        if (!isArray()) {
            return null;
        }
        String tipo = getTipo();
        return tipo.substring(0, tipo.lastIndexOf(" "));
    }
    
    public boolean tieneValorAsignado() {
        return valorAsignado;
    }
    
    public void asignarValor() {
        valorAsignado = true;
    }
    
    public boolean isConstante() {
        return isConstante;
    }
    
}
