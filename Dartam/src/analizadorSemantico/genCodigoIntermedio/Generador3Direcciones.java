package analizadorSemantico.genCodigoIntermedio;

import genCodigoEnsamblador.PData;
import genCodigoEnsamblador.TablaProcedimientos;
import genCodigoEnsamblador.VData;
import analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr;
import analizadorSemantico.genCodigoIntermedio.Tipo.TipoReferencia;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import jflex.base.Pair;

public class Generador3Direcciones {
    
    private final HashMap<String, VData> tablaVariables;
    private final HashSet<String> tablaEtiquetas;
    private final HashMap<String, PData> tablaProcedimientos;
    private final HashMap<String, Integer> numEtiquetasOVariablesConId;
    private final HashSet<String> variablesInicializadas = new HashSet<>();
//    private final ArrayList<String> listaProcedimientos; //Funcionara como una pila
    private final ArrayList<Instruccion> instrucciones;
//    private final ArrayList<Integer> inicializaciones = new ArrayList<>();

    public Generador3Direcciones() {
        this.tablaVariables = new HashMap<>();
        this.tablaProcedimientos = new HashMap();
//        this.listaProcedimientos = new ArrayList<>();
        this.instrucciones = new ArrayList<>();
        numEtiquetasOVariablesConId = new HashMap<>();
        tablaEtiquetas = new HashSet<>();
    }
    
    public Generador3Direcciones(HashMap<String, VData> tv, HashMap<String, PData> tp) {
        this.tablaVariables = tv;
        this.tablaProcedimientos = tp;
//        this.listaProcedimientos = new ArrayList<>();
        this.instrucciones = new ArrayList<>();
        numEtiquetasOVariablesConId = new HashMap<>();
        tablaEtiquetas = new HashSet<>();
    }
    
//    public String nuevoN(String nombre) {
//        return conseguirIdentificadorUnico("n_"+nombre);
//    }

    //Crecion de una etiqueta: Sintaxi etX donde X es el contador de etiquetas puestas
    public String nuevaEtiqueta() {
        String s = conseguirIdentificadorUnico("e");
        tablaEtiquetas.add(s);
        return s;
    }

    public String nuevaEtiqueta(String nombre) {
        String s = conseguirIdentificadorUnico("e_"+nombre);
        tablaEtiquetas.add(s);
        return s;
    }

    public String nuevaVariable() {
        return nuevaVariable(TipoReferencia.var);
    }
    
    public String nuevaVariable(String id) {
        return nuevaVariable(id, TipoReferencia.var);
    }
    
    //Le pasaremos si es una variable o un parametro, de que tipo, y si es un array o una tupla
    public String nuevaVariable(TipoReferencia tipoVariable) {
        String idVar = conseguirIdentificadorUnico("t");
        VData data = new VData(tipoVariable);
        tablaVariables.put(idVar, data);
        return idVar;
    }

    //Le pasaremos si es una variable o un parametro, de que tipo, y si es un array o una tupla
    public String nuevaVariable(String id, TipoReferencia tipoVariable) {
        String idVar = conseguirIdentificadorUnico(id);
        VData data = new VData(tipoVariable);
        tablaVariables.put(idVar, data);
        return idVar;
    }
    
    private String conseguirIdentificadorUnico(String id) {
        Integer num = numEtiquetasOVariablesConId.get(id);
        boolean hayRepe = num != null;
        String idVar = id;
        if (hayRepe) {
            while (tablaVariables.containsKey(idVar) || tablaProcedimientos.containsKey(idVar) || tablaEtiquetas.contains(idVar)) {
                num++;
                numEtiquetasOVariablesConId.put(id, num);
                idVar = id +"_"+ num;
            }
        } else {
            numEtiquetasOVariablesConId.put(id, 0);
        }
        return idVar;
    }
    
    public String nuevaDimension(String idArray, TipoReferencia tipoVariable) {
        String idVar = conseguirIdentificadorUnico("d_" + idArray);
        VData data = new VData(tipoVariable);
        tablaVariables.put(idVar, data);
        return idVar;
    }

//    //Devuelve la función que esta en el top de la pila
//    public String getFuncionActual(){
//        return listaProcedimientos.isEmpty() ? null : listaProcedimientos.get(listaProcedimientos.size() - 1);
//    }
//    
//    //Eliminar funcion
//    public String eliminarFuncion(){
//        return listaProcedimientos.remove(listaProcedimientos.size() - 1);
//    }
//    
//    //Permite añadir una nueva funcion
//    public void añadirFuncion(String id){
//        listaProcedimientos.add(id);
//    }
    //Permite crear un nuevo procedimiento y añadirlo a la tabla
    public int nuevoProcedimiento(String id, String etiqueta, ArrayList<Pair<String, String>> params, int bytesRetorno) {
        int contador = tablaProcedimientos.size();
        PData data = new PData(etiqueta, params, bytesRetorno);
        //tablaProcedimientos.put(id, data);
        tablaProcedimientos.put(etiqueta, data);
        
        return contador;
    }

    //Recibimos los datos de un procedimiento con el nombre pasado por parametro
    public PData getProcedimeinto(String id) {
        return this.tablaProcedimientos.get(id);
    }

    //Crearemos la instruccion de 3 direcciones y almacenaremos en el array list
    public void generarInstr(TipoInstr instruccion, Operador op1, Operador op2, Operador dst) {
        if(instruccion.isTipo(TipoInstr.COPY) && !variablesInicializadas.contains(dst.toString()) && op1.isLiteral()) {
            variablesInicializadas.add(dst.toString());
        }
        Instruccion ins = new Instruccion(instruccion, op1, op2, dst);
        this.instrucciones.add(ins);
    }
    
//    public void generarInstr(TipoInstr instruccion, String op1, String op2, String dst) {
//        Instruccion ins = new Instruccion(instruccion, op1 == null ? null : new Operador(op1), op2 == null ? null : new Operador(op2), dst == null ? null : new Operador(dst));
//        this.instrucciones.add(ins);
//    }

//    //Metodo que nos devolvera el arraylist con los objetos de 3 direcciones 
//    public ArrayList<Instruccion3Direcciones> instrucciones(){
//        return this.instrucciones;
//    }

    public ArrayList<Instruccion> getInstrucciones() {
        return instrucciones;
    }
    
    @Override
    public String toString() {
        String s = "";
        for (Instruccion i : instrucciones) {
            s += i + "\n";
        }
        return s;
    }
    
    public HashMap<String, VData> getTablaVariables() {
        return tablaVariables;
    }

    public HashMap<String, PData> getTablaProcedimientos() {
        return tablaProcedimientos;
    }
    
    public HashSet<String> getVariablesInicializadas() {
        return variablesInicializadas;
    }
    
}
