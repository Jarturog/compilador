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

%%
/** **
%standalone
 ** **/

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

sub_digit   = [0-9]
sub_letra   = [A-Za-z] // no confundir con carácter
sub_car     = {sub_digit}|{sub_letra}
sub_signo   = [+|-]? 
id          = ({sub_letra}|_)({sub_car}|_)*

//Descripción de digitos
val_decimal  = {sub_signo}{sub_digit}+
val_binario = 0b[01]+
val_octal   = 0o[0-7]+
val_hex     = 0x[A-Fa-f0-9]+
val_real    = {val_decimal}?\.{val_decimal}?([Ee]{val_decimal})?
val_prop    = {kw_true}|{kw_false}
val_char    = {sym_comillaS}{sub_car}{sym_comillaS}
val_cadena  = {sym_comillaD}{sub_car}*{sym_comillaD}

// Símbolos
sym_parenIzq	= \(
sym_parenDer	= \)
sym_llaveIzq	= \{
sym_llaveDer 	= \}
sym_bracketIzq	= \[
sym_bracketDer	= \]
sym_endInstr    = \;
sym_coma	= \,
sym_comillaS = \'
sym_comillaD = \"

//Operadores
op_eq       = \= 
op_diferent = \/\= 
op_mayor    = \>
op_menor    = \<
op_mayorEq  = (\>\=)|(\=\>)
op_menorEq  = (\<\=)|(\=\<)
op_sum      = \+
op_res      = \-
op_mul      = \*
op_div      = \/
op_porcent  = \%
op_swap     = \<\>
op_or       = \|
op_and      = &
op_mod      = \\
op_neg      = \¬
op_asig     = \:

//Palabras reservadas
type_char      = "car"
type_string    = "string"
type_int       = "ent"
type_double    = "val_real"
type_bool      = "prop"
type_void      = "vacio"

