import java.io.*;
%%
// Declaracions

digit       =[0-9]
digits      ={digit}+
letter      =[A-Za-z]
id          ={letter}({letter}|{digit})*
signo       =[+-]? 
base10       =signo[0|[1-9]{digito}*]
binario     =0b[01]+
octal       =0o[0-7]+
hex         =0x[A-Fa-f0-9]+
entero     [{base10}{binario}{octal}{hex}]
real    {base10}?\.{digitos}?([Ee]{ent10})?
// Símbolos

parenIzq	= \(
parenDer	= \)
laveIzq		= \{
llaveDer 	= \}
bracketIzq	= \[
bracketDer	= \]
endInstr    = ;
asig        = :
coma	    = ,

//Operadores

eq          = \=
diferent    = \/\=
mayor       = \>
menor       = \<
mayorEq     = \>\=
menorEq     = \<\=
Sum         = \+
Res         = \-
Mult        =\*
Div         = \/
Porcent     = \%
Or          = \|
And         = &
Mod         = \\
Neg         = ¬


//Palabras reservadas

kwChar      = "car"
kwInt       = "ent"
kwDouble    = "real"
kwBool      = "prop"
kwConst     = "inmut"  //inmutable
kwVoid      = "vacio"

kwMain      = "inicio"
kwIfO       = "si"
kwElif      = "sino"
kwElse      = "no"
kwSwitch    = "selec"
kwWhileFor  = "loop"
kwReturn    = "pop" "-|"
kwTrue      = "cierto"
kwFalse     = "falso"

comentarLinea   = "//"
comentarBloqueI=  "#"
comentarBloqueF  = "#"



%public // Per indicar que la classe és pública
%class Prova // El nom de la classe
%int // El tipus dels tokens identificats

// El següent codi es copiarà també, dins de la classe. És a dir, si es posa res
// ha de ser en el format adient: mètodes, atributs, etc.

%{
    public static void main(String []args) {
        if (args.length < 1) {
            System.err.println("Indica un fitxer amb les dades d'entrada");
            System.exit(0);
        }
        try {
            FileReader in = new FileReader(args[0]);
            Prova parser = new Prova(in);
            parser.yylex(); // <- El mètode d'invocació per començar
                            // a parsejar el document
        } catch (FileNotFoundException e) {
            System.err.println("El fitxer d'entrada '"+args[0]+"' no existeix");
        } catch (IOException e) {
            System.err.println("Error processant el fitxer d'entrada");
        }
    }
%}
%%

// Regles/accions
{digits} { System.out.println("He llegit el valor "+yytext()); }
{id} { System.out.println("He llegit l’identificador "+yytext()); }