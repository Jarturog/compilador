package genCodigoEnsamblador;

import analizadorSemantico.genCodigoIntermedio.Generador3Direcciones;
import analizadorSemantico.genCodigoIntermedio.Instruccion;
import analizadorSemantico.genCodigoIntermedio.Operador;
import analizadorSemantico.genCodigoIntermedio.Tipo;
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
    private final ArrayList<String> codigo, datos, subprogramas;
    private final ArrayList<Instruccion> instrucciones;
    private Instruccion instruccion = null;
    private boolean printUsado = false, scanUsado = false, readUsado = false, writeUsado = false, seConcatena = false;

    private final HashMap<String, VData> variables;
//    private HashMap<String, String> variableDictionary;
    private final HashMap<String, PData> procedureTable;
    private final HashSet<String> etiquetas;
    private final PData main;
    private final String etConc;
    private int DnActual = 0, AnActual = 0;
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
        main = generador.getMain();
        variables = generador.getTablaVariables();
        procedureTable = generador.getTablaProcedimientos();
        etiquetas = generador.getEtiquetas();
        codigo = new ArrayList<>();
        datos = new ArrayList<>();
        subprogramas = new ArrayList<>();
        etConc = crearEtiqueta("CONCATENAR");
//        text = new ArrayList<>();
//        pteStack = new Stack<>();
        procesarCodigo();
    }

    private void procesarCodigo() throws Exception {
        String etiqueta_main = crearEtiqueta(nombreFichero.toUpperCase());
        add(etiqueta_main, "", "", "Etiqueta inicial (main)");
        if (printUsado || scanUsado || readUsado || writeUsado) { // activar excepciones y teclado al inicializar el programa
            add("");
            add("MOVE.L", "#32, D0", "Task 32 of TRAP 15: Hardware/Simulator");
            add("MOVE.B", "#5, D1", "Enable exception processing (for input/output)");
            add("TRAP", "#15", "Interruption generated");
        }
        if (scanUsado) {
            add("");
            add("MOVE.L", "#62, D0", "Task 62 of TRAP 15: Enable/Disable keyboard IRQ");
            add("MOVE.W", "#$0103, D1", "Enable keyboard IRQ level 1 for key up and key down");
            add("TRAP", "#15", "Interruption generated");
        }
        add("");
        add("JSR", main.getEtiqueta(), "Se ejecuta el main");
        add("SIMHALT", "", "Fin de la ejecución");
        add("");
        for (int i = 0; i < instrucciones.size(); i++) {
            instruccion = instrucciones.get(i);
            String dst = null;
            if (instruccion.dst != null) {
                dst = instruccion.dst.toStringEnsamblador();
            }
            String op1 = instruccion.op1 == null ? null : instruccion.op1.toStringEnsamblador();
            String op2 = instruccion.op2 == null ? null : instruccion.op2.toStringEnsamblador();
            VData data = instruccion.dst == null ? null : variables.get(instruccion.dst.toString());
            if (instruccion.dst != null && data != null && data.estaInicializadaEnCodigoIntermedio() && !data.estaInicializadaEnCodigoEnsamblador()) {
                data.inicializar();
                declararVariable(instruccion.dst.toString(), data, instruccion.op1.getValor());
            } else {
                String instrCodigoIntermedio = instruccion.toString();
                if (instrCodigoIntermedio.isEmpty()) {
                    add("");
                } else {
                    add("; " + "-".repeat(MARGEN - 3), instruccion.toString(), "", "");
                }
                String et = dst;
                // si es goto, ifxx, skip, pmb, call, etc... y no es una función añade . para indicar que es local
                if (instruccion.getTipo().tieneEtiqueta() && !procedureTable.containsKey(dst)) {
                    et = "." + dst;
                }
                procesarInstruccion(op1, op2, dst, et);
            }
        }
        String et = getEtiqueta();
        if (!et.isEmpty()) {
            throw new Exception("Etiqueta inesperada: " + et);//add(et, "NOP", "", "NO OPERATION");
        }
        if (seConcatena) {
            crearSubprogramaConcatenacion();
        }
        for (String s : subprogramas) {
            add(s);
        }
        add("END " + etiqueta_main, "", "Fin del programa");
        for (HashMap.Entry<String, VData> variable : variables.entrySet()) {
            declararVariable(variable.getKey(), variable.getValue(), null);
        }
    }

    private void crearSubprogramaConcatenacion() {
        String etConc2 = crearEtiqueta(".CONC");
        String etEndConc = crearEtiqueta(".ENDCONC");
        add(etConc + ":", "MOVE.B", "(A0)+, D0", "");
        add("", "BEQ", etConc2, "");
        add("", "MOVE.B", "D0, (A2)+", "");
        add("", "BRA", etConc, "");
        add(etConc2 + ":", "MOVE.B", "(A1)+, D0", "");
        add("", "BEQ", etEndConc, "");
        add("", "MOVE.B", "D0, (A2)+", "");
        add("", "BRA", etConc2, "");
        add(etEndConc + ":", "RTS", "", "RETURN TO SUBROUTINE ...");
    }

    private String crearEtiqueta(String s) {
        String etOriginal = s;
        int acumulador = 0;
        while (etiquetas.contains(s)) {
            s = etOriginal + acumulador++;
        }
        etiquetas.add(s);
        return s;
    }

    private String load(Operador op, String sOp) throws Exception {
        if (AnActual > 6 || DnActual > 7) {
            throw new Exception("Error fatal, no existen suficientes registros para conseguir A" + AnActual + " y D" + DnActual + " sin generar conflictos");
        }
        String register;
        if (!op.isLiteral()) {
            register = "A" + AnActual++;
            add(getEtiqueta(), "LEA" + op.getExtension68K(), sOp + ", " + register, register + " = @" + sOp);
        } else {
            register = "D" + DnActual++;
            add(getEtiqueta(), "MOVE" + op.getExtension68K(), sOp + ", " + register, register + " = " + sOp);
        }
        return register;
    }

    private void store(String from, String to) {
        add(getEtiqueta(), "MOVE.L ", from + ", " + to, to + " = " + from);
    }

    private void push(String s) {
        add(getEtiqueta(), "MOVE.L", s + ", -(SP)", "PUSH INTO STACK " + s);
    }

    private void push(int bytes) {
        add(getEtiqueta(), "SUBA.L", "#" + bytes + ", SP", "SP = SP - " + bytes);
    }

    private void pop(String s) {
        add(getEtiqueta(), "MOVE.L", "(SP)+, " + s, s + " = POP FROM STACK");
    }

    private void pop(int bytes) {
        add(getEtiqueta(), "ADDA.L", "#" + bytes + ", SP", "SP = SP + " + bytes);
    }

    private void procesarInstruccion(String op1, String op2, String dst, String dstConPunto) throws Exception {
//        String extensiones = instruccionActual.getExtensiones68K();
//        String biggestExtension = (extensiones.contains("L") ? ".L" : (extensiones.contains("W") ? "W" : "B"));
//        
        Operador opop1, opop2, opdst;
        opop1 = instruccion.op1;
        opop2 = instruccion.op2;
        opdst = instruccion.dst;
        switch (instruccion.getTipo()) {
            case COPY -> { // op1 -> dst
                String register = load(opop1, op1);
                store(register, dst);
            }
            case NEG -> { // -op1 -> dst
                add(getEtiqueta(), "MOVE.L", op1 + ", D0", "D0 = " + op1);
                add("NEG.L", "D0", "D0 = -D0");
                add("MOVE.L", "D0, " + dst, dst + " = D0");
            }
            case ADD -> { // op1+op2 -> dst
                String r1 = load(opop1, op1);
                String r2 = load(opop2, op2);
                add("ADD.L", r1 + ", " + r2, r2 + " = " + r2 + "" + " + " + r1);
                store(r2, dst);
            }
            case SUB -> { // op1-op2 -> dst
                String r1 = load(opop1, op1);
                String r2 = load(opop2, op2);
                add("SUB.L", r1 + ", " + r2, r2 + " = " + r2 + "" + " - " + r1);
                store(r2, dst);
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
                add("MULS.W", "D1, D0", "D0 = D0 * D1");
                add("MULS.W", "D2, D3", "D3 = D2 * D3");
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
            case CONCAT -> { // op1 concat op2 -> dst
                seConcatena = true;
                String r1 = load(opop1, op1); // A0
                String r2 = load(opop2, op2); // A1
                add("LEA.L", dst + ", A2", "FETCH " + dst);
                add("JSR", etConc, "");
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
                push(dst);
            }
            case PMB -> { // pmb dst
                PData func = procedureTable.get(dst);
                int indice = func.getBytesRetorno();
                for (Pair<String, String> par : func.getParametros()) {
                    // par.snd son es el tipo
                    indice += 4; //indice += Tipo.getBytes(par.snd);
                    add(getEtiqueta(), "MOVE.L", indice + "(SP), " + par.fst, par.fst + " = POP FROM STACK");
                }
            }
            case RETURN -> { // rtn dst, ?
                PData func = procedureTable.get(dst);
                if (func.getBytesRetorno() > 0) {
                    push(dstConPunto);
                }
                add(getEtiqueta(), "RTS", "", "RETURN TO SUBROUTINE " + dstConPunto);
            }
            case CALL -> { // call dst
                PData func = procedureTable.get(dst);
                if (func == null) {
                    throw new Exception("Error, la función con etiqueta " + dst + " no existe");
                }
                PData.TipoMetodoEspecial tipo = PData.getEspecial(func.getNombre());
                if (tipo != null) {
                    procesarMetodoEspecial(procedureTable.get(dst), tipo, dst);
                }
                add(getEtiqueta(), "JSR", dstConPunto, "JUMP TO SUBROUTINE " + dstConPunto);
                int numBytes = 0;
                for (Pair<String, String> par : func.getParametros()) {
                    // par.snd son los bytes
                    numBytes += 4; //numBytes += Tipo.getBytes(par.snd);
                }
                if (numBytes > 0) {
                    add("ADDA.L", "#" + numBytes + ", SP", "SP = SP + " + numBytes);
                }
                if (func.getBytesRetorno() > 0) {
                    pop(op1);
                }
            }
            case SKIP -> { // dst: skip
                setEtiqueta(dstConPunto); // para etiquetas locales ponemos .
            }
            case SEPARADOR -> { // no hace nada pero resulta en mayor legibilidad del código ensamblador
                codigo.add("");
            }
            default ->
                throw new Exception("Instrucción " + instruccion + " no se puede pasar a código ensamblador");
        }
        AnActual = 0;
        DnActual = 0;
    }

    private void procesarMetodoEspecial(PData f, PData.TipoMetodoEspecial tipo, String dst) throws Exception {
        switch (tipo) {
            case PRINT -> { // print(dst)
                if (f.getParametros().size() != 1) {
                    throw new Exception("Error, no se ha implementao el print para tratar con " + f.getParametros().size() + " parámetros, sino con 1");
                }
                if (printUsado) {
                    return;
                }
                printUsado = true;
                int bytes = f.getBytesRetorno() + Tipo.getBytes(f.getParametros().get(0).snd);
                subprogramas.add(margen(dst, "MOVEA.L", bytes + "(SP), A1", "A1 = POP FROM STACK"));
                subprogramas.add(margen("", "MOVE.L", "#14, D0", "Task 14 of TRAP 15: Display the NULL terminated string pointed to by (A1)"));
                subprogramas.add(margen("", "TRAP", "#15", "Interruption generated"));
                //subprogramasIO.add(margen("", getEtiqueta(), "MOVE.L", dstConPunto + ", -(SP)", "PUSH INTO STACK " + dstConPunto);
                subprogramas.add(margen("", "RTS", "", "RETURN TO SUBROUTINE ..."));
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
        }
    }

    private String getEtiqueta() {
        String et = etiquetaActual;
        etiquetaActual = "";
        return et;
    }

    private void setEtiqueta(String s) {
        etiquetaActual = s + ":";
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

    private void declararVariable(String id, VData data, Object inicializacion) throws Exception {
        String s = data.getDeclaracionEnsamblador(inicializacion);
        datos.add(margen(id, s, "", ""));
        if (s.startsWith("DC.B")) {
            datos.add(margen("", "DS.W 0", "", "No pueden haber variables en zonas de memoria impar"));
        }
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
//                + (printUsado ? (margen("", "INCLUDE", "\"PRINT.X68\"", "Escritura en consola") + "\n") : "")
//                + (scanUsado ? (margen("", "INCLUDE", "\"PRINT.X68\"", "Escritura en consola") + "\n") : "")
//                + (writeUsado ? (margen("", "INCLUDE", "\"PRINT.X68\"", "Escritura en consola") + "\n") : "")
//                + (readUsado ? (margen("", "INCLUDE", "\"PRINT.X68\"", "Escritura en consola") + "\n") : "");
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
