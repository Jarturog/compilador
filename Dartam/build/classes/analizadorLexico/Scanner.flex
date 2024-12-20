/**
  * Assignatura 21742 - Compiladors I 
  * Estudis: Grau en Enginyeria Informàtica 
  * Itinerari: Computacio 
  * Curs: 2023-2024
  *
  * Equipo: Arturo, Dani y Marta
*/
package analizadorLexico;

import java.io.*;

import java_cup.runtime.*;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;

import analizadorSintactico.ParserSym;

%%
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica 
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
/****
  %standalone
****/

/****
  Indicacio de quin tipus d'analitzador sintàctic s'utilitzarà. En aquest cas 
  es fa us de Java CUP.
****/
%cup
/****
  La linia anterior es una alternativa a la indicacio element a element:

  %implements java_cup.runtime.Scanner
  %function next_token
  %type java_cup.runtime.Symbol
****/

%public              // Per indicar que la classe es publica
%class Scanner       // El nom de la classe

%char
%line
%column

%eofval{
  return symbol(ParserSym.EOF);
%eofval}

// Declaracions
// sub quiere decir que es un lexema que forma parte de otro
// val que comprende valores
// op que es una operacion
// sym que es un simbolo
// kw que es una palabra reservada (keyword)

// reutilizables
sub_digit   = [0-9]
sub_letra   = [A-Za-z] // no confundir con caracter
sub_car     = {sub_digit}|{sub_letra}

// simbolos
sym_parenIzq	= \(
sym_parenDer	= \)
sym_llaveIzq	= \{
sym_llaveDer 	= \}
sym_bracketIzq	= \[
sym_bracketDer	= \]
sym_endInstr    = \;
sym_coma	   = \,
sym_comillaS = \'
sym_comillaD = \"
sym_punto    = \.

// operadores
op_eq       = \= 
op_diferent = \/\= 
op_mayor    = \>
op_menor    = \<
op_mayorEq  = (\>\=)|(\=\>)
op_menorEq  = (\<\=)|(\=\<)
op_inc      = \+\+
op_dec      = \-\-
op_sum      = \+
op_res      = \-
op_mul      = \*
op_div      = \/
op_pot      = \*\*
op_porcent  = \%
op_swap     = \<\>
op_or       = \|
op_and      = &
op_mod      = \\
op_neg      = \¬
op_asig     = \:
op_and_asig = \&\:
op_or_asig  = \|\:
op_sum_asig = \+\:
op_res_asig = \-\:
op_mul_asig = \*\:
op_div_asig = \/\:
op_pot_asig = \*\*\:
op_mod_asig = \\\:
op_cond     = \?
op_arrow    = \-\>

// tipos (void es tipo de retorno pero no de variable)
type_char      = "car"
type_int       = "ent"
//type_double    = "real"
type_bool      = "prop"
type_void      = "vacio"

// palabras reservadas (const esta entre type y kw)
kw_const     = "inmut"  // inmutable
kw_main      = "inicio"
//kw_args      = "argumentos"
kw_method    = "f"
kw_if        = "si"
kw_elif      = "sino"
kw_else      = "no"
kw_switch    = "select"
kw_case      = "caso"
kw_default   = "_"
kw_while     = "loop"
kw_continue  = "continuar"
kw_break     = "parar"
kw_return    = "pop" 
kw_true      = "cierto"
kw_false     = "falso"
kw_in        = "scan"
kw_out       = "show"
kw_read      = "from"
kw_write     = "into"
kw_tuple     = "tupla"

// valores
val_decimal = {sub_digit}+
val_binario = 0b[01]+
val_octal   = 0o[0-7]+
val_hex     = 0x[A-Fa-f0-9]+
//val_real    = {val_decimal}?\.{val_decimal}?([Ee]{val_decimal})?
val_prop    = {kw_true}|{kw_false}
val_char    = "'"[^"'"]"'"
char_vacio  = "''"
char_string = "'"[^"'"][^"'"]+"'" // se inicializa un char con varios chars como si fuera un string
val_cadena  = "\""[^"\""]*"\""
id          = ({sub_letra}|_)({sub_car}|_)*

// casos especiales
espacioBlanco = [ \t]+
finLinea = [\r\n]+
comentario = ("\/\/".*)|("#"[^"#"]*"#")