kw_const     = "inmut"  // inmutable
kw_main      = "inicio"
kw_if        = "si"
kw_elif      = "sino"
kw_else      = "no"
kw_switch    = "select"
kw_while     = "loop"
kw_doLoop    = "do"
kw_return    = "pop" 
kw_true      = "cierto"
kw_false     = "falso"
kw_in        = "enter"
kw_out       = "show"

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
     private String tokens = "";
	private String errores = "";

	public String getTokens(){
		return tokens;
	}

	public String getErrores(){
		return errores;
	}

	private String errorToString(){
		return "!!! Error léxico: Token " + yytext() + " no reconocido en la posición [línea: " + (yyline+1) + ", columna: " + (yycolumn+1) + "]";
	}
	
	private int parseNum(String s) throws NumberFormatException {
		// We check whether the first number is a 0, if so there might be a prefix specifying base, unless it's just a 0 by itself.
		if(s.charAt(0) != '0' || s.length() == 1) return Integer.parseInt(s);
		// If 
		char base = s.charAt(1);
		String num = s.substring(2);
		//String[] sParts = s.split(""+base);
		switch(base){
			case 'b':
				return Integer.parseInt(num, 2);
			case 'o':
				return Integer.parseInt(num, 8);
			case 'x':
				return Integer.parseInt(num, 16);
			default:
				throw new NumberFormatException(errorMessage());
		}
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

{zerodigit}              { return symbol(ParserSym.valor, 0.0); } // quitar en el futuro?

//Operadores

{op_sum}                    { tokens += "OP_SUM: "+yytext()+"\n"; return symbol(ParserSym.ADD);    }
{op_res}                    { return symbol(ParserSym.SUB); }
{op_mul}                    { return symbol(ParserSym.MUL); }
{op_div}                    { return symbol(ParserSym.DIV); }
{op_mod}                    { return symbol(ParserSym.MOD); }
{op_eq}				              { return symbol(ParserSym.EQ); }
{op_mayorEq}                { return symbol(ParserSym.BEQ); }
{op_mayor}			            { return symbol(ParserSym.BT); }
{op_menorEq}                { return symbol(ParserSym.LEQ); }
{op_menor}			            { return symbol(ParserSym.LT); }
{op_diferent}               { return symbol(ParserSym.NEQ); }
{op_porcent}                { return symbol(ParserSym.PCT); }
{op_neg}			              { return symbol(ParserSym.NOT); }
{op_or}				              { return symbol(ParserSym.OR); }
{op_and}			              { return symbol(ParserSym.AND); }
{op_asig}                   { return symbol(ParserSym.ASSIGN); }
{op_swap} 			            { return symbol(ParserSym.SWAP); }

// Types
{type_double}			          { return symbol(ParserSym.DOUBLE); }
{type_int}		    	        { return symbol(ParserSym.INT); }
{type_char}			            { return symbol(ParserSym.CHAR); }
{type_bool}			            { return symbol(ParserSym.BOOL); }
{type_void}			            { return symbol(ParserSym.VOID); }
{type_string}               { return symbol(ParserSym.STRING); }

// Special characters
{sym_parenIzq}              { return symbol(ParserSym.LPAREN); }
{sym_parenDer}              { return symbol(ParserSym.RPAREN); }
{sym_llaveIzq}			        { return symbol(ParserSym.LKEY); }
{sym_llaveDer} 			        { return symbol(ParserSym.RKEY); }
{sym_bracketIzq}		        { return symbol(ParserSym.LBRACKET); }
{sym_bracketDer}		        { return symbol(ParserSym.RBRACKET); }
{sym_endInstr}			        { return symbol(ParserSym.ENDINSTR); }
{sym_coma}			            { return symbol(ParserSym.COMMA); }
{sym_comillaS}		      { return symbol(ParserSym.SQUOTE); }
{sym_comillaD}		      { return symbol(ParserSym.DQUOTE); }

// Palabras reservadas
{kw_main}						            { return symbol(ParserSym.MAIN); }
{kw_const}						            { return symbol(ParserSym.CONST); }
{kw_if}							            { return symbol(ParserSym.IF); }
{kw_elif}						            { return symbol(ParserSym.ELIF); }
{kw_else}						            { return symbol(ParserSym.ELSE); }
{kw_while}                   { return symbol(ParserSym.LOOP); }
{kw_doLoop}			            { return symbol(ParserSym.DO); }
{kw_switch }                    { return symbol(ParserSym.SWITCH); }
{kw_return}			            { return symbol(ParserSym.RETURN); }
{kw_in} 			  			            { return symbol(ParserSym.IN); }
{kw_out} 			  			            { return symbol(ParserSym.OUT); }


// Non-reserved words
{val_binario}            { return symbol(ParserSym.valor, Integer.parseInt(this.yytext().substring(2, this.yytext().length()),2)); }
{val_hex}                { return symbol(ParserSym.valor, Integer.parseInt(this.yytext().substring(2, this.yytext().length()),16)); }
{val_octal}                { return symbol(ParserSym.valor, Integer.parseInt(this.yytext().substring(2, this.yytext().length()),8)); }
{val_decimal}                { return symbol(ParserSym.valor, Integer.parseInt(this.yytext())); }
{val_real}                   { return symbol(ParserSym.valor, Double.parseDouble(this.yytext())); }
{val_char}            { return symbol(ParserSym.CHAR, yytext().charAt(0)); }
{val_prop}            { return symbol(ParserSym.BOOLEAN, Boolean.parseBoolean(yytext())); }
{val_cadena}            { return symbol(ParserSym.STRING, yytext());}
{id}		              { return symbol(ParserSym.ID, yytext()); }


{espacioBlanco}            { /* No fer res amb els espais */  }
{comentario}                { /* No fer res amb els espais */  }
{finLinea}                { return symbol(ParserSym.ENDLINE); }

[^]					{ errors.add(errorMessage()); System.err.println(errorMessage()); return symbol(ParserSym.error); }

/****************************************************************************/