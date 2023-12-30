/**
 * Asignatura: 21780 - Compiladores
 * Miembros:
 * 	- Korn, Andreas Manuel
 * 	- Román Colom, Marc
 * 	- Vilella Candía, Joan 
 */
package analizadorSemantico;

import analizadorSintactico.ParserSym;
import java.util.ArrayList;
import java.util.Stack;

import analizadorSintactico.symbols.SymbolBase;
import analizadorSintactico.symbols.SymbolBody;
import analizadorSintactico.symbols.SymbolDecs;
import analizadorSintactico.symbols.SymbolElse;
import analizadorSintactico.symbols.SymbolIf;
import analizadorSintactico.symbols.SymbolInstr;
import analizadorSintactico.symbols.SymbolLoop;
import analizadorSintactico.symbols.SymbolMain;
import analizadorSintactico.symbols.SymbolOperand;
import analizadorSintactico.symbols.SymbolReturn;
import analizadorSintactico.symbols.SymbolScript;
import analizadorSintactico.symbols.SymbolSwap;
import analizadorSintactico.symbols.*;

public class SemanticAnalysis {
    
    public TablaSimbolos TablaSimbolos;

    // Description to the function in which we are currently.
    private DescripcionSimbolo currentFunction;
    // When checking if a function's parameters are correct, we use this stack to store the declared function's types.
    // We use a stack because we will be taking elements out every time we process them.
    private Stack<SymbolTypeVar> currentArgs;
    private boolean returnFound;

    private ArrayList<String> errors;
    public boolean thereIsError = false;

    public SemanticAnalysis(SymbolScript script){
        TablaSimbolos = new TablaSimbolos();
        errors = new ArrayList<>();
        /*SymbolDecs decs = body.getDeclarations();
        if(decs != null) manage(decs);
        SymbolMain main = body.getMain();
        manage(main);*/
    }

    public String getErrors(){
        String s = "";
        for(String e : errors){
            s += e + "\n";
        }
        return s;
    }
    
    /**
     * Arg: getType(), identifier
     * @param arg
     */
    private void manage(SymbolArg arg){
        /* Possible errors:
         * 1. arg is already defined in this level (aka. two args with the same name). Thrown from TablaSimbolos.
         * 2. arg is type void
         */
        String identifier = arg.identifier;
        SymbolTypeVar type = arg.getType();
        if(type.isType(KW_VOID)) {
            reportError("Function argument '" + identifier + "' cannot be of type void", arg.line, arg.column);
            return;
        }
        if(type.isType(KW_ARRAY)) {
            reportError("Error with function argument '" + identifier + "': arrays are not supported in this version of LenguaG", arg.line, arg.column);
            return;
        }
        DescripcionSimbolo desc = new DescripcionSimbolo();
        desc.changeType(type);
        //try{
            TablaSimbolos.insertVariable(identifier, desc);
        //} catch(SemanticException se) {
        //    reportError("Argument '" + identifier + "' declared twice", arg.line, arg.column);
        //    return;
        //}
        currentFunction.addArgument(identifier, type);
    }

    /**
     * Args: getArg(), getNext()
     * @param args
     */
    private void manage(SymbolArgs args){
        /* Possible errors:
         * - None that are not treated inside of manage(SymbolArg)
         */
        manage(args.getArg());
        SymbolArgs next = args.getNext();
        if(next != null) manage(next);
    }

    /**
     * ArrSuff: getIndex(), getNext(), getDimensions()
     * @param arrSuff
     */
    private void manage(SymbolArrSuff arrSuff){
        /* Possible errors:
         * 1. Index is not an int
         */
        SymbolOperation index = arrSuff.getIndex();
        if(index == null) {
            reportError("No index specified", arrSuff.line, arrSuff.column);
            return;
        }
        manage(index);
        if(!index.type.isType(ParserSym.KW_INT)){
            reportError("Array must be indexed by an integer value", arrSuff.line, arrSuff.column);
            return;
        }
        SymbolArrSuff next = arrSuff.getNext();
        if(next != null) manage(next);
    }

