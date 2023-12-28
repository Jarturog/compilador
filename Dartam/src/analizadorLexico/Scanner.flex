/**
 * Assignatura 21742 - Compiladors I 
 * Estudis: Grau en Informàtica 
 * Itinerari: Computació 
 * Curs: 2023-2024
 *
 * Equipo: Marta, Arturo, Dani
 */

package analizadorLexico;

import java.io.*; // aquí van los imports

import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;

import analizadorSintactico.ParserSym;

//import jlex_cup_example.compiler_components.cup.ParserSym;

%%
/** **
%standalone
 ** **/

/****
 Para analizar sintácticamente se usará Java CUP.
 ****/
%cup
/****
La línia anterior és una alternativa a la indicació element a element:

%implements java_cup.runtime.Scanner
%function next_token
%type java_cup.runtime.Symbol

****/

%public              // Indicaremos que la clase creada es pública
%class Scanner       // El nombre de la clase

%char                //Los siguientes 3 elementos sirven para controlar errores
%line
%column

%eofval{
  return symbol(ParserSym.EOF);
%eofval}

// Declaracions
// sub quiere decir que es un lexema que forma parte de otro
// val que comprende valores
// op que es una operación
// sim que es un símbolo
// kw que es una palabra reservada (keyword)

sub_digit   = [0-9]
sub_letra   = [A-Za-z] // no confundir con carácter
sub_signo   = [+|-]? 
id          = ({sub_letra}|_)({sub_letra}|{sub_digit}|_)*

//Descripción de digitos
sub_base10  = {sub_signo}{sub_digit}+
sub_binario = 0b[01]+
sub_octal   = 0o[0-7]+
sub_hex     = 0x[A-Fa-f0-9]+

val_real    = {sub_base10}?\.{sub_base10}?([Ee]{sub_base10})?
val_prop    = {kw_True}|{kw_False}
val_entero  = {sub_base10}|{sub_binario}|{sub_octal}|{sub_hex}
val_char = {sim_comillaSimple}({sub_letra}|{sub_digit}){sim_comillaSimple}
val_cadena  = {sim_comillaDoble}({sub_letra}|{sub_digit})*{sim_comillaDoble}

// Símbolos
sym_parenIzq	= \(
sym_parenDer	= \)
sym_llaveIzq	= \{
sym_llaveDer 	= \}
sym_bracketIzq	= \[
sym_bracketDer	= \]
sym_endInstr    = \;
sym_asig        = \:
sym_coma	= \,
sym_comillaSimple = \'
sym_comillaDoble = \"

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
op_or       = \|
op_and      = &
op_mod      = \\
op_neg      = \!


//Palabras reservadas
type_Char      = "car"
type_String    = "string"
type_Int       = "ent"
type_Double    = "val_real"
type_Bool      = "prop"
type_Const     = "inmut"  // inmutable
type_Void      = "vacio"

kw_Main      = "inicio"
kw_If        = "si"
kw_Elif      = "sino"
kw_Else      = "no"
kw_Switch    = "select"
kw_WhileFor  = "loop"
kw_DoLoop    = "do"
kw_Return    = "pop" 
kw_True      = "cierto"
kw_False     = "falso"

comentarLinea  = "\/\/"
comentarBloque = "#" // tanto para el inicio como para el final
comentario = {comentarLinea}|{comentarBloque}[^]*{comentarBloque}

espacioBlanco = [ \t]+
finLinea = [\r\n]+

