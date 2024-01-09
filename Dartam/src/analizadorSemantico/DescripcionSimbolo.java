/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico;

import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolOperand;
import analizadorSintactico.symbols.SymbolParamsLista;
import analizadorSintactico.symbols.SymbolTipo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import jflex.base.Pair;

/**
 *
 * @author dasad
 */
public class DescripcionSimbolo {
    
    private String tipo;

    private int nivel;
    private boolean isConstante = false, valorAsignado = false;

    private ArrayList<Pair<String, DescripcionSimbolo>> parametros;
    private final HashMap<String, DescripcionSimbolo> miembros;
    private ArrayList<SymbolOperand> dimensiones;
    private DescripcionSimbolo memberOf = null;

    public int first;
    public int next;    //Apuntador al siguiente campo de una tupla
    public String idcamp;   //Identificador del campo de la tupla
    
    public int dcamp; //Desplazamiento dentro del array
    
    /**
     * Copia
     */
    public DescripcionSimbolo(DescripcionSimbolo d){
        this.tipo = d.tipo;
        this.nivel = d.nivel;
        this.isConstante = d.isConstante;
        this.valorAsignado = d.valorAsignado;

        // Assuming Pair, SymbolOperand, and DescripcionSimbolo have their own copy constructors
        if (d.parametros != null) {
            this.parametros = new ArrayList<>(d.parametros);
        }
        if (d.miembros != null) {
            this.miembros = new HashMap<>(d.miembros);
        } else {
            miembros = null;
        }
        if (d.dimensiones != null) {
            this.dimensiones = new ArrayList<>(d.dimensiones);
        }
        
        

        this.memberOf = (d.memberOf != null) ? new DescripcionSimbolo(d.memberOf) : null;

        this.first = d.first;
        this.next = d.next;
        this.idcamp = d.idcamp;
        this.dcamp = d.dcamp;
    }
    
    /**
     * Main
     */
    public DescripcionSimbolo(){
        tipo = null;
        miembros = null;
    }

    /**
     * Variable
     */
    public DescripcionSimbolo(String t, boolean isConst, boolean v, DescripcionSimbolo tupla){
        tipo = t;
        isConstante = isConst;
        valorAsignado = v;
        memberOf = tupla; // miembro de una tupla
        if (isTupla()){
            miembros = new HashMap<String, DescripcionSimbolo>(); // copia
            for (HashMap.Entry<String, DescripcionSimbolo> entry : memberOf.miembros.entrySet()) {
                miembros.put(entry.getKey(), new DescripcionSimbolo(entry.getValue()));
            }
        } else {
            miembros = null;
        }
    }
    
    /**
     * Array
     */
    public DescripcionSimbolo(String t, ArrayList<SymbolOperand> dim, DescripcionSimbolo tupla){
        tipo = t;
        dimensiones = dim;
        memberOf = tupla; // miembro de una tupla
        miembros = null;
    }
    
    /**
     * Tupla
     */
    public DescripcionSimbolo(HashMap<String, DescripcionSimbolo> m){
        miembros = (HashMap<String, DescripcionSimbolo>) m;
    }
    
    /**
     * Funcion
     * @param tipoRetorno
     */
    public DescripcionSimbolo(String tipoRetorno){
        tipo = tipoRetorno;
        parametros = new ArrayList<>();
        miembros = null;
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
        //return miembros != null;
        return tipo.startsWith(ParserSym.terminalNames[ParserSym.KW_TUPLE]);
    }
    
    public DescripcionSimbolo getMember(String id) {
        return miembros.get(id);

    }
    
    public String getNombreTupla() {
        if (memberOf == null || !isTupla()) {
            return null;
        }
        return tipo.substring(tipo.indexOf(" ") + 1);
    }
    
    public void anyadirMiembro(String id, DescripcionSimbolo ds) {
        miembros.put(id, ds);
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
