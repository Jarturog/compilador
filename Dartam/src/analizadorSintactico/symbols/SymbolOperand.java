
/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Informàtica 
 * Itinerari: Intel·ligència Artificial i Computació
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import java_cup.runtime.ComplexSymbolFactory.Location;



/**
 * Classe que implementa la classe base a partir de la que s'implementen totes
 * les variables de la gramàtica.
 * 
 * Bàsicament conté un valor enter
 */
public class SymbolOperand extends SymbolBase {
    

    public SymbolOperand(String variable, Double valor) {
        super(variable);
    }

    public SymbolOperand(SymbolBinaryExpression et) {
    super("");}

    public SymbolOperand(SymbolOperand arr, SymbolOperand idx) {
    super("");}

    public SymbolOperand(SymbolOperand tuple, String member) {
    super("");}

    public SymbolOperand(SymbolUnaryExpression et) {
    super("");}

    public SymbolOperand(SymbolOperand et) {
    super("");}

    public SymbolOperand(SymbolFCall et) {
    super("");}

    public SymbolOperand(SymbolAtomicExpression et) {
    super("");}

    public SymbolOperand(SymbolConditionalExpression et) {
    super("");}
    
}
