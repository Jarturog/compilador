/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSemantico;

import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolOperand;
import analizadorSintactico.symbols.SymbolTipo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import jflex.base.Pair;

public class DescripcionSimbolo {

    private String tipo;

    private int nivel;
    private boolean isConstante = false, valorAsignado = false;

    
    private final HashMap<String, DescripcionSimbolo> miembros;
    
    private DescripcionSimbolo memberOf = null; // apunta a la tupla de la cual es miembro

    public int first;
    public int next;    //Apuntador al siguiente campo de una tupla
    public String idcamp;   //Identificador del campo de la tupla

    

    /**
     * Copia de la descripción (para copiar de una plantilla de un miembro de
     * una tupla a una instancia de miembro de una instancia de tupla)
     */
    public DescripcionSimbolo(DescripcionSimbolo d) {
        this.tipo = d.tipo;
        this.nivel = d.nivel;
        this.isConstante = d.isConstante;
        this.valorAsignado = d.valorAsignado;
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
    public DescripcionSimbolo() {
        tipo = null;
        miembros = null;
    }
    
    /**
     * Otro?
     */
    public DescripcionSimbolo(String tipo) {
        this.tipo = tipo;
        miembros = null;
    }

    /**
     * Variable
     */
    public DescripcionSimbolo(String t, boolean isConst, boolean v, DescripcionSimbolo tupla) {
        tipo = t;
        isConstante = isConst;
        valorAsignado = v;
        memberOf = tupla; // miembro de una tupla
        if (isTupla()) {
            miembros = new HashMap<String, DescripcionSimbolo>(); // copia
            for (HashMap.Entry<String, DescripcionSimbolo> entry : memberOf.miembros.entrySet()) {
                miembros.put(entry.getKey(), new DescripcionSimbolo(entry.getValue()));
            }
        } else {
            miembros = null;
        }
    }

    

    

    

    



//    public int getNivel() {
//        return this.nivel;
//    }
//
//    public void setNivel(int n) {
//        this.nivel = n;
//    }



    public boolean isFunction() {
        return this instanceof DescripcionFuncion;
    }

    public boolean isArray() {
        return this instanceof DescripcionArray;
    }

    public boolean isTupla() {
        return this instanceof DescripcionDefinicionTupla;
    }
    
    private boolean isVariableTipoTupla() {
        if (tipo == null) {
            return false;
        }
        return tipo.startsWith(ParserSym.terminalNames[ParserSym.KW_TUPLE]);
    }

    public DescripcionSimbolo getMember(String id) throws Exception {
        if (!isVariableTipoTupla()) {
            throw new Exception("Se ha intentado acceder al miembro de una variable que no es de tipo tupla, sino " + tipo);
        }
        return miembros.get(id);
    }

    public String getNombreTupla() throws Exception {
        if (!isVariableTipoTupla()) {
            throw new Exception("Se ha intentado acceder al miembro de una variable que no es de tipo tupla, sino " + tipo);
        }
        return tipo.substring(tipo.indexOf(" ") + 1);
    }

    public void anyadirMiembro(String id, DescripcionSimbolo ds) throws Exception {
        if (!isVariableTipoTupla()) {
            throw new Exception("Se ha intentado acceder al miembro de una variable que no es de tipo tupla, sino " + tipo);
        }
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
    
    @Override
    public String toString() {
        String s;
        if (isFunction()) {
            String params = "";
            for (Pair<String, DescripcionSimbolo> param : parametros) {
                params += param.fst + " ";
            }
            params = params.length() > 0 ? "con argumentos" + params.substring(0, params.length() - 1) : "sin argumentos";
            s = "Función de tipo '" + tipo + "'  " + params;
        } else if (isArray()) {
            String dim = "";
            for (SymbolOperand op : dimensiones) {
                dim += op + " ";
            }
            s = "Array de tipo '" + tipo + "' con dimensiones " + dim.substring(0, dim.length() - 1);
        } else if (isTupla()) {
            String m = "";
            for (String miembro : miembros.keySet()) {
                m += miembro + " ";
            }
            m = m.length() > 0 ? "con miembros" + m.substring(0, m.length() - 1) : "sin miembros";
            s = "Tupla " + m;
        } else {
            String c = (isConstante() ? "consante" : "");
            String v = (valorAsignado ? "con" : "sin");
            s = "Variable "+c+" de tipo '" + tipo + "' " + v + " valor asignado";
        }
        return s + " declarado en el nivel " + nivel;
    }
}
