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


zerodigit	= 0


// El següent codi es copiarà també, dins de la classe. És a dir, si es posa res
// ha de ser en el format adient: mètodes, atributs, etc. 
%{
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

/****************************************************************************/
%%

// Regles/accions
// És molt important l'ordre de les regles!!!

{op_sum}                    { return symbol(ParserSym.ADD);    }
{op_res}                    { return symbol(ParserSym.SUB);    }
{op_mul}                    { return symbol(ParserSym.MUL);    }
{op_div}                    { return symbol(ParserSym.DIV);    }
{op_mod}                    { return symbol(ParserSym.MOD);    }
{sym_parenIzq}                 { return symbol(ParserSym.LParen); }
{sym_parenDer}                 { return symbol(ParserSym.RParen); }

{sym_asig}                 { return symbol(ParserSym.ASSIGN); }


{id}                     { return symbol(ParserSym.ID, this.yytext()); }

{zerodigit}              { return symbol(ParserSym.valor, 0.0); }
{sub_binario}            { return symbol(ParserSym.valor, Double.valueOf(Integer.parseInt(this.yytext().substring(2, this.yytext().length()),2))); }
{sub_hex}                { return symbol(ParserSym.valor, Double.valueOf(Integer.parseInt(this.yytext().substring(2, this.yytext().length()),16))); }
{sub_octal}        { return symbol(ParserSym.valor, Double.valueOf(Integer.parseInt(this.yytext().substring(2, this.yytext().length()),8))); }
{sub_base10}                { return symbol(ParserSym.valor, Double.parseDouble(this.yytext())); }
{val_real}                   { return symbol(ParserSym.valor, Double.parseDouble(this.yytext())); }

{espacioBlanco}                     { /* No fer res amb els espais */  }
{finLinea}                { return symbol(ParserSym.EndCmd); }

[^]                      { return symbol(ParserSym.error);  }

/****************************************************************************/