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

id		= [A-Za-z_][A-Za-z0-9_]*

digit19		= [1-9]
digit10		= [0-9]
digit2		= [0-1]
digit8		= [0-7]
digit16		= [0-9A-Fa-f]
zerodigit	= 0
tagbinari	= 0b
taghexa		= 0x
tagoctal	= 0o
tagexponent	= [eE]

signe		= [\+|\-]?
punt		= \.

decimal		= {digit19}{digit10}*
binari		= {digit2}+
octal		= {digit8}+
hexadecimal	= {digit16}+

realdigits1	= {digit10}+{punt}?{digit10}* 
realdigits2	= {digit10}*{punt}?{digit10}+
realdigits	= ({realdigits1}|{realdigits2})
exponent	= {tagexponent}{signe}{realdigits}
real		= {realdigits}{exponent}?

add          = \+
sub          = \-
mul          = \*
div          = \/
mod          = %
lparen       = \(
rparen       = \)

assign       = \=

lletraA      = ['A'|'a']
lletraE      = ['E'|'e']
lletraH      = ['H'|'h']
lletraI      = ['I'|'i']
lletraL      = ['L'|'l']
lletraN      = ['N'|'n']
lletraP      = ['P'|'p']
lletraR      = ['R'|'r']
lletraS      = ['S'|'s']
lletraT      = ['T'|'t']
lletraV      = ['V'|'v']
lletraX      = ['X'|'x']

invcmd       = {lletraI}{lletraN}{lletraV}
helpcmd      = {lletraH}{lletraE}{lletraL}{lletraP}
quitcmd      = {lletraE}{lletraX}{lletraI}{lletraT}
varscmd      = {lletraV}{lletraA}{lletraR}{lletraS}

anscmd       = {lletraA}{lletraN}{lletraS}

ws           = [' '|'\t']+
endline      = ['\r'|'\n'|"\r\n"]+

cmdLineEnd   = ['\r'|'\n'|"\r\n"]*;


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

{add}                    { return symbol(ParserSym.ADD);    }
{sub}                    { return symbol(ParserSym.SUB);    }
{mul}                    { return symbol(ParserSym.MUL);    }
{div}                    { return symbol(ParserSym.DIV);    }
{mod}                    { return symbol(ParserSym.MOD);    }
{lparen}                 { return symbol(ParserSym.LParen); }
{rparen}                 { return symbol(ParserSym.RParen); }

{assign}                 { return symbol(ParserSym.ASSIGN); }

{invcmd}                 { return symbol(ParserSym.INV);       }

{helpcmd}                { return symbol(ParserSym.HELPCMD);   }
{quitcmd}                { return symbol(ParserSym.QUITCMD);   }
{varscmd}                { return symbol(ParserSym.DUMPVARS);  }

{anscmd}                 { return symbol(ParserSym.ANS);       }

{id}                     { return symbol(ParserSym.ID, this.yytext()); }

{zerodigit}              { return symbol(ParserSym.valor, 0.0); }
{tagbinari}{binari}      { return symbol(ParserSym.valor, Double.valueOf(Integer.parseInt(this.yytext().substring(2, this.yytext().length()),2))); }
{taghexa}{hexadecimal}   { return symbol(ParserSym.valor, Double.valueOf(Integer.parseInt(this.yytext().substring(2, this.yytext().length()),16))); }
{tagoctal}{octal}        { return symbol(ParserSym.valor, Double.valueOf(Integer.parseInt(this.yytext().substring(2, this.yytext().length()),8))); }
{decimal}                { return symbol(ParserSym.valor, Double.parseDouble(this.yytext())); }
{real}                   { return symbol(ParserSym.valor, Double.parseDouble(this.yytext())); }

{cmdLineEnd}             { return symbol(ParserSym.EndCmdInteractive); }

{ws}                     { /* No fer res amb els espais */  }
{endline}                { return symbol(ParserSym.EndCmd); }

[^]                      { return symbol(ParserSym.error);  }

/****************************************************************************/