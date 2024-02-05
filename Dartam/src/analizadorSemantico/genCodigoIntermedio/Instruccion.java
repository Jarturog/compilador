package analizadorSemantico.genCodigoIntermedio;

public class Instruccion {

    private TipoInstr tipoInstruccion;
    private Operador op1, op2, dst;

    public Instruccion(TipoInstr tipoInstruccion, Operador op1, Operador op2, Operador dst) {
        this.tipoInstruccion = tipoInstruccion;
        this.op1 = op1;
        this.op2 = op2;
        this.dst = dst;
    }
    
    public Operador op1(){
        return op1;
    }
    
    public Operador op2(){
        return op2;
    }
    
    public Operador dst(){
        return dst;
    }
    
    public void setOp1(Operador o) {
        op1 = o;
    }
    
    public void setOp2(Operador o) {
        op2 = o;
    }
    
    public void setDst(Operador o) {
        dst = o;
    }

    //Tipo de instruccion
    public TipoInstr getTipo() {
        return tipoInstruccion;
    }
    
    public void setTipo(TipoInstr t) {
        this.tipoInstruccion = t;
    }

    public boolean isTipo(TipoInstr t) {
        return tipoInstruccion.tipo.equals(t.tipo);
    }

    public String getExtensiones68K() {
        String s1 = " ", s2 = " ", s3 = " ";
        if (op1.tipo() != null) {
            s1 = op1.tipo().getExtension68K().substring(1);
        }
        if (op2.tipo() != null) {
            s2 = op1.tipo().getExtension68K().substring(1);
        }
        if (dst.tipo() != null) {
            s3 = op1.tipo().getExtension68K().substring(1);
        }
        return s1 + s2 + s3;
    }

    @Override
    public String toString() {
        switch (tipoInstruccion) {
            case NEG -> {
                return dst + " = -" + op1;
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
                return dst + " = not " + op1;
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
            case COPY -> {
                return dst + " = " + op1;
            }
            case PMB -> {
                return "pmb " + dst;
            }
            case IND_ASS -> {
                return dst + "[" + op2 + "] = " + op1;
            }
            case IND_VAL -> {
                return dst + " = " + op1 + "[" + op2 + "]";
            }
            case PARAM_S -> {
                return "param_s " + dst;
            }
            case CONCAT -> {
                return dst + " = " + (op1.isLiteral() ? "\"" + op1 + "\"" : op1) + " concat " + (op2.isLiteral() ? "\"" + op2 + "\"" : op2);
            }
            case SEPARADOR -> {
                return ""; // para legibilidad separamos los métodos con una instrucción que no hace nada
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
        CONCAT("concat"),
        COPY("copy"),
        PMB("init"),
        IND_ASS("ind_ass"),
        IND_VAL("ind_val"),
        PARAM_S("param_s"),
        SEPARADOR(""); // para legibilidad separamos los métodos con una instrucción que no hace nada

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

        public boolean isCondGOTO() {
            return isTipo(IFLT) || isTipo(IFLE) || isTipo(IFGT)
                    || isTipo(IFGE) || isTipo(IFEQ) || isTipo(IFNE);
        }
        
        public TipoInstr getContrario() throws Exception{
            if(!isCondGOTO()){
                throw new Exception("Solo se pueden pasar condicionales a este método");
            }
            return switch (this) {
                case IFEQ -> TipoInstr.IFNE;
                case IFNE -> TipoInstr.IFEQ;
                case IFGT -> TipoInstr.IFLE;
                case IFGE -> TipoInstr.IFLT;
                case IFLT -> TipoInstr.IFGE;
                case IFLE -> TipoInstr.IFGT;
                default -> null;
            };
        }

        public boolean tieneEtiqueta() {
            return isCondGOTO() || isTipo(GOTO) || isTipo(SKIP)
                    || isTipo(CALL) || isTipo(RETURN) || isTipo(PMB);
        }

    }
}
