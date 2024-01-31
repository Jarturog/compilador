package genCodigoIntermedio;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author dasad
 */
public class TablaProcedimientos {
    
    private HashMap<String, PData> tablaProcedimientos;
    private int contador;
    
    public TablaProcedimientos() {
        this.tablaProcedimientos = new HashMap<>();
        this.contador = 0;
    }
    
    
    public PData get(String s){
        PData procedimiento = this.tablaProcedimientos.get(s);
        return procedimiento;
    }
    
    public boolean put(String s, PData data){
        if(this.tablaProcedimientos.containsKey(s)){ //Si ya existe devolvemos false
            return false; //Ya existe la clave
        }else{
            this.tablaProcedimientos.put(s, data);
            this.contador += 1;
            return true;
        }
    }
    
    public Set<String> getNombresProcedimientos(){
        return this.tablaProcedimientos.keySet();
    }
    
    public int getContador(){
        return this.contador;
    }
    
    public void decrementarContador(){
        this.contador -= 1;
    }
    
}