//Codigo necesario para que la clase Scanner funcione correctamente 
%{
    private ArrayList<String> tokens = new ArrayList<>();
    private ArrayList<String> errors = new ArrayList<>();
    
    public String writeTokens(){
		String tokenList = "";
		for(String s : tokens){
			tokenList += s + "\n";
		}
		return tokenList;
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

    private String errorMessage(){
		return " !! Lexic error: Not recognized token " + yytext() + " at position [line: " + (yyline+1) + ", column: " + (yycolumn+1) + "]";
    }
    
    /***
       Mecanismes de gestió de símbols basat en ComplexSymbol. Tot i que en
       aquest cas potser no és del tot necessari.
     ***/
    /**
     Construcció d'un symbol sense atribut associat.
     **/
    private ComplexSymbol symbol(int type) {
        // Sumar 1 per a que la primera línia i columna no sigui 0.
        ComplexSymbol simbolo = new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta);
        simbolo.left = yyline +1;
        simbolo.right = yycolumn;
        tokens.add(simbolo);
        return simbolo;
     }
    
    /**
     Construcció d'un symbol amb un atribut associat.
     **/
    private Symbol symbol(int type, Object value) {
        // Sumar 1 per a que la primera línia i columna no sigui 0.
        ComplexSymbol simbolo = new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta, value);
        simbolo.left = yyline + 1;
        simbolo.right = yycolumn;
        tokens.add(simbolo);
        return simbolo;
}
%}

/***************************************************************************************/
%%

// Regles/accions
{comentario}			{ /* No hacemos nada */ }

// Palabras reservadas
{kw_Main}			{ tokens.add(yytext() + " : KW_MAIN"); return symbol(ParserSym.RES_MAIN); }
{kw_Const}			{ tokens.add(yytext() + " : KW_CONSTANT"); return symbol(ParserSym.CONSTANT); }
{op_neg}			{ tokens.add(yytext() + " : KW_NOT"); return symbol(ParserSym.NOT); }
{op_or}				{ tokens.add(yytext() + " : KW_OR"); return symbol(ParserSym.OR); }
{op_and}			{ tokens.add(yytext() + " : KW_AND"); return symbol(ParserSym.AND); }
{kw_If}				{ tokens.add(yytext() + " : KW_IF"); return symbol(ParserSym.KW_IF); }
{kw_Elif}			{ tokens.add(yytext() + " : KW_ELIF"); return symbol(ParserSym.KW_ELIF); }
{kw_Else}			{ tokens.add(yytext() + " : KW_ELSE"); return symbol(ParserSym.KW_ELSE); }
{kw_WhileFor}                   { tokens.add(yytext() + " : KW_LOOP"); return symbol(ParserSym.KW_LOOP); }
{kw_DoLoop}			{ tokens.add(yytext() + " : KW_DO"); return symbol(ParserSym.KW_DO); }
{kw_Switch }                    { tokens.add(yytext() + " : KW_RETURN"); return symbol(ParserSym.KW_SWITCH); }
{kw_Return}			{ tokens.add(yytext() + " : KW_RETURN"); return symbol(ParserSym.KW_RETURN); }
//{resIn} 			{ tokens.add(yytext() + " : KW_IN"); return symbol(ParserSym.KW_IN); }
//{resOut} 			{ tokens.add(yytext() + " : KW_OUT"); return symbol(ParserSym.KW_OUT); }

// Types
{type_Double}			{ tokens.add(yytext() + " : TYPE_DOUBLE"); return symbol(ParserSym.DOUBLE); }
{type_Int}		    	{ tokens.add(yytext() + " : TYPE_INTEGER"); return symbol(ParserSym.TYPE_INTEGER); }
{type_Char}			{ tokens.add(yytext() + " : TYPE_CHARACTER"); return symbol(ParserSym.TYPE_CHARACTER); }
{type_Bool}			{ tokens.add(yytext() + " : TYPE_BOOLEAN"); return symbol(ParserSym.TYPE_BOOLEAN); }
{type_Void}			{ tokens.add(yytext() + " : TYPE_VOID"); return symbol(ParserSym.TYPE_VOID); }
{type_String}                   { tokens.add(yytext() + " : TYPE_STRING"); return symbol(ParserSym.TYPE_STRING); }

