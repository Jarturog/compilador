package genCodigoEnsamblador;

import java.util.ArrayList;
import java.util.Stack;

import genCodigoIntermedio.GeneradorCIntermedio;
import genCodigoIntermedio.EntradaProcedure;
import genCodigoIntermedio.EntradaVariable;
import analizadorSintactico.ParserSym;
import genCodigoIntermedio.Instruccion3Direcciones;
import genCodigoIntermedio.PData;
import genCodigoIntermedio.TablaProcedimientos;
import genCodigoIntermedio.TablaVariables;
import genCodigoIntermedio.Tipo;
import genCodigoIntermedio.VData;
import java.util.HashMap;

public class GeneradorEnsamblador {

    private final ArrayList<String> data, text;

    private final ArrayList<Instruccion3Direcciones> instructions;

    private final TablaVariables variableTable;
    private final HashMap<String, String> variableDictionary;
    private final TablaProcedimientos procedureTable;

    // Info from the current process
    private final Stack<PData> pteStack;

    private PData currentPte;
    private boolean firstParam = true, printUsed = false, scanUsed = false;

    public GeneradorEnsamblador(GeneradorCIntermedio generador) {
        instructions = generador.getInstructions();
        variableTable = generador.getVariableTable();
        procedureTable = generador.getProcedureTable();
        variableDictionary = new HashMap<>();
        data = new ArrayList<>();
        text = new ArrayList<>();
        pteStack = new Stack<>();

        // Declarations initialization
        data.add("\tsection .data");
        inicializarVariablesQuiza();

        text.add("\tsection .text\n\tglobal main\nmain:\n\tmov rbp,rsp\n\tpush rbp\n");

        for (Instruccion3Direcciones i : instructions) {
            generarInstruccion(i);
        }
    }

    private void inicializarVariablesQuiza() {
        for (String s : variableTable.keySet()) {
            VData vte = variableTable.get(s);
            if (s.startsWith(IntermediateCodeGenerator.DEF_FUNCTION)) {
                String t = vte.tName;
                switch (vte.type) {
                    case Tipo.BOOL ->
                        data.add(t + ":\tdb " + vte.initialValue);
                    case Tipo.CHAR -> {
                        if (vte.dimensions.size() > 0) {
                            data.add(t + ":\t times " + vte.getOccupation() + " db " + vte.initialValue);
                            data.add("\tdb 0");
                        } else {
                            data.add(t + ":\tdb " + vte.initialValue);
                        }
                    }
                    case Tipo.INT -> {
                        if (vte.dimensions.size() > 0) {
                            data.add(t + ":\t times " + vte.getOccupation() + " db " + vte.initialValue);
                        } else {
                            data.add(t + ":\tdd " + vte.initialValue);
                        }
                    }
                }
            } else {
                variableDictionary.put(vte.tName, s);
            }
        }
    }
    
    private void generarInstruccion(Instruccion3Direcciones instruction) {
        //if (LenguaG.DEBUGGING) {
        //    text.add("\t; " + instruction);
        //}

        String des = instruction.dst.getNombre();
        String left = instruction.op1.getNombre();
        String right = instruction.op2.getNombre();

        // We check if the variables are local variables/parameters
        String stackVar = variableDictionary.get(des);
        VData vte = variableTable.get(des);
        if (stackVar != null) {
            vte = variableTable.get(stackVar);
            des = "rsp+" + (vte.displacement);
            //if (LenguaG.DEBUGGING) {
            //    text.add("\t; //" + instruction.destination + " -> " + des);
            //}
        }
        des = isNumber(des) ? des : "[" + des + "]"; // des if it's a number, [des] if not. [des] mean the content of des
        if (left != null) {
            stackVar = variableDictionary.get(left);
            if (stackVar != null) {
                vte = variableTable.get(stackVar);
                left = "rsp+" + (vte.displacement);
                if (LenguaG.DEBUGGING) {
                    text.add("\t; //" + instruction.left + " -> " + left);
                }
            }
        }
        left = isNumber(left) ? left : "[" + left + "]";
        if (right != null) {
            stackVar = variableDictionary.get(right);
            if (stackVar != null) {
                vte = variableTable.get(stackVar);
                right = "rsp+" + (vte.displacement);
                if (LenguaG.DEBUGGING) {
                    text.add("\t; //" + instruction.right + " -> " + right);
                }
            }
        }
        right = isNumber(right) ? right : "[" + right + "]";

        pasarInstruccionAEnsamblador(instruction, des, left, right);

        text.add("\n\tpop rbp\n\tmov rax,0\n\tret");

        if (printUsed) {
            data.add("fmtOutInt: db \"%d\",10,0");
        }
        if (scanUsed) {
            data.add("fmtInInt:  db \"%d\", 0");
            data.add("fmtOutChar:  db \"%s\", 0");
            data.add("prompt: db \"> \", 0");
        }
    }

