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

    public final String tipo, variableAsociada;
    private Integer nBytes = null; // null si es un método
    private int nivel;
    private boolean isConstante = false, valorAsignado = false;
    private final ArrayList<DefinicionMiembro> miembros; // por si su tipo es de tupla
    private final DescripcionDefinicionTupla tipoTupla;

    /**
     * Variable
     */
    public DescripcionSimbolo(String t, boolean isConst, boolean v, DescripcionDefinicionTupla tipoTupla, String var) {
        tipo = t;
        isConstante = isConst;
        valorAsignado = v;
        this.tipoTupla = tipoTupla;
        if (tipoTupla != null) {
            miembros = new ArrayList<>(tipoTupla.miembros);
            this.asignarValor();
        } else {
            miembros = null;
        }
        Tipo tipoPrimitivo = Tipo.getTipo(this.tipo);
        if (tipoPrimitivo != null) { // solo asigna bytes para tipos primitivos
            nBytes = tipoPrimitivo.bytes;
        } else if (isTipoTupla()) {
            nBytes = tipoTupla.getBytes();
        } else if (isArray()) {
            // ----
        }
        this.variableAsociada = var;
    }

    public DescripcionSimbolo(DescripcionSimbolo d) {
        tipo = d.tipo;
        variableAsociada = d.variableAsociada;
        nBytes = d.nBytes;
        nivel = d.nivel;
        isConstante = d.isConstante;
        valorAsignado = d.valorAsignado;
        miembros = d.miembros;
        tipoTupla = d.tipoTupla;
    }

    public Integer getBytes() {
        return nBytes;
    }
    
    public void setBytes(Integer b) {
        nBytes = b;
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
        return tipoTupla != null;
    }

    public final boolean isDefinicionTupla() {
        return this instanceof DescripcionDefinicionTupla;
    }

    public DefinicionMiembro getMember(String id) throws Exception {
        if (!isTipoTupla()) {
            throw new Exception("Se ha intentado acceder al miembro de una variable que no es de tipo tupla, sino " + tipo);
        }
        for (DefinicionMiembro m : miembros) {
            if (id.equals(m.nombre)) {
                return m;
            }
        }
        return null;
    }

    public String getNombreTupla() throws Exception {
        if (!isTipoTupla()) {
            throw new Exception("Se ha intentado acceder al miembro de una variable que no es de tipo tupla, sino " + tipo);
        }
        int idxArr = tipo.length();
        if (isArray()) {
            idxArr = tipo.lastIndexOf(" ");
        }
        return tipo.substring(tipo.indexOf(" ") + 1, idxArr);
    }

    public boolean tieneValorAsignado() {
        return valorAsignado;
    }

    public final void asignarValor() {
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
