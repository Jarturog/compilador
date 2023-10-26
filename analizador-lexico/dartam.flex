import java.io.*;
%%
// Declaracions

digit [0-9]
digits {digit}+
letter [A-Za-z]
id {letter}({letter}|{digit})*

// Símbolos

ParenIzq	= \(
ParenDer	= \)
LaveIzq		= \{
LlaveDer 	= \}
BracketIzq	= \[
BracketDer	= \]
endInstr    = ;
asig        = :
coma	    = ,

//Operadores

Eq          = \=
Mayor       = \>
Menor       = \<
MayorEq     = \>\=
MenorEq     = \<\=
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
kwVoid      = "void"

kwMain      = "main"

kwIfO       = "si"
kwElif      = "sino"
kwElse      = "no"
kwSwitch    = "selec"
kwWhileFor  = "loop"
kwReturn    = "pop" "-|"
kwTrue      = ""
kwFalse     = ""



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