// El següent codi es copiarà tambe, dins de la classe. Es a dir, si es posa res
// ha de ser en el format adient: mètodes, atributs, etc. 
%{
/***
  Mecanismes de gestio de simbols basat en ComplexSymbol. Tot i que en
  aquest cas potser no es del tot necessari.
***/
private String tokens = "", errores = "";

public String getTokens(){
  return tokens;
}

public String getErrores(){
  return errores;
}

private String errorToString(){
  return "Error lexico: Token '" + yytext() + "' no reconocido en la posicion [linea: " + (yyline+1) + ", columna: " + (yycolumn+1) + "]\n";
}

/**
  Construccio d'un symbol sense atribut associat.
**/
private ComplexSymbol symbol(int type) {
  // Sumar 1 per a que la primera linia i columna no sigui 0.
  Location esquerra = new Location(yyline+1, yycolumn+1);
  Location dreta = new Location(yyline+1, yycolumn+yytext().length()+1); // , yycolumn+yylength()

  return new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta);
}

/**
  Construccio d'un symbol amb un atribut associat.
**/
private Symbol symbol(int type, Object value) {
  // Sumar 1 per a que la primera linia i columna no sigui 0.
  Location esquerra = new Location(yyline+1, yycolumn+1);
  Location dreta = new Location(yyline+1, yycolumn+yytext().length()+1); // , yycolumn+yylength()

  return new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta, value);
}

private Symbol procesarNumero() {
  Integer numero;
  try {
    if (yytext().length()>1) {
      int base;
      switch (yytext().charAt(1)) {
          case 'b' -> { base = 2; }
          case 'o' -> { base = 8; }
          case 'x' -> { base = 16; }
          default -> { base = 10; }
      }
      if (base == 10) {
        numero = Integer.parseInt(yytext(), base);
      } else {
        numero = Integer.parseInt(yytext().substring(2), base);
      }
    } else {
      numero = Integer.parseInt(yytext());
    }
    return symbol(ParserSym.ENT, numero);
  } catch (Exception e) { /* Error inesperado */
    errores += errorToString();
    errores += "No se permiten numeros fuera del rango " + Integer.MIN_VALUE + "..." +Integer.MAX_VALUE+"\n";
    return symbol(ParserSym.error);
  }
}
%}

/****************************************************************************/
%%

// Regles/accions
// Es molt important l'ordre de les regles!!!

// valores chars y strings
{val_char}                  { tokens += "VAL_CHAR: "+yytext()+"\n"; return symbol(ParserSym.CAR, yytext().charAt(1)); } // 1 porque 0 es la comilla simple '
{val_cadena}                { tokens += "VAL_CADENA: "+yytext()+"\n"; String s = yytext(); return symbol(ParserSym.STRING, s.substring(1, s.length() - 1)); }

