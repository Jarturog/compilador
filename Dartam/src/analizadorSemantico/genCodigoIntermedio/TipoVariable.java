package analizadorSemantico.genCodigoIntermedio;

import analizadorSintactico.ParserSym;

public enum TipoVariable {
    /**
     * Un string tiene una variable tipo string que contiene los datos, y puede
     * o no tener otras variables que apuntan a este string pero que son de tipo
     * puntero
     */
    INT(4), CHAR(4), BOOL(4), DOUBLE(4), PUNTERO(4), STRING(256), ARRAY(-1), TUPLA(-1);
    //INT(Integer.BYTES), CHAR(1 /* elegido por 68K */), BOOL(1), DOUBLE(Double.BYTES), PUNTERO(4), ESTRUCTURA(-1), STRING(256);

    public static final int FALSE = 0, TRUE = -1;
    public final Integer bytes;

    public static boolean isTupla(String tipo) {
        return tipo.startsWith(ParserSym.terminalNames[ParserSym.TUPLE]);
    }

    public static TipoVariable getTipo(String tipo, boolean isPuntero) throws Exception {
        if (isPuntero) {
            return PUNTERO;
        }
        if (tipo.equals(ParserSym.terminalNames[ParserSym.ENT])) {
            return INT;
        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.CAR])) {
            return CHAR;
        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.PROP])) {
            return BOOL;
        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.REAL])) {
            return DOUBLE;
        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.CAR] + " []")) {
            return STRING;
        } else if (tipo.contains("[")) {
            return ARRAY;
        } else if (tipo.startsWith(ParserSym.terminalNames[ParserSym.TUPLE])) {
            return TUPLA;
        }
        throw new Exception("Se ha intentado conseguir un tipo inexistente");
    }

    public static Integer getBytes(String tipo, boolean isPuntero) throws Exception {
        if (tipo.equalsIgnoreCase(ParserSym.terminalNames[ParserSym.VOID])) {
            return 0;
        }
        TipoVariable t = getTipo(tipo, isPuntero);
        return t.bytes > 0 ? t.bytes : null;
    }

    public String getExtension68K() {
        return getExt(bytes);
    }

    public static String getExtension68K(Integer bytes) {
        return getExt(bytes);
    }

    private static String getExt(Integer bytes) {
        return switch (bytes) {
            case 1 ->
                ".B";
            case 2 ->
                ".W";
            case 4 ->
                ".L";
            case 256 ->
                ".L";
            case -1 ->
                ".B"; // estructura
            default ->
                null;
        };
    }

    private TipoVariable(Integer t) {
        bytes = t;
    }

    @Override
    public String toString() {
        return switch (this) {
            case INT ->
                "INT";
            case CHAR ->
                "CHAR";
            case BOOL ->
                "BOOLEAN";
            case DOUBLE ->
                "REAL NUMBER";
            case PUNTERO ->
                "POINTER TO ARRAY, TUPLE OR STRING";
            case STRING ->
                "STRING";
            case ARRAY ->
                "ARRAY";
            case TUPLA ->
                "TUPLE";
            default ->
                null;
        };
    }

    public TipoVariable getAssignedType() {
        return switch (this) {
            case INT, CHAR, BOOL, DOUBLE, PUNTERO ->
                this;
            case STRING, ARRAY, TUPLA ->
                PUNTERO;
            default ->
                null;
        };
    }
}
