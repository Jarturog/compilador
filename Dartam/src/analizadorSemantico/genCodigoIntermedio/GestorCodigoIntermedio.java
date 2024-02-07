package analizadorSemantico.genCodigoIntermedio;

import analizadorSemantico.DescripcionDefinicionTupla;
import analizadorSemantico.DescripcionFuncion.Parametro;
import genCodigoEnsamblador.PData;
import genCodigoEnsamblador.VData;
import analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GestorCodigoIntermedio {

    private final HashMap<String, VData> tablaVariables;
    private final HashSet<String> tablaEtiquetas;
    private final HashMap<String, PData> tablaProcedimientos;
    private final HashMap<String, Integer> numEtiquetasOVariablesConId;
    private final HashMap<String, DescripcionDefinicionTupla> tablaTuplas;
    private final ArrayList<Instruccion> instrucciones;
    private PData main = null;

    public GestorCodigoIntermedio(GestorCodigoIntermedio gen) throws Exception {
        this.tablaVariables = new HashMap<>();
        for (Map.Entry<String, VData> p : gen.tablaVariables.entrySet()) {
            tablaVariables.put(p.getKey(), new VData(p.getValue()));
        }
        this.tablaEtiquetas = new HashSet<>(gen.tablaEtiquetas);
        this.tablaProcedimientos = new HashMap<>();
        for (Map.Entry<String, PData> p : gen.tablaProcedimientos.entrySet()) {
            tablaProcedimientos.put(p.getKey(), p.getValue());
        }
        this.numEtiquetasOVariablesConId = new HashMap<>();
        for (Map.Entry<String, Integer> p : gen.numEtiquetasOVariablesConId.entrySet()) {
            numEtiquetasOVariablesConId.put(p.getKey(), p.getValue());
        }
        this.tablaTuplas = new HashMap<>();
        for (Map.Entry<String, DescripcionDefinicionTupla> p : gen.tablaTuplas.entrySet()) {
            tablaTuplas.put(p.getKey(), p.getValue());
        }
        this.instrucciones = new ArrayList<>();
        for (Instruccion p : gen.instrucciones) {
            instrucciones.add(new Instruccion(p));
        }
        this.main = gen.main;
    }

    public GestorCodigoIntermedio() {
        this.tablaVariables = new HashMap<>();
        this.tablaProcedimientos = new HashMap();
        this.instrucciones = new ArrayList<>();
        numEtiquetasOVariablesConId = new HashMap<>();
        tablaEtiquetas = new HashSet<>();
        tablaTuplas = new HashMap<>();
    }

    public GestorCodigoIntermedio(HashMap<String, VData> tv, HashMap<String, PData> tp) {
        this.tablaVariables = tv;
        this.tablaProcedimientos = tp;
        this.instrucciones = new ArrayList<>();
        numEtiquetasOVariablesConId = new HashMap<>();
        tablaEtiquetas = new HashSet<>();
        tablaTuplas = new HashMap<>();
    }

    //Crecion de una etiqueta: Sintaxi etX donde X es el contador de etiquetas puestas
    public String nuevaEtiqueta() {
        String s = conseguirIdentificadorUnico("e");
        tablaEtiquetas.add(s);
        return s;
    }

    public String nuevaEtiqueta(String nombre) {
        String s = conseguirIdentificadorUnico("e_" + nombre);
        tablaEtiquetas.add(s);
        return s;
    }

    //Le pasaremos si es una variable o un parametro, de que tipo, y si es un array o una tupla
    public String nuevaVariable(TipoVariable t) throws Exception {
        String idVar = conseguirIdentificadorUnico("t");
        VData data = new VData(t);
        tablaVariables.put(idVar, data);
        return idVar;
    }

    //Le pasaremos si es una variable o un parametro, de que tipo, y si es un array o una tupla
    public String nuevaVariable(String id, TipoVariable t) throws Exception {
        String idVar = conseguirIdentificadorUnico(id);
        VData data = new VData(t);
        tablaVariables.put(idVar, data);
        return idVar;
    }
    
    public TipoVariable getTipoFromVar(String var) throws Exception {
        VData v = tablaVariables.get(var);
        if(v == null) {
            throw new Exception("Se ha intentado acceder al tipo de una variable inexistente: "+var);
        }
        return v.tipo();
    }

    private String conseguirIdentificadorUnico(String id) {
        id = id.toLowerCase();
        Integer num = numEtiquetasOVariablesConId.get(id);
        boolean hayRepe = num != null;
        String idVar = id;
        if (hayRepe) {
            while (tablaVariables.containsKey(idVar) || tablaProcedimientos.containsKey(idVar) || tablaEtiquetas.contains(idVar)) {
                num++;
                numEtiquetasOVariablesConId.put(id, num);
                idVar = id + "_" + num;
            }
        } else {
            numEtiquetasOVariablesConId.put(id, 0);
        }
        return idVar;
    }

    public String nuevaDimension(String idArray, TipoVariable t) throws Exception {
        String idVar = conseguirIdentificadorUnico("d_" + idArray);
        VData data = new VData(t); // es un entero
        tablaVariables.put(idVar, data);
        return idVar;
    }

    public void anyadirBytesEstructura(String var, int b) {
        tablaVariables.get(var).setBytesEstructura(b);
    }

    public void anyadirTupla(String id, DescripcionDefinicionTupla t) {
        tablaTuplas.put(id, t);
    }

    public HashMap<String, DescripcionDefinicionTupla> getTuplas() {
        return this.tablaTuplas;
    }

    public void relacionarDatoVariableConTupla(String var, DescripcionDefinicionTupla t) {
        tablaVariables.get(var).setTupla(t);
    }

    //Permite crear un nuevo procedimiento y añadirlo a la tabla
    public int nuevoProcedimiento(String id, String etiqueta, ArrayList<Parametro> params, int bytesRetorno) {
        int contador = tablaProcedimientos.size();
        PData data = new PData(id, etiqueta, params, bytesRetorno);
        tablaProcedimientos.put(etiqueta, data);
        return contador;
    }

    public int nuevoProcedimientoMain(String id, String etiqueta, ArrayList<Parametro> params, int bytesRetorno) {
        int contador = tablaProcedimientos.size();
        PData data = new PData(id, etiqueta, params, bytesRetorno);
        main = data;
        tablaProcedimientos.put(etiqueta, data);
        return contador;
    }

    //Recibimos los datos de un procedimiento con el nombre pasado por parametro
    public PData getProcedimeinto(String id) {
        return this.tablaProcedimientos.get(id);
    }

    //Crearemos la instruccion de 3 direcciones y almacenaremos en el array list
    public void generarInstr(TipoInstr instruccion, Operador op1, Operador op2, Operador dst) throws Exception {
        if (dst != null) {
            VData var = tablaVariables.get(dst.toString());
            if (instruccion.isTipo(TipoInstr.COPY) && op1.isLiteral() && (var == null || !var.estaInicializadaEnCodigoIntermedio())) {
                if (var == null) {
                    throw new Exception("Error, no se puede asignar a una variable inexistente");
                }
                var.inicializar(op1.getValor());
            }
        }
        Instruccion ins = new Instruccion(instruccion, op1, op2, dst);
        this.instrucciones.add(ins);
    }

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

    public PData getMain() {
        return main;
    }

    public HashSet<String> getEtiquetas() {
        return tablaEtiquetas;
    }
    
    public String tablas() {
        String s = "Tabla de procedimientos\n";
        for (Map.Entry<String, PData> entry : tablaProcedimientos.entrySet()) {
            s += entry.getValue() + "\n";
        }
        s += "\nTabla de variables\n";
        for (Map.Entry<String, VData> entry : tablaVariables.entrySet()) {
            s += entry.getKey() + ": " + entry.getValue() + "\n";
        }
//        s += "\nEtiquetas utilizadas en el programa\n";
//        for (String et : tablaEtiquetas) {
//            s += et + "\n";
//        }
        return s;
    }

}
