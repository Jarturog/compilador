package dartam;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileReader;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.SymbolFactory;
import analizadorLexico.Scanner;
import analizadorSintactico.Parser;
import analizadorSintactico.symbols.SymbolScript;
import java.io.FileWriter;
import java.io.IOException;

public class Dartam {

    private static String inputPath = "in.txt", outputPath = "out.txt";
    
    /**
    /**
     * @param args arguments de línia de comanda
     */
    public static void main(String[] args) {
        Reader input;
        // cutre lo de abajo
        args = new String[1]; args[0] = inputPath;
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
            //SymbolScript result = (SymbolScript) parser.parse().value;

            dump(outputPath, scanner.getTokens());
            System.out.println(scanner.getTokens());
            // Semantic analysis
            
            // Intermediate code generation
            
            // Machine code generation
            
            System.out.println("DONE");
            
        } catch(Exception e) {
            System.err.println("error: "+e);
            e.printStackTrace(System.err);
        }
    }

    static public void dump(String file, String content) throws IOException {
        // We write the file with the tokens
        FileWriter fileOut = new FileWriter(file);
        fileOut.write(content);
        fileOut.close();
    }
}
