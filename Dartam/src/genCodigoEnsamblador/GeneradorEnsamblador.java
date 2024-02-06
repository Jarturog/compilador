package genCodigoEnsamblador;

import analizadorSemantico.DescripcionDefinicionTupla;
import analizadorSemantico.DescripcionDefinicionTupla.DefinicionMiembro;
import analizadorSemantico.DescripcionFuncion.Parametro;
import analizadorSemantico.genCodigoIntermedio.GeneradorCodigoIntermedio;
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
import java.util.Map;
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
    private final HashMap<String, DescripcionDefinicionTupla> tablaTuplas;
//
//    // Info from the current process
//    private Stack<ProcTableEntry> pteStack;
//    private ProcTableEntry currentPte;
//    private boolean firstParam = true;
//
//    private boolean printUsed = false;
//    private boolean scanUsed = false;

    public GeneradorEnsamblador(String fichero, GeneradorCodigoIntermedio generador) throws Exception {
        nombreFichero = fichero;
        instrucciones = generador.getInstrucciones();
        main = generador.getMain();
        variables = generador.getTablaVariables();
        procedureTable = generador.getTablaProcedimientos();
        etiquetas = generador.getEtiquetas();
        tablaTuplas = generador.getTuplas();
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
        for (Map.Entry<String, DescripcionDefinicionTupla> entry : tablaTuplas.entrySet()) {
            if (entry.getValue().getMiembros().isEmpty()) {
                continue;
            }
            String id = entry.getKey();
            DescripcionDefinicionTupla tupla = entry.getValue();
            setEtiqueta(tupla.variableAsociada);
            variables.get(tupla.variableAsociada).inicializar();
            for (DefinicionMiembro miembro : tupla.getMiembros()) {
                if (miembro.tieneValorAsignado()) {
                    String s = getDeclaracionEnsamblador(new VData(Tipo.getTipo(miembro.tipo)), miembro.varInit);
                    //datos.add(margen(getEtiqueta(), "DC.B "+miembro.varInit, "", "Reservando memoria para el miembro " + miembro.nombre + " de la tupla "+id));
                    datos.add(margen(getEtiqueta(), s, "", "Inicializando el miembro " + miembro.nombre + " de la tupla " + id));
                } else {
                    datos.add(margen(getEtiqueta(), "DS.B " + miembro.getBytes(), "", "Reservando memoria para el miembro " + miembro.nombre + " de la tupla " + id));
                }
            }
        }

        add("JSR", main.getEtiqueta(), "Se ejecuta el main");
        add("SIMHALT", "", "Fin de la ejecución");
        add("");
        for (int i = 0; i < instrucciones.size(); i++) {
            instr = instrucciones.get(i);
            VData data = instr.dst() == null ? null : variables.get(instr.dst().toString());
            if (instr.dst() != null && data != null && data.estaInicializadaEnCodigoIntermedio() && !data.estaInicializadaEnCodigoEnsamblador()) {
                data.inicializar();
                declararVariable(instr.dst().toString(), data, instr.op1().getValor());
            }
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
        add("");
        // inicializamos las tuplas
        for (Map.Entry<String, VData> e : variables.entrySet()) {
            String id = e.getKey();
            VData data = e.getValue();
            if (data.getTupla() == null) {
                continue;
            }
            int bytes = data.getTupla().getBytes();
            int bytesLeft = bytes % 4;
            bytes -= bytesLeft;
            bytes /= 4;
            String finBucle = this.crearEtiqueta("endinit" + id), bucle = this.crearEtiqueta("init" + id);
            preMain.add(margen("", "LEA.L", data.getTupla().variableAsociada + ", A0", "load " + data.getTupla().variableAsociada + " into A0"));
            preMain.add(margen("", "LEA.L", id + ", A1", "load " + id + " into A1"));
            preMain.add(margen("", "MOVE.L", "#" + bytes + ", D0", ""));
            preMain.add(margen(bucle, "CMP.L", "#0, D0", ""));
            preMain.add(margen("", "BEQ", finBucle, ""));
            preMain.add(margen("", "MOVE.L", "(A0)+, (A1)+", "copy 4 bytes"));
            preMain.add(margen("", "SUB.L", "#1, D0", ""));
            preMain.add(margen("", "JMP", bucle, ""));
            preMain.add(margen(finBucle, "", "", "end of loop"));
            for (int i = 0; i < bytesLeft; i++) {
                preMain.add(margen("", "MOVE.B", "(A0)+, (A1)+", "copy 1 byte"));
            }
            preMain.add("");
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

    private void pop(int bytes) {
        add(getEtiqueta(), "ADDA.L", "#" + bytes + ", SP", "SP = SP + " + bytes);
    }

    private String getExtensionSuperior(String ext1, String ext2) {
        if (ext1 == null) {
            return ext2;
        } else if (ext2 == null) {
            return ext1;
        }
        if (ext1.endsWith("L") || ext2.endsWith("L")) {
            return ext1.endsWith("L") ? ext1 : ext2;
        }
        if (ext1.endsWith("W") || ext2.endsWith("W")) {
            return ext1.endsWith("W") ? ext1 : ext2;
        }
        return ext1;
    }

    private void procesarInstruccion(String dstConPunto) throws Exception {
        String extOp1 = instr.op1() == null ? null : (instr.op1().tipo() == null ? null : instr.op1().tipo().getExtension68K());
        String extOp2 = instr.op2() == null ? null : (instr.op2().tipo() == null ? null : instr.op2().tipo().getExtension68K());
        String extSuperior = getExtensionSuperior(extOp1, extOp2);
        String extDst = instr.dst() == null ? null : (instr.dst().tipo() == null ? null : instr.dst().tipo().getExtension68K());
        switch (instr.getTipo()) {
            case COPY -> { // op1 -> dst
                String register = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                store(register, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case NEG -> { // -op1 -> dst
                String register = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                add("NEG" + extOp1, register, register + " = -" + register);
                store(register, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case ADD -> { // op1+op2 -> dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo());
                add("ADD" + extSuperior, r1 + ", " + r2, r2 + " = " + r2 + "" + " + " + r1);
                store(r2, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case SUB -> { // op1-op2 -> dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo());
                add("SUB" + extSuperior, r2 + ", " + r1, r1 + " = " + r1 + "" + " - " + r2);
                store(r1, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case MUL -> { // op1*op2 -> dst
                // EASy68K no permite MULS.L, por lo que se ha de operar así:
                // A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16
                add("; " + " ".repeat(MARGEN - 3), "A*B = A1A0*B1B0 = A0*B0 + A1*B1 * 2^16", "", "");
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo());
                add("MOVE.W", r1 + ", D2", "D2.L = " + r2 + ".L");
                add("MOVE.W", r2 + ", D3", r2 + ".L = D3.L");
                add("ASR.L", "#8, " + r1, "FIRST 8 BITS OF " + r1 + " MOVED RIGHT");
                add("ASR.L", "#8, " + r1, r1 + ".L = old " + r1 + ".H");
                add("ASR.L", "#8, " + r2, "FIRST 8 BITS OF " + r2 + " MOVED RIGHT");
                add("ASR.L", "#8, " + r2, r2 + ".L = old " + r2 + ".H");
                add("MULS.W", r2 + ", " + r1, r1 + " = " + r1 + " * " + r2);
                add("MULS.W", "D2, D3", "D3 = D2 * D3");
                add("ASL.L", "#8, " + r1, "FIRST 8 BITS OF " + r1 + " MOVED LEFT");
                add("ASL.L", "#8, " + r1, r1 + ".H = old " + r1 + ".L");
                add("ADD.L", "D3, " + r1, r1 + " = " + r1 + " + D3");
                store(r1, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case DIV -> { // op1/op2 -> dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo());
                add("DIVS.W", r2 + ", " + r1, r1 + ".h = " + r1 + " % " + r2 + ". " + r1 + ".l = " + r1 + " / " + r2);
                add("AND.L", "#$0000FFFF, " + r1, "Mask");
                store(r1, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case MOD -> { // op1%op2 -> dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo());
                add("DIVS.W", r2 + ", " + r1, r1 + ".h = " + r1 + " % " + r2 + ". " + r1 + ".l = " + r1 + " / " + r2);
                add("LSR.L", "#8, " + r1, r1 + ".l = " + r1 + ".h");
                add("LSR.L", "#8, " + r1, r1 + ".l = " + r1 + ".h");
                store(r1, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case NOT -> { // not op1 -> dst
                String register = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                add("NOT" + extOp1, register, register + " = not " + register);
                store(register, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case AND -> { // op1 and op2 -> dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo());
                add("AND" + extSuperior, r1 + ", " + r2, r2 + " = " + r2 + "" + " and " + r1);
                store(r2, instr.dst().toAssembly(), instr.dst().tipo());
            }
            case OR -> { // op1 or op2 -> dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo());
                add("OR" + extSuperior, r1 + ", " + r2, r2 + " = " + r2 + "" + " or " + r1);
                store(r2, instr.dst().toAssembly(), instr.dst().tipo());
            }
//            case CONCAT -> { // op1 concat op2 -> dst
//                seConcatena = true;
//                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo()); // A0
//                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo()); // A1
//                add("LEA.L", instr.dst().toAssembly() + ", A2", "FETCH " + instr.dst().toAssembly());
//                add("JSR", etConc, "");
//            }
            case GOTO -> { // goto dst
                add(getEtiqueta(), "JMP", dstConPunto, "goto " + dstConPunto);
            }
            case IFEQ -> { // if op1 = op2 goto dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                add("CMP" + extOp1, instr.op2().toAssembly() + ", " + r1, "UPDATE FLAGS WITH " + r1 + " - " + instr.op2().toAssembly());
                add("BEQ", dstConPunto, "IF Z FLAG = 1 GOTO " + dstConPunto);
            }
            case IFNE -> { // if op1 /= op2 goto dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                add("CMP" + extOp1, instr.op2().toAssembly() + ", " + r1, "UPDATE FLAGS WITH " + r1 + " - " + instr.op2().toAssembly());
                add("BNE", dstConPunto, "IF Z FLAG = 0 GOTO " + dstConPunto);
            }
            case IFGE -> { // if op1 >= op2 goto dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                add("CMP" + extOp1, instr.op2().toAssembly() + ", " + r1, "UPDATE FLAGS WITH " + r1 + " - " + instr.op2().toAssembly());
                add("BGE", dstConPunto, "IF (N XOR V) FLAGS = 0 GOTO " + dstConPunto);
            }
            case IFLT -> { // if op1 < op2 goto dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                add("CMP" + extOp1, instr.op2().toAssembly() + ", " + r1, "UPDATE FLAGS WITH " + r1 + " - " + instr.op2().toAssembly());
                add("BLT", dstConPunto, "IF (N XOR V) FLAGS = 1 GOTO " + dstConPunto);
            }
            case IFGT -> { // if op1 > op2 goto dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                add("CMP" + extOp1, instr.op2().toAssembly() + ", " + r1, "UPDATE FLAGS WITH " + r1 + " - " + instr.op2().toAssembly());
                add("BGT", dstConPunto, "IF ((N XOR V) OR Z) FLAGS = 0 GOTO " + dstConPunto);
            }
            case IFLE -> { // if op1 <= op2 goto dst
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                add("CMP" + extOp1, instr.op2().toAssembly() + ", " + r1, "UPDATE FLAGS WITH " + r1 + " - " + instr.op2().toAssembly());
                add("BLE", dstConPunto, "IF ((N XOR V) OR Z) FLAGS = 1 GOTO " + dstConPunto);
            }
            case IND_ASS -> { // dst[op1] = op2
                String r1 = load(instr.op1(), instr.op1().toAssembly(), instr.op1().tipo());
                String r2 = load(instr.op2(), instr.op2().toAssembly(), instr.op2().tipo());
                String dt = load(instr.dst(), instr.dst().toAssembly(), instr.dst().tipo());
                add("ADDA.L ", r1 + ", " + dt, dt + " = " + dt + " + " + r1);
                add("MOVE.L ", r2 + ", (" + dt + ")", "(" + dt + ") = " + r2);
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
                for (int i = func.getParametros().size() - 1; i >= 0; i--) {
                    Parametro param = func.getParametros().get(i);
                    // par.snd son es el tipo
                    indice += Tipo.getBytes(param.tipo);
                    add(getEtiqueta(), "MOVE" + Tipo.getExtension68K(Tipo.getBytes(param.tipo)), indice + "(SP), " + param.variable, param.variable + " = POP FROM STACK");
                }
            }
            case RETURN -> { // rtn dst, ?
                PData func = procedureTable.get(instr.dst().toAssembly());
                int bytes = func.getBytesRetorno();
                if (func.getBytesRetorno() > 0) {
                    add(getEtiqueta(), "MOVE" + extOp1, instr.op1().toAssembly() + ", " + bytes + "(SP)", "PUSH INTO STACK " + instr.op1().toAssembly());
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
                if (func.getBytesRetorno() > 0) {
                    add("SUBA.L", "#" + func.getBytesRetorno() + ", SP", "SP = SP + " + func.getBytesRetorno());
                }
                if (esMetodoEspecial) {
                    procesarMetodoEspecial(procedureTable.get(instr.dst().toAssembly()), tipo, instr.dst().toAssembly());
                }
                add(getEtiqueta(), "JSR", dstConPunto, "JUMP TO SUBROUTINE " + dstConPunto);
                int numBytes = 0;
                for (Parametro param : func.getParametros()) {
                    // par.snd son los bytes
                    numBytes += Tipo.getBytes(param.tipo);
                }
                if (func.getBytesRetorno() > 0) {
                    add(getEtiqueta(), "MOVE" + extOp1, "(SP)+, " + instr.op1().toAssembly(), instr.op1().toAssembly() + " = POP FROM STACK");
                }
                if (numBytes > 0) {
                    add("ADDA.L", "#" + numBytes + ", SP", "SP = SP + " + numBytes);
                }

            }
            case SKIP -> { // dst: skip
                String et = getEtiqueta();
                if (et != null) {
                    add(et, "", "", "");
                }
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
                if (f.getParametros().size() != 2) {
                    throw new Exception("Error, no se ha implementao el scan para tratar con " + f.getParametros().size() + " parámetros, sino con 2");
                }
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
                default -> {
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
