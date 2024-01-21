/** 
* Assignatura 21780 - Compiladors
* Estudis: Grau en Enginyeria Informàtica 
* Itinerari: Intel·ligència Artificial i Computacio
*
* Equipo: Arturo, Dani y Marta
*/
package dartam;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileReader;
import java_cup.runtime.ComplexSymbolFactory;
import analizadorLexico.Scanner;
import analizadorSintactico.Parser;
import analizadorSintactico.symbols.SymbolScript;
import analizadorSemantico.AnalizadorSemantico;
import genCodigoIntermedio.GeneradorCIntermedio;
import genCodigoMaquina.GeneradorCMaquina;
import java.io.BufferedReader;
import optimizaciones.Optimizador;
import java.io.FileWriter;
import java.io.IOException;

public class Dartam {

    private static final String RUTA = "scripts/";
    
    public static void main(String[] args) {
        try {
            Reader ficheroIn;
            String nombreArchivo = "";
            if (args.length > 0) {
                nombreArchivo = args[0];
            } else {
                System.out.println("Indica el fichero a compilar");
                System.out.print(">>> ");
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    nombreArchivo = br.readLine();
                    br.close();
                } catch (IOException e) {
                    System.err.println("Error al abrir el fichero '" + nombreArchivo + "'");
                    return;
                }
            }
            ficheroIn = new FileReader(RUTA + nombreArchivo);
            // Análisis léxico
            Scanner scanner = new Scanner(ficheroIn);
            // Análisis sintáctico
            Parser parser = new Parser(scanner, new ComplexSymbolFactory());
            SymbolScript script = (SymbolScript) parser.parse().value;
            escribir("tokens.txt", scanner.getTokens());
            System.out.println(scanner.getTokens());
            // Análisis semántico
            AnalizadorSemantico sem = new AnalizadorSemantico(script);
            System.err.println(sem.getErrors());
            // Generación de código intermedio
            //GeneradorCIntermedio codigoIntermedio = new GeneradorCIntermedio(script);
            //escribir("codigoIntermedio.txt", codigoIntermedio.getCodigo());
            // Generación de código máquina
            //GeneradorCMaquina codigoMaquina = new GeneradorCMaquina(codigoIntermedio);
            // Optimzaciones
            //Optimizador op = new Optimizador();
            
            System.out.println("Codigo compilado");
            
        } catch(Exception e) {
            System.err.println("Error inesperado de compilación: "+e.getMessage());
        }
    }

    static public void escribir(String fileName, String str) throws IOException {
        FileWriter fileOut = new FileWriter(fileName);
        fileOut.write(str);
        fileOut.close();
    }

}