    /**
     * Assign: getVariable(), getRightSide()
     * @param assign
     */
    private void manage(SymbolAssign assign){
        /* Possible errors:
         * 1. Variable was not declared.
         * 2. Trying to change the value of a constant variable.
         * 3. Trying to assign a different type.
         */
        String var = assign.getVariable().getId();
        DescripcionSimbolo varDesc = TablaSimbolos.getDescription(var);
        if(varDesc == null) {
            reportError("Variable '" + var + "' was not declared", assign.line, assign.column);
            return;
        }
        if(varDesc.isConstant) {
            reportError("Can't change the value of constant '" + var + "'", assign.line, assign.column);
            return;
        }
        SymbolOperation rSide = assign.getRightSide();
        manage(rSide);
        if(!rSide.type.isType(varDesc.getType())){
            // Different types
            reportError("Type incongruency with '" + var + "': "
            + rSide.type + " cannot be cast into " + ParserSym.terminalNames[varDesc.getType()], assign.line, assign.column);
            return;
        }
        // Array declaration
        if(rSide.type.isType(KW_ARRAY)){
            if(!varDesc.getBaseType().equals(rSide.type.getBaseType())){
                // Arrays, but from different base type or dimensions
                reportError("Type incongruency with '" + var + "': lists of different base types or dimensions", assign.line, assign.column);
                return;
            }
        }
    }

    /**
     * Declaration: isConstant, variableName, getType(), getValue()
     * @param dec
     */
    private void manage(SymbolDec dec){
        /* Possible errors:
         * 1. Set as constant but has a variable value or no value
         * 2. Variable set as void
         * 3. type and the value's type are incompatible
         * 4. variableName already present in TablaSimbolos at this level.
         *      This is detected INSIDE TablaSimbolos, but error must be caught here
         */
        SymbolTypeVar type = dec.getType();
        // If type is void, error
        if(type.isType(KW_VOID)){
            reportError("Tried to declare variable '" + dec.variableName + "' as type void", dec.line, dec.column);
            return;
        }
        DescripcionSimbolo description = new DescripcionSimbolo();
        // manage(type);
        SymbolOperation value = dec.getValue();
        if(value != null) {
            manage(value);
            // Incompatible types
            if(!type.isType(value.type.getType())) {
                // Two different types
                reportError("Type incongruency with '" + dec.variableName + "': "
                + value.type + " cannot be cast into " + type, dec.line, dec.column);
                return;
            }
            // Constant declared, but value is variable.
            if(dec.isConstant && !value.isConstant){
                reportError("Cannot assign variable value to constant '" + dec.variableName + "'", dec.line, dec.column);
                return;
            }
            // Array declaration
            if(type.isType(KW_ARRAY)){
                if(!type.getBaseType().equals(value.type.getBaseType())){
                    // Arrays, but from different base type or dimensions
                    reportError("Type incongruency with '" + dec.variableName + "': lists of different base types or dimensions", dec.line, dec.column);
                    return;
                }
                // If it's an array and no dimension errors were found we can just copy the type
                type = value.type;
            }
        } else if(dec.isConstant){
            // Set as constant but no value given
            reportError("Constant '" + dec.variableName + "' declared without value", dec.line, dec.column);
            return;
        }

        // Everything ok!
        // We add the variable to the symbol table 
        description.changeType(type);
        description.isConstant = dec.isConstant;
        if(dec.isConstant) description.changeValue(value.getSemanticValue());
        try{ 
            TablaSimbolos.insertVariable(dec.variableName, description);
        } catch(SemanticException se){
            reportError(se.getMessage(), dec.line, dec.column);
        }
    }

    /**
     * Decs: Dec/Func, Decs.1
     * @param decs
     */
    private void manage(SymbolDecs decs){
        SymbolBase dec = decs.getDeclaration();
        if(dec instanceof SymbolDec) manage((SymbolDec) dec);
        else if (dec instanceof SymbolFunc) manage((SymbolFunc) dec);
        else // Impossible case. Here as a safeguard to notify us if we did something wrong.
            System.err.println(" !!! Compiler error !!!");
        
        SymbolDecs nextDecs = decs.getNext();
        if (nextDecs != null) manage(nextDecs);
    }

