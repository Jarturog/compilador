/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSemantico;

import analizadorSemantico.DescripcionDefinicionTupla.DefinicionMiembro;
import analizadorSemantico.genCodigoIntermedio.Tipo;
import analizadorSintactico.ParserSym;
import analizadorSintactico.symbols.SymbolOperand;
import analizadorSintactico.symbols.SymbolTipo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import jflex.base.Pair;

public class DescripcionSimbolo {

    public static enum dtipus {
        dnula, dvar, dconst, dtipus //, dproc, dcamp, darg_in, darg, dindex
    }
    
    protected final String tipo, variableAsociada;
    protected final Tipo tipoBytes;

    protected int nivel;
    private boolean isConstante = false, valorAsignado = false;

    private final HashMap<String, DescripcionSimbolo> miembros; // por si su tipo es de tupla
    private final DescripcionSimbolo memberOf; // apunta a la tupla de la cual es miembro
    private final DescripcionDefinicionTupla tipoTupla;

//    public int first;
//    public int next;    //Apuntador al siguiente campo de una tupla
//    public String idcamp;   //Identificador del campo de la tupla

    /**
     * Variable
     */
    public DescripcionSimbolo(String t, boolean isConst, boolean v, DescripcionSimbolo tupla, DescripcionDefinicionTupla tipoTupla, String var) {
        tipo = t;
        isConstante = isConst;
        valorAsignado = v;
        memberOf = tupla; // miembro de una tupla
        this.tipoTupla = tipoTupla;
        if (tipoTupla != null) {
            miembros = new HashMap<>();
            for (DefinicionMiembro m : tipoTupla.miembros) {
                DescripcionSimbolo ds = new DescripcionSimbolo(m.tipo, m.isConst, m.valorAsignado, this, m.tipoTupla, var+"_"+m.nombre);
                miembros.put(m.nombre, ds);
            }
        } else {
            miembros = null;
        }
        this.tipoBytes = Tipo.getTipo(tipo);
        this.variableAsociada = var;
    }

    public String getTipo() {
        return tipo;
    }

    public int getNivel() {
        return this.nivel;
    }

    public void setNivel(int n) {
        this.nivel = n;
    }

    public final boolean isFunction() {
        return this instanceof DescripcionFuncion;
    }

    public final boolean isArray() {
        return this instanceof DescripcionArray;
    }

    public final boolean isTipoTupla() {
        return miembros != null;
    }

    public final boolean isDefinicionTupla() {
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
        String c = (isConstante() ? "constante " : "");
        String v = (valorAsignado ? "con" : "sin");
        String s = "Variable " + c + "de tipo '" + tipo + "' " + v + " valor asignado";
        return s + " declarado en el nivel " + nivel;
    }
}
