package dartam;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileReader;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.SymbolFactory;
import analizadorLexico.Scanner;
import analizadorSintactico.Parser;

public class Dartam {

    /**
    /**
     * @param args arguments de línia de comanda
     */
    public static void main(String[] args) {
        Reader input;
        
        try {
            if (args.length > 0) {
                input = new FileReader(args[0]);
            } else {
                System.out.println("Escriu l'expressió que vols calcular (help; per ajuda):");
                System.out.print(">>> ");
                input = new InputStreamReader(System.in);
            }
            
            Scanner scanner = new Scanner(input);
        
            SymbolFactory sf = new ComplexSymbolFactory();
            Parser parser = new Parser(scanner, sf);
            parser.parse();
        } catch(Exception e) {
            System.err.println("error: "+e);
            e.printStackTrace(System.err);
        }
    }
}
