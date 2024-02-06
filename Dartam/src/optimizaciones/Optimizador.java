package optimizaciones;

import analizadorSemantico.genCodigoIntermedio.GeneradorCodigoIntermedio;
import analizadorSemantico.genCodigoIntermedio.Instruccion;
import analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr;
import analizadorSemantico.genCodigoIntermedio.Operador;
import genCodigoEnsamblador.VData;
import java.util.ArrayList;
import java.util.HashMap;

public class Optimizador {

    private final ArrayList<Instruccion> instrucciones;
    private final HashMap<String, VData> variables;

    public Optimizador(GeneradorCodigoIntermedio g3d) throws Exception {
        instrucciones = g3d.getInstrucciones();
        variables = g3d.getTablaVariables();
        while (saltosAdyacentes() || saltoSobreSalto() || asignacionesDiferidas() || etiquetasSinUsarse()); // se realiza la optimización hasta que no hayan cambios
    }

    private boolean asignacionesDiferidas() {
        boolean cambio = false;
        for (int i = 0; i < instrucciones.size(); i++) {
            TipoInstr t1 = instrucciones.get(i).getTipo();
            if (!t1.isTipo(TipoInstr.COPY)) { // añado la opción de IND_ASS?
                continue;
            }
            Operador var = instrucciones.get(i).dst();
            String nombre = var.getNombre();
            if (nombre == null) {
                continue;
            }
            for (int j = i + 1; j < instrucciones.size(); j++) {
                TipoInstr t2 = instrucciones.get(j).getTipo();
                if (!t2.isTipo(TipoInstr.COPY)) {
                    continue;
                }
                boolean seRepite = false;
                Operador segundaAparicionVar = instrucciones.get(j).op1();
                if (segundaAparicionVar.getNombre() == null || !segundaAparicionVar.getNombre().equals(nombre)) {
                    continue;
                }
                for (int k = j + 1; k < instrucciones.size(); k++) {
                    Instruccion instr = instrucciones.get(k);
                    if ((instr.op1() != null && instr.op1().getNombre() != null && instr.op1().getNombre().equals(nombre))
                            || (instr.op2() != null && instr.op2().getNombre() != null && instr.op2().getNombre().equals(nombre))
                            || (instr.dst() != null && instr.dst().getNombre() != null && instr.dst().getNombre().equals(nombre))) {
                        seRepite = true;
                        break;
                    }
                }
                if (!seRepite) {
                    String nuevaVar = instrucciones.get(j).dst().getNombre();
                    instrucciones.get(j).setOp1(instrucciones.remove(i).op1());
                    i--; // resto 1 por la eliminación
                    VData datosAntiguos = variables.remove(nombre);
                    VData nuevosDatos = variables.get(nuevaVar);
                    nuevosDatos.sustituirPor(datosAntiguos);
                    cambio = true;
                    break;
                }
            }
        }
        return cambio;
    }

    private boolean saltosAdyacentes() throws Exception {
        boolean cambio = false;
        for (int i = 0; i + 2 < instrucciones.size(); i++) {
            Instruccion cond = instrucciones.get(i);
            Instruccion saltoGoto = instrucciones.get(i + 1);
            Instruccion skip = instrucciones.get(i + 2);
            if (!cond.getTipo().isCondGOTO() || !saltoGoto.isTipo(TipoInstr.GOTO) || !skip.isTipo(TipoInstr.SKIP)
                    || !cond.dst().getNombre().equals(skip.dst().getNombre())
                    || cond.dst().getNombre().equals(saltoGoto.dst().getNombre())
                    || skip.dst().getNombre().equals(saltoGoto.dst().getNombre())) {
                continue;
            }
            for (int j = i + 3; j < instrucciones.size(); j++) {
                Instruccion skip2 = instrucciones.get(j);
                if (!skip2.isTipo(TipoInstr.SKIP) || !skip2.dst().getNombre().equals(saltoGoto.dst().getNombre())) {
                    continue;
                }
                cond.setTipo(cond.getTipo().getContrario());
                cond.setDst(skip2.dst());
                instrucciones.remove(i + 1);
                cambio = true;
                break;
            }
        }
        return cambio;
    }

    private boolean saltoSobreSalto() {
        boolean cambio = false;
        for (int i = 0; i < instrucciones.size(); i++) {
            Instruccion salto = instrucciones.get(i);
            if (!salto.getTipo().isCondGOTO() && !salto.isTipo(TipoInstr.GOTO)) {
                continue;
            }
            for (int j = i + 1; j + 1 < instrucciones.size(); j++) {
                Instruccion skip = instrucciones.get(j);
                Instruccion saltoGoto = instrucciones.get(j + 1);
                if (!saltoGoto.isTipo(TipoInstr.GOTO) || !skip.isTipo(TipoInstr.SKIP)
                        || !saltoGoto.dst().getNombre().equals(salto.dst().getNombre())
                        || saltoGoto.dst().getNombre().equals(skip.dst().getNombre())
                        || salto.dst().getNombre().equals(skip.dst().getNombre())) {
                    continue;
                }
                salto.setDst(saltoGoto.dst());
                cambio = true;
            }
        }
        return cambio;
    }

    private boolean etiquetasSinUsarse() {
        boolean cambio = false;
        for (int i = 0; i < instrucciones.size(); i++) {
            Instruccion skip = instrucciones.get(i);
            if (!skip.isTipo(TipoInstr.SKIP)) {
                continue;
            }
            boolean seUsa = false;
            for (int j = 0; !seUsa && j < instrucciones.size(); j++) {
                Instruccion instr = instrucciones.get(j);
                if (j == i || (!instr.isTipo(TipoInstr.GOTO)
                        && !instr.getTipo().isCondGOTO()
                        && !instr.isTipo(TipoInstr.CALL)
                        && !instr.isTipo(TipoInstr.RETURN)
                        && !instr.isTipo(TipoInstr.PMB))) {
                    continue;
                }
                seUsa = skip.dst().getNombre().equals(instr.dst().getNombre());
            }
            if (!seUsa) {
                instrucciones.remove(i);
                i--; // resto uno porque todos se han movido hacia la izquierda y en la siguiente iteración volverá a sumar 1
                cambio = true;
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
