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
import analizadorLexico.syntactic.ParserSym;

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
//sub_digits  = {sub_digit}+ NO SE USA
sub_letra   = [A-Za-z] // no confundir con carácter
id          = ({sub_letra}|_)({sub_letra}|{sub_digit}|_)*
sub_signo   = [+|-]? 

//digito* por [1-9]*
sub_base10  = {sub_signo}[0|[1-9]*]
sub_binario = 0b[01]+
sub_octal   = 0o[0-7]+
sub_hex     = 0x[A-Fa-f0-9]+
val_entero  = {sub_base10}|{sub_binario}|{sub_octal}|{sub_hex}

//cambiado digitos->sub_base10
val_real    = {sub_base10}?\.{sub_base10}?([Ee]{sub_base10})?
val_prop    = {kw_True}|{kw_False}

// Símbolos

sim_parenIzq	= \(
sim_parenDer	= \)
sim_llaveIzq	= \{
sim_llaveDer 	= \}
sim_bracketIzq	= \[
sim_bracketDer	= \]
sim_endInstr    = \;
sim_asig        = \:
sim_coma	= \,

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
op_neg      = ¬


//Palabras reservadas

kw_Char      = "car"
kw_Int       = "ent"
kw_Double    = "val_real"
kw_Bool      = "prop"
kw_Const     = "inmut"  // inmutable
kw_Void      = "vacio"

kw_Main      = "inicio"
kw_If        = "si"
kw_Elif      = "sino"
kw_Else      = "no"
kw_Switch    = "select"
kw_WhileFor  = "loop"
kw_DoLoop    = "do"
kw_Return    = "pop" "-|" // todavía pendiente
kw_True      = "cierto"
kw_False     = "falso"

comentarLinea  = "\/\/"
comentarBloque = "#" // tanto para el inicio como para el final
comentario = {comentarLinea}.*|{comentarBloque}[^]*{comentarBloque}

espacioBlanco = [  \t\r\n]+
finLinea = [\r\n]+

//Codigo necesario para que la clase Scanner funcione correctamente 
%{
    private ArrayList<String> tokens = new ArrayList<>();
    
    
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
    
    /***
       Mecanismes de gestió de símbols basat en ComplexSymbol. Tot i que en
       aquest cas potser no és del tot necessari.
     ***/
    /**
     Construcció d'un symbol sense atribut associat.
     **/
    private ComplexSymbol symbol(int type) {
        // Sumar 1 per a que la primera línia i columna no sigui 0.
        Location esquerra = new Location(yyline+1, yycolumn+1);
        Location dreta = new Location(yyline+1, yycolumn+yytext().length()+1);

        return new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta);
    }
    
    /**
     Construcció d'un symbol amb un atribut associat.
     **/
    private Symbol symbol(int type, Object value) {
        // Sumar 1 per a que la primera línia i columna no sigui 0.
        Location esquerra = new Location(yyline+1, yycolumn+1);
        Location dreta = new Location(yyline+1, yycolumn+yytext().length()+1);

        return new ComplexSymbol(ParserSym.terminalNames[type], type, esquerra, dreta, value);
    }
%}

/***************************************************************************************/
%%

// Regles/accions

// Palabras reservadas
{kw_Main}			{ tokens.add(yytext() + " : RES_MAIN"); return symbol(ParserSym.RES_MAIN); }
{kw_Const}			{ tokens.add(yytext() + " : CONSTANT"); return symbol(ParserSym.CONSTANT); }
{op_neg}				{ tokens.add(yytext() + " : NOT"); return symbol(ParserSym.NOT); }
{op_or}				{ tokens.add(yytext() + " : OR"); return symbol(ParserSym.OR); }
{op_and}			    { tokens.add(yytext() + " : AND"); return symbol(ParserSym.AND); }
{kw_If}				{ tokens.add(yytext() + " : RES_IF"); return symbol(ParserSym.RES_IF); }
{kw_Elif}			{ tokens.add(yytext() + " : RES_ELIF"); return symbol(ParserSym.RES_ELIF); }
{kw_Else}			{ tokens.add(yytext() + " : RES_ELSE"); return symbol(ParserSym.RES_ELSE); }
{kw_WhileFor}		{ tokens.add(yytext() + " : RES_LOOP"); return symbol(ParserSym.RES_LOOP); }
{kw_DoLoop}			{ tokens.add(yytext() + " : RES_DO"); return symbol(ParserSym.RES_DO); }
{kw_Double}			{ tokens.add(yytext() + " : RES_DOUBLE"); return symbol(ParserSym.RES_DOUBLE); }

{kw_Switch }                     { tokens.add(yytext() + " : RES_RETURN"); return symbol(ParserSym.SWITCH); }
{comentario}			{ /* No hacemos nada */ }

