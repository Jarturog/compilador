package genCodigoIntermedio;

import java.util.ArrayList;

import analizadorSintactico.ParserSym;
import java.util.HashMap;

/**
 * Proc.. table entry
 */
public class PTEntry {
    public static final int REGISTER_SIZE = 8; // Registers are 64 bits, 8 bytes
    public static final int UNKNOWN = -1;
    public int depth;
    public String eStart;
    public String eEnd;
    public int numParams;
    public ArrayList<String> params; // Arraylist to have the parameters in order
    public HashMap<String, VTEntry> variableTable;

    public PTEntry(){
        // default values
        depth = 1;
        numParams = 0;
        variableTable = new HashMap<>();
        params = new ArrayList<>();
    }

    public int getVarsOccupation(){
        int occupation = 0;
        for (VTEntry vte : variableTable.values()) {
            int o = vte.getOccupation();
            if(o == UNKNOWN) return UNKNOWN;
            occupation += o;
        }
        return occupation;
    }

    public void prepareForMachineCode(){
        cleanVariables();
        calculateDisplacements();
    }

    private void calculateDisplacements(){
        VTEntry vte;

        // We calculate the displacements of the parameters. They are positive.
        int paramDisplacement = REGISTER_SIZE * 2; // We reserve space for DISP and BP and for the return
        for(String s : params){
            vte = variableTable.get(s);
            vte.displacement = paramDisplacement;
            paramDisplacement += REGISTER_SIZE;
        }

        int localVarDisplacement = 0;
        for (String s : variableTable.keySet()) {
            vte = variableTable.get(s);
            if(vte.displacement == 0 && vte.getOccupation() != UNKNOWN){
                localVarDisplacement -= vte.getOccupation();
                vte.displacement = localVarDisplacement;
            }
        }
    }

    private void cleanVariables(){
        HashMap<String, VTEntry> cleanVariableTable = new HashMap<>();
        for (String s : variableTable.keySet()) {
            VTEntry vte = variableTable.get(s);
            cleanVariableTable.put(vte.tName, vte);
        }
        variableTable = cleanVariableTable;
    }

    @Override
    public String toString(){
        return "[depth: " + depth + ", eStart: " + eStart + ", eEnd: " + eEnd + ", numParams: " + numParams + ", varsOccup: " + getVarsOccupation() + "]";
    }
}