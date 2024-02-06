package analizadorSemantico.genCodigoIntermedio;

import analizadorSintactico.ParserSym;

public enum Tipo {
    /**
     * Un string tiene una variable tipo string que contiene los datos, y puede o no tener
     * otras variables que apuntan a este string pero que son de tipo puntero
     */
    INT(Integer.BYTES), CHAR(1 /* elegido por 68K */), BOOL(1), DOUBLE(Double.BYTES), PUNTERO(4), ESTRUCTURA(-1), STRING(256);

    public static final int FALSE = 0, TRUE = -1;
    public final Integer bytes;
    
    public static Tipo getTipo(String tipo) {
        if (tipo.equals(ParserSym.terminalNames[ParserSym.ENT])) {
            return INT;
        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.CAR])) {
            return CHAR;
        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.PROP])) {
            return BOOL;
        } else if (tipo.equals(ParserSym.terminalNames[ParserSym.REAL])) {
            return DOUBLE;
        } else if(tipo.equals(ParserSym.terminalNames[ParserSym.CAR] + " []")) {
            return STRING;
        }
        return null;
//        throw new Exception("Se ha intentado conseguir los bytes de un tipo no primitivo");
    }
    
    public static Integer getBytes(String tipo) {
        if(tipo.equalsIgnoreCase(ParserSym.terminalNames[ParserSym.KW_VOID])) {
            return 0;
        }
        Tipo t = getTipo(tipo);
        return t == null ? null : t.bytes;
    }

    public static Integer getBytes(Object valor) {
        if (valor instanceof Integer) {
            return INT.bytes;
        } else if (valor instanceof Character) {
            return CHAR.bytes;
        } else if (valor instanceof Boolean) {
            return BOOL.bytes;
        } else if (valor instanceof Double) {
            return DOUBLE.bytes;
        }
        return null;
    }
    
    public String getExtension68K() {
        return switch (bytes) {
            case 1 -> ".B";
            case 2 -> ".W";
            case 4 -> ".L";
            case 256 -> ".L";
            case -1 -> ".B"; // estructura
            default -> null;
        };
    }
    
    public static String getExtension68K(Integer bytes) {
        return switch (bytes) {
            case 1 -> ".B";
            case 2 -> ".W";
            case 4 -> ".L";
            case 256 -> ".L";
            case -1 -> ".B"; // estructura
            default -> null;
        };
    }
    
    private Tipo(Integer t) {
        bytes = t;
    }

    @Override
    public String toString() {
        return switch (this) {
            case INT -> "INT";
            case CHAR -> "CHAR";
            case BOOL -> "BOOLEAN";
            case DOUBLE -> "REAL NUMBER";
            case ESTRUCTURA -> "COPY OF ARRAY OR TUPLE";
            case PUNTERO -> "POINTER TO ARRAY OR TUPLE";
            case STRING -> "STRING";
            default -> null;
        };
    }

    public static enum TipoReferencia {
        param, var
    }
}