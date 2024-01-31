/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package genCodigoIntermedio;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dasad
 */
public class TablaVariables implements Iterable<VData> {
    
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
    
    //Metodo para buscar el numero de variable dado el nombre
    public int recibirNumeroVariable(String s){
        for(int i = 0; i<this.tablaVariables.size(); i++){
            VData v = this.tablaVariables.get(i);
            if(v.getNombre().equals(s)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<VData> iterator() {
        return new TablaVariablesIterator();
    }

    private class TablaVariablesIterator implements Iterator<VData> {
        private int indiceActual;

        public TablaVariablesIterator() {
            this.indiceActual = 0;
        }

        @Override
        public boolean hasNext() {
            return indiceActual < tablaVariables.size();
        }

        @Override
        public VData next() {
            if (!hasNext()) {
                throw new IndexOutOfBoundsException("No hay más elementos en la lista");
            }
            return tablaVariables.get(indiceActual++);
        }
    }

}
