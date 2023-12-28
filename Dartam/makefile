# Detectar sistema operativo
ifeq ($(OS),Windows_NT)
    RM = del /Q
    C = ;# CP_SEPARATOR
    B = \\# PATH_SEPARATOR
else
    RM = rm -f
    C = :# CP_SEPARATOR
    B = /# PATH_SEPARATOR
endif

# Variables
#MAIN_CLASS = Main
#CUP_DIR = #
#JFLEX_DIR = #

# Targets
compile: generate_scanner generate_parser
	@echo parser compilado

generate_scanner:
	java -jar lib\JFlex\jflex-1.9.1.jar src\analizadorLexico\Scanner.flex

generate_parser:
	java -jar lib\JavaCUP\java-cup-11b.jar -parser analizadorSintactico.Parser -symbols analizadorSintactico.Yytoken -destdir src\analizadorSintactico src\analizadorSintactico\Parser.cup

clean:
	$(RM) $(CUP_DIR)$(B)*.java $(CUP_DIR)$(B)*.class $(CUP_DIR)$(B)sym.java
	$(RM) $(JFLEX_DIR)$(B)Scanner.java $(JFLEX_DIR)$(B)*.class
	$(RM) $(SOURCE_DIR)$(B)*.class


