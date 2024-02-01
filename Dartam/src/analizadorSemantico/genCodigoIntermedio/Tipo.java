package analizadorSemantico.genCodigoIntermedio;

import analizadorSintactico.ParserSym;

public enum Tipo {
    INT(Integer.BYTES), CHAR(Character.BYTES), BOOL(1), DOUBLE(Double.BYTES), PUNTERO(4), STRING(PUNTERO.bytes); // puntero a un array (o string) o una tupla

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
        } else if(tipo.contains("[") || tipo.startsWith(ParserSym.terminalNames[ParserSym.KW_TUPLE])){
            return PUNTERO;
        }
        return null;
//        throw new Exception("Se ha intentado conseguir los bytes de un tipo no primitivo");
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
