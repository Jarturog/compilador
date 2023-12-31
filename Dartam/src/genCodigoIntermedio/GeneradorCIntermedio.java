package genCodigoIntermedio;

import java.util.ArrayList;
import java.util.Hashtable;

import analizadorSintactico.symbols.*;
import genCodigoMaquina.Instruction.InstructionType;
import genCodigoMaquina.Instruction;
import analizadorSintactico.ParserSym;

public class GeneradorCIntermedio {

    private ArrayList<Instruction> c3a;

    private Hashtable<String, VTEntry> variableTable;
    private Hashtable<String, PTEntry> procedureTable;
    
    private PTEntry currentProcTable;
    private String currentFunction;
    private int currentSublevel;
    static final String DEF_FUNCTION = ".";
    
    private String currentDec; // Variable used in array declaration
    private int currentDecLength;
    private ArrayList<String> dimensionsToCheck;

    private int numE;
    private int numT;
   
    public GeneradorCIntermedio(SymbolScript script){
        c3a = new ArrayList<>();
        variableTable = new Hashtable<>();
        procedureTable = new Hashtable<>();
        numE = 0;
        numT = 0;
        currentFunction = DEF_FUNCTION; // We use the format function.variable to store and access the variable table
        // This avoids the very real possibility of the user creating a variable of format tN (where N is a natural) 
        // which would cause undesired behaviour
        currentSublevel = 0;
        currentDecLength = -1;
    }
/*
    public Hashtable<String, VTEntry> getVariableTable(){
        return variableTable;
    }

    public Hashtable<String, PTEntry> getProcedureTable(){
        return procedureTable;
    }

    public ArrayList<Instruction> getInstructions(){
        return c3a;
    }


    public void generate(SymbolBody body){
        // No code is generated here, equal to its semantic.generate() equivalent
        SymbolDecs decs = body.getDeclarations();
        if(decs != null) generate(decs);
        SymbolMain main = body.getMain();
        generate(main);

        for (String s : procedureTable.keySet()) {
            PTEntry pte = procedureTable.get(s);
            // We clean the procedure table to ease its use during machine code generation
            pte.prepareForMachineCode();
        }
    }

    private void generate(SymbolArg arg){
        String t = newVariable();
        VTEntry vte = getVar(t);
        replaceVarTableKey(t, arg.identifier);
        currentProcTable.params.add(t);

        SymbolTypeVar type = arg.getType();
        while(type.isType(KW_ARRAY)){
            vte.dimensions.add(newVariable());
            type = type.getBaseType();
        }

        if(LenguaG.DEBUGGING) {
            System.out.println("Parameter for " + currentFunction + ": " + arg.identifier + " -> " + t);
            System.out.println("Dimensions: " + vte.dimensions);
        }
        arg.reference = t;
    }

    private void generate(SymbolArgs args){
        // SymbolArg
        // currentSublevel = -1;
        generate(args.getArg());
        SymbolArgs next = args.getNext();
        if(next != null) generate(next);
        // currentSublevel = 0;
    }


    private void generate(SymbolArrSuff arrSuff){
        // if first 
        //     t1 = i = arrSuff.reference = getIndex().reference;
        // else 
        //     t2 = t1 * d2 
        //     t3 = t2 + i2
        // end if
        // Our grammar does not allow us to differentiate between the first and the middle array suffixes
        // So we can generalize it by saying
        // foreach n
        //      t(n+1) = t(n) * d(n)
        //      t(n+2) = t(n+1) + i(n)
        // Where t(1) = 0. This is why if it's the first time calling this function, tn is 0
        String tn = arrSuff.reference == null ? "0" : arrSuff.reference; // Why tn is this symbol's reference is further explained in a few lines
        //String tn = index.reference;
        String dimensions = "0";
        if(dimensionsToCheck.size() != 0) {
            dimensions = dimensionsToCheck.remove(0);
        }
        String tn1 = newVariable();
        addInstruction(InstructionType.prod, tn, ""+dimensions, tn1); // tn1 = tn * d
        SymbolOperation index = arrSuff.getIndex();
        generate(index);
        String in = index.reference;
        String tn2 = newVariable();
        addInstruction(InstructionType.add, tn1, in, tn2); // tn2 = tn1 + in
        
        // Next iteration
        SymbolArrSuff next = arrSuff.getNext();
        if(next != null){
            next.reference = tn2; // We pass tn2 as a reference. This is a "cheat" to allow communication between father and son, but is
            // technically a devirtualization of the idea of the reference. 
            // This is also the reason why at the start of this function we had to check if the reference was null or not, as only the first iteration
            // should be null
            generate(next);
            tn2 = next.reference;
        }

        arrSuff.reference = tn2;
    }


    private void generate(SymbolAssign assign){
        SymbolVar var = assign.getVariable();
        generate(var);
        String t = var.reference;

        SymbolOperation rSide = assign.getRightSide();
        generate(rSide);
        String t1 = rSide.reference;

        if(rSide.type.isType(KW_ARRAY)){
            addInstruction(InstructionType.point, t1, t);
        } else addInstruction(InstructionType.copy, t1, t);
    }

    private void generate(SymbolDec dec){
        String t = newVariable();
        VTEntry vte = getVar(t);
        currentDec = dec.variableName;
        if(dec.isConstant){
            // addInstruction(InstructionType.copy, dec.getValue().getSemanticValue().toString(), t);
            vte.initialValue = dec.getValue().getSemanticValue().toString();
            replaceVarTableKey(t, dec.variableName);
            return;
        }
        replaceVarTableKey(t, dec.variableName);
        SymbolOperation value = dec.getValue();
        if(value != null){
            if(value.isConstant) {
                Object v = value.getSemanticValue();
                if(v instanceof Boolean) v = (Boolean) v ? VTEntry.TRUE : VTEntry.FALSE;
                vte.initialValue = v.toString();
            } else {
                generate(value);
                if(!t.equals(value.reference)) addInstruction(InstructionType.copy, value.reference, t);
            }
        }
        dec.reference = t;
        currentDec = null;
        currentDecLength = -1;
    }

  
    private void generate(SymbolDecs decs){
        // No code is generated here, equal to its semantic.generate() equivalent
        SymbolBase dec = decs.getDeclaration();
        if(dec instanceof SymbolDec) generate((SymbolDec) dec);
        else if (dec instanceof SymbolFunc) {
            String eContDecs = newTag();
            addInstruction(InstructionType.go_to, eContDecs); // We must skip the function during executions unless called
            generate((SymbolFunc) dec);
            addInstruction(InstructionType.skip, eContDecs);
        }
        
        SymbolDecs nextDecs = decs.getNext();
        if (nextDecs != null) generate(nextDecs);
    }


    private void generate(SymbolElse sElse){
        SymbolIf nextIf = sElse.getIf();
        SymbolInstrs instrs = sElse.getInstructions();
        if(nextIf != null) generate(nextIf);
        else if(instrs != null) {
            generate(instrs);
        }
    }


    private void generate(SymbolFor sFor){
        currentSublevel++;
        
        SymbolBase init = sFor.getInit();
        if(init instanceof SymbolDec) generate((SymbolDec) init);
        else if(init instanceof SymbolAssign) generate((SymbolAssign) init);
        else if(init instanceof SymbolSwap) generate((SymbolSwap) init);
        else if(init instanceof SymbolFuncCall) generate((SymbolFuncCall) init);

        String eStart = newTag();
        addInstruction(InstructionType.skip, eStart);

        SymbolOperation cond = sFor.getCondition();
        generate(cond);
        String tCond = cond.reference;

        String eEnd = newTag();
        addInstruction(InstructionType.if_EQ, tCond, "0", eEnd);

        SymbolInstrs instrs = sFor.getInstructions();
        generate(instrs);

        SymbolBase end = sFor.getFinal();
        if(end instanceof SymbolDec) generate((SymbolDec) end);
        else if(end instanceof SymbolAssign) generate((SymbolAssign) end);
        else if(end instanceof SymbolSwap) generate((SymbolSwap) end);
        else if(end instanceof SymbolFuncCall) generate((SymbolFuncCall) end);

        addInstruction(InstructionType.go_to, eStart);
        addInstruction(InstructionType.skip, eEnd);

        currentSublevel--;
    }


    private void generate(SymbolFunc func){
        String name = func.getFunctionName();

        currentFunction = createPTEntry(name);

        SymbolArgs args = func.getArgs();
        PTEntry pte = procedureTable.get(currentFunction);
        currentProcTable = pte;

        if(args != null) {
            generate(args);
            pte.numParams = args.getNArgs();
        }
        
        addInstruction(InstructionType.skip, pte.eStart);
        addInstruction(InstructionType.pmb, name);
        // pte.tReturn = newVariable();
        SymbolInstrs instrs = func.getInstructions();
        if(instrs != null) generate(instrs);

        // If no return was found, we must put it at the end
        // addInstruction(InstructionType.rtn, "0", name);
        if(func.getType().isType(KW_VOID)) 
            addInstruction(InstructionType.rtn, "0", name);
        currentProcTable.eEnd = newTag();
        addInstruction(InstructionType.skip, currentProcTable.eEnd);

        currentFunction = DEF_FUNCTION;
        currentProcTable = null;
    }

 
    private void generate(SymbolFuncCall functionCall){
        String funcName = functionCall.getFunctionName();
        SymbolParams params = functionCall.getParams();
        if(params != null) generate(params);

        String t = newVariable();
        addInstruction(InstructionType.call, funcName, t);
        functionCall.reference = t;
    }

 
    private void generate(SymbolIf sIf){
        // We change the current function so as to denote that this is a different block, but we must restore it after.
        currentSublevel++;

        SymbolOperation cond = sIf.getCondition();
        generate(cond);
        String tCond = cond.reference;

        String eElse = newTag();
        addInstruction(InstructionType.if_EQ, tCond, "0", eElse);

        SymbolInstrs instrs = sIf.getInstructions();
        generate(instrs);

        addInstruction(InstructionType.skip, eElse);
        
        SymbolElse sElse = sIf.getElse();
        if(sElse != null) generate(sElse);
        currentSublevel--;
    }

    private void generate(SymbolIn in){
        SymbolVar oper = in.getVar();
        generate(oper);
        String t = oper.reference;
        addInstruction(InstructionType.in, t);
    }

    private void generate(SymbolInstr instruction){
        switch(instruction.getInstructionType()){
            case instDeclaration:
                generate((SymbolDec) instruction);
                return;
            case instAssignation:
                generate((SymbolAssign) instruction);
                return;
            case instSwap:
                generate((SymbolSwap) instruction);
                return;
            case instFunctionCall:
                generate((SymbolFuncCall) instruction);
                return;
            case instReturn:
                generate((SymbolReturn) instruction);
                return;
            case instIf:
                generate((SymbolIf) instruction);
                return;
            case instLoop:
                generate((SymbolLoop) instruction);
                return;
            case instFor:
                generate((SymbolFor) instruction);
                return;
            case instIn:
                generate((SymbolIn) instruction);
                return;
            case instOut:
                generate((SymbolOut) instruction);
                return;
            default:
                // Nothing to do
        }
    }


    private void generate(SymbolInstrs instructions){
        generate(instructions.getInstruction());
        SymbolInstrs next = instructions.getNext();
        if(next != null) generate(next);
    }


    private void generate(SymbolList list){
        String t;
        VTEntry vte = null;
        // This is to deal with the indexing of the lists
        if(currentDec != null){
            vte = getVar(currentDec);
            t = vte.tName;
        } else  {
            t = newVariable();
            currentDec = t;
        }
        String right = "";

        int length = list.type.arrayLength;
        // We store the dimensions of the list inside of the variable table
        if(vte != null && length != VTEntry.UNKNOWN){
            if(vte.dimensions.size() <= list.type.getArrayDepth()){
                vte.dimensions.add(""+length);
            }
        }

        SymbolOperation value = list.getValue();
        if(!value.isConstant) { 
            generate(value);
            right = value.reference;
        } else if(value.getSemanticValue() instanceof Character){
            char cVal = (Character) value.getSemanticValue();
            right = "" + (int) cVal; // We store ASCII value
            getVar(currentDec).type = ParserSym.KW_CHAR;
        } else if(value.getSemanticValue() instanceof Boolean){
            boolean bVal = (Boolean) value.getSemanticValue();
            // We store true or false in bits, not in Boolean
            right = "" + (bVal ? VTEntry.TRUE : VTEntry.FALSE);
        } else if(value.getSemanticValue() instanceof Integer){
            right = "" + (int) value.getSemanticValue();
        }
        
        // If the depth is 1 it means we are now on the "ground level" of the array and we can start to assign
        if(list.type.getArrayDepth() == 1){
            currentDecLength++;
            // We must store the generated values as an index of the first one, t.
            int size = VTEntry.CHAR_BYTES;
            if(value.type.isType(ParserSym.KW_INT)) size = VTEntry.INTEGER_BYTES;
            int displacement = currentDecLength * size;
            addInstruction(InstructionType.ind_ass, ""+displacement, right, getVar(currentDec).tName);
        }

        SymbolList next = list.getNext();
        if(next != null && next.getValue() != null) {
            generate(next);
        }

        list.reference = t;
    }

    private void generate(SymbolLoop loop){
        currentSublevel++;

        String eStart = newTag();
        addInstruction(InstructionType.skip, eStart);

        SymbolOperation cond = loop.getCondition();
        generate(cond);
        String tCond = cond.reference;

        String eEnd = newTag();
        addInstruction(InstructionType.if_EQ, tCond, "0", eEnd);

        SymbolInstrs instrs = loop.getInstructions();
        generate(instrs);

        addInstruction(InstructionType.go_to, eStart);
        addInstruction(InstructionType.skip, eEnd);

        currentSublevel--;
    }

    private void generate(SymbolMain main){
        SymbolInstrs instrs = main.getInstructions();
        if(instrs != null) {
            // We prepare the procedure table and insert the info about the main procedure
            currentFunction = createPTEntry("main");
            currentProcTable = procedureTable.get(currentFunction);
            String eMain = currentProcTable.eStart;

            addInstruction(InstructionType.skip, eMain);
            addInstruction(InstructionType.pmb, "main");
            generate(instrs);

            currentFunction = DEF_FUNCTION; // Reset current function
            currentProcTable = null;
        }
    }

    private void generate(SymbolOperand operand){
        String t = "";

        Object value = operand.getValue();

        if(!operand.isLeaf()){
            SymbolOperation operation = (SymbolOperation) operand.getValue();
            generate(operation);
            t = operation.reference;
        } else if(value instanceof SymbolValue){
            SymbolValue sValue = (SymbolValue) operand.getValue();
            generate(sValue);
            t = sValue.reference;
        } else if(value instanceof Integer){
            t = newVariable();
            addInstruction(InstructionType.copy, value.toString(), t);
            //getVar(t).occupation = VTEntry.INTEGER_BYTES;
        } else if(value instanceof Character){
            t = newVariable();
            char cVal = (Character) value;
            // We store the ASCII value
            addInstruction(InstructionType.copy, "" + (int) cVal, t);
            getVar(t).type = ParserSym.KW_CHAR;
        } else if(value instanceof Boolean){
            t = newVariable();
            boolean bVal = (Boolean) value;
            // We store true or false in bits, not in Boolean
            addInstruction(InstructionType.copy, "" + (bVal ? VTEntry.TRUE : VTEntry.FALSE), t);
        }

        if(!operand.isConstant && operand.isNegated()){
            if(operand.type.isType(ParserSym.KW_INT)){
                addInstruction(InstructionType.neg, t, t);

            } else {
                addInstruction(InstructionType.not, t, t);
            }
        }
        operand.reference = t;
    }

    private void generate(SymbolOperation operation){
        String t;

        SymbolOperand lValue = operation.getLValue();
        generate(lValue);
        t = lValue.reference;
        SymbolOp op = operation.getOperation();
        SymbolOperand rValue = operation.getRValue();
        
        // If nothing else is needed we don't keep going.
        if(op == null || rValue == null){
            operation.reference = t;
            return;
        }

        generate(rValue);
        t = newVariable();
        String eTrue, eFalse;
        switch (op.operation) {
            case VTEntry.ADD:
                addInstruction(InstructionType.add, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.SUB:
                addInstruction(InstructionType.sub, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.PROD:
                addInstruction(InstructionType.prod, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.DIV:
                addInstruction(InstructionType.div, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.MOD:
                addInstruction(InstructionType.mod, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.OR:
                addInstruction(InstructionType.or, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.AND:    
                addInstruction(InstructionType.and, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.IS_EQUAL:
                eTrue = newTag();
                eFalse = newTag();
                addInstruction(InstructionType.if_EQ, lValue.reference, rValue.reference, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.FALSE, t);
                addInstruction(InstructionType.go_to, eFalse);
                addInstruction(InstructionType.skip, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.TRUE, t);
                addInstruction(InstructionType.skip, eFalse);
                break;
            case VTEntry.BIGGER:
                eTrue = newTag();
                eFalse = newTag();
                addInstruction(InstructionType.if_GT, lValue.reference, rValue.reference, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.FALSE, t);
                addInstruction(InstructionType.go_to, eFalse);
                addInstruction(InstructionType.skip, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.TRUE, t);
                addInstruction(InstructionType.skip, eFalse);
                break;
            case VTEntry.BEQ:
                eTrue = newTag();
                eFalse = newTag();
                addInstruction(InstructionType.if_GE, lValue.reference, rValue.reference, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.FALSE, t);
                addInstruction(InstructionType.go_to, eFalse);
                addInstruction(InstructionType.skip, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.TRUE, t);
                addInstruction(InstructionType.skip, eFalse);
                break;
            case VTEntry.LESSER:
                eTrue = newTag();
                eFalse = newTag();
                addInstruction(InstructionType.if_LT, lValue.reference, rValue.reference, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.FALSE, t);
                addInstruction(InstructionType.go_to, eFalse);
                addInstruction(InstructionType.skip, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.TRUE, t);
                addInstruction(InstructionType.skip, eFalse);
                break;
            case VTEntry.LEQ:
                eTrue = newTag();
                eFalse = newTag();
                addInstruction(InstructionType.if_LE, lValue.reference, rValue.reference, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.FALSE, t);
                addInstruction(InstructionType.go_to, eFalse);
                addInstruction(InstructionType.skip, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.TRUE, t);
                addInstruction(InstructionType.skip, eFalse);
                break;
            case VTEntry.NEQ:
                eTrue = newTag();
                eFalse = newTag();
                addInstruction(InstructionType.if_NE, lValue.reference, rValue.reference, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.FALSE, t);
                addInstruction(InstructionType.go_to, eFalse);
                addInstruction(InstructionType.skip, eTrue);
                addInstruction(InstructionType.copy, ""+VTEntry.TRUE, t);
                addInstruction(InstructionType.skip, eFalse);
                break;
        }

        operation.reference = t;
    }

    private void generate(SymbolOut out){
        SymbolOperation oper = out.getValue();
        generate(oper);
        String t = oper.reference;
        addInstruction(InstructionType.out, t);
    }

    private void generate(SymbolParams params){
        SymbolParams next = params.getNext();
        if(next != null) generate(next);

        SymbolOperation oper = params.getValue();
        generate(oper);
        String t = oper.reference;
        
        if(oper.type.isType(KW_ARRAY)){
            addInstruction(InstructionType.param_c, t);
            SymbolTypeVar type = oper.type;
            // Since we need to know the dimensions of the arrays but parameters are not known in compilation time,
            // we pass them as parameters which the function's preamble will take care of during assembly code generation.
            while(type != null && type.arrayLength != VTEntry.UNKNOWN) {
                addInstruction(InstructionType.param_s, ""+type.arrayLength);
                type = type.getBaseType();
            }
        } else addInstruction(InstructionType.param_s, t);
    }


    private void generate(SymbolReturn sReturn){
        String t;
        SymbolOperation oper = sReturn.getValue();
        if(oper != null){
            generate(oper);
            t = oper.reference;
        } else t = "0";
        
        String name = currentFunction.replace(".", "");
        addInstruction(InstructionType.rtn, t, name);
    }


    private void generate(SymbolSwap swap){
        SymbolVar var1 = swap.getVar1();
        generate(var1);
        String t1 = var1.reference;

        String t = newVariable();
        addInstruction(InstructionType.copy, t1, t);

        SymbolVar var2 = swap.getVar2();
        generate(var2);
        String t2 = var2.reference;

        addInstruction(InstructionType.copy, t2, t1);
        addInstruction(InstructionType.copy, t, t2);
    }


    private void generate(SymbolValue value){
        Object val = value.getSemanticValue();
        String t = "";
        if(val instanceof SymbolVar) {
            SymbolVar var = (SymbolVar) val;
            generate(var);
            t = var.reference;
        } else if(val instanceof SymbolFuncCall) {
            SymbolFuncCall fcall = (SymbolFuncCall) val;
            generate(fcall);
            t = fcall.reference;
        } else if(val instanceof SymbolList) {
            SymbolList list = (SymbolList) val;
            // If a list is not declared (like as a parameter for out) then generate list will change currentDec, but not restore it afterwards.
            String prevDec = currentDec; 
            generate(list);
            t = list.reference;
            // We calculate the total dimensions on this variable, starting by the first level's length
            VTEntry vte = getVar(currentDec);
            currentDec = prevDec; // We restore the previous value of currentDec
            SymbolTypeVar subLevel = list.type;
            // We go through each sublevel so that we can calculate the occupation accordingly.
            while(subLevel.getArrayDepth() > vte.dimensions.size()){
                vte.dimensions.add(""+subLevel.arrayLength);
                subLevel = subLevel.getBaseType();
            }
        } else if(val instanceof Integer) {
            int intValue = (Integer) val; 
            t = newVariable();
            addInstruction(InstructionType.copy, "" + intValue, t);
        } else if(val instanceof Boolean) {
            boolean boolValue = (Boolean) val;
            t = newVariable();
            addInstruction(InstructionType.copy, "" + (boolValue ? VTEntry.TRUE : VTEntry.FALSE), t);
        } else if(val instanceof Character) {
            char cValue = (Character) val;
            t = newVariable();
            addInstruction(InstructionType.copy, "" + cValue, t);
            getVar(t).type = ParserSym.KW_CHAR;
        }
        value.reference = t;
    }


    private void generate(SymbolVar var){
        VTEntry vte = getVar(var.getId());
        String t = vte.tName;

        SymbolArrSuff arrSuff = var.getArrSuff();
        if(arrSuff != null){
            dimensionsToCheck = vte.cloneDimensions();
            generate(arrSuff);
            String tSuffix = arrSuff.reference;
            int nBytes = VTEntry.CHAR_BYTES;
            if(vte.type == ParserSym.KW_INT){
                nBytes = VTEntry.INTEGER_BYTES;
            }
            // Here should go 
            // tn = tm - b 
            // but b is always 0 in our language, as all arrays start with 0
            // so we don't have to include it in this case.
            // t' = tSuff * nbytes
            // t'' = t[t']
            // t <- t''
            String tPrima = newVariable();
            addInstruction(InstructionType.prod, tSuffix, "" + nBytes, tPrima);
            String arrayOrigin = t;
            t = newVariable();
            addInstruction(InstructionType.ind_val, arrayOrigin, tPrima, t);
        }

        var.reference = t;
    }

    private String newVariable(){
        String t = "t" + numT++;
        VTEntry vte = new VTEntry(t);
        variableTable.put(currentFunction + currentSublevel + t, vte);
        if(currentProcTable != null) currentProcTable.variableTable.put(currentSublevel + t, vte);
        return t;
    }

    private String newTag(){
        return "e" + numE++;
    }

    private VTEntry getVar(String t){
        VTEntry vte = variableTable.get(currentFunction + currentSublevel + t);
        int i = currentSublevel-1;

        // We check up until -1 since parameters are in sublevel -1
        while(vte == null && i >= -1) {
            vte = variableTable.get(currentFunction + i-- + t);
        }
        if(vte == null) {
            vte = variableTable.get(DEF_FUNCTION + 0 + t);
        }
        return vte;
    }

    private void removeVar(String t){
        VTEntry vte = variableTable.remove(currentFunction + currentSublevel + t);
        int i = currentSublevel-1;
        while(vte == null && i >= 0) {
            vte = variableTable.remove(currentFunction + i-- + t);
        }
        if(vte == null) {
            vte = variableTable.remove(DEF_FUNCTION + 0 + t);
        }
    }
    
    private void replaceVarTableKey(String oldKey, String newKey){
        VTEntry vte = getVar(oldKey);
        removeVar(oldKey);
        variableTable.put(currentFunction + currentSublevel + newKey, vte);
    }

    private String createPTEntry(String procName){
        String internalFunctionName = procName + DEF_FUNCTION;
        PTEntry pte = new PTEntry();
        pte.eStart = newTag();
        procedureTable.put(internalFunctionName, pte);
        return internalFunctionName;
    }

    private void addInstruction(InstructionType instruction, String left, String right, String destination){
        Instruction i = new Instruction(instruction, left, right, destination);
        c3a.add(i);
    }
    
    private void addInstruction(InstructionType instruction, String left, String destination){
        Instruction i = new Instruction(instruction, left, destination);
        c3a.add(i);
    }
    
    private void addInstruction(InstructionType instruction, String destination){
        Instruction i = new Instruction(instruction, destination);
        c3a.add(i);
    }

    public String toString(){
        String s = "";
        for(Instruction i : c3a){
            s += i + "\n";
        }
        return s;
    }*/
}