    /**
     * sElse: getIf() | getInstructions()
     * @param sElse
     */
    private void manage(SymbolElse sElse){
        SymbolIf nextIf = sElse.getIf();
        SymbolInstrs instrs = sElse.getInstructions();
        if(nextIf != null) manage(nextIf);
        else if(instrs != null) {
            TablaSimbolos.enterBlock();
            manage(instrs);
            try {
                TablaSimbolos.exitBlock();
            } catch(CompilerException ce) {
                ce.printStackTrace();
            }
        } else {
            reportError("Unexpected compilation error: else missing", sElse.line, sElse.column);
            return;
        }
    }

    /**
     * sFor: getInit(), getCondition(), getFinal(), getInstructions()
     * @param sFor
     */
    private void manage(SymbolFor sFor){
        /* Possible errors:
         * 1. Condition not a viable value (can't be void or char)
         */
        
        // We must enter a new block
        TablaSimbolos.enterBlock();

        // getInit() -> SymbolDec, SymbolAssign, SymbolSwap or SymbolFuncCall
        SymbolBase init = sFor.getInit();
        if(init instanceof SymbolDec) manage((SymbolDec) init);
        else if(init instanceof SymbolAssign) manage((SymbolAssign) init);
        else if(init instanceof SymbolSwap) manage((SymbolSwap) init);
        else if(init instanceof SymbolFuncCall) manage((SymbolFuncCall) init);

        SymbolOperation cond = sFor.getCondition();
        manage(cond);
        if(cond.type.isType(KW_VOID)|| cond.type.isType(KW_CHARACTER)){
            reportError("Condition can't be of type " + cond.type, sFor.line, sFor.column);
            // We don't return: we try to find errors in the instructions.
        }

        // If we are in a function, the inside of a for may or may not be accessed, so a return in there is not enough for us to make sure that a return was found.
        // We must store the previous value and then restore it.
        boolean prevReturn = returnFound;

        SymbolInstrs instrs = sFor.getInstructions();
        if(instrs != null) {
            manage(instrs);
        }

        // getInit() -> SymbolDec, SymbolAssign, SymbolSwap or SymbolFuncCall
        SymbolBase end = sFor.getFinal();
        if(end instanceof SymbolDec) manage((SymbolDec) end);
        else if(end instanceof SymbolAssign) manage((SymbolAssign) end);
        else if(end instanceof SymbolSwap) manage((SymbolSwap) end);
        else if(end instanceof SymbolFuncCall) manage((SymbolFuncCall) end);

        returnFound = prevReturn;

        try {
            TablaSimbolos.exitBlock();
        } catch(CompilerException ce){
            ce.printStackTrace();
        }
    }

    /**
     * Function: getType(), getFunctionName(), getArgs(), getInstructions()
     * @param func
     */
    private void manage(SymbolFunc func){
        /* Possible errors:
         * 1. Function already declared (checked inside of TablaSimbolos)
         * 2. Function type and return type are not compatible.
         *      Complex solution. No direct connection between SymbolFunc and any instruction in the function.
         *      Is solved instead in manage(SymbolReturn)
         * 3. Returns something but no return was found 
         */
        String name = func.getFunctionName();
        // We check whether the function was already declared or not. 
        try {
            // We set current function to the current function's empty description so that we can fill it up further in the process.
            currentFunction = new DescripcionSimbolo(); 
            TablaSimbolos.insertVariable(name, currentFunction);
        } catch(SemanticException se){
            // If symbol table already found the name of the function, it's a compilation error. 
            // This means that a variable and a function cannot share the same name.
            reportError(se.getMessage(), func.line, func.column);
            return;
        }

        // Everything ok so far!
        // We enter a new ambit on the symbol table
        TablaSimbolos.enterBlock();
        // We change the type of the created symbol on the symbol table to TYPE_FUNCTION
        currentFunction.changeType(KW_FUNCTION);
        // We manage the return type of the function.
        SymbolTypeVar type = func.getType();
        // We set a found return to false
        returnFound = false;
        // manage(type); No need to do anything
        currentFunction.setReturnType(type); // and set it in the description.

        // Arguments treatment. Inside here, currentFunction should receive the different arguments of the function.
        SymbolArgs args = func.getArgs();
        if(args != null) manage(args);

        // Instructions treatment. Inside here, we will deal with the return statement being of a compatible type with the function's return type
        SymbolInstrs instrs = func.getInstructions();
        if(instrs != null) manage(instrs);

        // After managing the instructions, we check if a return was found.
        if(!returnFound && !type.isType(KW_VOID)){
            reportError("Return for function " + name + " not found or inside branches which might not be accessed", func.line, func.column);
        }
        
        // We return to the previous ambit
        try{
            TablaSimbolos.exitBlock();
        } catch(CompilerException ce){
            // !!! Compiler error !!!
            ce.printStackTrace();
        }
    }

