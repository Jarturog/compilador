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

    @Override
    public String toString() {
        return "op1: " + this.op1 + " | " + this.op2 + " | " + this.dst;
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
        SWAP("swap"),
        COPY("copy"),
        INIT("init"),
        POT("pot"),
        PCT("pct"),
        IND_ASS("ind_ass"),
        CAST("cast"),
        IND_VAL("ind_val");

        public final String tipo;

        private TipoInstr(String t) {
            this.tipo = t;
        }

        @Override
        public String toString() {
            return this.tipo;
        }

    }
}