    private void pasarInstruccionAEnsamblador(Instruccion3Direcciones inst, String des, String left, String right) throws Exception {
        switch (inst.tipo()) {
            case copy -> {
                // copy: des = left
                text.add("\tmov eax," + left);
                text.add("\tmov " + des + ",eax");
            }
            case add -> {
                // add: des = left + right
                text.add("\tmov eax," + left);
                text.add("\tmov ebx," + right);
                text.add("\tadd ebx,eax");
                text.add("\tmov " + des + ",ebx");
            }
            case and -> {
                // and: des = left and right
                text.add("\tmov eax," + left);
                text.add("\tmov ebx," + right);
                text.add("\tand ebx,eax");
                text.add("\tmov " + des + ",ebx");
            }
            case call -> {
                PData pte = procedureTable.get(left + GeneradorCIntermedio.DEF_FUNCTION);
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

                text.add("\tmov " + des + ",ebx");
            }
            case div -> {
                // DIV does EDX:EAX / ECX. Result goes in EAX
                text.add("\tmov eax," + left);
                text.add("\tmov edx,0");
                text.add("\tmov ecx," + right);
                text.add("\tdiv ecx");
                text.add("\tmov " + des + ",eax");
            }
            case go_to ->
                text.add("\tjmp " + des);
            case if_EQ -> {
                // if left = right goto des
                text.add("\tmov eax," + left);
                text.add("\tmov ebx," + right);
                text.add("\tcmp eax,ebx");
                text.add("\tje " + des);
            }
            case if_GE -> {
                // if left >= right goto des
                text.add("\tmov eax," + left);
                text.add("\tmov ebx," + right);
                text.add("\tcmp eax,ebx");
                text.add("\tjge " + des);
            }
            case if_GT -> {
                // if left > right goto des
                text.add("\tmov eax," + left);
                text.add("\tmov ebx," + right);
                text.add("\tcmp eax,ebx");
                text.add("\tjg " + des);
            }
            case if_LE -> {
                // if left <= right goto des
                text.add("\tmov eax," + left);
                text.add("\tmov ebx," + right);
                text.add("\tcmp eax,ebx");
                text.add("\tjle " + des);
            }
            case if_LT -> {
                // if left < right goto des
                text.add("\tmov eax," + left);
                text.add("\tmov ebx," + right);
                text.add("\tcmp eax,ebx");
                text.add("\tjl " + des);
            }
            case if_NE -> {
                // if left != right goto des
                text.add("\tmov eax," + left);
                text.add("\tmov ebx," + right);
                text.add("\tcmp eax,ebx");
                text.add("\tjne " + des);
            }
            case in -> {
                scanUsed = true;

                // We print a prompt to signal an input is expected
                text.add("\tmov rdi,fmtOutChar");
                text.add("\tmov rsi,prompt");
                text.add("\tmov rax, 0");
                text.add("\tcall printf");

                // This case is complex: we need the destionation's address into rsi instead of its contents
                if (des.contains("rsp")) {
                    String[] split = des.split("\\+");
                    text.add("\tmov rsi,[rsp]");
                    text.add("\tadd rsi," + split[1]);
                } else {
                    text.add("\tmov rsi," + des);
                }
                text.add("\tmov rdi,fmtInInt");
                text.add("\tmov al, 0");
                text.add("\tcall scanf");
            }
            case ind_ass -> {
                // des[left] = right

                // This case is complex: we need the destionation's address into eax instead of its contents, 
                if (des.contains("rsp")) {
                    String[] split = des.split("\\+");
                    text.add("\tmov rax,[rsp]");
                    text.add("\tadd rax," + split[1]);
                } else {
                    text.add("\tmov rax," + des);
                }
                text.add("\txor rbx,rbx");
                text.add("\tmov ebx," + left);
                text.add("\tadd rax,rbx");
                text.add("\tmov ebx," + right);
                text.add("\tmov [rax],ebx");
            }
            case ind_val -> {
                // des = left[right]
                if (left.contains("rsp")) {
                    String[] split = left.split("\\+");
                    text.add("\tmov rax,[rsp]");
                    text.add("\tadd rax," + split[1]);
                } else {
                    text.add("\tmov rax," + left);
                }
                text.add("\txor rbx,rbx");
                text.add("\tmov ebx," + right);
                text.add("\tadd rax,rbx");
                text.add("\tmov eax,[rax]");
                text.add("\tmov " + des + ",eax");
            }
            case mod -> {
                // DIV does EDX:EAX / ECX. Remainder goes in EDX
                text.add("\tmov eax," + left);
                text.add("\tmov edx,0");
                text.add("\tmov ecx," + right);
                text.add("\tdiv ecx");
                text.add("\tmov " + des + ",eax");
            }
            case neg -> {
                // and: des = - left
                text.add("\tmov eax," + left);
                text.add("\tneg eax");
                text.add("\tmov " + des + ",eax");
            }
            case not -> {
                // and: des = not left
                text.add("\tmov eax," + left);
                text.add("\tnot eax");
                text.add("\tmov " + des + ",eax");
            }
            case or -> {
                // and: des = left and right
                text.add("\tmov eax," + left);
                text.add("\tmov ebx," + right);
                text.add("\tor ebx,eax");
                text.add("\tmov " + des + ",ebx");
            }
            case out -> {
                printUsed = true;
                text.add("\tmov rdi,fmtOutInt");
                text.add("\tmov rsi," + des);
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
                text.add("\tmov eax," + des);
                text.add("\tpush rax");
            }
            case pmb -> {
                // pmb: des
                // Where des is the name of the function. As such, we should be able to find it in procedureTable
                pteStack.push(currentPte);
                currentPte = procedureTable.get(des + GeneradorCIntermedio.DEF_FUNCTION);
            }
            case point -> {
                // des -> left
                if (left.contains("rsp")) {
                    String[] split = left.split("\\+");
                    text.add("\tmov rax,[rsp]");
                    text.add("\tadd rax," + split[1]);
                } else {
                    text.add("\tmov rax," + left);
                }
                text.add("\tmov " + des + ",rax");
            }
            case prod -> {
                // the operation mul multiplies the specified location
                // (register or memory) with EAX. I use register because it
                // is a bit faster.
                text.add("\tmov eax," + left);
                text.add("\tmov ecx," + right);
                text.add("\tmul ecx");
                text.add("\tmov " + des + ",eax");
            }
            case rtn -> {
                text.add("\tmov eax," + left);
                text.add("\tmov [rsp+8],eax");
                text.add("\tjmp " + currentPte.eEnd);
                // We jump to the return point. This was done to more easily deal with the current function
            }
            case skip -> {
                // des: skip
                text.add(des + ":");
                if (currentPte != null && des.equals(currentPte.eEnd)) {
                    currentPte = pteStack.pop();
                    text.add("\tret");
                }
            }
            case sub -> {
                // des = left - right
                text.add("\tmov eax," + left);
                text.add("\tmov ebx," + right);
                text.add("\tsub eax,ebx");
                text.add("\tmov " + des + ",eax");
            }
            default ->
                throw new Exception("Instrucción no implementada: " + inst.toString());
        }
    }
    
    private boolean isNumber(String string){
        return string.matches("-?\\d+");
//        try {
//            
//            Integer.valueOf(string);
//            return true;
//        } catch (NumberFormatException e){
//            return false;
//        }
    }

//    @Override
//    public String toString(){
//        String s = "";
//        if(printUsed) s += "\textern printf\n";
//        if(scanUsed) s += "\textern scanf\n";
//        for(String i : text){
//            s += i + "\n";
//        }
//        for(String i : data){
//            s += i + "\n";
//        }
//        return s;
//    }
}