    /**
     * functionCall: getFunctionName(), getNParams(), getParams()
     * @param functionCall
     */
    private void manage(SymbolFuncCall functionCall){
        /* Possible errors:
         * 1. Function does not exist.
         * 2. nParams and declared function's nArgs are different
         * 3. Params are a different type than the function's Args
         */
        String name = functionCall.getFunctionName();
        DescripcionSimbolo desc = TablaSimbolos.getDescription(name);
        if(desc == null) {
            // Function not declared
            reportError("Function '" + name + "' was not declared", functionCall.line, functionCall.column);
            return;
        }
        if(functionCall.getNParams() != desc.getNArgs()) {
            // Differing parameter amounts
            reportError("Differing parameters amount with function '" 
            + name + "', expecting " + desc.getNArgs() + " parameters, found " + functionCall.getNParams(), functionCall.line, functionCall.column);
            return;
        }
        SymbolParams params = functionCall.getParams();
        if(params != null) {
            currentArgs = desc.getArgsTypes();
            manage(params);
        }
        // We set the type to the function's return type
        functionCall.type = desc.getReturnType();
    }

    /**
     * sIf: getCondition(), getInstructions(), getElse()
     * @param sIf
     */
    private void manage(SymbolIf sIf){
        /* Possible errors:
         * 1. Condition not a viable value (can't be void or char)
         */
        
        // We enter a new block
        TablaSimbolos.enterBlock();
        // We will need two booleans to check if a return was found inside of a branch, and whether a return statement will always be reached,
        // and another to store the previous value just in case.
        boolean returnIf = false, returnElse = false, prevReturn = returnFound;

        SymbolOperation cond = sIf.getCondition();
        manage(cond);
        if(cond.type.isType(KW_VOID) || cond.type.isType(KW_CHARACTER)){
            reportError("Condition can't be of type " + cond.type, sIf.line, sIf.column);
            // We don't return: we try to find errors in the instructions.
        }

        // We manage the instructions. This may change returnFound.
        SymbolInstrs instrs = sIf.getInstructions();
        if(instrs != null) {
            manage(instrs);
            // We store if a return was found inside of the if, and reset returnFound.
            returnIf = returnFound;
            returnFound = prevReturn;
        }

        try {
            TablaSimbolos.exitBlock();
        } catch (CompilerException ce){
            ce.printStackTrace();
        }

        SymbolElse sElse = sIf.getElse();
        if(sElse != null) {
            // We manage the else operation, which may also change return found. 
            manage(sElse);
            returnElse = returnFound;
            returnFound = prevReturn;
        }

        // there is a return if a return was found previously or if both branches of the if had a safe return found.
        returnFound = returnFound || (returnIf && returnElse);
    }

    private void manage(SymbolIn in){
        /*
         * Possible errors:
         * 1. Variable is a constant
         * 2. Variable type not supported (supported types are int or char)
         */
        SymbolVar var = in.getVar();
        int line = in.line, column = in.column;
        if (var == null) {
            reportError("Parameter expected for operation int()", line, column);
            return;
            
        }
        manage(var);
        if(var.isConstant){
            reportError("Cannot store input in value " + var.getId(), line, column);
            return;
        }
        int type = var.type.getType();
        if(type != ParserSym.KW_INT && type != KW_CHARACTER){
            reportError("Unsupported parameter type: "+ var.type, line, column);
            return;
        }
    }

