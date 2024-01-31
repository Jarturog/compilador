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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import optimizaciones.Optimizador;
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
            //String [] a = {(String)(RUTA + nombreArchivo)};
            //visualizarArbol(a);
            Scanner scanner = new Scanner(ficheroIn);
            // Análisis sintáctico
            Parser parser = new Parser(scanner, new ComplexSymbolFactory());
            Symbol resultado = parser.parse();
            escribir("tokens.txt", scanner.getTokens());
            if (!scanner.getErrores().isEmpty()) {
                System.err.println(scanner.getErrores());
                return;
            } else if (!parser.getErrores().isEmpty()) {
                System.err.println(parser.getErrores());
                return;
            }
            //System.out.println(scanner.getTokens());
            SymbolScript script = (SymbolScript) resultado.value;
            // Análisis semántico
            AnalizadorSemantico sem = new AnalizadorSemantico(script);
            escribir("symbols.txt", sem.getSymbols());
            if (!sem.getErrores().isEmpty()) {
                System.err.println(sem.getErrores());
                return;
            }
            // Generación de código intermedio
            System.out.println(sem.getInstrucciones());
            escribir("codigoIntermedio.txt", sem.getInstrucciones());
            // Generación de código máquina
            //GeneradorCMaquina codigoMaquina = new GeneradorCMaquina(codigoIntermedio);
            // Optimzaciones
            //Optimizador op = new Optimizador();
        } catch (Exception e) {
            e.printStackTrace(); System.err.println("Error inesperado de compilacion: " + e.getMessage());
            //System.err.println("Error inesperado de compilacion, error detallado en "+LOG);
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
        System.out.flush();
        System.out.println("Compilacion finalizada");
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
    
    /**
     * Código sacado de https://www2.cs.tum.edu/projects/cup/examples.php#gast
     * Ahora mismo no funciona.
     * Otra fuente para visualizar el árbol: https://www.skenz.it/compilers
     * y https://www.skenz.it/compilers/install_windows
     */
    private static void visualizarArbol(String[] args) throws Exception {
        // initialize the symbol factory
      ComplexSymbolFactory csf = new ComplexSymbolFactory();
      // create a buffering scanner wrapper
      //ScannerBuffer lexer = new ScannerBuffer(new Lexer(new BufferedReader(new FileReader(args[0]))));
      ScannerBuffer lexer = new ScannerBuffer(new Lexer(csf));
      // start parsing
      Parser p = new Parser(lexer,csf);
      XMLElement e = (XMLElement)p.parse().value;
      // create XML output file 
      XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
      XMLStreamWriter sw = outFactory.createXMLStreamWriter(new FileOutputStream(args[1]));
      // dump XML output to the file
      XMLElement.dump(lexer,sw,e,"expr","stmt");

       // transform the parse tree into an AST and a rendered HTML version
      Transformer transformer = TransformerFactory.newInstance()
	    .newTransformer(new StreamSource(new File("tree.xsl")));
      Source text = new StreamSource(new File(args[1]));
      transformer.transform(text, new StreamResult(new File("output.xml")));
      transformer = TransformerFactory.newInstance()
	    .newTransformer(new StreamSource(new File("tree-view.xsl")));
      text = new StreamSource(new File("output.xml"));
      transformer.transform(text, new StreamResult(new File("ast.html")));
    }
}
