/**
  * Assignatura 21742 - Compiladors I 
  * Estudis: Grau en Informàtica 
  * Itinerari: Computació 
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
/****
  %standalone
****/

/****
  Indicació de quin tipus d'analitzador sintàctic s'utilitzarà. En aquest cas 
  es fa ús de Java CUP.
****/
%cup
/****
  La línia anterior és una alternativa a la indicació element a element:

  %implements java_cup.runtime.Scanner
  %function next_token
  %type java_cup.runtime.Symbol
****/

%public              // Per indicar que la classe és pública
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
// op que es una operación
// sym que es un símbolo
// kw que es una palabra reservada (keyword)

zerodigit	= 0

// reutilizables
sub_digit   = [0-9]
sub_letra   = [A-Za-z] // no confundir con carácter
sub_car     = {sub_digit}|{sub_letra}
sub_signo   = [+|-]? 

// símbolos
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
op_cond     = \?
op_arrow    = \-\>

// tipos (void es tipo de retorno pero no de variable)
type_char      = "car"
type_string    = "string"
type_int       = "ent"
type_double    = "val_real"
type_bool      = "prop"
type_void      = "vacio"

// palabras reservadas (const está entre type y kw)
kw_const     = "inmut"  // inmutable
kw_main      = "inicio"
kw_args      = "argumentos"
kw_method    = "f"
kw_if        = "si"
kw_elif      = "sino"
kw_else      = "no"
kw_switch    = "select"
kw_case      = "caso"
kw_default   = "_"
kw_while     = "loop"
kw_doLoop    = "do"
kw_return    = "pop" 
kw_true      = "cierto"
kw_false     = "falso"
kw_in        = "enter"
kw_out       = "show"
kw_read      = "from"
kw_write     = "into"
kw_tuple     = "tupla"

// valores
val_decimal = {sub_signo}{sub_digit}+
val_binario = 0b[01]+
val_octal   = 0o[0-7]+
val_hex     = 0x[A-Fa-f0-9]+
val_real    = {val_decimal}?\.{val_decimal}?([Ee]{val_decimal})?
val_prop    = {kw_true}|{kw_false}
val_char    = {sym_comillaS}{sub_car}{sym_comillaS}
val_cadena  = {sym_comillaD}{sub_car}*{sym_comillaD}
id          = ({sub_letra}|_)({sub_car}|_)*

// casos especiales
espacioBlanco = [ \t]+
finLinea = [\r\n]+
comentarLinea  = "\/\/"
comentarBloque = "#" // tanto para el inicio como para el final
comentario = {comentarLinea}.*|{comentarBloque}[^]*{comentarBloque}

// El següent codi es copiarà també, dins de la classe. És a dir, si es posa res
// ha de ser en el format adient: mètodes, atributs, etc. 
%{
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
%}

/****************************************************************************/
%%

// Regles/accions
// És molt important l'ordre de les regles!!!