    /**
     * Instruction: instructionType
     * @param instruction
     */
    private void manage(SymbolInstr instruction){
        switch(instruction.getInstructionType()){
            case instDeclaration:
                manage((SymbolDec) instruction);
                return;
            case instAssignation:
                manage((SymbolAssign) instruction);
                return;
            case instSwap:
                manage((SymbolSwap) instruction);
                return;
            case instFunctionCall:
                manage((SymbolFuncCall) instruction);
                return;
            case instReturn:
                manage((SymbolReturn) instruction);
                return;
            case instIf:
                manage((SymbolIf) instruction);
                return;
            case instLoop:
                manage((SymbolLoop) instruction);
                return;
            case instFor:
                manage((SymbolFor) instruction);
                return;
            case instIn:
                manage((SymbolIn) instruction);
                return;
            case instOut:
                manage((SymbolOut) instruction);
                return;
            default:
                reportError("Unidentified instruction", instruction.line, instruction.column);
        }
    }

    /**
     * Instructions: getInstruction(), getNext()
     * @param instructions
     */
    private void manage(SymbolInstrs instructions){
        if(returnFound){
            // This is a warning, not a semantic error.
            System.err.println("Warning: Instructions directly after return might not be accessed at position [line: " + instructions.line + ", column: " + instructions.column + "]");
        }
        manage(instructions.getInstruction());
        SymbolInstrs next = instructions.getNext();
        if(next != null) manage(next);
    }

    /**
     * List: getValue(), getNext(), length
     * @param list
     */
    private void manage(SymbolList list){
        /* Possible errors:
         * 1. Different types declared in array
         */
        SymbolOperation value = list.getValue();
        manage(value);
        SymbolList next = list.getNext();
        if(next != null && next.getValue() != null) {
            manage(next);
            // if(!value.type.isType(next.type.getBaseType().getType())){
            if(!value.type.equals(next.type.getBaseType())){
                reportError("Cannot declare an array with heterogeneous types", list.line, list.column);
            }
        }
        list.type = new SymbolTypeVar(KW_ARRAY, value.type);
    }

    /**
     * Loop: getCondition(), getInstructions()
     * @param loop
     */
    private void manage(SymbolLoop loop){
        /*
         * Possible errors:
         * 1. Condition not a viable value (can't be void or char)
         */

        // We must enter a new block
        TablaSimbolos.enterBlock();
        SymbolOperation cond = loop.getCondition();
        manage(cond);
        if (cond.type.isType(KW_VOID) || cond.type.isType(KW_CHARACTER)) {
            reportError("Condition can't be of type " + cond.type, loop.line, loop.column);
            // We don't return: we try to find errors in the instructions.
        }

        // The instructions inside a while may or may not be accessed, so if we are inside a function, reaching a return statement is not ensured even if there is one inside.
        // We must store the previous value and then restore it.
        boolean prevReturn = returnFound;

        SymbolInstrs instrs = loop.getInstructions();
        if (instrs != null) {
            manage(instrs);
        }

        returnFound = prevReturn;

        try {
            TablaSimbolos.exitBlock();
        } catch (CompilerException ce) {
            ce.printStackTrace();
        }
    }

    /**
     * Main: getInstructions()
     * @param main
     */
    private void manage(SymbolMain main){
        currentFunction = new DescripcionSimbolo();
        currentFunction.changeType(KW_FUNCTION); // This sets the current function to a void function with 0 arguments

        // Main cannot have a return.
        returnFound = false;

        SymbolInstrs instrs = main.getInstructions();
        if(instrs != null) {
            TablaSimbolos.enterBlock();
            manage(instrs);
            try {
                TablaSimbolos.exitBlock();
            } catch (CompilerException ce){
                // !!! Compiler error !!!
                ce.printStackTrace();
            }
        }
    }

