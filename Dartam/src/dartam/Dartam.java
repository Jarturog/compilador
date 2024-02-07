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
import analizadorSemantico.genCodigoIntermedio.GestorCodigoIntermedio;
import genCodigoEnsamblador.GeneradorEnsamblador;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import optimizaciones.Optimizador;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java_cup.Lexer;
import java_cup.runtime.ScannerBuffer;
import java_cup.runtime.Symbol;
import java_cup.runtime.XMLElement;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import optimizaciones.Optimizador;

public class Dartam {

    private boolean error = false;
    public static final boolean DEBUG = true;
    private static final String RUTA = "scriptsCorrectos/", RUTA_ERRORES = "scriptsErroneos/", LOG = "log.txt", EXTENSION = ".dtm";
    private final String nombreFichero;

    public static void main(String[] args) {
        String nombreArchivo;
        if (args.length > 0) {
            nombreArchivo = args[0]; // por si es ejecutado mediante la terminal
        } else {
            // Mostrar los nombres de los archivos en la ruta
            mostrarArchivos();
            nombreArchivo = elegirArchivo();
        }
        try {
            Dartam dartam = new Dartam(nombreArchivo);
            if (!dartam.error) {
                System.out.flush();
                System.out.println("Compilacion finalizada");
                System.out.println("Ficheros creados en la carpeta " + RUTA + nombreArchivo.substring(0, nombreArchivo.lastIndexOf(".")));
            }
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                System.err.println("Fichero " + nombreArchivo + " no encontrado en " + RUTA + " ni en " + RUTA_ERRORES);
                return;
            }
            e.printStackTrace();
            System.err.println("Error inesperado de compilacion: " + e.getMessage());
            //System.err.println("Error inesperado de compilacion, error detallado en "+LOG);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();
            try {
                FileWriter fileOut = new FileWriter(LOG);
                fileOut.write(stackTrace);
                fileOut.close();
            } catch (IOException ex) {
                System.err.println("No se ha podido guardar el error en el " + LOG + "\nContacta con los desarrolladores: ");
                e.printStackTrace();
            }
        }
    }

    public Dartam(String nombreArchivo) throws Exception {
        Reader ficheroIn;
        try {
            ficheroIn = new FileReader(RUTA + nombreArchivo);
        } catch(FileNotFoundException e) {
            ficheroIn = new FileReader(RUTA_ERRORES + nombreArchivo);
        }
        nombreFichero = nombreArchivo.replace(EXTENSION, "");
        // Analisis lexico
        Scanner scanner = new Scanner(ficheroIn);
        // Analisis sintactico
        Parser parser = new Parser(scanner, new ComplexSymbolFactory());
        Symbol resultado = parser.parse();
        if (!scanner.getErrores().isEmpty()) {
            System.err.println(scanner.getErrores());
            error = true;
            return;
        } else if (!parser.getErrores().isEmpty()) {
            System.err.println(parser.getErrores());
            error = true;
            return;
        }
        SymbolScript script = (SymbolScript) resultado.value;
        // Analisis semantico
        AnalizadorSemantico sem = new AnalizadorSemantico(script);
        if (!sem.getErrores().isEmpty()) {
            System.err.println(sem.getErrores());
            error = true;
            return;
        }
        File directory = new File(RUTA + nombreFichero);
        if (!directory.exists()) {
            directory.mkdirs(); // creates parent directories as needed
        }
        // si no ha habido errores empieza a escribir en los ficheros
        escribir("tokens_" + nombreFichero + ".txt", scanner.getTokens());
        escribir("symbols_" + nombreFichero + ".txt", sem.getSymbols());
        // Generacion de codigo intermedio realizada durante el analisis semantico
        GestorCodigoIntermedio generadorCodigoIntermedio = sem.getGenerador();
        GestorCodigoIntermedio generadorParaOptimizar = new GestorCodigoIntermedio(generadorCodigoIntermedio); // se copia
        escribir("codigoIntermedio_" + nombreFichero + ".txt", generadorCodigoIntermedio.toString());
        escribir("tablasVariablesProcedimientos_" + nombreFichero + ".txt", generadorCodigoIntermedio.tablas());
        // Generacion de codigo ensamblador
        
        GeneradorEnsamblador codigoEnsamblador = new GeneradorEnsamblador(nombreFichero, generadorCodigoIntermedio);
        escribir(nombreFichero + "SinOptimizaciones.X68", codigoEnsamblador.toString());
        // Optimzaciones
        
        Optimizador op = new Optimizador(generadorParaOptimizar);
        escribir("codigoOptimizado_" + nombreFichero + ".txt", op.toString());
        // Generacion de codigo ensamblador
        codigoEnsamblador = new GeneradorEnsamblador(nombreFichero, generadorParaOptimizar);
        escribir(nombreFichero + ".X68", codigoEnsamblador.toString());
    }

    public final void escribir(String fileName, String str) throws IOException {
        try (FileWriter fileOut = new FileWriter(RUTA + nombreFichero + "/" + fileName)) {
            fileOut.write(str);
        }
    }

    private static String elegirArchivo() {
        String nombreArchivo = "¡ningun archivo seleccionado!";
        System.out.println("Indica el fichero a compilar (sin indicar la ruta): ");
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
        File dir1 = new File(RUTA);
        File dir2 = new File(RUTA_ERRORES);
        File[] archivos1 = dir1.listFiles();
        File[] archivos2 = dir2.listFiles();

        if (archivos1 == null) {
            System.err.println("Error al listar archivos en la ruta " + dir1.getAbsolutePath());
            return;
        }
        if (archivos2 == null) {
            System.err.println("Error al listar archivos en la ruta " + dir2.getAbsolutePath());
            return;
        }

        String strArchvios1 = "";
        for (File archivo : archivos1) {
            if (archivo.isFile() && archivo.toString().endsWith(EXTENSION)) {
                strArchvios1 += archivo.getName() + ", ";
            }
        }
        System.out.println("Ficheros correctos en la ruta " + dir1.getAbsolutePath()
                + ": \n" + strArchvios1.substring(0, strArchvios1.length() - 2) + "\n");
        
        String strArchvios2 = "";
        for (File archivo : archivos2) {
            if (archivo.isFile() && archivo.toString().endsWith(EXTENSION)) {
                strArchvios2 += archivo.getName() + ", ";
            }
        }
        System.out.println("Ficheros erroneos en la ruta " + dir2.getAbsolutePath()
                + ": \n" + strArchvios2.substring(0, strArchvios2.length() - 2) + "\n");
    }
}
