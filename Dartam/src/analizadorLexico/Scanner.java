// DO NOT EDIT
// Generated by JFlex 1.9.1 http://jflex.de/
// source: src/analizadorLexico/Scanner.flex

/**
 * Assignatura 21742 - Compiladors I 
 * Estudis: Grau en Informàtica 
 * Itinerari: Computació 
 * Curs: 2023-2024
 *
 * Equipo: Marta, Arturo y Dani
 */
package analizadorLexico;

import java.io.*;

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;

import analizadorSintactico.ParserSym;


@SuppressWarnings("fallthrough")
public class Scanner implements java_cup.runtime.Scanner {

  /** This character denotes the end of file. */
  public static final int YYEOF = -1;

  /** Initial size of the lookahead buffer. */
  private static final int ZZ_BUFFERSIZE = 16384;

  // Lexical states.
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = {
     0, 0
  };

  /**
   * Top-level table for translating characters to character classes
   */
  private static final int [] ZZ_CMAP_TOP = zzUnpackcmap_top();

  private static final String ZZ_CMAP_TOP_PACKED_0 =
    "\1\0\37\u0100\1\u0200\267\u0100\10\u0300\u1020\u0100";

  private static int [] zzUnpackcmap_top() {
    int [] result = new int[4352];
    int offset = 0;
    offset = zzUnpackcmap_top(ZZ_CMAP_TOP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackcmap_top(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Second-level tables for translating characters to character classes
   */
  private static final int [] ZZ_CMAP_BLOCKS = zzUnpackcmap_blocks();

  private static final String ZZ_CMAP_BLOCKS_PACKED_0 =
    "\11\0\1\1\1\2\2\3\1\2\22\0\1\1\1\0"+
    "\1\4\1\5\1\0\1\6\1\7\1\10\1\11\1\12"+
    "\1\13\1\14\1\15\1\16\1\17\1\20\1\21\1\22"+
    "\6\23\2\24\1\25\1\26\1\27\1\30\1\31\2\0"+
    "\4\32\1\33\1\32\24\34\1\35\1\36\1\37\1\0"+
    "\1\40\1\0\1\41\1\42\1\43\1\44\1\45\1\46"+
    "\1\47\1\50\1\51\2\34\1\52\1\53\1\54\1\55"+
    "\1\56\1\34\1\57\1\60\1\61\1\62\1\63\1\64"+
    "\1\65\2\34\1\66\1\67\1\70\7\0\1\3\46\0"+
    "\1\71\u017b\0\2\3\326\0\u0100\3";

  private static int [] zzUnpackcmap_blocks() {
    int [] result = new int[1024];
    int offset = 0;
    offset = zzUnpackcmap_blocks(ZZ_CMAP_BLOCKS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackcmap_blocks(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /**
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\1\4\1\1\1\5\1\6"+
    "\1\7\1\10\1\11\1\12\1\13\1\14\1\15\1\16"+
    "\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26"+
    "\1\27\1\30\1\31\1\32\12\27\1\33\1\34\1\35"+
    "\1\36\1\37\2\0\1\2\2\0\1\16\1\0\1\2"+
    "\1\40\3\0\1\41\1\42\1\43\2\27\1\44\4\27"+
    "\1\45\4\27\1\46\2\27\1\47\1\0\1\16\1\50"+
    "\1\51\1\52\1\53\1\27\1\54\4\27\1\55\14\27"+
    "\1\56\1\57\1\27\1\60\1\61\3\27\1\62\1\63"+
    "\1\27\1\64\2\27\1\65\1\27\1\66\1\67\1\70"+
    "\2\27\1\71";

  private static int [] zzUnpackAction() {
    int [] result = new int[121];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\72\0\164\0\256\0\350\0\u0122\0\72\0\72"+
    "\0\u015c\0\72\0\72\0\72\0\u0196\0\72\0\u0196\0\u01d0"+
    "\0\u020a\0\u0244\0\u027e\0\72\0\72\0\u02b8\0\u02f2\0\u032c"+
    "\0\u0366\0\72\0\72\0\72\0\u03a0\0\u03da\0\u0414\0\u044e"+
    "\0\u0488\0\u04c2\0\u04fc\0\u0536\0\u0570\0\u05aa\0\72\0\u0196"+
    "\0\72\0\72\0\72\0\350\0\u0122\0\u0122\0\u05e4\0\u061e"+
    "\0\u0658\0\u0692\0\u06cc\0\72\0\u0706\0\u0740\0\u077a\0\72"+
    "\0\72\0\72\0\u07b4\0\u07ee\0\u0366\0\u0828\0\u0862\0\u089c"+
    "\0\u08d6\0\u0366\0\u0910\0\u094a\0\u0984\0\u09be\0\u09f8\0\u0a32"+
    "\0\u0a6c\0\72\0\u0aa6\0\u0aa6\0\u0706\0\u0740\0\u077a\0\u0366"+
    "\0\u0ae0\0\u0b1a\0\u0b54\0\u0b8e\0\u0bc8\0\u0c02\0\u0366\0\u0c3c"+
    "\0\u0c76\0\u0cb0\0\u0cea\0\u0d24\0\u0d5e\0\u0d98\0\u0dd2\0\u0e0c"+
    "\0\u0e46\0\u0e80\0\u0eba\0\u0366\0\u0366\0\u0ef4\0\u0366\0\u0366"+
    "\0\u0f2e\0\u0f68\0\u0fa2\0\u0366\0\u0366\0\u0fdc\0\u0366\0\u1016"+
    "\0\u1050\0\u0366\0\u108a\0\u0366\0\u0366\0\u0366\0\u10c4\0\u10fe"+
    "\0\u0366";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[121];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length() - 1;
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /**
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpacktrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\2\1\5\1\6\1\7\1\10"+
    "\1\11\1\12\1\13\1\14\1\15\1\16\1\17\1\20"+
    "\1\21\1\22\3\23\1\24\1\25\1\26\1\27\1\30"+
    "\3\31\1\32\1\33\1\34\3\31\1\35\1\36\1\37"+
    "\1\40\2\31\1\41\1\42\1\31\1\43\1\31\1\44"+
    "\1\31\1\45\2\31\1\46\2\31\1\47\1\50\1\51"+
    "\1\52\73\0\1\3\72\0\1\4\73\0\1\53\14\0"+
    "\4\54\5\0\3\54\4\0\25\54\4\0\5\55\1\56"+
    "\64\55\21\0\4\57\5\0\3\57\4\0\25\57\25\0"+
    "\4\23\61\0\1\60\1\0\1\60\2\0\4\61\6\0"+
    "\1\62\11\0\1\62\21\0\1\60\22\0\1\63\7\0"+
    "\1\64\60\0\1\20\1\0\4\23\15\0\1\65\12\0"+
    "\1\66\7\0\1\67\23\0\1\20\1\0\4\23\75\0"+
    "\1\70\1\71\67\0\1\70\1\0\1\72\70\0\1\72"+
    "\62\0\4\31\5\0\3\31\3\0\26\31\25\0\4\31"+
    "\5\0\3\31\3\0\1\31\1\73\7\31\1\74\14\31"+
    "\25\0\4\31\5\0\3\31\3\0\15\31\1\75\10\31"+
    "\25\0\4\31\5\0\3\31\3\0\14\31\1\76\11\31"+
    "\25\0\4\31\5\0\3\31\3\0\1\31\1\77\24\31"+
    "\25\0\4\31\5\0\3\31\3\0\14\31\1\100\11\31"+
    "\25\0\4\31\5\0\3\31\3\0\15\31\1\101\10\31"+
    "\25\0\4\31\5\0\3\31\3\0\15\31\1\102\10\31"+
    "\25\0\4\31\5\0\3\31\3\0\15\31\1\103\1\31"+
    "\1\104\6\31\25\0\4\31\5\0\3\31\3\0\5\31"+
    "\1\105\2\31\1\106\1\107\7\31\1\110\4\31\25\0"+
    "\4\31\5\0\3\31\3\0\1\31\1\111\24\31\14\0"+
    "\1\112\102\0\4\61\66\0\4\61\6\0\1\62\11\0"+
    "\1\62\40\0\1\113\1\0\1\113\2\0\4\114\42\0"+
    "\1\113\2\0\2\63\2\0\66\63\21\0\2\115\70\0"+
    "\3\116\67\0\4\117\5\0\2\117\5\0\6\117\44\0"+
    "\4\31\5\0\3\31\3\0\17\31\1\120\6\31\25\0"+
    "\4\31\5\0\3\31\3\0\5\31\1\121\20\31\25\0"+
    "\4\31\5\0\3\31\3\0\21\31\1\122\4\31\25\0"+
    "\4\31\5\0\3\31\3\0\12\31\1\123\13\31\25\0"+
    "\4\31\5\0\3\31\3\0\11\31\1\124\1\31\1\125"+
    "\12\31\25\0\4\31\5\0\3\31\3\0\15\31\1\126"+
    "\10\31\25\0\4\31\5\0\3\31\3\0\16\31\1\127"+
    "\7\31\25\0\4\31\5\0\3\31\3\0\15\31\1\130"+
    "\10\31\25\0\4\31\5\0\3\31\3\0\12\31\1\131"+
    "\13\31\25\0\4\31\5\0\3\31\3\0\15\31\1\132"+
    "\10\31\25\0\4\31\5\0\3\31\3\0\14\31\1\133"+
    "\11\31\25\0\4\31\5\0\3\31\3\0\17\31\1\134"+
    "\6\31\25\0\4\31\5\0\3\31\3\0\3\31\1\135"+
    "\6\31\1\136\13\31\25\0\4\114\66\0\4\31\5\0"+
    "\3\31\3\0\17\31\1\137\6\31\25\0\4\31\5\0"+
    "\3\31\3\0\5\31\1\140\20\31\25\0\4\31\5\0"+
    "\3\31\3\0\20\31\1\141\5\31\25\0\4\31\5\0"+
    "\3\31\3\0\3\31\1\142\22\31\25\0\4\31\5\0"+
    "\3\31\3\0\22\31\1\143\3\31\25\0\4\31\5\0"+
    "\3\31\3\0\16\31\1\144\7\31\25\0\4\31\5\0"+
    "\3\31\3\0\16\31\1\145\7\31\25\0\4\31\5\0"+
    "\3\31\3\0\5\31\1\146\20\31\25\0\4\31\5\0"+
    "\3\31\3\0\24\31\1\147\1\31\25\0\4\31\5\0"+
    "\3\31\3\0\15\31\1\150\10\31\25\0\4\31\5\0"+
    "\3\31\3\0\11\31\1\151\14\31\25\0\4\31\5\0"+
    "\3\31\3\0\11\31\1\152\14\31\25\0\4\31\5\0"+
    "\3\31\3\0\1\153\25\31\25\0\4\31\5\0\3\31"+
    "\3\0\21\31\1\141\4\31\25\0\4\31\5\0\3\31"+
    "\3\0\17\31\1\154\6\31\25\0\4\31\5\0\3\31"+
    "\3\0\15\31\1\155\10\31\25\0\4\31\5\0\3\31"+
    "\3\0\11\31\1\156\14\31\25\0\4\31\5\0\3\31"+
    "\3\0\21\31\1\157\4\31\25\0\4\31\5\0\3\31"+
    "\3\0\3\31\1\160\22\31\25\0\4\31\5\0\3\31"+
    "\3\0\14\31\1\161\11\31\25\0\4\31\5\0\3\31"+
    "\3\0\15\31\1\162\10\31\25\0\4\31\5\0\3\31"+
    "\3\0\17\31\1\163\6\31\25\0\4\31\5\0\3\31"+
    "\3\0\15\31\1\164\10\31\25\0\4\31\5\0\3\31"+
    "\3\0\21\31\1\165\4\31\25\0\4\31\5\0\3\31"+
    "\3\0\7\31\1\166\16\31\25\0\4\31\5\0\3\31"+
    "\3\0\5\31\1\167\20\31\25\0\4\31\5\0\3\31"+
    "\3\0\1\31\1\170\24\31\25\0\4\31\5\0\3\31"+
    "\3\0\12\31\1\171\13\31\4\0";

  private static int [] zzUnpacktrans() {
    int [] result = new int[4408];
    int offset = 0;
    offset = zzUnpacktrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpacktrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** Error code for "Unknown internal scanner error". */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  /** Error code for "could not match input". */
  private static final int ZZ_NO_MATCH = 1;
  /** Error code for "pushback value was too large". */
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /**
   * Error messages for {@link #ZZ_UNKNOWN_ERROR}, {@link #ZZ_NO_MATCH}, and
   * {@link #ZZ_PUSHBACK_2BIG} respectively.
   */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state {@code aState}
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\4\1\2\11\1\1\3\11\1\1\1\11"+
    "\5\1\2\11\4\1\3\11\12\1\1\11\1\1\3\11"+
    "\2\0\1\1\2\0\1\1\1\0\1\1\1\11\3\0"+
    "\3\11\17\1\1\11\1\0\56\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[121];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** Input device. */
  private java.io.Reader zzReader;

  /** Current state of the DFA. */
  private int zzState;

  /** Current lexical state. */
  private int zzLexicalState = YYINITIAL;

  /**
   * This buffer contains the current text to be matched and is the source of the {@link #yytext()}
   * string.
   */
  private char zzBuffer[] = new char[Math.min(ZZ_BUFFERSIZE, zzMaxBufferLen())];

  /** Text position at the last accepting state. */
  private int zzMarkedPos;

  /** Current text position in the buffer. */
  private int zzCurrentPos;

  /** Marks the beginning of the {@link #yytext()} string in the buffer. */
  private int zzStartRead;

  /** Marks the last character in the buffer, that has been read from input. */
  private int zzEndRead;

  /**
   * Whether the scanner is at the end of file.
   * @see #yyatEOF
   */
  private boolean zzAtEOF;

  /**
   * The number of occupied positions in {@link #zzBuffer} beyond {@link #zzEndRead}.
   *
   * <p>When a lead/high surrogate has been read from the input stream into the final
   * {@link #zzBuffer} position, this will have a value of 1; otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /** Number of newlines encountered up to the start of the matched text. */
  private int yyline;

  /** Number of characters from the last newline up to the start of the matched text. */
  private int yycolumn;

  /** Number of characters up to the start of the matched text. */
  private long yychar;

  /** Whether the scanner is currently at the beginning of a line. */
  @SuppressWarnings("unused")
  private boolean zzAtBOL = true;

  /** Whether the user-EOF-code has already been executed. */
  private boolean zzEOFDone;

  /* user code: */
/***
  Mecanismes de gestió de símbols basat en ComplexSymbol. Tot i que en
  aquest cas potser no és del tot necessari.
***/
private String tokens = "", errores = "";

public String getTokens(){
  return tokens;
}

public String getErrores(){
  return errores;
}

private String errorToString(){
  return "!!! Error léxico: Token " + yytext() + " no reconocido en la posición [línea: " + (yyline+1) + ", columna: " + (yycolumn+1) + "]\n";
}

/**
  Construcció d'un symbol sense atribut associat.
**/
private ComplexSymbol symbol(int type) {
  // Sumar 1 per a que la primera línia i columna no sigui 0.
  Location esquerra = new Location(yyline+1, yycolumn+1);
  Location dreta = new Location(yyline+1, yycolumn+yytext().length()+1); // , yycolumn+yylength()

  return new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta);
}

/**
  Construcció d'un symbol amb un atribut associat.
**/
private Symbol symbol(int type, Object value) {
  // Sumar 1 per a que la primera línia i columna no sigui 0.
  Location esquerra = new Location(yyline+1, yycolumn+1);
  Location dreta = new Location(yyline+1, yycolumn+yytext().length()+1); // , yycolumn+yylength()

  return new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta, value);
}


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Scanner(java.io.Reader in) {
    this.zzReader = in;
  }


  /** Returns the maximum size of the scanner buffer, which limits the size of tokens. */
  private int zzMaxBufferLen() {
    return Integer.MAX_VALUE;
  }

  /**  Whether the scanner buffer can grow to accommodate a larger token. */
  private boolean zzCanGrow() {
    return true;
  }

  /**
   * Translates raw input code points to DFA table row
   */
  private static int zzCMap(int input) {
    int offset = input & 255;
    return offset == input ? ZZ_CMAP_BLOCKS[offset] : ZZ_CMAP_BLOCKS[ZZ_CMAP_TOP[input >> 8] | offset];
  }

  /**
   * Refills the input buffer.
   *
   * @return {@code false} iff there was new input.
   * @exception java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead - zzStartRead);

      /* translate stored positions */
      zzEndRead -= zzStartRead;
      zzCurrentPos -= zzStartRead;
      zzMarkedPos -= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate && zzCanGrow()) {
      /* if not, and it can grow: blow it up */
      char newBuffer[] = new char[Math.min(zzBuffer.length * 2, zzMaxBufferLen())];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      if (requested == 0) {
        throw new java.io.EOFException("Scan buffer limit reached ["+zzBuffer.length+"]");
      }
      else {
        throw new java.io.IOException(
            "Reader returned 0 characters. See JFlex examples/zero-reader for a workaround.");
      }
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
        if (numRead == requested) { // We requested too few chars to encode a full Unicode character
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        } else {                    // There is room in the buffer for at least one more char
          int c = zzReader.read();  // Expecting to read a paired low surrogate char
          if (c == -1) {
            return true;
          } else {
            zzBuffer[zzEndRead++] = (char)c;
          }
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }


  /**
   * Closes the input reader.
   *
   * @throws java.io.IOException if the reader could not be closed.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true; // indicate end of file
    zzEndRead = zzStartRead; // invalidate buffer

    if (zzReader != null) {
      zzReader.close();
    }
  }


  /**
   * Resets the scanner to read from a new input stream.
   *
   * <p>Does not close the old reader.
   *
   * <p>All internal variables are reset, the old input stream <b>cannot</b> be reused (internal
   * buffer is discarded and lost). Lexical state is set to {@code ZZ_INITIAL}.
   *
   * <p>Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader The new input stream.
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzEOFDone = false;
    yyResetPosition();
    zzLexicalState = YYINITIAL;
    int initBufferSize = Math.min(ZZ_BUFFERSIZE, zzMaxBufferLen());
    if (zzBuffer.length > initBufferSize) {
      zzBuffer = new char[initBufferSize];
    }
  }

  /**
   * Resets the input position.
   */
  private final void yyResetPosition() {
      zzAtBOL  = true;
      zzAtEOF  = false;
      zzCurrentPos = 0;
      zzMarkedPos = 0;
      zzStartRead = 0;
      zzEndRead = 0;
      zzFinalHighSurrogate = 0;
      yyline = 0;
      yycolumn = 0;
      yychar = 0L;
  }


  /**
   * Returns whether the scanner has reached the end of the reader it reads from.
   *
   * @return whether the scanner has reached EOF.
   */
  public final boolean yyatEOF() {
    return zzAtEOF;
  }


  /**
   * Returns the current lexical state.
   *
   * @return the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state.
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   *
   * @return the matched text.
   */
  public final String yytext() {
    return new String(zzBuffer, zzStartRead, zzMarkedPos-zzStartRead);
  }


  /**
   * Returns the character at the given position from the matched text.
   *
   * <p>It is equivalent to {@code yytext().charAt(pos)}, but faster.
   *
   * @param position the position of the character to fetch. A value from 0 to {@code yylength()-1}.
   *
   * @return the character at {@code position}.
   */
  public final char yycharat(int position) {
    return zzBuffer[zzStartRead + position];
  }


  /**
   * How many characters were matched.
   *
   * @return the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * <p>In a well-formed scanner (no or only correct usage of {@code yypushback(int)} and a
   * match-all fallback rule) this method will only be called with things that
   * "Can't Possibly Happen".
   *
   * <p>If this method is called, something is seriously wrong (e.g. a JFlex bug producing a faulty
   * scanner etc.).
   *
   * <p>Usual syntax/scanner level error handling should be done in error fallback rules.
   *
   * @param errorCode the code of the error message to display.
   */
  private static void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    } catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * <p>They will be read again by then next call of the scanning method.
   *
   * @param number the number of characters to be read again. This number must not be greater than
   *     {@link #yylength()}.
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
  yyclose();    }
  }




  /**
   * Resumes scanning until the next regular expression is matched, the end of input is encountered
   * or an I/O-Error occurs.
   *
   * @return the next token.
   * @exception java.io.IOException if any I/O-Error occurs.
   */
  @Override  public java_cup.runtime.Symbol next_token() throws java.io.IOException
  {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char[] zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      yychar+= zzMarkedPosL-zzStartRead;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':  // fall through
        case '\u000C':  // fall through
        case '\u0085':  // fall through
        case '\u2028':  // fall through
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is
        // (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof)
            zzPeek = false;
          else
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMap(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
            zzDoEOF();
          {   return symbol(ParserSym.EOF);
 }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1:
            { errores += errorToString(); System.err.println(errorToString()); return symbol(ParserSym.error);
            }
          // fall through
          case 58: break;
          case 2:
            { /* No fer res amb els espais */
            }
          // fall through
          case 59: break;
          case 3:
            { tokens += "FIN_LINEA: \n"; return symbol(ParserSym.ENDLINE);
            }
          // fall through
          case 60: break;
          case 4:
            { tokens += "SYM_DQUOTE: "+yytext()+"\n"; return symbol(ParserSym.DQUOTE);
            }
          // fall through
          case 61: break;
          case 5:
            { tokens += "OP_PORCENT: "+yytext()+"\n"; return symbol(ParserSym.PCT);
            }
          // fall through
          case 62: break;
          case 6:
            { tokens += "OP_AND: "+yytext()+"\n"; return symbol(ParserSym.AND);
            }
          // fall through
          case 63: break;
          case 7:
            { tokens += "SYM_SQUOTE: "+yytext()+"\n"; return symbol(ParserSym.SQUOTE);
            }
          // fall through
          case 64: break;
          case 8:
            { tokens += "SYM_LPAREN: "+yytext()+"\n"; return symbol(ParserSym.LPAREN);
            }
          // fall through
          case 65: break;
          case 9:
            { tokens += "SYM_RPAREN: "+yytext()+"\n"; return symbol(ParserSym.RPAREN);
            }
          // fall through
          case 66: break;
          case 10:
            { tokens += "OP_MUL: "+yytext()+"\n"; return symbol(ParserSym.MUL);
            }
          // fall through
          case 67: break;
          case 11:
            { tokens += "OP_SUM: "+yytext()+"\n"; return symbol(ParserSym.ADD);
            }
          // fall through
          case 68: break;
          case 12:
            { tokens += "SYM_COMMA: "+yytext()+"\n"; return symbol(ParserSym.COMMA);
            }
          // fall through
          case 69: break;
          case 13:
            { tokens += "OP_RES: "+yytext()+"\n"; return symbol(ParserSym.SUB);
            }
          // fall through
          case 70: break;
          case 14:
            { tokens += "VAL_REAL: "+yytext()+"\n"; return symbol(ParserSym.valor, Double.parseDouble(yytext()));
            }
          // fall through
          case 71: break;
          case 15:
            { tokens += "OP_DIV: "+yytext()+"\n"; return symbol(ParserSym.DIV);
            }
          // fall through
          case 72: break;
          case 16:
            { return symbol(ParserSym.valor, 0.0);
            }
          // fall through
          case 73: break;
          case 17:
            { tokens += "VAL_DECIMAL: "+yytext()+"\n"; return symbol(ParserSym.valor, Integer.parseInt(yytext()));
            }
          // fall through
          case 74: break;
          case 18:
            { tokens += "OP_ASIG: "+yytext()+"\n"; return symbol(ParserSym.ASSIGN);
            }
          // fall through
          case 75: break;
          case 19:
            { tokens += "SYM_ENDINSTR: "+yytext()+"\n"; return symbol(ParserSym.ENDINSTR);
            }
          // fall through
          case 76: break;
          case 20:
            { tokens += "OP_MENOR: "+yytext()+"\n"; return symbol(ParserSym.LT);
            }
          // fall through
          case 77: break;
          case 21:
            { tokens += "OP_EQ: "+yytext()+"\n"; return symbol(ParserSym.EQ);
            }
          // fall through
          case 78: break;
          case 22:
            { tokens += "OP_MAYOR: "+yytext()+"\n"; return symbol(ParserSym.BT);
            }
          // fall through
          case 79: break;
          case 23:
            { tokens += "ID: "+yytext()+"\n"; return symbol(ParserSym.ID, yytext());
            }
          // fall through
          case 80: break;
          case 24:
            { tokens += "SYM_LBRACKET: "+yytext()+"\n"; return symbol(ParserSym.LBRACKET);
            }
          // fall through
          case 81: break;
          case 25:
            { tokens += "OP_MOD: "+yytext()+"\n"; return symbol(ParserSym.MOD);
            }
          // fall through
          case 82: break;
          case 26:
            { tokens += "SYM_RBRACKET: "+yytext()+"\n"; return symbol(ParserSym.RBRACKET);
            }
          // fall through
          case 83: break;
          case 27:
            { tokens += "SYM_LKEY: "+yytext()+"\n"; return symbol(ParserSym.LKEY);
            }
          // fall through
          case 84: break;
          case 28:
            { tokens += "OP_OR: "+yytext()+"\n"; return symbol(ParserSym.OR);
            }
          // fall through
          case 85: break;
          case 29:
            { tokens += "SYM_RKEY: "+yytext()+"\n"; return symbol(ParserSym.RKEY);
            }
          // fall through
          case 86: break;
          case 30:
            { tokens += "OP_NEG: "+yytext()+"\n"; return symbol(ParserSym.NOT);
            }
          // fall through
          case 87: break;
          case 31:
            { tokens += "VAL_CADENA: "+yytext()+"\n"; return symbol(ParserSym.STRING, yytext());
            }
          // fall through
          case 88: break;
          case 32:
            { tokens += "OP_DIFERENT: "+yytext()+"\n"; return symbol(ParserSym.NEQ);
            }
          // fall through
          case 89: break;
          case 33:
            { tokens += "OP_MENOREQ: "+yytext()+"\n"; return symbol(ParserSym.LEQ);
            }
          // fall through
          case 90: break;
          case 34:
            { tokens += "OP_SWAP: "+yytext()+"\n"; return symbol(ParserSym.SWAP);
            }
          // fall through
          case 91: break;
          case 35:
            { tokens += "OP_MAYOREQ: "+yytext()+"\n"; return symbol(ParserSym.BEQ);
            }
          // fall through
          case 92: break;
          case 36:
            { tokens += "KW_DO: "+yytext()+"\n"; return symbol(ParserSym.DO);
            }
          // fall through
          case 93: break;
          case 37:
            { tokens += "KW_ELSE: "+yytext()+"\n"; return symbol(ParserSym.ELSE);
            }
          // fall through
          case 94: break;
          case 38:
            { tokens += "KW_IF: "+yytext()+"\n"; return symbol(ParserSym.IF);
            }
          // fall through
          case 95: break;
          case 39:
            { tokens += "VAL_CHAR: "+yytext()+"\n"; return symbol(ParserSym.CHAR, yytext().charAt(0));
            }
          // fall through
          case 96: break;
          case 40:
            { tokens += "VAL_BINARIO: "+yytext()+"\n"; return symbol(ParserSym.valor, Integer.parseInt(yytext().substring(2, yytext().length()),2));
            }
          // fall through
          case 97: break;
          case 41:
            { tokens += "VAL_OCTAL: "+yytext()+"\n"; return symbol(ParserSym.valor, Integer.parseInt(yytext().substring(2, yytext().length()),8));
            }
          // fall through
          case 98: break;
          case 42:
            { tokens += "VAL_HEX: "+yytext()+"\n"; return symbol(ParserSym.valor, Integer.parseInt(yytext().substring(2, yytext().length()),16));
            }
          // fall through
          case 99: break;
          case 43:
            { tokens += "TYPE_CHAR: "+yytext()+"\n"; return symbol(ParserSym.CHAR);
            }
          // fall through
          case 100: break;
          case 44:
            { tokens += "TYPE_INT: "+yytext()+"\n"; return symbol(ParserSym.INT);
            }
          // fall through
          case 101: break;
          case 45:
            { tokens += "KW_RETURN: "+yytext()+"\n"; return symbol(ParserSym.RETURN);
            }
          // fall through
          case 102: break;
          case 46:
            { tokens += "KW_WHILE: "+yytext()+"\n"; return symbol(ParserSym.LOOP);
            }
          // fall through
          case 103: break;
          case 47:
            { tokens += "TYPE_BOOL: "+yytext()+"\n"; return symbol(ParserSym.BOOL);
            }
          // fall through
          case 104: break;
          case 48:
            { tokens += "KW_OUT: "+yytext()+"\n"; return symbol(ParserSym.OUT);
            }
          // fall through
          case 105: break;
          case 49:
            { tokens += "KW_ELIF: "+yytext()+"\n"; return symbol(ParserSym.ELIF);
            }
          // fall through
          case 106: break;
          case 50:
            { tokens += "KW_IN: "+yytext()+"\n"; return symbol(ParserSym.IN);
            }
          // fall through
          case 107: break;
          case 51:
            { tokens += "VAL_PROP: "+yytext()+"\n"; return symbol(ParserSym.BOOLEAN, yytext() == "cierto");
            }
          // fall through
          case 108: break;
          case 52:
            { tokens += "KW_CONST: "+yytext()+"\n"; return symbol(ParserSym.CONST);
            }
          // fall through
          case 109: break;
          case 53:
            { tokens += "TYPE_VOID: "+yytext()+"\n"; return symbol(ParserSym.VOID);
            }
          // fall through
          case 110: break;
          case 54:
            { tokens += "KW_MAIN: "+yytext()+"\n"; return symbol(ParserSym.MAIN);
            }
          // fall through
          case 111: break;
          case 55:
            { tokens += "KW_SWITCH: "+yytext()+"\n"; return symbol(ParserSym.SWITCH);
            }
          // fall through
          case 112: break;
          case 56:
            { tokens += "TYPE_STRING: "+yytext()+"\n"; return symbol(ParserSym.STRING);
            }
          // fall through
          case 113: break;
          case 57:
            { tokens += "TYPE_DOUBLE: "+yytext()+"\n"; return symbol(ParserSym.DOUBLE);
            }
          // fall through
          case 114: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
