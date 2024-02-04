package genCodigoEnsamblador;

import analizadorSemantico.genCodigoIntermedio.Generador3Direcciones;
import analizadorSemantico.genCodigoIntermedio.Instruccion;
import analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr;
import static analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr.SCAN;
import static dartam.Dartam.DEBUG;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import jflex.base.Pair;

public class GeneradorEnsamblador {

    private static final int MARGEN = 12; // número de espacios en blanco hasta el código
    private static final int ANCHURA_INSTR = 8;
    private static final int ORIGEN_MEM = 1000; // dirección de memoria desde la que empieza el programa
    private String etiquetaActual = "";
    private final String nombreFichero;
    //public ArrayList<String> data, text;
    private final ArrayList<String> codigo, datos;
    private final ArrayList<Instruccion> instrucciones;
    private Instruccion instruccionActual = null;
    private boolean IO = false;

    private final HashMap<String, VData> variablesSinInicializar;
//    private HashMap<String, String> variableDictionary;
    private final HashMap<String, PData> procedureTable;
    private final HashSet<String> inicializaciones;
//
//    // Info from the current process
//    private Stack<ProcTableEntry> pteStack;
//    private ProcTableEntry currentPte;
//    private boolean firstParam = true;
//
//    private boolean printUsed = false;
//    private boolean scanUsed = false;

    public GeneradorEnsamblador(String fichero, Generador3Direcciones generador) throws Exception {
        nombreFichero = fichero;
        instrucciones = generador.getInstrucciones();
        variablesSinInicializar = generador.getTablaVariables();
        procedureTable = generador.getTablaProcedimientos();
        inicializaciones = generador.getVariablesInicializadas();
        codigo = new ArrayList<>();
        datos = new ArrayList<>();
//        text = new ArrayList<>();
//        pteStack = new Stack<>();
        procesarCodigo();
    }

    private void declararVariable(String id, VData data, Object inicializacion) throws Exception {
        datos.add(margen(id, data.getDeclaracionEnsamblador(inicializacion), "", ""));
    }

    private void procesarCodigo() throws Exception {

        add(nombreFichero.toUpperCase(), "", "", "Etiqueta inicial (main)");
        IO = true;
        if (IO) { // activar excepciones y teclado al inicializar el programa
            add("MOVE.L", "#32, D0", "Task 32 of TRAP 15: Hardware/Simulator");
            add("MOVE.B", "#5, D1", "Enable exception processing (for input/output)");
            add("TRAP", "#15", "Interruption generated");
            add("");
            add("MOVE.L", "#62, D0", "Task 62 of TRAP 15: Enable/Disable keyboard IRQ");
            add("MOVE.W", "#$0103, D1", "Enable keyboard IRQ level 1 for key up and key down");
            add("TRAP", "#15", "Interruption generated");
        }
        add("");
        for (int i = 0; i < instrucciones.size(); i++) {
            Instruccion instr = instrucciones.get(i);

            instruccionActual = instr;
            String instrCodigoIntermedio = instr.toString();
            if (instrCodigoIntermedio.isEmpty()) {
                add("");
            } else {
                add("; " + "-".repeat(MARGEN - 3), instr.toString(), "", "");
            }
            String dst = null;
            if (instr.dst != null) {
                dst = instr.dst.getOperadorEnsamblador();
            }
            String op1 = instr.op1 == null ? null : instr.op1.getOperadorEnsamblador();
            String op2 = instr.op2 == null ? null : instr.op2.getOperadorEnsamblador();
            if (instr.dst != null && !inicializaciones.isEmpty() && inicializaciones.contains(instr.dst.toString())) {
                String id = instr.dst.toString();
                inicializaciones.remove(id);
                VData data = variablesSinInicializar.remove(id);
                if (data == null) {
                    throw new Exception("Se ha intentado inicializar una varible inexistente: " + id);
                }
                declararVariable(id, data, instr.op1.getValor());
            } else {
                String et = dst;
                if(instruccionActual.getTipo().tieneEtiqueta()) {
                    et = procedureTable.containsKey(dst) ? "" : "." + dst;
                }
                procesarInstruccion(op1, op2, dst, et);
            }

        }
        String et = getEtiqueta();
        if (!et.isEmpty()) {
            add(et, "NOP", "", "NO OPERATION");
        }
        add("SIMHALT", "", "Fin de la ejecución");
        add("END " + nombreFichero.toUpperCase(), "", "Fin del programa");
        for (HashMap.Entry<String, VData> variable : variablesSinInicializar.entrySet()) {
            declararVariable(variable.getKey(), variable.getValue(), null);
        }
    }

