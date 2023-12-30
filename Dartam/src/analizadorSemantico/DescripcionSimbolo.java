package analizadorSemantico;

import analizadorSintactico.ParserSym;
import java.util.ArrayList;
import java.util.Stack;
import analizadorSintactico.symbols.SymbolTypeVar;
/**
 * Class that represents a variable's description in the symbol table
 */
public class DescripcionSimbolo {

    public static final int TYPE_ARRAY = Integer.MAX_VALUE;
    public static final int TYPE_FUNCTION = TYPE_ARRAY-1;
    
    // The variable's type
    private SymbolTypeVar type; // Acts as the return type when a function.
    private Object value;

    public int declaredLevel;
    public boolean isConstant;

    // Function information
    private boolean isFunction;
    private int nArgs;
    private ArrayList<Argument> args; // arguments are name and type

    /**
     * Creates an empty description
     */
    public DescripcionSimbolo(){
        type = new SymbolTypeVar();
        isConstant = false;
        isFunction = false;
    }

    /**
     * Changes the type of the variable to which this description is associated, given a symbol created by Parser.java.
     * @param type
     */
    public void changeType(SymbolTypeVar type) {
        this.type = type;
    }

    /**
     * Changes the type of the variable to which this description is associated, given the value of Constants.
     * If given TYPE_ARRAY, baseType and depth will be set to void and 0, respectively, so those should be set afterwards
     * using changeBaseType() and changeDepth().
     * Likewise, nArgs, returnType and args will be set to 0, void and an empty HashMap, respectively. 
     * Use addArgument() and setReturnType() to change them.
     * @param type
     */
    public void changeType(int type) {
        if(type == TYPE_FUNCTION){
            isFunction = true;
            nArgs = 0;
            this.type = new SymbolTypeVar();
            args = new ArrayList<>();
        }
    }

    public void changeValue(Object value){
        this.value = value;
    }

    public Object getValue(){
        return value;
    }

    // Function methods

    /**
     * Adds the param of the given type to the parameters of the function, incrementing nArgs
     * @param name
     * @param type
     */
    public void addArgument(String name, SymbolTypeVar type) {
        args.add(new Argument(name, type));
        nArgs++;
    }

    public int getNArgs() {
        return nArgs;
    }

    public Stack<SymbolTypeVar> getArgsTypes() {
        Stack<SymbolTypeVar> types = new Stack<>();
        for(Argument a : args){
            types.push(a.type);
        }
        return types;
    }

    public void setReturnType(SymbolTypeVar returnType) {
        if(!isFunction) throw new RuntimeException(" !! Compiler error");
        type = returnType;
    }

    public SymbolTypeVar getReturnType() {
        return type;
    }

    @Override
    public String toString(){
        String sd = "[Type: " + (isFunction ? ParserSym.terminalNames[TYPE_FUNCTION] : type);
        if(isFunction) sd += " (Returns: " + type + ", args:" + args + ")";
        sd += "\n\tConstant: " + isConstant;
        if(isConstant) sd += "\n\tValue: " + value;
        sd += "\n\tDeclared level: " + declaredLevel + "]";
        return sd;
    }

    private class Argument{
        public String name;
        public SymbolTypeVar type;

        public Argument(String name, SymbolTypeVar type){
            this.name = name;
            this.type = type;
        }

        @Override
        public String toString(){
            return type + " " + name;
        }
    }
}