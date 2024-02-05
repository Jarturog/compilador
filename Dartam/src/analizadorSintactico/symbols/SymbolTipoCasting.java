/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package analizadorSintactico.symbols;

import analizadorSintactico.ParserSym;
import java_cup.runtime.ComplexSymbolFactory;

/**
 * Clase símbolo de mentira, para englobar TipoPrimitivo y StringCasting
 * @author jartu
 */
public abstract class SymbolTipoCasting extends SymbolBase {

    public SymbolTipoCasting(String name) {
        super(name);
    }

    public SymbolTipoCasting(String name, Object value, ComplexSymbolFactory.Location left, ComplexSymbolFactory.Location right) {
        super(name, value, left, right);
    }

    public abstract String getTipo();

}

class SymbolStringCasting extends SymbolTipoCasting {

    public SymbolStringCasting(String name) {
        super(name);
    }

    @Override
    public String getTipo() {
        return ParserSym.terminalNames[ParserSym.CAR] + " []";
    }

}