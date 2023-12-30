package genCodigoMaquina;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import genCodigoIntermedio.GeneradorCIntermedio;
import genCodigoIntermedio.PTEntry;
import genCodigoIntermedio.VTEntry;
import analizadorSintactico.ParserSym;

public class GeneradorCMaquina {
    
    public ArrayList<String> data;
    public ArrayList<String> text;

    private ArrayList<Instruction> instructions;

    private Hashtable<String, VTEntry> variableTable;
    private Hashtable<String, String> variableDictionary;
    private Hashtable<String, PTEntry> procedureTable;

    // Info from the current process
    private Stack<PTEntry> pteStack;
    private PTEntry currentPte;
    private boolean firstParam = true;

    private boolean printUsed = false;
    private boolean scanUsed = false;

    public GeneradorCMaquina(GeneradorCIntermedio c3a){
        instructions = null;//c3a.getInstructions();
        variableTable = null;//c3a.getVariableTable();
        procedureTable = null;//c3a.getProcedureTable();
        variableDictionary = new Hashtable<>();
        data = new ArrayList<>();
        text = new ArrayList<>();
        pteStack = new Stack<>();
    }

    public void generateCode(){
        PTEntry pte = new PTEntry();
        VTEntry vte;
        // Declarations initialization
        data.add("\tsection .data");

        for (String s : variableTable.keySet()) {
            vte = variableTable.get(s);
            if(true){//s.startsWith(GeneradorCIntermedio.DEF_FUNCTION)){
                String t = vte.tName;
                switch(vte.type){
                    case ParserSym.KW_BOOL:
                        data.add(t + ":\tdb " + vte.initialValue);
                        break;
                    case ParserSym.KW_CHAR:
                        if(vte.dimensions.size() > 0){
                            data.add(t + ":\t times " + vte.getOccupation() + " db " + vte.initialValue);
                            data.add("\tdb 0");
                        } else
                        data.add(t + ":\tdb " + vte.initialValue);
                        break;
                    case ParserSym.KW_INT:
                        if(vte.dimensions.size() > 0){
                            data.add(t + ":\t times " + vte.getOccupation() + " db " + vte.initialValue);
                        } else
                        data.add(t + ":\tdd " + vte.initialValue);
                        break;
                }
            } else variableDictionary.put(vte.tName, s);
        }

        text.add("\tsection .text\n"
                + "\tglobal main\n"
                + "main:\n"
                + "\tmov rbp,rsp\n"
                + "\tpush rbp\n");

        for (Instruction instruction : instructions) {
            //if (LenguaG.DEBUGGING)     text.add("\t; " + instruction);
            
            String des = instruction.destination;
            String left = instruction.left;
            String right = instruction.right;
            
            // We check if the variables are local variables/parameters
            String stackVar = variableDictionary.get(des);
            vte = variableTable.get(des);
            if(stackVar != null) {
                vte = variableTable.get(stackVar);
                des = "rsp+"+(vte.displacement);
                //if(LenguaG.DEBUGGING)text.add("\t; //" + instruction.destination + " -> " + des);
            }
            String aDes = des; // String that contains the address of des.
            des = isNumber(des)? des : "[" + des + "]"; // des if it's a number, [des] if not. [des] mean the content of des
            if(left != null){
                stackVar = variableDictionary.get(left);
                if(stackVar != null) {
                    vte = variableTable.get(stackVar);
                    left = "rsp+"+(vte.displacement);
                    //if(LenguaG.DEBUGGING) text.add("\t; //" + instruction.left + " -> " + left);
                }
            }
            String aLeft = left;
            left = isNumber(left)? left : "[" + left + "]";
            if(right != null){
                stackVar = variableDictionary.get(right);
                if(stackVar != null) {
                    vte = variableTable.get(stackVar);
                    right = "rsp+"+(vte.displacement);
                    //if(LenguaG.DEBUGGING) text.add("\t; //" + instruction.right + " -> " + right);
                }
            }
            right = isNumber(right)? right : "[" + right + "]";


            switch (instruction.instruction) {
                case copy:
                    // copy: des = left
                    text.add("\tmov eax," + left);
                    text.add("\tmov " + des + ",eax");
                    break;
                case add:
                    // add: des = left + right
                    text.add("\tmov eax," + left);
                    text.add("\tmov ebx," + right);
                    text.add("\tadd ebx,eax");
                    text.add("\tmov " + des + ",ebx");
                    break;
                case and:
                    // and: des = left and right
                    text.add("\tmov eax," + left);
                    text.add("\tmov ebx," + right);
                    text.add("\tand ebx,eax");
                    text.add("\tmov " + des + ",ebx");
                    break;
                case call:
                    //pte = procedureTable.get(aLeft + GeneradorCIntermedio.DEF_FUNCTION);
                    text.add("\tpush rax");
                    text.add("\tcall " + pte.eStart);
                    text.add("\tpop rbx"); // We store return into rbx

                    for(int i = 0; i < pte.numParams; i++){
                        text.add("\tpop rax");
                    }
                    if(!firstParam){
                        text.add("\tsub rsp," + currentPte.getVarsOccupation());
                        firstParam = true;
                    }

                    text.add("\tmov " + des + ",ebx");
                    break;
                case div:
                    // DIV does EDX:EAX / ECX. Result goes in EAX
                    text.add("\tmov eax," + left);
                    text.add("\tmov edx,0");
                    text.add("\tmov ecx," + right);
                    text.add("\tdiv ecx");
                    text.add("\tmov " + des + ",eax");
                    break;
                case go_to:
                    text.add("\tjmp " + aDes);
                    break;
                case if_EQ:
                    // if left = right goto des
                    text.add("\tmov eax," + left);
                    text.add("\tmov ebx," + right);
                    text.add("\tcmp eax,ebx");
                    text.add("\tje " + aDes);
                    break;
                case if_GE:
                    // if left >= right goto des
                    text.add("\tmov eax," + left);
                    text.add("\tmov ebx," + right);
                    text.add("\tcmp eax,ebx");
                    text.add("\tjge " + aDes);
                    break;
                case if_GT:
                    // if left > right goto des
                    text.add("\tmov eax," + left);
                    text.add("\tmov ebx," + right);
                    text.add("\tcmp eax,ebx");
                    text.add("\tjg " + aDes);
                    break;
                case if_LE:
                    // if left <= right goto des
                    text.add("\tmov eax," + left);
                    text.add("\tmov ebx," + right);
                    text.add("\tcmp eax,ebx");
                    text.add("\tjle " + aDes);
                    break;
                case if_LT:
                    // if left < right goto des
                    text.add("\tmov eax," + left);
                    text.add("\tmov ebx," + right);
                    text.add("\tcmp eax,ebx");
                    text.add("\tjl " + aDes);
                    break;
                case if_NE:
                    // if left != right goto des
                    text.add("\tmov eax," + left);
                    text.add("\tmov ebx," + right);
                    text.add("\tcmp eax,ebx");
                    text.add("\tjne " + aDes);
                    break;
                case in:
                    scanUsed = true;

                    // We print a prompt to signal an input is expected
                    text.add("\tmov rdi,fmtOutChar");
                    text.add("\tmov rsi,prompt");
                    text.add("\tmov rax, 0");
                    text.add("\tcall printf");
                    
                    // This case is complex: we need the destionation's address into rsi instead of its contents
                    if(aDes.contains("rsp")){
                        String[] split = aDes.split("\\+");
                        text.add("\tmov rsi,[rsp]");
                        text.add("\tadd rsi," + split[1]);
                    }
                    else text.add("\tmov rsi," + aDes);
                    text.add("\tmov rdi,fmtInInt");
                    text.add("\tmov al, 0");
                    text.add("\tcall scanf");
                    break;
                case ind_ass:
                    // des[left] = right

                    // This case is complex: we need the destionation's address into eax instead of its contents, 
                    if(aDes.contains("rsp")){
                        String[] split = aDes.split("\\+");
                        text.add("\tmov rax,[rsp]");
                        text.add("\tadd rax," + split[1]);
                    }
                    else text.add("\tmov rax," + aDes);
                    text.add("\txor rbx,rbx");
                    text.add("\tmov ebx," + left);
                    text.add("\tadd rax,rbx");
                    text.add("\tmov ebx," + right);
                    text.add("\tmov [rax],ebx");

                    break;
                case ind_val:
                    // des = left[right]
                    if(aLeft.contains("rsp")){
                        String[] split = aLeft.split("\\+");
                        text.add("\tmov rax,[rsp]");
                        text.add("\tadd rax," + split[1]);
                    }
                    else text.add("\tmov rax," + aLeft);
                    text.add("\txor rbx,rbx");
                    text.add("\tmov ebx," + right);
                    text.add("\tadd rax,rbx");
                    text.add("\tmov eax,[rax]");
                    text.add("\tmov " + des + ",eax");
                    break;
                case mod:
                    // DIV does EDX:EAX / ECX. Remainder goes in EDX
                    text.add("\tmov eax," + left);
                    text.add("\tmov edx,0");
                    text.add("\tmov ecx," + right);
                    text.add("\tdiv ecx");
                    text.add("\tmov " + des + ",eax");
                    break;
                case neg:
                    // and: des = - left
                    text.add("\tmov eax," + left);
                    text.add("\tneg eax");
                    text.add("\tmov " + des + ",eax");
                    break;
                case not:
                    // and: des = not left
                    text.add("\tmov eax," + left);
                    text.add("\tnot eax");
                    text.add("\tmov " + des + ",eax");
                    break;
                case or:
                    // and: des = left and right
                    text.add("\tmov eax," + left);
                    text.add("\tmov ebx," + right);
                    text.add("\tor ebx,eax");
                    text.add("\tmov " + des + ",ebx");
                    break;
                case out:
                    printUsed = true;

                    text.add("\tmov rdi,fmtOutInt");
                    text.add("\tmov rsi," + des);
                    text.add("\tmov rax, 0");
                    text.add("\tcall printf");
                    break;
                // case param_c:
                    
                //     break;
                case param_s:
                    if(firstParam){
                        text.add("\tadd rsp," + currentPte.getVarsOccupation());
                        firstParam = false;
                    }

                    text.add("\txor rax,rax");
                    text.add("\tmov eax," + des);
                    text.add("\tpush rax");
                    break;
                case pmb:
                    // pmb: des
                    // Where des is the name of the function. As such, we should be able to find it in procedureTable
                    pteStack.push(currentPte);
                    //currentPte = procedureTable.get(aDes + GeneradorCIntermedio.DEF_FUNCTION);
                    
                    break;
                case point:
                    // des -> left
                    if(aLeft.contains("rsp")){
                        String[] split = aLeft.split("\\+");
                        text.add("\tmov rax,[rsp]");
                        text.add("\tadd rax," + split[1]);
                    }
                    else text.add("\tmov rax," + aLeft);
                    text.add("\tmov " + des + ",rax");
                    break;
                case prod:
                    // the operation mul multiplies the specified location
                    // (register or memory) with EAX. I use register because it
                    // is a bit faster.
                    text.add("\tmov eax," + left);
                    text.add("\tmov ecx," + right);
                    text.add("\tmul ecx");
                    text.add("\tmov " + des + ",eax");
                    break;
                case rtn:
                    text.add("\tmov eax," + left);
                    text.add("\tmov [rsp+8],eax");
                    text.add("\tjmp " + currentPte.eEnd);
                    // We jump to the return point. This was done to more easily deal with the current function
                    break;
                case skip:
                    // des: skip
                    text.add(aDes + ":");
                    if(currentPte != null && aDes.equals(currentPte.eEnd)){
                        currentPte = pteStack.pop();
                        text.add("\tret");
                    }
                    break;
                case sub:
                    // des = left - right
                    text.add("\tmov eax," + left);
                    text.add("\tmov ebx," + right);
                    text.add("\tsub eax,ebx");
                    text.add("\tmov " + des + ",eax");
                    break;
                default:
                    throw new RuntimeException("Operation not implemented: " + instruction);
            }
        }

        text.add("\n\tpop rbp\n"
                + "\tmov rax,0\n"
                + "\tret");

        if(printUsed){
            data.add("fmtOutInt: db \"%d\",10,0");
        }
        if(scanUsed){
            data.add("fmtInInt:  db \"%d\", 0");
            data.add("fmtOutChar:  db \"%s\", 0");
            data.add("prompt: db \"> \", 0");
        }
    }

    private boolean isNumber(String string){
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    @Override
    public String toString(){
        String s = "";
        if(printUsed) s += "\textern printf\n";
        if(scanUsed) s += "\textern scanf\n";
        for(String i : text){
            s += i + "\n";
        }
        for(String i : data){
            s += i + "\n";
        }
        return s;
    }
}