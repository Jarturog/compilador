/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSemantico;

import java.util.ArrayList;

/**
 *
 * @author dasad
 */
public class TablaVariables {
    
    //Aquí iremos almacenando las variables    
    private ArrayList<VData> tablaVariables;
    
    //Contador de variables actuales metidas
    private int contador;
    
    
    public TablaVariables(){
        this.tablaVariables = new ArrayList<>();
        this.contador = 0;
    }
    
    public VData get(int i){
        VData data = this.tablaVariables.get(i);
        return data;
    }
    
    
    public VData put(VData v){
        this.contador++;
        this.tablaVariables.add(v);
        return v;
    }
    
    public int getContador(){
        return this.contador;
    }
    
    public void decrementarContador(){
        this.contador--;
    }


}