//{resReturn}			{ tokens.add(yytext() + " : RES_RETURN"); return symbol(ParserSym.RES_RETURN); }
//{resIn} 			{ tokens.add(yytext() + " : RES_IN"); return symbol(ParserSym.RES_IN); }
//{resOut} 			{ tokens.add(yytext() + " : RES_OUT"); return symbol(ParserSym.RES_OUT); }
// Types
{kw_Int}		    	{ tokens.add(yytext() + " : TYPE_INTEGER"); return symbol(ParserSym.TYPE_INTEGER); }
{kw_Char}			{ tokens.add(yytext() + " : TYPE_CHARACTER"); return symbol(ParserSym.TYPE_CHARACTER); }
{kw_Bool}			{ tokens.add(yytext() + " : TYPE_BOOLEAN"); return symbol(ParserSym.TYPE_BOOLEAN); }
{kw_Void}			{ tokens.add(yytext() + " : TYPE_VOID"); return symbol(ParserSym.TYPE_VOID); }
{kw_Return}			{ tokens.add(yytext() + " : RES_RETURN"); return symbol(ParserSym.RETURN); }

// Special characters
{sim_parenIzq}			{ tokens.add(yytext() + " : L_PAREN"); return symbol(ParserSym.L_PAREN); }
{sim_parenDer}			{ tokens.add(yytext() + " : R_PAREN"); return symbol(ParserSym.R_PAREN); }
{sim_llaveIzq}			{ tokens.add(yytext() + " : L_KEY"); return symbol(ParserSym.L_KEY); }
{sim_llaveDer} 			{ tokens.add(yytext() + " : R_KEY"); return symbol(ParserSym.R_KEY); }
{sim_bracketIzq}		{ tokens.add(yytext() + " : L_BRACKET"); return symbol(ParserSym.L_BRACKET); }
{sim_bracketDer}		{ tokens.add(yytext() + " : R_BRACKET"); return symbol(ParserSym.R_BRACKET); }
{sim_endInstr}			{ tokens.add(yytext() + " : ENDLINE"); return symbol(ParserSym.ENDLINE); }
{sim_coma}				{ tokens.add(yytext() + " : COMMA"); return symbol(ParserSym.COMMA); }

{op_sum}			{ tokens.add(yytext() + " : ADD"); return symbol(ParserSym.ADD); }
{op_res}			{ tokens.add(yytext() + " : SUB"); return symbol(ParserSym.SUB); }
{op_mul}		    { tokens.add(yytext() + " : PROD"); return symbol(ParserSym.PROD); }
{op_div}			{ tokens.add(yytext() + " : DIV"); return symbol(ParserSym.DIV); }
{op_mod}			{ tokens.add(yytext() + " : MOD"); return symbol(ParserSym.MOD); }
{op_eq}				{ tokens.add(yytext() + " : IS_EQUAL"); return symbol(ParserSym.IS_EQUAL); }
{op_mayorEq}	    { tokens.add(yytext() + " : BEQ"); return symbol(ParserSym.BEQ); }
{op_mayor}			{ tokens.add(yytext() + " : BIGGER"); return symbol(ParserSym.BIGGER); }
{op_menorEq}		{ tokens.add(yytext() + " : LEQ"); return symbol(ParserSym.LEQ); }
{op_menor}			{ tokens.add(yytext() + " : LESSER"); return symbol(ParserSym.LESSER); }
{op_diferent}		{ tokens.add(yytext() + " : NEQ"); return symbol(ParserSym.NEQ); }
{op_porcent} // pendiente

{sim_asig}	    		{ tokens.add(yytext() + " : EQUAL"); return symbol(ParserSym.EQUAL); }
// no tenemos: {swapSym} 			{ tokens.add(yytext() + " : OP_SWAP"); return symbol(ParserSym.OP_SWAP); }

// Non-reserved words
// {character}			{ tokens.add(yytext() + " : CHARACTER"); return symbol(ParserSym.CHARACTER, yytext().charAt(1)); }

{val_real}                          { try {tokens.add(yytext() + " : DOUBLE"); return symbol(ParserSym.valor, Double.parseDouble(this.yytext()));} catch(Exception nf){return symbol(ParserSym.error);}}     
{val_entero}    		{ try {tokens.add(yytext() + " : INTEGER"); return symbol{ParserSym.INT, Integer.parseInt(yytext())};} catch(Exception nf){return symbol(ParserSym.error);} }
{val_prop}			{ tokens.add(yytext() + " : BOOLEAN"); return symbol(ParserSym.BOOLEAN, Boolean.parseBoolean(yytext())); }
// {string}			{ tokens.add(yytext() + " : STRING"); return symbol(ParserSym.STRING, yytext());}

{id}		{ tokens.add(yytext() + " : IDENTIFIER"); return symbol(ParserSym.IDENTIFIER, yytext()); }
{finLinea}  {return symbol(ParserSym.EOF)}
{espacioBlanco} { /* no hacemos nada*/}
[^]					{ errors.add(errorMessage()); System.err.println(errorMessage());
						return symbol(ParserSym.error); 
					}

/******************************************************************************************************************/