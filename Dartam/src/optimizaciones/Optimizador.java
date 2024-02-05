package optimizaciones;

import analizadorSemantico.genCodigoIntermedio.Generador3Direcciones;
import analizadorSemantico.genCodigoIntermedio.Instruccion;
import analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr;
import analizadorSemantico.genCodigoIntermedio.Operador;
import genCodigoEnsamblador.VData;
import java.util.ArrayList;
import java.util.HashMap;

public class Optimizador {

    private final ArrayList<Instruccion> instrucciones;
    private final HashMap<String, VData> variables;

    public Optimizador(Generador3Direcciones g3d) throws Exception {
        instrucciones = g3d.getInstrucciones();
        variables = g3d.getTablaVariables();
        while (saltosAdyacentes() || saltoSobreSalto() || asignacionesDiferidas()); // se realiza la optimización hasta que no hayan cambios
    }

    private boolean asignacionesDiferidas() {
        boolean cambio = false;
        for (int i = 0; i + 1 < instrucciones.size(); i++) {
            TipoInstr t1 = instrucciones.get(i).getTipo(), t2 = instrucciones.get(i + 1).getTipo();
            if (!t1.isTipo(TipoInstr.COPY) || !t2.isTipo(TipoInstr.COPY)) {
                continue;
            }
            Operador var = instrucciones.get(i).dst();
            String nombre = var.getNombre();
            boolean seRepite = false;
            Operador segundaAparicionVar = instrucciones.get(i + 1).op1();
            if (segundaAparicionVar.getNombre() == null || nombre == null || !segundaAparicionVar.getNombre().equals(nombre)) {
                continue;
            }
            for (int j = i + 2; j < instrucciones.size(); j++) {
                Instruccion instr = instrucciones.get(j);
                if ((instr.op1() != null && instr.op1().getNombre() != null && instr.op1().getNombre().equals(nombre))
                        || (instr.op2() != null && instr.op2().getNombre() != null && instr.op2().getNombre().equals(nombre))
                        || (instr.dst() != null && instr.dst().getNombre() != null && instr.dst().getNombre().equals(nombre))) {
                    seRepite = true;
                    break;
                }
            }
            if (!seRepite) {
                String nuevaVar = instrucciones.get(i + 1).dst().getNombre();
                instrucciones.get(i + 1).setOp1(instrucciones.remove(i).op1());
                VData datosAntiguos = variables.remove(nombre);
                VData nuevosDatos = variables.get(nuevaVar);
                nuevosDatos.sustituirPor(datosAntiguos);
                cambio = true;
            }
        }
        return cambio;
    }
    
    private boolean saltosAdyacentes() throws Exception {
        boolean cambio = false;
        for (int i = 0; i + 2 < instrucciones.size(); i++) {
            Instruccion instr1 = instrucciones.get(i);
            TipoInstr tipo = instr1.getTipo();
            if (!tipo.isCondGOTO()) {
                continue;
            }
            Instruccion instr2 = instrucciones.get(i + 1);
            if (instr2.getTipo() != TipoInstr.GOTO) {
                continue;
            }
            Instruccion instr3 = instrucciones.get(i + 2);
            if (!instr3.dst().toString().equals(instr1.dst().toString()) || !instr3.getTipo().isTipo(TipoInstr.SKIP)) {
                continue;
            }
            TipoInstr contrario = instrucciones.get(i).getTipo().getContrario();
            instrucciones.get(i).setTipo(contrario);
            instrucciones.get(i).setDst(instr2.dst());
            instrucciones.remove(i + 1);
            cambio = true;
            boolean borrar = true;
            for (int j = 0; j < instrucciones.size(); j++) {
                if (instrucciones.get(j).dst().toString().equals(instr1.dst().toString())
                        && (instrucciones.get(j).getTipo().isCondGOTO() || instrucciones.get(j).isTipo(TipoInstr.GOTO))) {
                    borrar = false;
                    break;
                }
            }
            if (borrar) {
                instrucciones.remove(i + 1);
            }
        }
        return cambio;
    }

    private boolean saltoSobreSalto() {
        boolean cambio = false;
        for (int i = 0; i < instrucciones.size(); i++) {
            if (!instrucciones.get(i).getTipo().isCondGOTO()) {
                continue;
            }
            String etiqueta = instrucciones.get(i).dst().getNombre();
            for (int j = i + 1; j + 1 < instrucciones.size(); j++) {
                String etiqueta2 = instrucciones.get(j).dst().getNombre();
                if (!instrucciones.get(j + 1).isTipo(TipoInstr.GOTO)
                        && (!instrucciones.get(j).isTipo(TipoInstr.SKIP) || !etiqueta2.equals(etiqueta))) {
                    continue;
                }
                instrucciones.get(i).setDst(instrucciones.get(j + 1).dst());
                boolean eliminar = true;
                for (int k = 0; k < instrucciones.size(); k++) {
                    etiqueta2 = instrucciones.get(k).dst().toString();
                    if (etiqueta2.equals(etiqueta)
                            && (instrucciones.get(k).getTipo().isCondGOTO() || instrucciones.get(k).isTipo(TipoInstr.GOTO))) {
                        eliminar = false;
                        break;
                    }
                }
                if (eliminar) {
                    cambio = true;
                    instrucciones.remove(j);
                }
            }
        }
        return cambio;
    }

    @Override
    public String toString() {
        String s = "";
        for (Instruccion i : instrucciones) {
            s += i + "\n";
        }
        return s;
    }

}
