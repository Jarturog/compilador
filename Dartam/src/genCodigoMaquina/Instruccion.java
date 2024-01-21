package genCodigoMaquina;

public class Instruccion {
    public enum InstructionType {
        copy, add, sub, prod, div, mod, neg, and, or, not, ind_val, ind_ass, 
        skip, go_to, if_LT, if_LE, if_EQ, if_NE, if_GE, if_GT, pmb, call, rtn, 
        param_s, param_c, in, out, point
    }

    public InstructionType instruction;
    public String left;
    public String right;
    public String destination;

    public Instruccion(InstructionType instruction, String left, String right, String destination){
        this.instruction = instruction;
        this.left = left;
        this.right = right;
        this.destination = destination;
    }

    public Instruccion(InstructionType instruction, String left, String destination){
        this.instruction = instruction;
        this.left = left;
        this.right = null;
        this.destination = destination;
    }

    public Instruccion(InstructionType instruction, String destination){
        this.instruction = instruction;
        this.left = null;
        this.right = null;
        this.destination = destination;
    }

    @Override
    public String toString(){
        return switch (instruction) {
            case copy -> "\t" + destination + " = " + left;
            case add -> "\t" + destination + " = " + left + " + " + right;
            case sub -> "\t" + destination + " = " + left + " - " + right;
            case prod -> "\t" + destination + " = " + left + " * " + right;
            case and -> "\t" + destination + " = " + left + " and " + right;
            case call -> "\t" + destination + " = call " + left;
            case div -> "\t" + destination + " = " + left + " / " + right;
            case go_to -> "\tgoto " + destination;
            case if_EQ -> "\tif " + left + "=" + right + " goto " + destination;
            case if_GE -> "\tif " + left + ">=" + right + " goto " + destination;
            case if_GT -> "\tif " + left + ">" + right + " goto " + destination;
            case if_LE -> "\tif " + left + "<=" + right + " goto " + destination;
            case if_LT -> "\tif " + left + "<" + right + " goto " + destination;
            case if_NE -> "\tif " + left + "!=" + right + " goto " + destination;
            case ind_ass -> "\t" + destination + "[" + left + "] = " + right;
            case ind_val -> "\t" + destination + " = " + left + "[" + right + "]";
            case mod -> "\t" + destination + " = " + left + " mod " + right;
            case neg -> "\t" + destination + " = -" + left;
            case not -> "\t" + destination + " = not " + left;
            case or -> "\t" + destination + " = " + left + " or " + right;
            case param_c -> "\tparam_c: " + destination + "[]";
            case param_s -> "\tparam_s: " + destination;
            case pmb -> "\tpmb " + destination;
            case rtn -> "\trtn " + destination + ": " + left;
            case skip -> destination + ": skip";
            case in -> "\tin: " + destination;
            case out -> "\tout: " + destination;
            case point -> "\t" + destination + " => " + left;
            default -> null;
        }; // + left + "]";
    }
}