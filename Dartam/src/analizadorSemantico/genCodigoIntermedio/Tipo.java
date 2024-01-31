package analizadorSemantico.genCodigoIntermedio;

import analizadorSintactico.ParserSym;

public enum Tipo {
    INT(4), CHAR(1), BOOL(1), STRING(128), DOUBLE(4); // no es 4 creo

    public static Tipo getTipo(String tipo) {
        if (tipo.equals(ParserSym.terminalNames[ParserSym.ENT])) {
            return INT;
        } else if(tipo.equals(ParserSym.terminalNames[ParserSym.CAR])) {
            return CHAR;
        } else if(tipo.equals(ParserSym.terminalNames[ParserSym.PROP])) {
            return BOOL;
        } else if(tipo.equals(ParserSym.terminalNames[ParserSym.REAL])) {
            return DOUBLE;
        } else if(tipo.equals(ParserSym.terminalNames[ParserSym.STRING])) {
            return STRING;
        }
        return null;
    }

    public final Integer bytes;

    private Tipo(Integer t) {
        bytes = t;
    }

    @Override
    public String toString() {
        return bytes.toString();
    }
    
}
