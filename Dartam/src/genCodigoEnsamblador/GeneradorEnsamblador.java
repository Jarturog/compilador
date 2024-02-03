package genCodigoEnsamblador;

import analizadorSemantico.genCodigoIntermedio.Instruccion;
import analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr;
import static analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr.SCAN;
import static dartam.Dartam.DEBUG;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class GeneradorEnsamblador {

    private static final int MARGEN = 12; // número de espacios en blanco hasta el código
    private static final int ANCHURA_INSTR = 8;
    private static final int ORIGEN_MEM = 1000; // dirección de memoria desde la que empieza el programa
    private String etiquetaActual = "";
    private final String nombreFichero;
    //public ArrayList<String> data, text;
    private final ArrayList<String> codigo;
    private ArrayList<Instruccion> instrucciones;
    private Instruccion instruccionActual = null;

//    private HashMap<String, VarTableEntry> variableTable;
//    private HashMap<String, String> variableDictionary;
//    private HashMap<String, ProcTableEntry> procedureTable;
//
//    // Info from the current process
//    private Stack<ProcTableEntry> pteStack;
//    private ProcTableEntry currentPte;
//    private boolean firstParam = true;
//
//    private boolean printUsed = false;
//    private boolean scanUsed = false;
    public GeneradorEnsamblador(String fichero, ArrayList<Instruccion> instrs) {
        nombreFichero = fichero;
        instrucciones = instrs;
//        variableTable = c3a.getVariableTable();
//        procedureTable = c3a.getProcedureTable();
//        variableDictionary = new HashMap<>();
        codigo = new ArrayList<>();
//        text = new ArrayList<>();
//        pteStack = new Stack<>();
        procesarCodigo();
    }

    private void procesarCodigo() {

        add(cabecera());
        add("");
        add("ORG", "$" + ORIGEN_MEM, "Origen");
        add("");
        add("INCLUDE", "\"PRINT.X68\"", "Escritura en consola");
        add("INCLUDE", "\"SCAN.X68\"", "Lectura de teclado");
        add("INCLUDE", "\"WRITE.X68\"", "Escritura de ficheros");
        add("INCLUDE", "\"READ.X68\"", "Lectura de ficheros");
        //add("START", "", "Etiqueta inicial (main)");
        add("");
        for (Instruccion instr : instrucciones) {
            instruccionActual = instr;
            add(instr.toString().isEmpty() ? "" : "; " + instr.toString());
            String dest = instr.dst == null ? null : instr.dst.toString();
            String op1 = instr.op1 == null ? null : instr.op1.toString();
            String op2 = instr.op2 == null ? null : instr.op2.toString();
            procesarInstruccion(op1, op2, dest);
        }
        add("SIMHALT", "", "Fin de la ejecución");
        add("END", "START", "Fin del programa");

    }

    private void procesarInstruccion(String op1, String op2, String dest) {
        switch (instruccionActual.tipo()) {
            case COPY -> { // op1 -> dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("MOVE.W ", dest + ", D0", dest + " = D0");
            }
            case NEG -> { // -op1 -> dst
                add(getEtiqueta(), "MOVE.W", dest + ", D0", "D0 = " + dest);
                add("NEG.W", "D0", "D0 = -D0");
                add("MOVE.W", "D0, " + dest, dest + " = D0");
            }
            case ADD -> { // op1+op2 -> dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("ADD.W", op2 + ", D0", "D0 = D0 + " + op2);
                add("MOVE.W", "D0, " + dest, dest + " = D0");
            }
            case SUB -> { // op1-op2 -> dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("SUB.W", op2 + ", D0", "D0 = D0 - " + op2);
                add("MOVE.W", "D0, " + dest, dest + " = D0");
            }
            case MUL -> { // op1*op2 -> dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("MULS.W", op2 + ", D0", "D0 = D0 * " + op2);
                add("MOVE.W", "D0, " + dest, dest + " = D0");
            }
            case DIV -> { // op1/op2 -> dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("DIVS.W", op2 + ", D0", "D0.h = D0 % " + op2 + ". D0.l = D0 / " + op2);
                add("MOVE.B", "D0, " + dest, dest + " = D0.l");
            }
            case MOD -> { // op1%op2 -> dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("DIVS.W", op2 + ", D0", "D0.h = D0 % " + op2 + ". D0.l = D0 / " + op2);
                add("LSR.W", "#8, D0", "D0.l = D0.h");
                add("MOVE.B", "D0, " + dest, dest + " = D0.l");
            }
            case NOT -> { // not op1 -> dst
                add(getEtiqueta(), "MOVE.W", dest + ", D0", "D0 = " + dest);
                add("NOT.W", "D0", "D0 = not D0");
                add("MOVE.W", "D0, " + dest, dest + " = D0");
            }
            case AND -> { // op1 and op2 -> dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("AND.W", op2 + ", D0", "D0 = D0 and " + op2);
                add("MOVE.W", "D0, " + dest, dest + " = D0");
            }
            case OR -> { // op1 or op2 -> dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("OR.W", op2 + ", D0", "D0 = D0 or " + op2);
                add("MOVE.W", "D0, " + dest, dest + " = D0");
            }
            case GOTO -> { // goto dst
                add(getEtiqueta(), "JMP", dest, "goto " + dest);
            }
            case IFEQ -> { // if op1 = op2 goto dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("CMP.W", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BEQ", dest, "IF Z FLAG = 1 GOTO " + dest);
            }
            case IFNE -> { // if op1 /= op2 goto dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("CMP.W", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BNE", dest, "IF Z FLAG = 0 GOTO " + dest);
            }
            case IFGE -> { // if op1 >= op2 goto dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("CMP.W", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BGE", dest, "IF (N XOR V) FLAGS = 0 GOTO " + dest);
            }
            case IFGT -> { // if op1 > op2 goto dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("CMP.W", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BGT", dest, "IF ((N XOR V) OR Z) FLAGS = 0 GOTO " + dest);
            }
            case IFLE -> { // if op1 <= op2 goto dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("CMP.W", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BLE", dest, "IF ((N XOR V) OR Z) FLAGS = 1 GOTO " + dest);
            }
            case IFLT -> { // if op1 < op2 goto dst
                add(getEtiqueta(), "MOVE.W", op1 + ", D0", "D0 = " + op1);
                add("CMP.W", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BLT", dest, "IF (N XOR V) FLAGS = 1 GOTO " + dest);
            }
            case PRINT -> { // print(dst)
                // ---
            }
            case SCAN -> {
                // ---
            }
            case READ -> {
                // ---
            }
            case WRITE -> {
                // ---
            }
            case IND_ASS -> { // dst[op1] = op2
                add(getEtiqueta(), "MOVEA.W", dest + ", A0", "A0 = " + dest);
                add("ADDA.W ", op1 + ", A0", "A0 = A0 + " + op1);
                add("MOVE.W ", op2 + ", (A0)", "(A0) = " + op2);
            }
            case IND_VAL -> { // dst = op1[op2]
                add(getEtiqueta(), "MOVEA.W", op1 + ", A0", "A0 = " + op1);
                add("ADDA.W ", op2 + ", A0", "A0 = A0 + " + op2);
                add("MOVE.W ", "(A0), " + dest, dest + " = (A0)");
            }
            case PARAM_S -> { // param_s dst
                String extension = getExtension(dest);
                add(getEtiqueta(), "MOVE." + extension, dest + ", -(SP)", "PUSH INTO STACK " + dest);
            }
            case PMB -> { // pmb dst
                // add("MOVE." + extension, "(SP)+, " + dest, dest + " = POP FROM STACK");
            }
            case RETURN -> { // rtn dst, ?
                String extension = getExtension(op1);
                String et = getEtiqueta();
                // mientras haya cosas que devolver
                add(et, "MOVE." + extension, dest + ", -(SP)", "PUSH INTO STACK " + dest);
                add("RTS", "", "RETURN TO SUBROUTINE " + dest);
            }
            case CALL -> { // call dst
                String extension = getExtension(dest);
                add(getEtiqueta(), "JSR", dest, "JUMP TO SUBROUTINE " + dest);
                add("MOVE." + extension, "(SP)+, " + dest, dest + " = POP FROM STACK");

            }
            case SKIP -> { // dst: skip
                setEtiqueta(dest);
            }
            case SEPARADOR -> { // no hace nada pero resulta en mayor legibilidad del código ensamblador
                codigo.add("");
            }
            default ->
                throw new RuntimeException("Operation not implemented");//+ instruccion);
        }
    }

    private String getExtension(String s) {
        return "W";
    }

    private String getEtiqueta() {
        String et = etiquetaActual;
        etiquetaActual = "";
        return et;
    }

    private void setEtiqueta(String s) {
        etiquetaActual = s;
    }

    @Override
    public String toString() {
        String s = "";
        for (String i : codigo) {
            s += i + "\n";
        }
        return s;
    }

    private String cabecera() {
        return "; ==============================================================================\n"
                + "; TITLE       : " + nombreFichero + "\n"
                + "; COMPILED BY : " + System.getProperty("user.name") + "\n"
                + "; COMPILER BY : Juan Arturo Abaurrea Calafell\n"
                + ";               Dani Salanova Dmitriyev\n"
                + ";               Marta González Juan\n"
                + "; ==============================================================================\n";
    }

    private void add(String s) {
        codigo.add(s);
    }

    private void add(String i1, String i2, String com) {
        codigo.add(margenCom(i1, i2, com));
    }

    private void add(String et, String i1, String i2, String com) {
        codigo.add(margenEtCom(et, i1, i2, com));
    }

    private static String mCol(String instr) {
        int margen = MARGEN - instr.length();
        if (margen < 1) {
            margen = 1;
        }
        return instr + " ".repeat(margen);
    }

    private static String margen(String i) {
        return margenEt("", i, "");
    }

    private static String margen(String i1, String i2) {
        return margenEt("", i1, i2);
    }

    private static String margenEt(String et, String i1, String i2) {
        int margenIzq = MARGEN - et.length();
        if (margenIzq < 1) {
            margenIzq = 1;
        }
        return et + " ".repeat(margenIzq) + mCol(i1) + i2;
    }

    private static String margenCom(String com) {
        return margenEtCom("", "", "", com);
    }

    private static String margenCom(String i, String com) {
        return margenEtCom("", i, "", com);
    }

    private static String margenCom(String i1, String i2, String com) {
        return margenEtCom("", i1, i2, com);
    }

    private static String margenEtCom(String et, String i1, String i2, String com) {
        String parteIzq = margenEt(et, i1, i2);
        int margenDer = 3 * MARGEN - (parteIzq.length());
        if (margenDer < 1) {
            margenDer = 1;
        }
        return parteIzq + " ".repeat(margenDer) + (com.isEmpty() ? "" : "; ") + com;
    }

}
