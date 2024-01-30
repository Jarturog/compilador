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
import genCodigoEnsamblador.GeneradorEnsamblador;
import java.io.BufferedReader;
import java.io.File;
import optimizaciones.Optimizador;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Dartam {

    private static final String RUTA = "scripts/", LOG = "log.txt", EXTENSION = ".dtm";

    public static void main(String[] args) {
        try {
            Reader ficheroIn;
            String nombreArchivo;
            if (args.length > 0) {
                nombreArchivo = args[0];
            } else {
                // Mostrar los nombres de los archivos en la ruta
                mostrarArchivos();
                nombreArchivo = elegirArchivo();
            }
            ficheroIn = new FileReader(RUTA + nombreArchivo);
            // Análisis léxico
            Scanner scanner = new Scanner(ficheroIn);
            // Análisis sintáctico
            Parser parser = new Parser(scanner, new ComplexSymbolFactory());
            Object resultado = parser.parse().value;
            escribir("tokens.txt", scanner.getTokens());
            if (!scanner.getErrores().isEmpty()) {
                System.err.println(scanner.getErrores());
                return;
            } else if (!parser.getErrores().isEmpty()) {
                System.err.println(parser.getErrores());
                return;
            }
            System.out.println(scanner.getTokens());
            SymbolScript script = (SymbolScript) resultado;
            // Análisis semántico
            AnalizadorSemantico sem = new AnalizadorSemantico(script);
            escribir("symbols.txt", sem.getSymbols());
            System.err.println(sem.getErrores());
            // Generación de código intermedio
            //GeneradorCIntermedio codigoIntermedio = new GeneradorCIntermedio(script);
            //escribir("codigoIntermedio.txt", codigoIntermedio.getCodigo());
            // Generación de código máquina
            //GeneradorCMaquina codigoMaquina = new GeneradorCMaquina(codigoIntermedio);
            // Optimzaciones
            //Optimizador op = new Optimizador();

            System.out.println("Codigo compilado");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error inesperado de compilacion: " + e.getMessage());//System.err.println("Error inesperado de compilacion, error detallado en "+LOG);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();
            try {
                escribir(LOG, stackTrace);
            } catch (IOException ex) {
                System.err.println("No se ha podido guardar el error en el " + LOG + "\nContacta con los desarrolladores: ");
                e.printStackTrace();
            }
        }
    }

    static public void escribir(String fileName, String str) throws IOException {
        try (FileWriter fileOut = new FileWriter(fileName)) {
            fileOut.write(str);
        }
    }

    private static String elegirArchivo() {
        String nombreArchivo = "¡ningún archivo seleccionado!";
        System.out.println("Indica el fichero a compilar: ");
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                nombreArchivo = br.readLine();
            }
            if (!nombreArchivo.contains(".")) {
                nombreArchivo += EXTENSION;
            }
        } catch (IOException e) {
            System.err.println("Error al abrir el fichero '" + nombreArchivo + "'");
        }
        return nombreArchivo;
    }

    private static void mostrarArchivos() {
        File directorio = new File(RUTA);
        File[] archivos = directorio.listFiles();

        if (archivos == null) {
            System.err.println("Error al listar archivos en la ruta " + directorio.getAbsolutePath());
            return;
        }

        String strArchvios = "";
        for (File archivo : archivos) {
            if (archivo.isFile() && archivo.toString().endsWith(EXTENSION)) {
                strArchvios += archivo.getName() + ", ";
            }
        }
        System.out.println("Archivos en la ruta " + directorio.getAbsolutePath() + ": \n" + strArchvios.substring(0, strArchvios.length() - 2) + "\n");
    }
}