// operadores
{op_and_asig}               { tokens += "OP_AND_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_ANDA, yytext()); }
{op_or_asig}                { tokens += "OP_OR_ASSIGNMENT: " +yytext()+"\n"; return symbol(ParserSym.AS_ORA, yytext()); }
{op_sum_asig}               { tokens += "OP_SUM_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_ADDA, yytext()); }
{op_res_asig}               { tokens += "OP_RES_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_SUBA, yytext()); }
{op_mul_asig}               { tokens += "OP_MUL_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_MULA, yytext()); }
{op_div_asig}               { tokens += "OP_DIV_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_DIVA, yytext()); }
{op_pot_asig}               { tokens += "OP_POT_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_POTA, yytext()); }
{op_mod_asig}               { tokens += "OP_MOD_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_MODA, yytext()); }
{op_inc}                    { tokens += "OP_INC: "+yytext()+"\n"; return symbol(ParserSym.OP_INC, yytext()); }
{op_dec}                    { tokens += "OP_DEC: "+yytext()+"\n"; return symbol(ParserSym.OP_DEC, yytext()); }
{op_sum}                    { tokens += "OP_SUM: "+yytext()+"\n"; return symbol(ParserSym.OP_ADD, yytext()); }
{op_res}                    { tokens += "OP_RES: "+yytext()+"\n"; return symbol(ParserSym.OP_SUB, yytext()); }
{op_mul}                    { tokens += "OP_MUL: "+yytext()+"\n"; return symbol(ParserSym.OP_MUL, yytext()); }
{op_div}                    { tokens += "OP_DIV: "+yytext()+"\n"; return symbol(ParserSym.OP_DIV, yytext()); }
{op_mod}                    { tokens += "OP_MOD: "+yytext()+"\n"; return symbol(ParserSym.OP_MOD, yytext()); }
{op_eq}                     { tokens += "OP_EQ: " +yytext()+"\n"; return symbol(ParserSym.OP_EQ, yytext()); }
{op_mayorEq}                { tokens += "OP_MAYOREQ: " +yytext()+"\n"; return symbol(ParserSym.OP_BEQ, yytext()); }
{op_mayor}                  { tokens += "OP_MAYOR: "   +yytext()+"\n"; return symbol(ParserSym.OP_BT, yytext()); }
{op_menorEq}                { tokens += "OP_MENOREQ: " +yytext()+"\n"; return symbol(ParserSym.OP_LEQ, yytext()); }
{op_menor}                  { tokens += "OP_MENOR: "   +yytext()+"\n"; return symbol(ParserSym.OP_LT, yytext()); }
{op_diferent}               { tokens += "OP_DIFERENT: "+yytext()+"\n"; return symbol(ParserSym.OP_NEQ, yytext()); }
{op_pot}                    { tokens += "OP_POTENCIA: "+yytext()+"\n"; return symbol(ParserSym.OP_POT, yytext()); }
{op_porcent}                { tokens += "OP_PORCENT: " +yytext()+"\n"; return symbol(ParserSym.OP_PCT, yytext()); }
{op_neg}                    { tokens += "OP_NEG: "  +yytext()+"\n"; return symbol(ParserSym.OP_NOT, yytext()); }
{op_or}                     { tokens += "OP_OR: "   +yytext()+"\n"; return symbol(ParserSym.OP_OR, yytext()); }
{op_and}                    { tokens += "OP_AND: "  +yytext()+"\n"; return symbol(ParserSym.OP_AND, yytext()); }
{op_asig}                   { tokens += "OP_ASIG: " +yytext()+"\n"; return symbol(ParserSym.AS_ASSIGN, yytext()); }
{op_swap}                   { tokens += "OP_SWAP: " +yytext()+"\n"; return symbol(ParserSym.OP_SWAP, yytext()); }
{op_cond}                   { tokens += "OP_COND: " +yytext()+"\n"; return symbol(ParserSym.OP_COND, yytext()); }
{op_arrow}                  { tokens += "OP_ARROW: "+yytext()+"\n"; return symbol(ParserSym.ARROW, yytext()); }

// tipos
//{type_double}           { tokens += "TYPE_DOUBLE: "+yytext()+"\n"; return symbol(ParserSym.KW_DOUBLE, yytext()); }
{type_int}              { tokens += "TYPE_INT: "   +yytext()+"\n"; return symbol(ParserSym.KW_INT, yytext()); }
{type_char}             { tokens += "TYPE_CHAR: "  +yytext()+"\n"; return symbol(ParserSym.KW_CHAR, yytext()); }
{type_bool}             { tokens += "TYPE_BOOL: "  +yytext()+"\n"; return symbol(ParserSym.KW_BOOL, yytext()); }
{type_void}             { tokens += "TYPE_VOID: "  +yytext()+"\n"; return symbol(ParserSym.VOID, yytext()); }
//{type_string}           { tokens += "TYPE_STRING: "+yytext()+"\n"; return symbol(ParserSym.KW_STRING, yytext()); }

// simbolos
{sym_parenIzq}          { tokens += "SYM_LPAREN: "  +yytext()+"\n"; return symbol(ParserSym.LPAREN, yytext()); }
{sym_parenDer}          { tokens += "SYM_RPAREN: "  +yytext()+"\n"; return symbol(ParserSym.RPAREN, yytext()); }
{sym_llaveIzq}          { tokens += "SYM_LKEY: "    +yytext()+"\n"; return symbol(ParserSym.LKEY, yytext()); }
{sym_llaveDer}          { tokens += "SYM_RKEY: "    +yytext()+"\n"; return symbol(ParserSym.RKEY, yytext()); }
{sym_bracketIzq}        { tokens += "SYM_LBRACKET: "+yytext()+"\n"; return symbol(ParserSym.LBRACKET, yytext()); }
{sym_bracketDer}        { tokens += "SYM_RBRACKET: "+yytext()+"\n"; return symbol(ParserSym.RBRACKET, yytext()); }
{sym_endInstr}          { tokens += "SYM_ENDINSTR: "+yytext()+"\n"; return symbol(ParserSym.ENDINSTR, yytext()); }
{sym_coma}              { tokens += "SYM_COMMA: "   +yytext()+"\n"; return symbol(ParserSym.COMMA, yytext()); }
{sym_punto}             { tokens += "SYM_PUNTO: "   +yytext()+"\n"; return symbol(ParserSym.OP_MEMBER, yytext()); }

// keywords
{kw_main}               { tokens += "KW_MAIN: "   +yytext()+"\n"; return symbol(ParserSym.KW_MAIN, yytext()); }
//{kw_args}               { tokens += "KW_ARGS: "   +yytext()+"\n"; return symbol(ParserSym.KW_ARGS, yytext()); }
{kw_method}             { tokens += "KW_METHOD: " +yytext()+"\n"; return symbol(ParserSym.KW_METHOD, yytext()); }
{kw_const}              { tokens += "KW_CONST: "  +yytext()+"\n"; return symbol(ParserSym.KW_CONST, yytext()); }
{kw_if}                 { tokens += "KW_IF: "     +yytext()+"\n"; return symbol(ParserSym.KW_IF, yytext()); }
{kw_elif}               { tokens += "KW_ELIF: "   +yytext()+"\n"; return symbol(ParserSym.KW_ELIF, yytext()); }
{kw_else}               { tokens += "KW_ELSE: "   +yytext()+"\n"; return symbol(ParserSym.KW_ELSE, yytext()); }
{kw_while}              { tokens += "KW_WHILE: "  +yytext()+"\n"; return symbol(ParserSym.KW_LOOP, yytext()); }
{kw_switch}             { tokens += "KW_SWITCH: " +yytext()+"\n"; return symbol(ParserSym.KW_SWITCH, yytext()); }
{kw_case}               { tokens += "KW_CASE: "   +yytext()+"\n"; return symbol(ParserSym.KW_CASE, yytext()); }
{kw_default}            { tokens += "KW_DEFAULT: "+yytext()+"\n"; return symbol(ParserSym.KW_DEFAULT, yytext()); }
{kw_return}             { tokens += "KW_RETURN: " +yytext()+"\n"; return symbol(ParserSym.KW_RETURN, yytext()); }
{kw_in}                 { tokens += "SCAN: "     +yytext()+"\n"; return symbol(ParserSym.SCAN, yytext()); }
{kw_out}                { tokens += "SHOW: "      +yytext()+"\n"; return symbol(ParserSym.SHOW, yytext()); }
{kw_read}               { tokens += "FROM: "      +yytext()+"\n"; return symbol(ParserSym.FROM, yytext()); }
{kw_write}              { tokens += "INTO: "      +yytext()+"\n"; return symbol(ParserSym.INTO, yytext()); }
{kw_tuple}              { tokens += "TUPLE: "  +yytext()+"\n"; return symbol(ParserSym.TUPLE, yytext()); }
{kw_continue}           { tokens += "KW_CONTINUE: "+yytext()+"\n";return symbol(ParserSym.KW_CONTINUE, yytext()); }
{kw_break}              { tokens += "KW_BREAK: "  +yytext()+"\n"; return symbol(ParserSym.KW_BREAK, yytext()); }

// valores
{val_binario}       { tokens += "VAL_BINARIO: "+yytext()+"\n"; return procesarNumero(); }
{val_hex}           { tokens += "VAL_HEX: "+yytext()+"\n"; return procesarNumero(); }
{val_octal}         { tokens += "VAL_OCTAL: "+yytext()+"\n"; return procesarNumero(); }
{val_decimal}       { tokens += "VAL_DECIMAL: "+yytext()+"\n"; return procesarNumero(); }
//{val_real}          { tokens += "VAL_REAL: "+yytext()+"\n"; return symbol(ParserSym.REAL, Double.parseDouble(yytext())); } // posible control de errores?
{val_prop}          { tokens += "VAL_PROP: "+yytext()+"\n"; return symbol(ParserSym.PROP, "cierto".equals(yytext())); }
{id}                { tokens += "ID: "+yytext()+"\n"; return symbol(ParserSym.ID, yytext()); }

// casos especiales
{sym_comillaS}  { errores += "Error lexico: Token '" + yytext() + "' utilizado para abrir un caracter pero no se ha cerrado con otro "+yytext()+" en la posicion [linea: " + (yyline+1) + ", columna: " + (yycolumn+1) + "]\n"; return symbol(ParserSym.error); }
{sym_comillaD}  { errores += "Error lexico: Token '" + yytext() + "' utilizado para abrir un string pero no se ha cerrado con otro "+yytext()+" en la posicion [linea: " + (yyline+1) + ", columna: " + (yycolumn+1) + "]\n"; return symbol(ParserSym.error); }
{char_vacio}    { errores += "Error lexico: Token '" + yytext() + "' utilizado para crear un caracter vacio (se crean strings con \") en la posicion [linea: " + (yyline+1) + ", columna: " + (yycolumn+1) + "]\n"; return symbol(ParserSym.error); }
{char_string}   { errores += "Error lexico: Token '" + yytext() + "' utilizado para crear varios caracteres (se crean strings con \") en la posicion [linea: " + (yyline+1) + ", columna: " + (yycolumn+1) + "]\n"; return symbol(ParserSym.error); }
{espacioBlanco} {}
{comentario}    {}
{finLinea}      {}
[^]             { errores += errorToString(); return symbol(ParserSym.error); }

/****************************************************************************/