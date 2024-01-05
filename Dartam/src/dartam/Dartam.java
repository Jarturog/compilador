package dartam;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileReader;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.SymbolFactory;
import analizadorLexico.Scanner;
import analizadorSintactico.Parser;
import analizadorSintactico.symbols.SymbolScript;
import analizadorSemantico.SemanticAnalysis;
import genCodigoIntermedio.GeneradorCIntermedio;
import genCodigoMaquina.GeneradorCMaquina;
import optimizaciones.Optimizador;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ClassCastException;

public class Dartam {

    private static String inputPath = "in.txt", outputPath = "out.txt";
    
    /**
    /**
     * @param args arguments de linia de comanda
     */
    public static void main(String[] args) {
        Reader input;
        // cutre lo de abajo
        args = new String[1];
        args[0] = inputPath;
        try {
            if (args.length > 0) {
                input = new FileReader(args[0]);
            } else {
                System.out.println("Escriu l'expressio que vols calcular (help; per ajuda):");
                System.out.print(">>> ");
                input = new InputStreamReader(System.in);
            }
            Scanner scanner = new Scanner(input);
            
            // sintactico
            Parser parser = new Parser(scanner, new ComplexSymbolFactory());
            SymbolScript script;
            try {
                script = (SymbolScript) parser.parse().value;
            } catch (NullPointerException | ClassCastException e) { // analisis sintactico ha ido mal o no se ha encontrado main
                System.err.println("error: "+e);
                e.printStackTrace(System.err);
                return;
            }
            
            //dump(outputPath, scanner.getTokens());
            System.out.println(scanner.getTokens());
            // Semantic analysis
            SemanticAnalysis sem = new SemanticAnalysis(script);
            System.err.println(sem.getErrors());
            // Intermediate code generation
            GeneradorCIntermedio codigoIntermedio = new GeneradorCIntermedio(script);
            // Machine code generation
            GeneradorCMaquina codigoMaquina = new GeneradorCMaquina(codigoIntermedio);
            // Optimzaciones
            Optimizador op = new Optimizador(); // pasar por parametro codigomaQUINA
            
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
