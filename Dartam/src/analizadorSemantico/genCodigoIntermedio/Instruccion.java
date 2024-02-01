package analizadorSemantico.genCodigoIntermedio;

public class Instruccion {

    private final TipoInstr tipoInstruccion;
    public final Operador op1, op2, dst;

    public Instruccion(TipoInstr tipoInstruccion, Operador op1, Operador op2, Operador dst) {
        this.tipoInstruccion = tipoInstruccion;
        this.op1 = op1;
        this.op2 = op2;
        this.dst = dst;
    }

    //Devuelve todos los operadores de la instruccion
    public Operador[] operadores() {
        Operador[] ops = {this.op1, this.op2, this.dst};
        return ops;
    }

    //Tipo de instruccion
    public TipoInstr tipo() {
        return tipoInstruccion;
    }

    public boolean isTipo(TipoInstr t) {
        return tipoInstruccion.tipo.equals(t.tipo);
    }

//    @Override
//    public String toString() {
//        return tipoInstruccion+": " + this.op1 + " | " + this.op2 + " | " + this.dst;
//    }
    @Override
    public String toString() {
        switch (tipoInstruccion) {
            case NEG -> {
                return "-" + dst;
            }
            case ADD -> {
                return dst + " = " + op1 + " + " + op2;
            }
            case SUB -> {
                return dst + " = " + op1 + " - " + op2;
            }
            case MUL -> {
                return dst + " = " + op1 + " * " + op2;
            }
            case DIV -> {
                return dst + " = " + op1 + " / " + op2;
            }
            case MOD -> {
                return dst + " = " + op1 + " mod " + op2;
            }
            case AND -> {
                return dst + " = " + op1 + " and " + op2;
            }
            case OR -> {
                return dst + " = " + op1 + " or " + op2;
            }
            case NOT -> {
                return "not " + dst;
            }
            case SKIP -> {
                return dst + ": skip";
            }
            case GOTO -> {
                return "goto " + dst;
            }
            case IFLT -> {
                return "if " + op1 + " < " + op2 + " goto " + dst;
            }
            case IFLE -> {
                return "if " + op1 + " <= " + op2 + " goto " + dst;
            }
            case IFGT -> {
                return "if " + op1 + " > " + op2 + " goto " + dst;
            }
            case IFGE -> {
                return "if " + op1 + " >= " + op2 + " goto " + dst;
            }
            case IFEQ -> {
                return "if " + op1 + " == " + op2 + " goto " + dst;
            }
            case IFNE -> {
                return "if " + op1 + " /= " + op2 + " goto " + dst;
            }
            case CALL -> {
                return "call " + dst + (op1 == null ? "" : ", " + op1);
            }
            case RETURN -> {
                return "rtn " + dst + (op1 == null ? "" : ", " + op1);
            }
            case READ -> {
                return "read " + dst;
            }
            case WRITE -> {
                return "write " + dst;
            }
            case SCAN -> {
                return "scan " + dst;
            }
            case PRINT -> {
                return "print " + dst;
            }
            case COPY -> {
                return dst + " = " + op1;
            }
            case PMB -> {
                return "pmb " + dst;
            }
//            case POT -> {
//                return "pot " + op1 + ", " + op2;
//            }
//            case PCT -> {
//                return "pct " + op1 + ", " + op2;
//            }
            case IND_ASS -> {
                return dst + "[" + op2 + "] = " + op1;
            }
            case CAST -> {
                return "cast " + op1 + ", " + op2;
            }
            case IND_VAL -> {
                return dst + " = [" + op2 + "]" + op1;
            }
            case PARAM_S -> {
                return "param_s " + dst;
            }
            case PARAM_C -> {
                return "param_c " + dst + "[" + op1 + "]";
            }
            default -> {
                return null;
            }
        }
    }

    public static enum TipoInstr {
        NEG("neg"),
        ADD("add"),
        SUB("sub"),
        MUL("mul"),
        DIV("div"),
        MOD("mod"),
        AND("and"),
        OR("or"),
        NOT("not"),
        SKIP("skip"),
        GOTO("goto"),
        IFLT("iflt"),
        IFLE("ifle"),
        IFGT("ifgt"),
        IFGE("ifge"),
        IFEQ("ifeq"),
        IFNE("ifne"),
        CALL("call"),
        RETURN("return"),
        READ("read"),
        WRITE("write"),
        SCAN("scan"),
        PRINT("print"),
        COPY("copy"),
        PMB("init"),
        POT("pot"),
        PCT("pct"),
        IND_ASS("ind_ass"),
        CAST("cast"),
        IND_VAL("ind_val"),
        PARAM_S("param_s"),
        PARAM_C("param_c");

        public final String tipo;

        private TipoInstr(String t) {
            this.tipo = t;
        }

        @Override
        public String toString() {
            return this.tipo;
        }

        public boolean isTipo(TipoInstr t) {
            return tipo.equals(t.tipo);
        }

    }
}
