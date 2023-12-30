/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSintactico.symbols;

/**
 *
 * @author jartu
 */
public class SymbolMiembrosTupla extends SymbolBase {
    private static int id = 0;

    public SymbolMiembrosTupla (String variable, Double valor) {
        super(variable, valor);
    }
    
}
