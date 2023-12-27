/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package dartam;

import analizadorLexico.Scanner;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileReader;
import java_cup.runtime.*;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.SymbolFactory;


/**
 *
 * @author dasad
 */
public class Dartam {

   public static void main(String[] args) {
        Reader input;
        
        try {
            
            //Leeremos el documento ejemplo.txt que contiene varioss valores
            input = new FileReader("ejemplo.txt");
            Scanner scanner = new Scanner(input);
            
        } catch(Exception e) {
            System.err.println("error: "+e);
            e.printStackTrace(System.err);
        }
    }
}