    /**
     * Operand: Value/Operation, isNegated
     * @param operand
     */
    private void manage(SymbolOperand operand){
        if(operand.isLeaf()) {
            SymbolValue value = (SymbolValue) operand.getValue();
            manage(value);
            operand.type = value.type;
            // We set the constant state to the value's and set its semantic value if constant
            if(operand.isConstant = value.isConstant) operand.setSemanticValue(value.getSemanticValue());
        } else {
            SymbolOperation operation = (SymbolOperation) operand.getValue();
            manage(operation);
            operand.type = operation.type;
            if(operand.isConstant = operation.isConstant) operand.setSemanticValue(operation.getSemanticValue());
        }
        
        if(operand.isConstant){
            if(operand.isNegated()){
                if(operand.getSemanticValue() instanceof Integer)
                    operand.setSemanticValue(-(Integer) operand.getSemanticValue());
                else if(operand.getSemanticValue() instanceof Boolean)
                    operand.setSemanticValue(!(Boolean) operand.getSemanticValue());
            }
        }
    }

    /**
     * Operation: getLValue(), getOp(), getRValue().
     * @param operation
    */
    private void manage(SymbolOperation operation){
        /* Possible errors:
         * 1. l-value and r-value have incompatible types
         * 2. Operation not supported for operand's types
         */
        SymbolOperand lValue = operation.getLValue();
        manage(lValue);
        SymbolOp op = operation.getOperation();
        SymbolOperand rValue = operation.getRValue();
        if(op == null && rValue == null){
            // It only encapsulated an operand, so no operation must be done. Type and constant state is inherited
            operation.type = lValue.type;
            operation.isConstant = lValue.isConstant;
            if(operation.isConstant) operation.setSemanticValue(lValue.getSemanticValue());
            return;
        } else if(op == null || rValue == null) {
            // Impossible case: one of them is null, but not both. Here as a safeguard to notify us
            System.err.println(" !! Compiler error in manage operation.");
        }
        manage(rValue);

        // If any of the operands is not constant, the result of the operation is also not constant
        if(!lValue.isConstant || !rValue.isConstant){
            operation.isConstant = false;
        }

        switch (op.operation) {
            case Constants.ADD:
                // Supported operations
                // int + int
                if(((lValue.type.isType(ParserSym.KW_INT)) && (rValue.type.isType(ParserSym.KW_INT)))){
                    // We accept. Result is int (same type as lValue)
                    operation.type = lValue.type;
                    if(operation.isConstant) operation.setSemanticValue((Integer) lValue.getSemanticValue() + (Integer) rValue.getSemanticValue());
                    break; // Exit switch case
                }
                // We do not accept.
                reportError("Unsupported operation: " + lValue.type + " and " + rValue.type + " are incompatible", operation.line, operation.column);
                return;
            case Constants.OR:
                // Only supported operation: bool or bool
                if(((lValue.type.isType(KW_BOOL)) && (rValue.type.isType(KW_BOOL)))){
                    // We accept. Result is boolean (same type as lValue)
                    operation.type = lValue.type;
                    if(operation.isConstant){
                        operation.setSemanticValue((Boolean) lValue.getSemanticValue() || (Boolean) rValue.getSemanticValue());
                    }
                } else {
                    reportError("Unsupported operation: " + lValue.type + " and " + rValue.type + " are incompatible", operation.line, operation.column);
                    return;
                }
                break;
            case Constants.AND:
                // Only supported operation: bool (op) bool
                if(((lValue.type.isType(KW_BOOL)) && (rValue.type.isType(KW_BOOL)))){
                    // We accept. Result is boolean (same type as lValue)
                    operation.type = lValue.type;
                    if(operation.isConstant){
                        operation.setSemanticValue((Boolean) lValue.getSemanticValue() && (Boolean) rValue.getSemanticValue());
                    }
                } else {
                    reportError("Unsupported operation: " + lValue.type + " and " + rValue.type + " are incompatible", operation.line, operation.column);
                    return;
                }
                break;
            default:
                // Left to check: SUB, PROD, DIV, MOD. They follow the same rules, so they are here in order to recycle code and avoid making many comparisons.
                if(!op.isRelational){
                    // Only supported operation: int (op) int
                    if(((lValue.type.isType(ParserSym.KW_INT)) && (rValue.type.isType(ParserSym.KW_INT)))){
                        // We accept. Result is int (same type as lValue)
                        operation.type = lValue.type;
                        if(operation.isConstant){
                            switch(op.operation){
                                case Constants.SUB:
                                    operation.setSemanticValue((Integer) lValue.getSemanticValue() - (Integer) rValue.getSemanticValue());
                                case Constants.PROD:
                                    operation.setSemanticValue((Integer) lValue.getSemanticValue() * (Integer) rValue.getSemanticValue());
                                case Constants.DIV:
                                    operation.setSemanticValue((Integer) lValue.getSemanticValue() / (Integer) rValue.getSemanticValue());
                                case Constants.MOD:
                                    operation.setSemanticValue((Integer) lValue.getSemanticValue() % (Integer) rValue.getSemanticValue());
                            }
                        }
                    } else {
                        reportError("Unsupported operation: " + lValue.type + " and " + rValue.type + " are incompatible", operation.line, operation.column);
                        return;
                    }
                    break;

                } else {
                    // Relational operation
                    // Only supported operation: int (op_rel) int
                    if(((lValue.type.isType(ParserSym.KW_INT)) && (rValue.type.isType(ParserSym.KW_INT)))){
                        // We accept. Result is boolean
                        operation.type = new SymbolTypeVar(KW_BOOL);
                        if(operation.isConstant){
                            switch(op.operation){
                                case Constants.IS_EQUAL:
                                    operation.setSemanticValue((Integer) lValue.getSemanticValue() == (Integer) rValue.getSemanticValue());
                                case Constants.BIGGER:
                                    operation.setSemanticValue((Integer) lValue.getSemanticValue() > (Integer) rValue.getSemanticValue());
                                case Constants.BEQ:
                                    operation.setSemanticValue((Integer) lValue.getSemanticValue() >= (Integer) rValue.getSemanticValue());
                                case Constants.LESSER:
                                    operation.setSemanticValue((Integer) lValue.getSemanticValue() < (Integer) rValue.getSemanticValue());
                                case Constants.LEQ:
                                    operation.setSemanticValue((Integer) lValue.getSemanticValue() <= (Integer) rValue.getSemanticValue());
                                case Constants.NEQ:
                                    operation.setSemanticValue((Integer) lValue.getSemanticValue() != (Integer) rValue.getSemanticValue());
                            }
                        }
                    } else {
                        reportError("Unsupported operation: Cannot compare " + lValue.type + " and " + rValue.type, operation.line, operation.column);
                        return;
                    }
                }
        }
    }