    private void procesarInstruccion(String op1, String op2, String dst, String dstConPunto) throws Exception {
//        String extensiones = instruccionActual.getExtensiones68K();
//        String biggestExtension = (extensiones.contains("L") ? ".L" : (extensiones.contains("W") ? "W" : "B"));
//        
        switch (instruccionActual.getTipo()) {
            case COPY -> { // op1 -> dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("MOVE.L ", "D0, " + dst, dst + " = D0");
            }
            case NEG -> { // -op1 -> dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("NEG.L", "D0", "D0 = -D0");
                add("MOVE.L", "D0, " + dst, dst + " = D0");
            }
            case ADD -> { // op1+op2 -> dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("ADD.L", op2 + ", D0", "D0 = D0 + " + op2);
                add("MOVE.L", "D0, " + dst, dst + " = D0");
            }
            case SUB -> { // op1-op2 -> dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("SUB.L", op2 + ", D0", "D0 = D0 - " + op2);
                add("MOVE.L", "D0, " + dst, dst + " = D0");
            }
            case MUL -> { // op1*op2 -> dst
                // EASy68K no permite MULS.L, por lo que se ha de operar así:
                // A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16
                add("; " + " ".repeat(MARGEN - 3), "A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16", "", "");
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("MOVE.L", op2 + ", D1", "D1 = " + op2);
                add("MOVE.W", "D0, D2", "D2.L = D1.L");
                add("MOVE.W", "D1, D3", "D1.L = D3.L");
                add("ASR.L", "#8, D0", "FIRST 8 BITS OF D0 MOVED RIGHT");
                add("ASR.L", "#8, D0", "D0.L = old D0.H");
                add("ASR.L", "#8, D1", "FIRST 8 BITS OF D1 MOVED RIGHT");
                add("ASR.L", "#8, D1", "D1.L = old D1.H");
                add("MULS.W","D1, D0", "D0 = D0 * D1");
                add("MULS.W","D2, D3", "D3 = D2 * D3");
                add("ASL.L", "#8, D0", "FIRST 8 BITS OF D0 MOVED LEFT");
                add("ASL.L", "#8, D0", "D0.H = old D0.L");
                add("ADD.L", "D3, D0", "D0 = D0 + D3");
                add("MOVE.L", "D0, " + dst, dst + " = D0");
            }
            case DIV -> { // op1/op2 -> dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("DIVS.L", op2 + ", D0", "D0.h = D0 % " + op2 + ". D0.l = D0 / " + op2);
                add("MOVE.B", "D0, " + dst, dst + " = D0.l");
            }
            case MOD -> { // op1%op2 -> dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("DIVS.L", op2 + ", D0", "D0.h = D0 % " + op2 + ". D0.l = D0 / " + op2);
                add("LSR.L", "#8, D0", "D0.l = D0.h");
                add("MOVE.B", "D0, " + dst, dst + " = D0.l");
            }
            case NOT -> { // not op1 -> dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("NOT.L", "D0", "D0 = not D0");
                add("MOVE.L", "D0, " + dst, dst + " = D0");
            }
            case AND -> { // op1 and op2 -> dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("AND.L", op2 + ", D0", "D0 = D0 and " + op2);
                add("MOVE.L", "D0, " + dst, dst + " = D0");
            }
            case OR -> { // op1 or op2 -> dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("OR.L", op2 + ", D0", "D0 = D0 or " + op2);
                add("MOVE.L", "D0, " + dst, dst + " = D0");
            }
            case GOTO -> { // goto dst
                add(getEtiqueta(), "JMP", dstConPunto, "goto " + dstConPunto);
            }
            case IFEQ -> { // if op1 = op2 goto dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("CMP.L", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BEQ", dstConPunto, "IF Z FLAG = 1 GOTO " + dstConPunto);
            }
            case IFNE -> { // if op1 /= op2 goto dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("CMP.L", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BNE", dstConPunto, "IF Z FLAG = 0 GOTO " + dstConPunto);
            }
            case IFGE -> { // if op1 >= op2 goto dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("CMP.L", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BLT", dstConPunto, "IF (N XOR V) FLAGS = 1 GOTO " + dstConPunto);
            }
            case IFLT -> { // if op1 < op2 goto dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("CMP.L", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BGE", dstConPunto, "IF (N XOR V) FLAGS = 0 GOTO " + dstConPunto);
            }
            case IFGT -> { // if op1 > op2 goto dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("CMP.L", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BLE", dstConPunto, "IF ((N XOR V) OR Z) FLAGS = 1 GOTO " + dstConPunto);
            }
            case IFLE -> { // if op1 <= op2 goto dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("CMP.L", op2 + ", D0", "UPDATE FLAGS WITH D0 - " + op2);
                add("BGT", dstConPunto, "IF ((N XOR V) OR Z) FLAGS = 0 GOTO " + dstConPunto);
            }
            case PRINT -> { // print(dst)
                add(getEtiqueta(), "LEA.L", dst + ", A1", "A1 = " + dst);
                add("MOVE.L", "#14, D0", "Task 14 of TRAP 15: Display the NULL terminated string pointed to by (A1)");
                add("TRAP", "#15", "Interruption generated");
            }
            case SCAN -> { // scan(dst)
                add(getEtiqueta(), "CLR.L", "D1", "Empty D1 for later use");
                add("MOVE.L", "#5, D0", "Task 5 of TRAP 15: Read single ASCII character from the keyboard into D1.B");
                add("TRAP", "#15", "Interruption generated");
                add("MOVE.L", "D1, " + dst, "D1 = character typed on the keyboard");
            }
            case READ -> { // read(dst)
                add(getEtiqueta(), "LEA.L", dst + ", A1", "Pre: (A1) null terminated file name");
                add("MOVE.L", "#51, D0", "Task 51 of TRAP 15: Open existing file");
                add("TRAP", "#15", "Interruption generated");
                add("MOVE.L", "D1, " + dst, "Post: D1.L contains the File-ID (FID)"); // esto solo abre el file, no lee ------------------------------
            }
            case WRITE -> {
                // ---
            }
            case IND_ASS -> { // dst[op1] = op2
                add(getEtiqueta(), "MOVEA.L", dst + ", A0", "A0 = " + dst);
                add("ADDA.L ", op1 + ", A0", "A0 = A0 + " + op1);
                add("MOVE.L ", op2 + ", (A0)", "(A0) = " + op2);
            }
            case IND_VAL -> { // dst = op1[op2]
                add(getEtiqueta(), "MOVEA.L", op1 + ", A0", "A0 = " + op1);
                add("ADDA.L ", op2 + ", A0", "A0 = A0 + " + op2);
                add("MOVE.L ", "(A0), " + dst, dst + " = (A0)");
            }
            case PARAM_S -> { // param_s dst
                add(getEtiqueta(), "MOVE.L", dst + ", -(SP)", "PUSH INTO STACK " + dst);
            }
            case PMB -> { // pmb dst
                PData func = procedureTable.get(dst);
                for (Pair<String, String> par : func.getParametros()) {
                    // par.snd son los bytes
                    add(getEtiqueta(), "MOVE.L", "(SP)+, " + par.fst, par.fst + " = POP FROM STACK");
                }
            }
            case RETURN -> { // rtn dst, ?
                PData func = procedureTable.get(dst);
                if (func.getBytesRetorno() > 0) {
                    add(getEtiqueta(), "MOVE.L", dstConPunto + ", -(SP)", "PUSH INTO STACK " + dstConPunto);
                }
                add(getEtiqueta(), "RTS", "", "RETURN TO SUBROUTINE " + dstConPunto);
            }
            case CALL -> { // call dst
                add(getEtiqueta(), "JSR", dstConPunto, "JUMP TO SUBROUTINE " + dstConPunto);
                if (op1 != null) {
                    add("MOVE.L", "(SP)+, " + op1, op1 + " = POP FROM STACK");
                }
            }
            case SKIP -> { // dst: skip
                setEtiqueta(dstConPunto); // para etiquetas locales ponemos .
            }
            case SEPARADOR -> { // no hace nada pero resulta en mayor legibilidad del código ensamblador
                codigo.add("");
            }
            default ->
                throw new Exception("Instrucción " + instruccionActual + " no se puede pasar a código ensamblador");
        }
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
        String s = cabecera();
        for (String d : datos) {
            s += d + "\n";
        }
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
                + "; ==============================================================================\n"
                + "\n"
                + margen("", "ORG", "$" + ORIGEN_MEM, "Origen") + "\n"
                + "\n";
        //add("INCLUDE", "\"PRINT.X68\"", "Escritura en consola");
        //add("INCLUDE", "\"SCAN.X68\"", "Lectura de teclado");
        //add("INCLUDE", "\"WRITE.X68\"", "Escritura de ficheros");
        //add("INCLUDE", "\"READ.X68\"", "Lectura de ficheros");
    }

    private void add(String s) {
        codigo.add(s);
    }

    private void add(String i1, String i2, String com) {
        codigo.add(margen("", i1, i2, com));
    }

    private void add(String et, String i1, String i2, String com) {
        codigo.add(margen(et, i1, i2, com));
    }

    private static String mCol(String instr) {
        int margen = MARGEN - instr.length();
        if (margen < 1) {
            margen = 1;
        }
        return instr + " ".repeat(margen);
    }

    private static String margen(String et, String i1, String i2, String com) {
        int margenIzq = MARGEN - et.length();
        if (margenIzq < 1) {
            margenIzq = 1; // mínimo un espacio
        }
        String parteIzq = et + " ".repeat(margenIzq) + mCol(i1) + i2;
        int margenDer = 3 * MARGEN - (parteIzq.length());
        if (margenDer < 1) {
            margenDer = 1;
        }
        return parteIzq + " ".repeat(margenDer) + (com.isEmpty() ? "" : "; ") + com;
    }

}
