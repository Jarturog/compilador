# Detectar sistema operativo
ifeq ($(OS),Windows_NT)
    RM = del /Q
    C = ;# CP_SEPARATOR
    B = \\# PATH_SEPARATOR
	GREEN = ^[[32m
	RESET = ^[[0m
else
    RM = rm -f
    C = :# CP_SEPARATOR
    B = /# PATH_SEPARATOR
	GREEN = \033[32m
	RESET = \033[0m
endif

# Variables
#MAIN_CLASS = Main
#CUP_DIR = #
#JFLEX_DIR = #

# Targets
compile: generate_scanner generate_parser
	@echo $(GREEN) parser compilado $(RESET)

generate_scanner:
	java -jar lib$(B)JFlex$(B)jflex-1.9.1.jar src$(B)analizadorLexico$(B)Scanner.flex

generate_parser:
	java -jar lib$(B)JavaCUP$(B)java-cup-11b.jar -parser analizadorSintactico.Parser -symbols analizadorSintactico.Yytoken -destdir src$(B)analizadorSintactico -locations src$(B)analizadorSintactico$(B)Parser.cup

clean:
	$(RM) $(CUP_DIR)$(B)*.java $(CUP_DIR)$(B)*.class $(CUP_DIR)$(B)sym.java
	$(RM) $(JFLEX_DIR)$(B)Scanner.java $(JFLEX_DIR)$(B)*.class
	$(RM) $(SOURCE_DIR)$(B)*.class


