/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package genCodigoEnsamblador;

import analizadorSemantico.DescripcionFuncion.Parametro;
import analizadorSemantico.genCodigoIntermedio.TipoVariable;
import analizadorSintactico.ParserSym;
import java.util.ArrayList;
import jflex.base.Pair;

public class ProcInfo {
    private final String nombre, etiqueta;
    private final ArrayList<Parametro> parametros; // id, bytes
    private final int bytesRetorno;
    
    public ProcInfo(String nombre, String etiqueta, ArrayList<Parametro> params, int bytesValorRetorno) {
        this.nombre = nombre;
        this.etiqueta = etiqueta;
        this.parametros = params;
        this.bytesRetorno = bytesValorRetorno;
    }

    //Recibimos la etiqueta del procedimientos
    public String getEtiqueta() {
        return this.etiqueta;
    }

    //Metodo con el que podemos recuperar los parametros del procedimiento
    public ArrayList<Parametro> getParametros() {
        return parametros;
    }

    //Metodo que nos devolvera la cantidad de parametros que tiene
    public int getCantidadParametros() {
        return parametros.size();
    }

    //Metodo que permite añadir un nuevo parametro al procedimiento
    public void añadirParametro(String i, String variable, String tipo, TipoVariable t) throws Exception {
        this.parametros.add(new Parametro(i, variable, tipo));
    }

    public int getBytesRetorno() {
        return bytesRetorno;
    }

    public static enum TipoMetodoEspecial {
        PRINT, SCAN, READ, WRITE
    }

    public static TipoMetodoEspecial getEspecial(String id) throws Exception {
        if (id.equalsIgnoreCase(ParserSym.terminalNames[ParserSym.SHOW])) {
            return TipoMetodoEspecial.PRINT;
        } else if (id.equalsIgnoreCase(ParserSym.terminalNames[ParserSym.SCAN])) {
            return TipoMetodoEspecial.SCAN;
        } else if (id.equalsIgnoreCase(ParserSym.terminalNames[ParserSym.FROM])) {
            return TipoMetodoEspecial.READ;
        } else if (id.equalsIgnoreCase(ParserSym.terminalNames[ParserSym.INTO])) {
            return TipoMetodoEspecial.WRITE;
        }
        return null; //throw new Exception("No existe el método especial " + id);
    }
    
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public String toString() {
        String params = "";
        for (Parametro parametro : parametros) {
            params += parametro + ", ";
        }
        if (!params.isEmpty()) {
            params = " y sus parámetros son " + params.substring(0, params.length() - 2);
        } else {
            params = " y no recibe parámetros";
        }
        return nombre + ": identificado por " + etiqueta + ", devuelve " + bytesRetorno + " bytes de retorno" + params;
    }
    
}