    private void manage(SymbolOut out){
        /* Possible  errors:
         * 1. 0 parameters passed by
         * 2. Parameter void type
         */
        SymbolOperation value = out.getValue();
        int line=out.line,column=out.column;
        if(value == null){
            reportError("Parameter required for function out()", line, column);
            return;
        }
        manage(value);
        if(value.type.isType(KW_VOID)){
            reportError("Unsupported Operation: Parameter of out() cannot be void type", line,column);
        }
    }

    /**
     * Params: getValue(), getNext()
     * @param params
     */
    private void manage(SymbolParams params){
        /* Possible errors:
         * 1. Type incongruency between params and declared arguments
         */
        // Arguments are in a stack so we must go to the last one
        SymbolParams next = params.getNext();
        if(next != null) manage(next);
        
        SymbolOperation value = params.getValue();
        manage(value);
        SymbolTypeVar arg = currentArgs.pop();
        if(!value.type.equals(arg)){
            reportError("Type incongruency with parameter: expected " + arg + " but received " + value.type + " instead", params.line, params.column);
            return;
        }
    }

    /**
     * sReturn: getValue()
     * @param sReturn
    */
    private void manage(SymbolReturn sReturn){
        /* Possible errors:
         * 1. value's type does not equal the function return type.
         */
        SymbolOperation value = sReturn.getValue();

        if(value == null){
            if(!currentFunction.getReturnType().isType(KW_VOID)){
                reportError("Function must return " + currentFunction.getReturnType(), sReturn.line, sReturn.column);
                return;
            }
            // Accept
            return; 
        } 

        manage(value);
        
        if(!value.type.equals(currentFunction.getReturnType())){
            reportError("Can't return " + value.type + ", function must return " + currentFunction.getReturnType(), sReturn.line, sReturn.column);
            return;
        }

        returnFound = true;
    }

