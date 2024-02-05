package optimizaciones;

import analizadorSemantico.genCodigoIntermedio.Instruccion;
import analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr;
import analizadorSemantico.genCodigoIntermedio.Operador;
import java.util.ArrayList;

public class Optimizador {

    private final ArrayList<Instruccion> codigo;

    public Optimizador(ArrayList<Instruccion> codigo) throws Exception {
        this.codigo = codigo;
        while(ba() || bsb()); // se realiza la optimización hasta que no hayan cambios
    }

    //checked
    private boolean ba() throws Exception {
        boolean cambio = false;
        for (int i = 0; i + 2 < codigo.size(); i++) {
            Instruccion instr1 = codigo.get(i);
            TipoInstr tipo = instr1.getTipo();
            if (!tipo.isCondGOTO()) {
                continue;
            }
            //guardo la etiqueta
            Operador e1 = instr1.dst();
            int posIf = i;
            Instruccion instr2 = codigo.get(i + 1);
            if (instr2.getTipo() != TipoInstr.GOTO) {
                continue;
            }
            //guardamos la etiqueta del goto
            Operador e2 = instr2.dst();
            int posGoto = i;
            Instruccion instr3 = codigo.get(i + 2);
            if (instr3.getTipo() != TipoInstr.SKIP || !instr3.dst().toString().equals(e1.toString())) { //e1 : skip
                continue;
            }
            TipoInstr contrario = codigo.get(posIf).getTipo().getContrario();
            codigo.get(posIf).setTipo(contrario);
            codigo.get(posIf).setDst(e2);
            codigo.remove(posGoto);
            cambio = true;
            //miramos si podemos borrar el e1:skip
            boolean borrar = true;
            for (int j = 0; j < codigo.size(); j++) {
                if ((codigo.get(j).getTipo().isCondGOTO() || codigo.get(j).getTipo() == TipoInstr.GOTO) && codigo.get(j).dst().toString().equals(e1.toString())) {
                    borrar = false;
                    break;
                }
            }
            if (borrar) {
                //el skip e1 ahora estará en la posición del goto porque como hemos borrado el goto, todas las
                //instrucciones por delante del goto han retrocedido una posición.
                codigo.remove(posGoto);
            }
        }
        return cambio;
    }

    //checked
    private boolean bsb() {
        boolean cambio = false;
        for (int i = 0; i < codigo.size(); i++) {
            if (!codigo.get(i).getTipo().isCondGOTO()) {
                continue;
            }
            int posIf = i;
            Operador e1 = codigo.get(i).dst();
            for (int j = i + 1; j < codigo.size(); j++) {
                if (!(codigo.get(j).getTipo() == TipoInstr.SKIP && codigo.get(j).dst().toString().equals(e1.toString())) && !(j + 1 < codigo.size() && codigo.get(j + 1).getTipo() == TipoInstr.GOTO)) {
                    continue;
                }
                codigo.get(posIf).setDst(codigo.get(j + 1).dst());
                boolean borrar = true;
                for (int k = 0; k < codigo.size(); k++) {
                    if ((codigo.get(k).getTipo().isCondGOTO() || codigo.get(k).getTipo() == TipoInstr.GOTO) && codigo.get(k).dst().toString().equals(e1.toString())) {
                        borrar = false;
                        break;
                    }
                }
                if (borrar) {
                    cambio = true;
                    codigo.remove(j);
                }
            }
        }
        return cambio;
    }
    
}
