package genCodigoMaquina;

public class Instruction {
    public enum InstructionType {
        copy, add, sub, prod, div, mod, neg, and, or, not, ind_val, ind_ass, 
        skip, go_to, if_LT, if_LE, if_EQ, if_NE, if_GE, if_GT, pmb, call, rtn, 
        param_s, param_c, in, out, point
    }

    public InstructionType instruction;
    public String left;
    public String right;
    public String destination;

    public Instruction(InstructionType instruction, String left, String right, String destination){
        this.instruction = instruction;
        this.left = left;
        this.right = right;
        this.destination = destination;
    }

    public Instruction(InstructionType instruction, String left, String destination){
        this.instruction = instruction;
        this.left = left;
        this.right = null;
        this.destination = destination;
    }

    public Instruction(InstructionType instruction, String destination){
        this.instruction = instruction;
        this.left = null;
        this.right = null;
        this.destination = destination;
    }

    @Override
    public String toString(){
        switch (instruction) {
            case copy:
                return "\t" + destination + " = " + left;
            case add:
                return "\t" + destination + " = " + left + " + " + right;
            case sub:
                return "\t" + destination + " = " + left + " - " + right;
            case prod:
                return "\t" + destination + " = " + left + " * " + right;
            case and:
                return "\t" + destination + " = " + left + " and " + right;
            case call:
                return "\t" + destination + " = call " + left;
            case div:
                return "\t" + destination + " = " + left + " / " + right;
            case go_to:
                return "\tgoto " + destination;
            case if_EQ:
                return "\tif " + left + "=" + right + " goto " + destination;
            case if_GE:
                return "\tif " + left + ">=" + right + " goto " + destination;
            case if_GT:
                return "\tif " + left + ">" + right + " goto " + destination;
            case if_LE:
                return "\tif " + left + "<=" + right + " goto " + destination;
            case if_LT:
                return "\tif " + left + "<" + right + " goto " + destination;
            case if_NE:
                return "\tif " + left + "!=" + right + " goto " + destination;
            case ind_ass:
                return "\t" + destination + "[" + left + "] = " + right;
            case ind_val:
                return "\t" + destination + " = " + left + "[" + right + "]";
            case mod:
                return "\t" + destination + " = " + left + " mod " + right;
            case neg:
                return "\t" + destination + " = -" + left; 
            case not:
                return "\t" + destination + " = not " + left;
            case or:
                return "\t" + destination + " = " + left + " or " + right;
            case param_c:
                return "\tparam_c: " + destination + "[]";// + left + "]";
            case param_s:
                return "\tparam_s: " + destination;
            case pmb:
                return "\tpmb " + destination;
            case rtn:
                return "\trtn " + destination + ": " + left;
            case skip:
                return destination + ": skip";
            case in:
                return "\tin: " + destination;
            case out:
                return "\tout: " + destination;
            case point:
                return "\t" + destination + " => " + left;
            default:
                return null;
        }
    }
}