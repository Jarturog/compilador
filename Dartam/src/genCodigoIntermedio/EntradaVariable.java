package genCodigoIntermedio;

import java.util.ArrayList;
import analizadorSintactico.ParserSym;
/**
 * Variable table entry
 */
public class EntradaVariable {
    public static final int TYPE_ARRAY = Integer.MAX_VALUE;
    public static final int TYPE_FUNCTION = TYPE_ARRAY-1;

    public static final int UNKNOWN = -1;

    public static final byte FALSE = 0;
    public static final byte TRUE = -1;

    public static final int INTEGER_BYTES = 4;
    public static final int CHAR_BYTES = 1;
    public static final int BOOL_BYTES = 1;
    public String tName;
    public int displacement;
    public boolean isConstant;
    public int type;
    public int subyacentType;
    public ArrayList<String> dimensions;
    public String initialValue = "0";
    public int operationType;
    
    public EntradaVariable(String tName){
        this.tName = tName;
        type = ParserSym.KW_INT;
        subyacentType = ParserSym.KW_INT;
        dimensions = new ArrayList<>();
    }

    public ArrayList<String> cloneDimensions(){
        ArrayList<String> i = new ArrayList<>();
        for (String d : dimensions) {
            i.add(d);
        }
        return i;
    }

    public int getOccupation(){
        int occupation;
        if(type == ParserSym.KW_INT){
            occupation = INTEGER_BYTES;
        }
        else occupation = CHAR_BYTES;
        for (String s : dimensions) {
            try {
                occupation *= Integer.parseInt(s);
            } catch (NumberFormatException e){
                occupation = UNKNOWN;
            }
        }
        return occupation;
    }

    @Override
    public String toString(){
        String s = "[var: " + tName + ", occup: " + getOccupation() + ", disp: " + displacement + ", type: " + ParserSym.terminalNames[subyacentType];
        for (String d : dimensions) {
            s += "[" + d + "]";
        }
        s += ", tsb: " + ParserSym.terminalNames[subyacentType] + "]";
        return s;
    }

}