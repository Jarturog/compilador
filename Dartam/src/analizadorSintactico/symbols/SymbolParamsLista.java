/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

/**
 *
 * @author jartu
 */
public class SymbolParamsLista extends ComplexSymbol {
    private static int id = 0;

    public SymbolParamsLista(String variable, Double valor) {
        super(variable, id++, valor);
    }
    
}