// operadores
{op_and_asig}               { tokens += "OP_AND_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_ANDA); }
{op_or_asig}                { tokens += "OP_OR_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_ORA); }
{op_sum_asig}               { tokens += "OP_SUM_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_ADDA); }
{op_res_asig}               { tokens += "OP_RES_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_SUBA); }
{op_mul_asig}               { tokens += "OP_MUL_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_MULA); }
{op_div_asig}               { tokens += "OP_DIV_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_DIVA); }
{op_pot_asig}               { tokens += "OP_POT_ASSIGNMENT: "+yytext()+"\n"; return symbol(ParserSym.AS_POTA); }
{op_inc}                    { tokens += "OP_INC: "+yytext()+"\n"; return symbol(ParserSym.OP_INC); }
{op_dec}                    { tokens += "OP_DEC: "+yytext()+"\n"; return symbol(ParserSym.OP_DEC); }
{op_sum}                    { tokens += "OP_SUM: "+yytext()+"\n"; return symbol(ParserSym.OP_ADD); }
{op_res}                    { tokens += "OP_RES: "+yytext()+"\n"; return symbol(ParserSym.OP_SUB); }
{op_mul}                    { tokens += "OP_MUL: "+yytext()+"\n"; return symbol(ParserSym.OP_MUL); }
{op_div}                    { tokens += "OP_DIV: "+yytext()+"\n"; return symbol(ParserSym.OP_DIV); }
{op_mod}                    { tokens += "OP_MOD: "+yytext()+"\n"; return symbol(ParserSym.OP_MOD); }
{op_eq}                     { tokens += "OP_EQ: "+yytext()+"\n"; return symbol(ParserSym.OP_EQ); }
{op_mayorEq}                { tokens += "OP_MAYOREQ: "+yytext()+"\n"; return symbol(ParserSym.OP_BEQ); }
{op_mayor}                  { tokens += "OP_MAYOR: "+yytext()+"\n"; return symbol(ParserSym.OP_BT); }
{op_menorEq}                { tokens += "OP_MENOREQ: "+yytext()+"\n"; return symbol(ParserSym.OP_LEQ); }
{op_menor}                  { tokens += "OP_MENOR: "+yytext()+"\n"; return symbol(ParserSym.OP_LT); }
{op_diferent}               { tokens += "OP_DIFERENT: "+yytext()+"\n"; return symbol(ParserSym.OP_NEQ); }
{op_pot}                    { tokens += "OP_POTENCIA: "+yytext()+"\n"; return symbol(ParserSym.OP_POT); }
{op_porcent}                { tokens += "OP_PORCENT: "+yytext()+"\n"; return symbol(ParserSym.OP_PCT); }
{op_neg}                    { tokens += "OP_NEG: "+yytext()+"\n"; return symbol(ParserSym.OP_NOT); }
{op_or}                     { tokens += "OP_OR: "+yytext()+"\n"; return symbol(ParserSym.OP_OR); }
{op_and}                    { tokens += "OP_AND: "+yytext()+"\n"; return symbol(ParserSym.OP_AND); }
{op_asig}                   { tokens += "OP_ASIG: "+yytext()+"\n"; return symbol(ParserSym.AS_ASSIGN); }
{op_swap}                   { tokens += "OP_SWAP: "+yytext()+"\n"; return symbol(ParserSym.OP_SWAP); }
{op_cond}                   { tokens += "OP_COND: "+yytext()+"\n"; return symbol(ParserSym.OP_COND); }
{op_arrow}                  { tokens += "OP_ARROW: "+yytext()+"\n"; return symbol(ParserSym.ARROW); }

// tipos
{type_double}           { tokens += "TYPE_DOUBLE: "+yytext()+"\n"; return symbol(ParserSym.KW_DOUBLE); }
{type_int}              { tokens += "TYPE_INT: "+yytext()+"\n"; return symbol(ParserSym.KW_INT); }
{type_char}             { tokens += "TYPE_CHAR: "+yytext()+"\n"; return symbol(ParserSym.KW_CHAR); }
{type_bool}             { tokens += "TYPE_BOOL: "+yytext()+"\n"; return symbol(ParserSym.KW_BOOL); }
{type_void}             { tokens += "TYPE_VOID: "+yytext()+"\n"; return symbol(ParserSym.KW_VOID); }
{type_string}           { tokens += "TYPE_STRING: "+yytext()+"\n"; return symbol(ParserSym.KW_STRING); }

// simbolos
{sym_parenIzq}          { tokens += "SYM_LPAREN: "+yytext()+"\n"; return symbol(ParserSym.LPAREN); }
{sym_parenDer}          { tokens += "SYM_RPAREN: "+yytext()+"\n"; return symbol(ParserSym.RPAREN); }
{sym_llaveIzq}          { tokens += "SYM_LKEY: "+yytext()+"\n"; return symbol(ParserSym.LKEY); }
{sym_llaveDer}          { tokens += "SYM_RKEY: "+yytext()+"\n"; return symbol(ParserSym.RKEY); }
{sym_bracketIzq}        { tokens += "SYM_LBRACKET: "+yytext()+"\n"; return symbol(ParserSym.LBRACKET); }
{sym_bracketDer}        { tokens += "SYM_RBRACKET: "+yytext()+"\n"; return symbol(ParserSym.RBRACKET); }
{sym_endInstr}          { tokens += "SYM_ENDINSTR: "+yytext()+"\n"; return symbol(ParserSym.ENDINSTR); }
{sym_coma}              { tokens += "SYM_COMMA: "+yytext()+"\n"; return symbol(ParserSym.COMMA); }
{sym_comillaS}          {}//{ tokens += "SYM_SQUOTE: "+yytext()+"\n"; return symbol(ParserSym.SQUOTE); }
{sym_comillaD}          {}//{ tokens += "SYM_DQUOTE: "+yytext()+"\n"; return symbol(ParserSym.DQUOTE); }
{sym_punto}             { tokens += "SYM_PUNTO: "+yytext()+"\n"; return symbol(ParserSym.OP_MEMBER); }

// keywords
{kw_main}               { tokens += "KW_MAIN: "+yytext()+"\n"; return symbol(ParserSym.KW_MAIN); }
{kw_args}               { tokens += "KW_ARGS: "+yytext()+"\n"; return symbol(ParserSym.KW_ARGS); }
{kw_method}             { tokens += "KW_METHOD: "+yytext()+"\n"; return symbol(ParserSym.KW_METHOD); }
{kw_const}              { tokens += "KW_CONST: "+yytext()+"\n"; return symbol(ParserSym.KW_CONST); }
{kw_if}                 { tokens += "KW_IF: "+yytext()+"\n"; return symbol(ParserSym.KW_IF); }
{kw_elif}               { tokens += "KW_ELIF: "+yytext()+"\n"; return symbol(ParserSym.KW_ELIF); }
{kw_else}               { tokens += "KW_ELSE: "+yytext()+"\n"; return symbol(ParserSym.KW_ELSE); }
{kw_while}              { tokens += "KW_WHILE: "+yytext()+"\n"; return symbol(ParserSym.KW_LOOP); }
{kw_doLoop}             { tokens += "KW_DO: "+yytext()+"\n"; return symbol(ParserSym.KW_DO); }
{kw_switch}             { tokens += "KW_SWITCH: "+yytext()+"\n"; return symbol(ParserSym.KW_SWITCH); }
{kw_case}               { tokens += "KW_CASE: "+yytext()+"\n"; return symbol(ParserSym.KW_CASE); }
{kw_default}            { tokens += "KW_DEFAULT: "+yytext()+"\n"; return symbol(ParserSym.KW_DEFAULT); }
{kw_return}             { tokens += "KW_RETURN: "+yytext()+"\n"; return symbol(ParserSym.KW_RETURN); }
{kw_in}                 { tokens += "KW_IN: "+yytext()+"\n"; return symbol(ParserSym.KW_IN); }
{kw_out}                { tokens += "KW_OUT: "+yytext()+"\n"; return symbol(ParserSym.KW_OUT); }
{kw_read}               { tokens += "KW_READ: "+yytext()+"\n"; return symbol(ParserSym.KW_READ); }
{kw_write}              { tokens += "KW_WRITE: "+yytext()+"\n"; return symbol(ParserSym.KW_WRITE); }
{kw_tuple}              { tokens += "KW_TUPLE: "+yytext()+"\n"; return symbol(ParserSym.KW_TUPLE); }

// valores
{val_binario}       { tokens += "VAL_BINARIO: "+yytext()+"\n"; return symbol(ParserSym.INT, Integer.parseInt(yytext().substring(2, yytext().length()),2)); }
{val_hex}           { tokens += "VAL_HEX: "+yytext()+"\n"; return symbol(ParserSym.INT, Integer.parseInt(yytext().substring(2, yytext().length()),16)); }
{val_octal}         { tokens += "VAL_OCTAL: "+yytext()+"\n"; return symbol(ParserSym.INT, Integer.parseInt(yytext().substring(2, yytext().length()),8)); }
{val_decimal}       { tokens += "VAL_DECIMAL: "+yytext()+"\n"; return symbol(ParserSym.INT, Integer.parseInt(yytext())); }
{val_real}          { tokens += "VAL_REAL: "+yytext()+"\n"; return symbol(ParserSym.INT, Double.parseDouble(yytext())); }
{val_char}          { tokens += "VAL_CHAR: "+yytext()+"\n"; return symbol(ParserSym.CHAR, yytext().charAt(0)); }
{val_prop}          { tokens += "VAL_PROP: "+yytext()+"\n"; return symbol(ParserSym.BOOL, "cierto".equals(yytext())); }
{val_cadena}        { tokens += "VAL_CADENA: "+yytext()+"\n"; return symbol(ParserSym.STRING, yytext()); }
{id}                { tokens += "ID: "+yytext()+"\n"; return symbol(ParserSym.ID, yytext()); }

// casos especiales
{espacioBlanco}        { /* No fer res amb els espais */  }
{comentario}           { /* No fer res amb els comentaris */  }
{finLinea}             {}//{ tokens += "FIN_LINEA: \n"; return symbol(ParserSym.ENDLINE); }
[^]				             { errores += errorToString(); System.err.println(errorToString()); return symbol(ParserSym.error); }

/****************************************************************************/