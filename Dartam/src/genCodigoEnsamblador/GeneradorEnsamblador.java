package genCodigoEnsamblador;

import analizadorSemantico.genCodigoIntermedio.Generador3Direcciones;
import analizadorSemantico.genCodigoIntermedio.Instruccion;
import analizadorSemantico.genCodigoIntermedio.Operador;
import analizadorSemantico.genCodigoIntermedio.Tipo;
import static analizadorSemantico.genCodigoIntermedio.Tipo.BOOL;
import static analizadorSemantico.genCodigoIntermedio.Tipo.CHAR;
import static analizadorSemantico.genCodigoIntermedio.Tipo.DOUBLE;
import static analizadorSemantico.genCodigoIntermedio.Tipo.INT;
import static analizadorSemantico.genCodigoIntermedio.Tipo.PUNTERO;
import static analizadorSemantico.genCodigoIntermedio.Tipo.STRING;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import jflex.base.Pair;

public class GeneradorEnsamblador {

    private static final int MARGEN = 12; // número de espacios en blanco hasta el código
    private static final int ANCHURA_INSTR = 8;
    private static final int ORIGEN_MEM = 1000; // dirección de memoria desde la que empieza el programa
    private String etiquetaActual = "";
    private final String nombreFichero;
    //public ArrayList<String> data, text;
    private final ArrayList<String> codigo, datos, subprogramas, preMain;
    private final ArrayList<Instruccion> instrucciones;
    private Instruccion instr = null;
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
        preMain = new ArrayList<>();
        etConc = crearEtiqueta("CONCATENAR");
//        text = new ArrayList<>();
//        pteStack = new Stack<>();
        procesarCodigo();
    }

    private void procesarCodigo() throws Exception {
        String etiqueta_main = crearEtiqueta(nombreFichero.toUpperCase());

        add("JSR", main.getEtiqueta(), "Se ejecuta el main");
        add("SIMHALT", "", "Fin de la ejecución");
        add("");
        for (int i = 0; i < instrucciones.size(); i++) {
            instr = instrucciones.get(i);
            VData data = instr.dst() == null ? null : variables.get(instr.dst().toString());
            if (instr.dst() != null && data != null && data.estaInicializadaEnCodigoIntermedio() && !data.estaInicializadaEnCodigoEnsamblador()) {
                data.inicializar();
                declararVariable(instr.dst().toString(), data, instr.op1().getValor());
            } else {
                String instrCodigoIntermedio = instr.toString();
                if (instrCodigoIntermedio.isEmpty()) {
                    add("");
                } else {
                    add("; " + "-".repeat(MARGEN - 3), instr.toString(), "", "");
                }
                String et = instr.dst() == null ? null : instr.dst().toAssembly();
                // si es goto, ifxx, skip, pmb, call, etc... y no es una función añade . para indicar que es local
                if (et != null && instr.getTipo().tieneEtiqueta() && !procedureTable.containsKey(et)) {
                    et = "." + et;
                }
                procesarInstruccion(et);
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
            VData data = variable.getValue();
            if (!data.estaInicializadaEnCodigoEnsamblador()) {
                declararVariable(variable.getKey(), data, null);
            }
        }
        add("");
        preMain.add(margen(etiqueta_main, "", "", "Etiqueta inicial (main)"));
        if (printUsado || scanUsado || readUsado || writeUsado) { // activar excepciones y teclado al inicializar el programa
            preMain.add("");
            preMain.add(margen("", "MOVE.L", "#32, D0", "Task 32 of TRAP 15: Hardware/Simulator"));
            preMain.add(margen("", "MOVE.B", "#5, D1", "Enable exception processing (for input/output)"));
            preMain.add(margen("", "TRAP", "#15", "Interruption generated"));
        }
        if (scanUsado) {
            preMain.add("");
            preMain.add(margen("", "MOVE.L", "#62, D0", "Task 62 of TRAP 15: Enable/Disable keyboard IRQ"));
            preMain.add(margen("", "MOVE.W", "#$0103, D1", "Enable keyboard IRQ level 1 for key up and key down"));
            preMain.add(margen("", "TRAP", "#15", "Interruption generated"));
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
        while (etiquetas.contains(s) || variables.containsKey(s) || procedureTable.containsKey(s)) {
            s = etOriginal + acumulador++;
        }
        etiquetas.add(s);
        return s;
    }

    private String load(Operador op, String sOp, Tipo t) throws Exception {
        if (AnActual > 6 || DnActual > 7) {
            throw new Exception("Error fatal, no existen suficientes registros para conseguir A" + AnActual + " y D" + DnActual + " sin generar conflictos");
        }
        String ext = t.getExtension68K();
        String register, operacion;
        if (op.isPuntero()) {//(!op.isLiteral()) {
            register = "A" + AnActual++;
            operacion = "LEA.L";
        } else {
            register = "D" + DnActual++;
            operacion = "MOVE" + ext;
        }
        if (!operacion.endsWith("L")) {
            add(getEtiqueta(), "CLR.L ", register, "CLEAR " + register);
        }
        add(getEtiqueta(), operacion, sOp + ", " + register, register + " = " + sOp);
        return register;
    }

    private void store(String from, String to, Tipo t) {
        add(getEtiqueta(), "MOVE" + t.getExtension68K() + " ", from + ", " + to, to + " = " + from);
    }

    private void push(Operador op, String s, Tipo t) throws Exception {
        String thingToPush = s;
        if (t.equals(Tipo.PUNTERO) || t.equals(Tipo.STRING)) {
            thingToPush = load(op, s, t);
        }
        add(getEtiqueta(), "MOVE" + t.getExtension68K(), thingToPush + ", -(SP)", "PUSH INTO STACK " + s);
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

    private void procesarInstruccion(String dstConPunto) throws Exception {
//        String extensiones = instruccionActual.getExtensiones68K();
//        String biggestExtension = (extensiones.contains("L") ? ".L" : (extensiones.contains("W") ? "W" : "B"));
//        
        switch (instr.getTipo()) {
            case COPY -> { // op1 -> dst
                String register = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                store(register, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case NEG -> { // -op1 -> dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("NEG.L", "D0", "D0 = -D0");
                add("MOVE.L", "D0, " + instr.dst().toAssembly(), instr.dst().toAssembly() + " = D0");
            }
            case ADD -> { // op1+op2 -> dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo());
                add("ADD.L", r1 + ", " + r2, r2 + " = " + r2 + "" + " + " + r1);
                store(r2, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case SUB -> { // op1-op2 -> dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo());
                add("SUB.L", r1 + ", " + r2, r2 + " = " + r2 + "" + " - " + r1);
                store(r2, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case MUL -> { // op1*op2 -> dst
                // EASy68K no permite MULS.L, por lo que se ha de operar así:
                // A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16
                add("; " + " ".repeat(MARGEN - 3), "A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16", "", "");
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("MOVE.L", instr.op2().toAssembly() + ", D1", "D1 = " + instr.op2().toAssembly());
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
                add("MOVE.L", "D0, " + instr.dst().toAssembly(), instr.dst().toAssembly() + " = D0");
            }
            case DIV -> { // op1/op2 -> dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("DIVS.L", instr.op2().toAssembly() + ", D0", "D0.h = D0 % " + instr.op2().toAssembly() + ". D0.l = D0 / " + instr.op2().toAssembly());
                add("MOVE.B", "D0, " + instr.dst().toAssembly(), instr.dst().toAssembly() + " = D0.l");
            }
            case MOD -> { // op1%op2 -> dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("DIVS.L", instr.op2().toAssembly() + ", D0", "D0.h = D0 % " + instr.op2().toAssembly() + ". D0.l = D0 / " + instr.op2().toAssembly());
                add("LSR.L", "#8, D0", "D0.l = D0.h");
                add("LSR.L", "#8, D0", "D0.l = D0.h");
                add("MOVE.B", "D0, " + instr.dst().toAssembly(), instr.dst().toAssembly() + " = D0.l");
            }
            case NOT -> { // not op1 -> dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("NOT.L", "D0", "D0 = not D0");
                add("MOVE.L", "D0, " + instr.dst().toAssembly(), instr.dst().toAssembly() + " = D0");
            }
            case AND -> { // op1 and op2 -> dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("AND.L", instr.op2().toAssembly() + ", D0", "D0 = D0 and " + instr.op2().toAssembly());
                add("MOVE.L", "D0, " + instr.dst().toAssembly(), instr.dst().toAssembly() + " = D0");
            }
            case OR -> { // op1 or op2 -> dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("OR.L", instr.op2().toAssembly() + ", D0", "D0 = D0 or " + instr.op2().toAssembly());
                add("MOVE.L", "D0, " + instr.dst().toAssembly(), instr.dst().toAssembly() + " = D0");
            }
            case CONCAT -> { // op1 concat op2 -> dst
                seConcatena = true;
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo()); // A0
                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo()); // A1
                add("LEA.L", instr.dst().toAssembly() + ", A2", "FETCH " + instr.dst().toAssembly());
                add("JSR", etConc, "");
            }
            case GOTO -> { // goto dst
                add(getEtiqueta(), "JMP", dstConPunto, "goto " + dstConPunto);
            }
            case IFEQ -> { // if op1 = op2 goto dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("CMP.L", instr.op2().toAssembly() + ", D0", "UPDATE FLAGS WITH D0 - " + instr.op2().toAssembly());
                add("BEQ", dstConPunto, "IF Z FLAG = 1 GOTO " + dstConPunto);
            }
            case IFNE -> { // if op1 /= op2 goto dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("CMP.L", instr.op2().toAssembly() + ", D0", "UPDATE FLAGS WITH D0 - " + instr.op2().toAssembly());
                add("BNE", dstConPunto, "IF Z FLAG = 0 GOTO " + dstConPunto);
            }
            case IFGE -> { // if op1 >= op2 goto dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("CMP.L", instr.op2().toAssembly() + ", D0", "UPDATE FLAGS WITH D0 - " + instr.op2().toAssembly());
                add("BLT", dstConPunto, "IF (N XOR V) FLAGS = 1 GOTO " + dstConPunto);
            }
            case IFLT -> { // if op1 < op2 goto dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("CMP.L", instr.op2().toAssembly() + ", D0", "UPDATE FLAGS WITH D0 - " + instr.op2().toAssembly());
                add("BGE", dstConPunto, "IF (N XOR V) FLAGS = 0 GOTO " + dstConPunto);
            }
            case IFGT -> { // if op1 > op2 goto dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("CMP.L", instr.op2().toAssembly() + ", D0", "UPDATE FLAGS WITH D0 - " + instr.op2().toAssembly());
                add("BLE", dstConPunto, "IF ((N XOR V) OR Z) FLAGS = 1 GOTO " + dstConPunto);
            }
            case IFLE -> { // if op1 <= op2 goto dst
                add(getEtiqueta(), "MOVE.L", instr.op1().toAssembly() + ", D0", "D0 = " + instr.op1().toAssembly());
                add("CMP.L", instr.op2().toAssembly() + ", D0", "UPDATE FLAGS WITH D0 - " + instr.op2().toAssembly());
                add("BGT", dstConPunto, "IF ((N XOR V) OR Z) FLAGS = 0 GOTO " + dstConPunto);
            }
            case IND_ASS -> { // dst[op1] = op2
                add(getEtiqueta(), "MOVEA.L", instr.dst().toAssembly() + ", A0", "A0 = " + instr.dst().toAssembly());
                add("ADDA.L ", instr.op1().toAssembly() + ", A0", "A0 = A0 + " + instr.op1().toAssembly());
                add("MOVE.L ", instr.op2().toAssembly() + ", (A0)", "(A0) = " + instr.op2().toAssembly());
            }
            case IND_VAL -> { // dst = op1[op2]
                add(getEtiqueta(), "MOVEA.L", instr.op1().toAssembly() + ", A0", "A0 = " + instr.op1().toAssembly());
                add("ADDA.L ", instr.op2().toAssembly() + ", A0", "A0 = A0 + " + instr.op2().toAssembly());
                add("MOVE.L ", "(A0), " + instr.dst().toAssembly(), instr.dst().toAssembly() + " = (A0)");
            }
            case PARAM_S -> { // param_s dst
                push(instr.dst(), instr.dst().toAssembly(), instr.dst().tipo());
            }
            case PMB -> { // pmb dst
                PData func = procedureTable.get(instr.dst().toAssembly());
                int indice = func.getBytesRetorno();
                for (Pair<String, String> par : func.getParametros()) {
                    // par.snd son es el tipo
                    indice += 4; //indice += Tipo.getBytes(par.snd);
                    add(getEtiqueta(), "MOVE.L", indice + "(SP), " + par.fst, par.fst + " = POP FROM STACK");
                }
            }
            case RETURN -> { // rtn dst, ?
                PData func = procedureTable.get(instr.dst().toAssembly());
                if (func.getBytesRetorno() > 0) {
                    push(instr.dst(), dstConPunto, instr.dst().tipo());
                }
                add(getEtiqueta(), "RTS", "", "RETURN TO SUBROUTINE " + dstConPunto);
            }
            case CALL -> { // call dst, ?
                PData func = procedureTable.get(instr.dst().toAssembly());
                if (func == null) {
                    throw new Exception("Error, la función con etiqueta " + instr.dst().toAssembly() + " no existe");
                }
                PData.TipoMetodoEspecial tipo = PData.getEspecial(func.getNombre());
                boolean esMetodoEspecial = tipo != null;
                if (esMetodoEspecial) {
                    procesarMetodoEspecial(procedureTable.get(instr.dst().toAssembly()), tipo, instr.dst().toAssembly());
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
                    pop(instr.op1().toAssembly());
                }
            }
            case SKIP -> { // dst: skip
                setEtiqueta(dstConPunto); // para etiquetas locales ponemos .
            }
            case SEPARADOR -> { // no hace nada pero resulta en mayor legibilidad del código ensamblador
                codigo.add("");
            }
            default ->
                throw new Exception("Instrucción " + instr + " no se puede pasar a código ensamblador");
        }
        AnActual = 0;
        DnActual = 0;
    }

    private void procesarMetodoEspecial(PData f, PData.TipoMetodoEspecial tipo, String idMetodo) throws Exception {
        switch (tipo) {
            case PRINT -> { // print(dst)
                if (f.getParametros().size() != 1) {
                    throw new Exception("Error, no se ha implementao el print para tratar con " + f.getParametros().size() + " parámetros, sino con 1");
                }
                if (printUsado) {
                    return;
                }
                printUsado = true;
                //int bytes = f.getBytesRetorno() + Tipo.getBytes(f.getParametros().get(0).snd);
                subprogramas.add(margen(idMetodo, "MOVEA.L", Tipo.PUNTERO.bytes + "(SP), A1", "A1 = POP FROM STACK"));
                subprogramas.add(margen("", "MOVE.L", "#13, D0", "Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF"));
                subprogramas.add(margen("", "TRAP", "#15", "Interruption generated"));
                //subprogramasIO.add(margen("", getEtiqueta(), "MOVE.L", dstConPunto + ", -(SP)", "PUSH INTO STACK " + dstConPunto);
                subprogramas.add(margen("", "RTS", "", "RETURN TO SUBROUTINE ..."));
            }
            case SCAN -> { // scan(dst)
                if (scanUsado) {
                    return;
                }
                scanUsado = true;
                String etFin = crearEtiqueta("." + idMetodo), mnsjError = crearEtiqueta("errorTeclado");
                subprogramas.add(margen(idMetodo, "CLR.L", "D1", "Empty D1 for later use"));
                subprogramas.add(margen("", "MOVE.L", "#5, D0", "Task 5 of TRAP 15: Read single ASCII character from the keyboard into D1.B"));
                subprogramas.add(margen("", "TRAP", "#15", "Interruption generated"));
                subprogramas.add(margen("", "MOVE.L", "D1, " + idMetodo, "D1 = character typed on the keyboard"));
                subprogramas.add(margen("", "RTS", "", "RETURN TO SUBROUTINE ..."));
            }
            case READ -> { // read(dst)
                if (readUsado) {
                    return;
                }
                readUsado = true;
                String etFin = crearEtiqueta("." + idMetodo), mnsjError = crearEtiqueta("errRead");
                datos.add(margen(mnsjError, "DC.B 'Error de lectura',0", "", "Mensaje de error"));
                datos.add(margen("", "DS.W 0", "", "Para evitar imparidad"));

                subprogramas.add(margen(idMetodo, "MOVEA.L", (2 * Tipo.PUNTERO.bytes) + "(SP), A1", "Pre: (A1) null terminated file name"));
                subprogramas.add(margen("", "MOVEA.L", Tipo.PUNTERO.bytes + "(SP), A2", "A2: pop"));
                //MOVE.L      A1, (A2)      ; -//subprogramas.add(margen("", "MOVE.L", "A2, A1", ""));
                subprogramas.add(margen("", "MOVE.L", "#51, D0", "Task 51 of TRAP 15: Open existing file"));
                subprogramas.add(margen("", "TRAP", "#15", "Interruption generated"));
                subprogramas.add(margen("", "CMP.W", "#2, D0", "Si error"));
                subprogramas.add(margen("", "BEQ", etFin, "Fin"));
                subprogramas.add(margen("", "MOVEA.L", "A2, A1", ""));
                subprogramas.add(margen("", "MOVE.L", "#" + Tipo.STRING.bytes + ", D2", ""));
                subprogramas.add(margen("", "MOVE.L", "#53, D0", "Task 53 of TRAP 15: Read file"));
                subprogramas.add(margen("", "TRAP", "#15", "Interruption generated"));
                subprogramas.add(margen("", "MOVE.L", "#56, D0", "Task 56 of TRAP 15: Close file"));
                subprogramas.add(margen("", "TRAP", "#15", "Interruption generated"));
                subprogramas.add(margen("", "RTS", "", "RETURN TO SUBROUTINE ..."));
                subprogramas.add(margen(etFin + ":", "LEA.L", mnsjError + ", A1", "A1 = mnsj error"));
                subprogramas.add(margen("", "MOVE.L", "#13, D0", "Task 13 of TRAP 15: Display the NULL terminated string pointed to by (A1) with CR, LF"));
                subprogramas.add(margen("", "TRAP", "#15", "Interruption generated"));
                subprogramas.add(margen("", "RTS", "", "RETURN TO SUBROUTINE ..."));
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
        for (String i : preMain) {
            s += i + "\n";
        }
        for (String i : codigo) {
            s += i + "\n";
        }
        return s;
    }

    private void declararVariable(String id, VData data, Object inicializacion) throws Exception {
        String s = getDeclaracionEnsamblador(data, inicializacion);
        datos.add(margen(id, s, "", data.tipo().toString()));
        if (s.startsWith("DC.B") || s.startsWith("DS.B")) {
            datos.add(margen("", "DC.B 0", "", "Los strings y chars acaban en 0"));
        }
        if (s.startsWith("DC.B") || s.startsWith("DS.B")) {
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

    private static String mCol(String i) {
        int margen = MARGEN - i.length();
        if (margen < 1) {
            margen = 1;
        }
        return i + " ".repeat(margen);
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

    public static String getDeclaracionEnsamblador(VData data, Object inicializacion) throws Exception {
        Tipo tipo = data.tipo();
        String ext = tipo.getExtension68K();
        if (inicializacion == null) {
            switch (tipo) {
                case BOOL -> {
                    return "DS" + ext + " " + 1;
                }
                case CHAR -> {
                    return "DS" + ext + " " + 1;
                }
                case DOUBLE -> {
                    return "DS.L 2";
                }
                case INT -> {
                    return "DS" + ext + " " + 1;
                }
                case ESTRUCTURA -> {
                    return "DS.B " + data.getBytesEstructura();
                }
                case STRING, PUNTERO -> {
                    Integer n = data.getBytesEstructura();
                    if (n == null) {
                        return "DS.L 1"; // puntero a algo (a un string, a un array, a una tupla)
                    } else {
                        return "DS.B " + n; // instancia de array, string o tupla
                    }
                }
                default ->{
                    throw new Exception("Declarando tipo inválido: " + tipo);
                }
            }
        } else {
            switch (tipo) {
                case BOOL -> {
                    return "DC" + ext + " " + inicializacion;
                }
                case CHAR -> {
                    return "DC" + ext + " '" + inicializacion + "'";
                }
//            case DOUBLE -> {
//                return "DC" + ext + " " + inicializacion;
//            }
                case INT -> {
                    return "DC" + ext + " " + inicializacion;
                }
                case STRING -> {
                    return "DC.B '" + inicializacion + "'";
                }
                case PUNTERO -> {
                    return "DC.L " + inicializacion;
                }
                default -> {
                    throw new Exception("Valor de inicialización " + inicializacion.toString() + " inválido");
                }
            }
        }
    }

}