// Special characters
{sym_parenIzq}			{ tokens.add(yytext() + " : L_PAREN"); return symbol(ParserSym.L_PAREN); }
{sym_parenDer}			{ tokens.add(yytext() + " : R_PAREN"); return symbol(ParserSym.R_PAREN); }
{sym_llaveIzq}			{ tokens.add(yytext() + " : L_KEY"); return symbol(ParserSym.L_KEY); }
{sym_llaveDer} 			{ tokens.add(yytext() + " : R_KEY"); return symbol(ParserSym.R_KEY); }
{sym_bracketIzq}		{ tokens.add(yytext() + " : L_BRACKET"); return symbol(ParserSym.L_BRACKET); }
{sym_bracketDer}		{ tokens.add(yytext() + " : R_BRACKET"); return symbol(ParserSym.R_BRACKET); }
{sym_endInstr}			{ tokens.add(yytext() + " : ENDLINE"); return symbol(ParserSym.ENDLINE); }
{sym_coma}			{ tokens.add(yytext() + " : COMMA"); return symbol(ParserSym.COMMA); }
{sym_comillaSimple}		{ tokens.add(yytext() + " : SQUOTE"); return symbol(ParserSym.SQUOTE); }
{sym_comillaDoble}		{ tokens.add(yytext() + " : DQUOTE"); return symbol(ParserSym.DQUOTE); }

//Operadores
{op_sum}			{ tokens.add(yytext() + " : OP_ADD"); return symbol(ParserSym.ADD); }
{op_res}			{ tokens.add(yytext() + " : OP_SUB"); return symbol(ParserSym.SUB); }
{op_mul}                        { tokens.add(yytext() + " : OP_PROD"); return symbol(ParserSym.PROD); }
{op_div}			{ tokens.add(yytext() + " : OP_DIV"); return symbol(ParserSym.DIV); }
{op_mod}			{ tokens.add(yytext() + " : OP_MOD"); return symbol(ParserSym.MOD); }
{op_eq}				{ tokens.add(yytext() + " : OP_IS_EQUAL"); return symbol(ParserSym.IS_EQUAL); }
{op_mayorEq}                    { tokens.add(yytext() + " : OP_BEQ"); return symbol(ParserSym.BEQ); }
{op_mayor}			{ tokens.add(yytext() + " : OP_BIGGER"); return symbol(ParserSym.BIGGER); }
{op_menorEq}                    { tokens.add(yytext() + " : OP_LEQ"); return symbol(ParserSym.LEQ); }
{op_menor}			{ tokens.add(yytext() + " : OP_LESSER"); return symbol(ParserSym.LESSER); }
{op_diferent}                   { tokens.add(yytext() + " : OP_NEQ"); return symbol(ParserSym.NEQ); }
{op_porcent} // pendiente

{sim_asig}	    		{ tokens.add(yytext() + " : EQUAL"); return symbol(ParserSym.EQUAL); }
// no tenemos: {swapSym} 			{ tokens.add(yytext() + " : OP_SWAP"); return symbol(ParserSym.OP_SWAP); }

// Non-reserved words
// {character}			{ tokens.add(yytext() + " : CHARACTER"); return symbol(ParserSym.CHARACTER, yytext().charAt(1)); }

{val_real}                          { try {tokens.add(yytext() + " : DOUBLE"); return symbol(ParserSym.REAL, Double.parseDouble(this.yytext()));} catch(Exception nf){return symbol(ParserSym.error);}}     
{val_entero}    		{ try {tokens.add(yytext() + " : INTEGER"); return symbol{ParserSym.INT, Integer.parseInt(yytext())};} catch(Exception nf){return symbol(ParserSym.error);} }
{val_prop}			{ tokens.add(yytext() + " : BOOLEAN"); return symbol(ParserSym.BOOLEAN, Boolean.parseBoolean(yytext())); }
{val_char}                      { tokens.add(yytext() + " : CHAR"); return symbol(ParserSym.STRING, yytext().charAt(0));}
{val_cadena}			{ tokens.add(yytext() + " : STRING"); return symbol(ParserSym.STRING, yytext());}

{id}		{ tokens.add(yytext() + " : IDENTIFIER"); return symbol(ParserSym.IDENTIFIER, yytext()); }
{finLinea}  {return symbol(ParserSym.EOF)}
{espacioBlanco} { /* no hacemos nada*/}
[^]					{ errors.add(errorMessage()); System.err.println(errorMessage());
						return symbol(ParserSym.error); 
					}

/******************************************************************************************************************/