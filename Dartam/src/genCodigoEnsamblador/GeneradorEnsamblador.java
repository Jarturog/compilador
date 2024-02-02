package genCodigoEnsamblador;

import analizadorSemantico.genCodigoIntermedio.Instruccion;
import static dartam.Dartam.DEBUG;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class GeneradorEnsamblador {

    public ArrayList<String> data, text;

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
    public GeneradorEnsamblador(ArrayList<Instruccion> instrs) {
        instrucciones = instrs;
//        variableTable = c3a.getVariableTable();
//        procedureTable = c3a.getProcedureTable();
//        variableDictionary = new HashMap<>();
        data = new ArrayList<>();
        text = new ArrayList<>();
//        pteStack = new Stack<>();
        procesarCodigo();
    }

    private void procesarCodigo() {
//        ProcTableEntry pte;
//        VarTableEntry vte;
        // Declarations initialization
        data.add("\tsection .data");

//        for (String s : variableTable.keySet()) {
//            vte = variableTable.get(s);
//            if (s.startsWith(IntermediateCodeGenerator.DEF_FUNCTION)) {
//                String t = vte.tName;
//                switch (vte.type) {
//                    case Constants.TYPE_BOOLEAN -> data.add(t + ":\tdb " + vte.initialValue);
//                    case Constants.TYPE_CHARACTER -> {
//                        if (vte.dimensions.size() > 0) {
//                            data.add(t + ":\t times " + vte.getOccupation() + " db " + vte.initialValue);
//                            data.add("\tdb 0");
//                        } else {
//                            data.add(t + ":\tdb " + vte.initialValue);
//                        }
//                    }
//                    case Constants.TYPE_INTEGER -> {
//                        if (vte.dimensions.size() > 0) {
//                            data.add(t + ":\t times " + vte.getOccupation() + " db " + vte.initialValue);
//                        } else {
//                            data.add(t + ":\tdd " + vte.initialValue);
//                        }
//                    }
//                }
//            } else {
//                variableDictionary.put(vte.tName, s);
//            }
//        }
        text.add("\tsection .text\n"
                + "\tglobal main\n"
                + "main:\n"
                + "\tmov rbp,rsp\n"
                + "\tpush rbp\n");

        for (Instruccion instr : instrucciones) {
            instruccionActual = instr;
            if (DEBUG) {
                text.add("\t; " + instr);
            }

            String dest = instr.dst.toString();
            String op1 = instr.op1.toString();
            String op2 = instr.op2.toString();

//            // We check if the variables are local variables/parameters
//            String stackVar = variableDictionary.get(dest);
//            vte = variableTable.get(dest);
//            if (stackVar != null) {
//                vte = variableTable.get(stackVar);
//                dest = "rsp+" + (vte.displacement);
//                if (LenguaG.DEBUGGING) {
//                    text.add("\t; //" + instruction.destination + " -> " + dest);
//                }
//            }
//            String aDes = dest; // String that contains the address of des.
//            dest = isNumber(dest) ? dest : "[" + dest + "]"; // des if it's a number, [des] if not. [des] mean the content of des
//            if (op1 != null) {
//                stackVar = variableDictionary.get(op1);
//                if (stackVar != null) {
//                    vte = variableTable.get(stackVar);
//                    op1 = "rsp+" + (vte.displacement);
//                    if (LenguaG.DEBUGGING) {
//                        text.add("\t; //" + instruction.left + " -> " + op1);
//                    }
//                }
//            }
//            String aLeft = op1;
//            op1 = isNumber(op1) ? op1 : "[" + op1 + "]";
//            if (op2 != null) {
//                stackVar = variableDictionary.get(op2);
//                if (stackVar != null) {
//                    vte = variableTable.get(stackVar);
//                    op2 = "rsp+" + (vte.displacement);
//                    if (LenguaG.DEBUGGING) {
//                        text.add("\t; //" + instruction.right + " -> " + op2);
//                    }
//                }
//            }
//            op2 = isNumber(op2) ? op2 : "[" + op2 + "]";
        procesarInstruccion(op1, op2, dest);
            text.add("\n\tpop rbp\n"
                    + "\tmov rax,0\n"
                    + "\tret");

            if (printUsed) {
                data.add("fmtOutInt: db \"%d\",10,0");
            }
            if (scanUsed) {
                data.add("fmtInInt:  db \"%d\", 0");
                data.add("fmtOutChar:  db \"%s\", 0");
                data.add("prompt: db \"> \", 0");
            }
        }
    }

        

    private void procesarInstruccion(String op1, String op2, String dest){
    switch (instruction.instruction) {
        case copy -> {
            // copy: des = left
            text.add("\tmov eax," + op1);
            text.add("\tmov " + dest + ",eax");
        }
        case add -> {
            // add: des = left + right
            text.add("\tmov eax," + op1);
            text.add("\tmov ebx," + op2);
            text.add("\tadd ebx,eax");
            text.add("\tmov " + dest + ",ebx");
        }
        case and -> {
            // and: des = left and right
            text.add("\tmov eax," + op1);
            text.add("\tmov ebx," + op2);
            text.add("\tand ebx,eax");
            text.add("\tmov " + dest + ",ebx");
        }
        case call -> {
            pte = procedureTable.get(aLeft + IntermediateCodeGenerator.DEF_FUNCTION);
            text.add("\tpush rax");
            text.add("\tcall " + pte.eStart);
            text.add("\tpop rbx"); // We store return into rbx

            for (int i = 0; i < pte.numParams; i++) {
                text.add("\tpop rax");
            }
            if (!firstParam) {
                text.add("\tsub rsp," + currentPte.getVarsOccupation());
                firstParam = true;
            }

            text.add("\tmov " + dest + ",ebx");
        }
        case div -> {
            // DIV does EDX:EAX / ECX. Result goes in EAX
            text.add("\tmov eax," + op1);
            text.add("\tmov edx,0");
            text.add("\tmov ecx," + op2);
            text.add("\tdiv ecx");
            text.add("\tmov " + dest + ",eax");
        }
        case go_to -> {
            text.add("\tjmp " + aDes);
        }
        case if_EQ -> {
            // if left = right goto des
            text.add("\tmov eax," + op1);
            text.add("\tmov ebx," + op2);
            text.add("\tcmp eax,ebx");
            text.add("\tje " + aDes);
        }
        case if_GE -> {
            // if left >= right goto des
            text.add("\tmov eax," + op1);
            text.add("\tmov ebx," + op2);
            text.add("\tcmp eax,ebx");
            text.add("\tjge " + aDes);
        }
        case if_GT -> {
            // if left > right goto des
            text.add("\tmov eax," + op1);
            text.add("\tmov ebx," + op2);
            text.add("\tcmp eax,ebx");
            text.add("\tjg " + aDes);
        }
        case if_LE -> {
            // if left <= right goto des
            text.add("\tmov eax," + op1);
            text.add("\tmov ebx," + op2);
            text.add("\tcmp eax,ebx");
            text.add("\tjle " + aDes);
        }
        case if_LT -> {
            // if left < right goto des
            text.add("\tmov eax," + op1);
            text.add("\tmov ebx," + op2);
            text.add("\tcmp eax,ebx");
            text.add("\tjl " + aDes);
        }
        case if_NE -> {
            // if left != right goto des
            text.add("\tmov eax," + op1);
            text.add("\tmov ebx," + op2);
            text.add("\tcmp eax,ebx");
            text.add("\tjne " + aDes);
        }
        case in -> {
            scanUsed = true;

            // We print a prompt to signal an input is expected
            text.add("\tmov rdi,fmtOutChar");
            text.add("\tmov rsi,prompt");
            text.add("\tmov rax, 0");
            text.add("\tcall printf");

            // This case is complex: we need the destionation's address into rsi instead of its contents
            if (aDes.contains("rsp")) {
                String[] split = aDes.split("\\+");
                text.add("\tmov rsi,[rsp]");
                text.add("\tadd rsi," + split[1]);
            } else {
                text.add("\tmov rsi," + aDes);
            }
            text.add("\tmov rdi,fmtInInt");
            text.add("\tmov al, 0");
            text.add("\tcall scanf");
        }
        case ind_ass -> {
            // des[left] = right

            // This case is complex: we need the destionation's address into eax instead of its contents, 
            if (aDes.contains("rsp")) {
                String[] split = aDes.split("\\+");
                text.add("\tmov rax,[rsp]");
                text.add("\tadd rax," + split[1]);
            } else {
                text.add("\tmov rax," + aDes);
            }
            text.add("\txor rbx,rbx");
            text.add("\tmov ebx," + op1);
            text.add("\tadd rax,rbx");
            text.add("\tmov ebx," + op2);
            text.add("\tmov [rax],ebx");
        }
        case ind_val -> {
            // des = left[right]
            if (aLeft.contains("rsp")) {
                String[] split = aLeft.split("\\+");
                text.add("\tmov rax,[rsp]");
                text.add("\tadd rax," + split[1]);
            } else {
                text.add("\tmov rax," + aLeft);
            }
            text.add("\txor rbx,rbx");
            text.add("\tmov ebx," + op2);
            text.add("\tadd rax,rbx");
            text.add("\tmov eax,[rax]");
            text.add("\tmov " + dest + ",eax");
        }
        case mod -> {
            // DIV does EDX:EAX / ECX. Remainder goes in EDX
            text.add("\tmov eax," + op1);
            text.add("\tmov edx,0");
            text.add("\tmov ecx," + op2);
            text.add("\tdiv ecx");
            text.add("\tmov " + dest + ",eax");
        }
        case neg -> {
            // and: des = - left
            text.add("\tmov eax," + op1);
            text.add("\tneg eax");
            text.add("\tmov " + dest + ",eax");
        }
        case not -> {
            // and: des = not left
            text.add("\tmov eax," + op1);
            text.add("\tnot eax");
            text.add("\tmov " + dest + ",eax");
        }
        case or -> {
            // and: des = left and right
            text.add("\tmov eax," + op1);
            text.add("\tmov ebx," + op2);
            text.add("\tor ebx,eax");
            text.add("\tmov " + dest + ",ebx");
        }
        case out -> {
            printUsed = true;
            text.add("\tmov rdi,fmtOutInt");
            text.add("\tmov rsi," + dest);
            text.add("\tmov rax, 0");
            text.add("\tcall printf");
            // case param_c:
        }
        case param_s -> {
            if (firstParam) {
                text.add("\tadd rsp," + currentPte.getVarsOccupation());
                firstParam = false;
            }

            text.add("\txor rax,rax");
            text.add("\tmov eax," + dest);
            text.add("\tpush rax");
        }
        case pmb -> {
            // pmb: des
            // Where des is the name of the function. As such, we should be able to find it in procedureTable
            pteStack.push(currentPte);
            currentPte = procedureTable.get(aDes + IntermediateCodeGenerator.DEF_FUNCTION);
        }
        case point -> {
            // des -> left
            if (aLeft.contains("rsp")) {
                String[] split = aLeft.split("\\+");
                text.add("\tmov rax,[rsp]");
                text.add("\tadd rax," + split[1]);
            } else {
                text.add("\tmov rax," + aLeft);
            }
            text.add("\tmov " + dest + ",rax");
        }
        case prod -> {
            // the operation mul multiplies the specified location
            // (register or memory) with EAX. I use register because it
            // is a bit faster.
            text.add("\tmov eax," + op1);
            text.add("\tmov ecx," + op2);
            text.add("\tmul ecx");
            text.add("\tmov " + dest + ",eax");
        }
        case rtn -> {
            text.add("\tmov eax," + op1);
            text.add("\tmov [rsp+8],eax");
            text.add("\tjmp " + currentPte.eEnd);
            // We jump to the return point. This was done to more easily deal with the current function
        }
        case skip -> {
            // des: skip
            text.add(aDes + ":");
            if (currentPte != null && aDes.equals(currentPte.eEnd)) {
                currentPte = pteStack.pop();
                text.add("\tret");
            }
        }
        case sub -> {
            // des = left - right
            text.add("\tmov eax," + op1);
            text.add("\tmov ebx," + op2);
            text.add("\tsub eax,ebx");
            text.add("\tmov " + dest + ",eax");
        }
        default ->
            throw new RuntimeException("Operation not implemented: " + instruction);
    } //     break;
}

@Override
public String toString() {
        String s = "";
        if (printUsed) {
            s += "\textern printf\n";
        }
        if (scanUsed) {
            s += "\textern scanf\n";
        }
        for (String i : text) {
            s += i + "\n";
        }
        for (String i : data) {
            s += i + "\n";
        }
        return s;
    }
}