    /**
     * Swap: getVar1(), getVar2()
     * @param swap
     */
    private void manage(SymbolSwap swap){
        /* Possible errors:
         * 1. Try to swap constant(s).
         * 2. Variables of different types.
         */
        SymbolVar var1 = swap.getVar1();
        manage(var1);
        SymbolVar var2 = swap.getVar2();
        manage(var2);
        if(var1.isConstant){
            reportError("Can't swap constant '" + var1.getId() + "'", var1.line, var1.column);
            return;
        }
        if(var2.isConstant){
            reportError("Can't swap constant '" + var2.getId() + "'", var2.line, var2.column);
            return;
        }
        if(!var1.type.equals(var2.type)){
            reportError("Can't swap " + var1.type + " and " + var2.type, swap.line, swap.column);
            return;
        }
    }

    /**
     * Value: getValue(), isConstant
     * @param value
     */
    private void manage(SymbolValue value){
        Object val = value.getSemanticValue();
        if(val instanceof SymbolVar) {
            SymbolVar var = (SymbolVar) val;
            manage(var);
            value.type = var.type;
            value.isConstant = var.isConstant;
            if(value.isConstant) value.setSemanticValue(var.getSemanticValue());
        } else if(val instanceof SymbolFuncCall) {
            SymbolFuncCall fcall = (SymbolFuncCall) val;
            manage(fcall);
            value.type = fcall.type;
            value.isConstant = false;
        } else if(val instanceof SymbolList) {
            SymbolList list = (SymbolList) val;
            manage(list);
            value.type = list.type;
            value.type.arrayLength = list.getLength();
            value.isConstant = false;
        } else if(val instanceof Integer) value.type = new SymbolTypeVar(ParserSym.KW_INT);
        else if(val instanceof Boolean) value.type = new SymbolTypeVar(KW_BOOL);
        else if(val instanceof Character) value.type = new SymbolTypeVar(KW_CHARACTER);
    }

    /**
     * Var: getId(), getArrSuffix()
     * @param var
    */
    private void manage(SymbolVar var){
        /* Possible errors:
         * 1. Variable not declared
         * 2. Var is detected as array, but was not declared as array.
         * 3. The dimensions of the suffix are not the same as the dimensions of the declared array
         */
        String id = var.getId();
        DescripcionSimbolo desc = TablaSimbolos.getDescription(id);
        if(desc == null){
            reportError("Variable " + id + " has not been declared", var.line, var.column);
            return;
        }
        if(var.isArray()){
            if(desc.getType() != KW_ARRAY){
                reportError("Cannot index variable " + id, var.line, var.column);
                return;
            }
            SymbolArrSuff arrSuff = var.getArrSuff();
            manage(arrSuff);

            // Array suffix correct!
            var.isConstant = false;
            // We get the BASE type of the array.
            var.type = desc.getBaseType();
            // If it has more than one dimensions, we must iterate until we get to the last BaseType
            for(int i = 1; i < arrSuff.getDimensions(); i++){
                SymbolTypeVar temp = var.type.getBaseType();
                if(temp == null){
                    reportError("Array indexing dimensions different than the array's dimensions", var.line, var.column);
                    return;
                }
                var.type = temp;
            }
            return;
        }
        if(desc.getType() == KW_ARRAY){
            var.type = new SymbolTypeVar(KW_ARRAY, desc.getBaseType());
            var.type.arrayLength = desc.getLength();
            var.isConstant = false;
            return;
        }

        // Not an array: we get the type of the variable.
        var.type = new SymbolTypeVar(desc.getType());
        var.isConstant = desc.isConstant;
        if(var.isConstant) var.setSemanticValue(desc.getValue());
    }

    private void reportError(String errorMessage, int line, int column){
        thereIsError = true;
        errorMessage = " !! Semantic error: " + errorMessage + " at position [line: " + line + ", column: " + column + "]";
        System.err.println(errorMessage);
        errors.add(errorMessage);
    }

}