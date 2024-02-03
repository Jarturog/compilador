package optimizaciones;

import analizadorSemantico.genCodigoIntermedio.Instruccion;
import analizadorSemantico.genCodigoIntermedio.Instruccion.TipoInstr;
import analizadorSemantico.genCodigoIntermedio.Operador;
import java.util.ArrayList;

public class Optimizador {

    private final ArrayList<Instruccion> codigo;

    public Optimizador(ArrayList<Instruccion> codigo) {
        this.codigo = codigo;
    }
//
//    //checked
//    public boolean brancamentAdjacent() {
//        boolean canvi = false;
//        for (int i = 0; i + 2 < codigo.size(); i++) {
//            Instruccion instr1 = codigo.get(i);
//            TipoInstr tipo = instr1.tipo();
//            if (!tipo.isCondGOTO()) {
//                continue;
//            }
//            //guardo la etiqueta
//            Operador e1 = instr1.operadores()[2];
//            int posIf = i;
//            Instruccion instr2 = codigo.get(i + 1);
//            if (!(i < codigo.size() && instr2.tipo() == TipoInstr.GOTO)) {
//                continue;
//            }
//            //guardamos la etiqueta del goto
//            Operador e2 = instr2.operadores()[2];
//            int posGoto = i;
//            Instruccion instr3 = codigo.get(i + 2);
//            if (!(instr3.tipo() == TipoInstr.SKIP && instr3.operadores()[2].getLabel().equals(e1.getLabel()))) { //e1 : skip
//                continue;
//            }
//            codigo.get(posIf).setTipoInstr(negCond(codigo.get(posIf).tipo()));
//            codigo.get(posIf).setOperator(2, e2);
//            codigo.remove(posGoto);
//            canvi = true;
//            //miramos si podemos borrar el e1:skip
//            boolean borrar = true;
//            for (int j = 0; j < codigo.size(); j++) {
//                if ((codigo.get(j).tipo().isCondGOTO() || codigo.get(j).tipo() == TipoInstr.GOTO) && codigo.get(j).getOperators()[2].getLabel().equals(e1.getLabel())) {
//                    borrar = false;
//                    break;
//                }
//            }
//            if (borrar) {
//                //el skip e1 ahora estará en la posición del goto porque como hemos borrado el goto, todas las
//                //instrucciones por delante del goto han retrocedido una posición.
//                codigo.remove(posGoto);
//            }
//        }
//        return canvi;
//    }
//
//    //checked
//    public boolean brancamentSobreBrancament() {
//        boolean canvi = false;
//        for (int i = 0; i < codigo.size(); i++) {
//            if (isIf(codigo.get(i).getTipoInstr())) {
//                int posIf = i;
//                Operador e1 = codigo.get(i).getOperators()[2];
//                for (int j = i + 1; j < codigo.size(); j++) {
//                    if (codigo.get(j).getTipoInstr() == TipoInstr.SKIP && codigo.get(j).getOperators()[2].getLabel().equals(e1.getLabel())) {
//                        if (j + 1 < codigo.size() && codigo.get(j + 1).getTipoInstr() == TipoInstr.GOTO) {
//                            codigo.get(posIf).setOperator(2, codigo.get(j + 1).getOperators()[2]);
//                            boolean borrar = true;
//                            for (int k = 0; k < codigo.size(); k++) {
//                                if ((isIf(codigo.get(k).getTipoInstr()) || codigo.get(k).getTipoInstr() == TipoInstr.GOTO) && codigo.get(k).getOperators()[2].getLabel().equals(e1.getLabel())) {
//                                    borrar = false;
//                                    break;
//                                }
//                            }
//                            if (borrar) {
//                                canvi = true;
//                                codigo.remove(j);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return canvi;
//    }
//
//    //checked
//    public boolean operacioConstant1() {
//        boolean canvi = false;
//        for (int i = 0; i < codigo.size(); i++) {
//            if (isOp(codigo.get(i).getTipoInstr()) && codigo.get(i).getOperators()[0].getType() == Type.literal
//                    && codigo.get(i).getOperators()[1].getType() == Type.literal) {
//                canvi = true;
//                int a = (int) codigo.get(i).getOperators()[0].getLiteral();
//                int b = (int) codigo.get(i).getOperators()[1].getLiteral();
//                switch (codigo.get(i).getTipoInstr()) {
//                    case ADD:
//                        a = a + b;
//                        break;
//                    case DIV:
//                        a = a / b;
//                        break;
//                    case MOD:
//                        a = a % b;
//                        break;
//                    case MUL:
//                        a = a * b;
//                        break;
//                    case SUB:
//                        a = a - b;
//                        break;
//                }
//                codigo.get(i).setTipoInstr(TipoInstr.CLONE);
//                codigo.get(i).setOperator(0, new Operador(a, CastType.INT));
//                codigo.get(i).setOperator(1, null);
//            }
//        }
//        return canvi;
//    }
//
//    //checked
//    public boolean operacioConstant2() {
//        boolean canvi = false;
//        for (int i = 0; i < codigo.size(); i++) {
//            if (isIf(codigo.get(i).getTipoInstr()) && codigo.get(i).getOperators()[0].getType() == Type.literal
//                    && codigo.get(i).getOperators()[1].getType() == Type.literal) {
//                boolean res = false;
//                switch (codigo.get(i).getOperators()[0].getCastType()) {
//                    case INT:
//                    case BOOL:
//                        int a;
//                        int b;
//                        if (codigo.get(i).getOperators()[0].getCastType() == CastType.BOOL) {
//                            if ((boolean) codigo.get(i).getOperators()[0].getLiteral() == true) {
//                                a = 1;
//                            } else {
//                                a = 0;
//                            }
//                        } else {
//                            a = (int) codigo.get(i).getOperators()[0].getLiteral();
//                        }
//                        if (codigo.get(i).getOperators()[1].getCastType() == CastType.BOOL) {
//                            if ((boolean) codigo.get(i).getOperators()[1].getLiteral() == true) {
//                                b = 1;
//                            } else {
//                                b = 0;
//                            }
//                        } else {
//                            b = (int) codigo.get(i).getOperators()[1].getLiteral();
//                        }
//                        switch (codigo.get(i).getTipoInstr()) {
//                            case IFEQ:
//                                if (a == b) {
//                                    res = true;
//                                }
//                                break;
//                            case IFGE:
//                                if (a >= b) {
//                                    res = true;
//                                }
//                                break;
//                            case IFGT:
//                                if (a > b) {
//                                    res = true;
//                                }
//                                break;
//                            case IFLE:
//                                if (a <= b) {
//                                    res = true;
//                                }
//                                break;
//                            case IFLT:
//                                if (a < b) {
//                                    res = true;
//                                }
//                                break;
//                            case IFNE:
//                                if (a != b) {
//                                    res = true;
//                                }
//                                break;
//                        }
//                        break;
//                    case CHAR:
//                        char c = (char) codigo.get(i).getOperators()[0].getLiteral();
//                        char d = (char) codigo.get(i).getOperators()[1].getLiteral();
//                        switch (codigo.get(i).getTipoInstr()) {
//                            case IFEQ:
//                                if (c == d) {
//                                    res = true;
//                                }
//                                break;
//                            case IFGE:
//                                if (c >= d) {
//                                    res = true;
//                                }
//                                break;
//                            case IFGT:
//                                if (c > d) {
//                                    res = true;
//                                }
//                                break;
//                            case IFLE:
//                                if (c <= d) {
//                                    res = true;
//                                }
//                                break;
//                            case IFLT:
//                                if (c < d) {
//                                    res = true;
//                                }
//                                break;
//                            case IFNE:
//                                if (c != d) {
//                                    res = true;
//                                }
//                                break;
//                        }
//                        break;
//                    case STRING:
//                        String g = (String) codigo.get(i).getOperators()[0].getLiteral();
//                        String h = (String) codigo.get(i).getOperators()[1].getLiteral();
//                        switch (codigo.get(i).getTipoInstr()) {
//                            case IFEQ:
//                                if (g.equals(h)) {
//                                    res = true;
//                                }
//                                break;
//                            case IFNE:
//                                if (!g.equals(h)) {
//                                    res = true;
//                                }
//                                break;
//                        }
//                        break;
//                }
//                if (res) {
//                    canvi = true;
//                    codigo.get(i).setTipoInstr(TipoInstr.GOTO);
//                    codigo.get(i).setOperator(0, null);
//                    codigo.get(i).setOperator(1, null);
//                } else {
//                    canvi = true;
//                    codigo.remove(i);
//                }
//            }
//        }
//        return canvi;
//    }
//
//    //checked
//    public boolean codiInaccesible1() {
//        boolean canvi = false;
//        for (int i = 0; i < codigo.size(); i++) {
//            //borrar goto innecesarios
//            if (codigo.get(i).getTipoInstr() == TipoInstr.GOTO
//                    && i + 1 < codigo.size() && codigo.get(i + 1).getTipoInstr() == TipoInstr.GOTO) {
//                canvi = true;
//                codigo.remove(i + 1);
//            }
//        }
//        return canvi;
//    }
//
//    //checked
//    public boolean codiInaccesible2() {
//        boolean canvi = false;
//        for (int i = 0; i < codigo.size(); i++) {
//            if (codigo.get(i).getTipoInstr() == TipoInstr.GOTO) {
//                String label = codigo.get(i).getOperators()[2].getLabel();
//                for (int j = i + 1; j < codigo.size(); j++) {
//                    if (codigo.get(j).getTipoInstr() == TipoInstr.SKIP) {
//                        if (codigo.get(j).getOperators()[2].getLabel().equals(label)) {
//                            //borrar codigo desde el goto e1 hasta skip e1
//                            for (int k = i + 1; k < j; k++) {
//                                canvi = true;
//                                codigo.remove(i + 1);
//                            }
//                            break;
//                        } else {
//                            //mirar si tiene un goto en el codigo
//                            String aux = codigo.get(j).getOperators()[2].getLabel();
//                            boolean trobat = false;
//                            for (int k = 0; k < codigo.size(); k++) {
//                                if ((codigo.get(k).getTipoInstr() == TipoInstr.GOTO || isIf(codigo.get(k).getTipoInstr()))
//                                        && codigo.get(k).getOperators()[2].getLabel().equals(aux)) {
//                                    trobat = true;
//                                    break;
//                                }
//                            }
//                            if (trobat) {
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return canvi;
//    }
//
//    //checked
//    public boolean assignacioDiferida() {
//        boolean canvi = false;
//        for (int i = 0; i < codigo.size(); i++) {
//            if (isOp(codigo.get(i).getTipoInstr()) || codigo.get(i).getTipoInstr() == TipoInstr.CLONE
//                    || codigo.get(i).getTipoInstr() == TipoInstr.NEG || codigo.get(i).getTipoInstr() == TipoInstr.INDVALUE) {
//                int res = codigo.get(i).getOperators()[2].getReference();
//                int cont = 0;
//                int pos = -1;
//                int j;
//                for (j = 1; j < codigo.size(); j++) {
//                    if (j != i) {
//                        if ((codigo.get(j).getOperators()[0] != null && codigo.get(j).getOperators()[0].getType() == Type.reference && codigo.get(j).getOperators()[0].getReference() == res)
//                                || (codigo.get(j).getOperators()[1] != null && codigo.get(j).getOperators()[1].getType() == Type.reference && codigo.get(j).getOperators()[1].getReference() == res)
//                                || (codigo.get(j).getTipoInstr() == TipoInstr.SIMPLEPARAM
//                                && codigo.get(j).getOperators()[2] != null && codigo.get(j).getOperators()[2].getReference() == res)
//                                || (codigo.get(j).getTipoInstr() == TipoInstr.RETURN
//                                && codigo.get(j).getOperators()[2] != null && codigo.get(j).getOperators()[2].getReference() == res)) {
//                            if (cont == 1) {
//                                cont++;
//                                break;
//                            } else {
//                                cont++;
//                                pos = j;
//                            }
//                        }
//                        if ((isOp(codigo.get(j).getTipoInstr()) || codigo.get(j).getTipoInstr() == TipoInstr.CLONE
//                                || codigo.get(j).getTipoInstr() == TipoInstr.NEG || codigo.get(j).getTipoInstr() == TipoInstr.INDVALUE) && codigo.get(j).getOperators()[2].getReference() == res) {
//                            cont = 2;
//                            break;
//                        }
//                    }
//
//                }
//                if (cont == 1) {
//                    if (codigo.get(i).getTipoInstr() == TipoInstr.CLONE || codigo.get(i).getTipoInstr() == TipoInstr.NEG) {
//                        Operador op = codigo.get(i).getOperators()[0];
//                        if (codigo.get(pos).getTipoInstr() == TipoInstr.CLONE || codigo.get(pos).getTipoInstr() == TipoInstr.NEG
//                                /*|| codigo.get(pos).getTipoInstr() == TipoInstr.PRINT || codigo.get(pos).getTipoInstr() == TipoInstr.PRINTLN*/
//                                || codigo.get(pos).getTipoInstr() == TipoInstr.READ) {
//                            codigo.get(pos).setOperator(0, op);
//                            codigo.remove(i);
//                            canvi = true;
//                            i--;
//                        } /*else if (codigo.get(pos).getTipoInstr() == TipoInstr.SIMPLEPARAM ||  codigo.get(pos).getTipoInstr() == TipoInstr.RETURN) {
//                            codigo.get(pos).setOperator(2, op);
//                            codigo.remove(i);
//                            canvi = true;
//                            i--;
//                        }*/ else if (codigo.get(pos).getTipoInstr() == TipoInstr.PRINT || codigo.get(pos).getTipoInstr() == TipoInstr.PRINTLN) {
//                        } else {
//                            if (codigo.get(pos).getOperators()[0] != null && codigo.get(pos).getOperators()[0].getType() == Type.reference && codigo.get(pos).getOperators()[0].getReference() == res) {
//                                codigo.get(pos).setOperator(0, op);
//                            } else { // sera el otro operador el que ha coincidido
//                                codigo.get(pos).setOperator(1, op);
//                            }
//                            codigo.remove(i);
//                            canvi = true;
//                            i--;
//                        }
//                    } else {
//                        Operador op1 = codigo.get(i).getOperators()[0];
//                        Operador op2 = codigo.get(i).getOperators()[1];
//                        if (codigo.get(pos).getTipoInstr() == TipoInstr.CLONE) {
//                            codigo.get(pos).setTipoInstr(codigo.get(i).getTipoInstr());
//                            codigo.get(pos).setOperator(0, op1);
//                            codigo.get(pos).setOperator(1, op2);
//                            codigo.remove(i);
//                            i--;
//                            canvi = true;
//                        }
//                    }
//                }
//            }
//        }
//        return canvi;
//    }
//
//    //checked
//    private boolean isIf(TipoInstr inst) {
//        return inst == TipoInstr.IFEQ || inst == TipoInstr.IFGE || inst == TipoInstr.IFGT
//                || inst == TipoInstr.IFLE || inst == TipoInstr.IFLT || inst == TipoInstr.IFNE;
//    }
//
//    //checked
//    private TipoInstr negCond(TipoInstr inst) {
//        return switch (inst) {
//            case IFLT -> TipoInstr.IFGE;
//            case IFLE -> TipoInstr.IFGT;
//            case IFGT -> TipoInstr.IFLE;
//            case IFGE -> TipoInstr.IFLT;
//            case IFEQ -> TipoInstr.IFNE;
//            case IFNE -> TipoInstr.IFEQ;
//            default -> null;
//        };
//    }
//
//    //checked
//    private boolean isOp(TipoInstr inst) {
//        return inst == TipoInstr.ADD || inst == TipoInstr.DIV || inst == TipoInstr.MOD
//                || inst == TipoInstr.MUL || inst == TipoInstr.SUB;
//    }
//
//    public ArrayList<Instruction3Address> getCode() {
//        return codigo;
//    }
}
