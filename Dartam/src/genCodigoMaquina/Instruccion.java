package genCodigoMaquina;

public class Instruccion {

    public enum Tipo {
        copy, add, sub, prod, div, mod, neg, and, or, not, ind_val, ind_ass,
        skip, go_to, if_LT, if_LE, if_EQ, if_NE, if_GE, if_GT, pmb, call, rtn,
        param_s, param_c, in, out, point
    }

    public Tipo instruccion;
    public String izq, der, des;

    /*
        Si tenemos las 4 casillas
     */
    public Instruccion(Tipo instruccion, String izq, String der, String des) {
        this.instruccion = instruccion;
        this.izq = izq;
        this.der = der;
        this.des = des;
    }

    /*
        Si tenemos únicamente 3 casillas
     */
    public Instruccion(Tipo instruccion, String izq, String des) {
        this.instruccion = instruccion;
        this.izq = izq;
        this.der = null;
        this.des = des;
    }

    /*
        Si tenemos 2 casillas
     */
    public Instruccion(Tipo instruccion, String des) {
        this.instruccion = instruccion;
        this.izq = null;
        this.der = null;
        this.des = des;
    }

    @Override
    public String toString() {
        if (instruccion == null) {
            return null;
        }

        String resultado;

        if (instruccion == Tipo.copy) {
            resultado = "\t" + des + " = " + izq;
        } else if (instruccion == Tipo.add) {
            resultado = "\t" + des + " = " + izq + " + " + der;
        } else if (instruccion == Tipo.sub) {
            resultado = "\t" + des + " = " + izq + " - " + der;
        } else if (instruccion == Tipo.prod) {
            resultado = "\t" + des + " = " + izq + " * " + der;
        } else if (instruccion == Tipo.and) {
            resultado = "\t" + des + " = " + izq + " and " + der;
        } else if (instruccion == Tipo.call) {
            resultado = "\t" + des + " = call " + izq;
        } else if (instruccion == Tipo.div) {
            resultado = "\t" + des + " = " + izq + " / " + der;
        } else if (instruccion == Tipo.go_to) {
            resultado = "\tgoto " + des;
        } else if (instruccion == Tipo.if_EQ) {
            resultado = "\tif " + izq + "=" + der + " goto " + des;
        } else if (instruccion == Tipo.if_GE) {
            resultado = "\tif " + izq + ">=" + der + " goto " + des;
        } else if (instruccion == Tipo.if_GT) {
            resultado = "\tif " + izq + ">" + der + " goto " + des;
        } else if (instruccion == Tipo.if_LE) {
            resultado = "\tif " + izq + "<=" + der + " goto " + des;
        } else if (instruccion == Tipo.if_LT) {
            resultado = "\tif " + izq + "<" + der + " goto " + des;
        } else if (instruccion == Tipo.if_NE) {
            resultado = "\tif " + izq + "!=" + der + " goto " + des;
        } else if (instruccion == Tipo.ind_ass) {
            resultado = "\t" + des + "[" + izq + "] = " + der;
        } else if (instruccion == Tipo.ind_val) {
            resultado = "\t" + des + " = " + izq + "[" + der + "]";
        } else if (instruccion == Tipo.mod) {
            resultado = "\t" + des + " = " + izq + " mod " + der;
        } else if (instruccion == Tipo.neg) {
            resultado = "\t" + des + " = -" + izq;
        } else if (instruccion == Tipo.not) {
            resultado = "\t" + des + " = not " + izq;
        } else if (instruccion == Tipo.or) {
            resultado = "\t" + des + " = " + izq + " or " + der;
        } else if (instruccion == Tipo.param_c) {
            resultado = "\tparam_c: " + des + "[]";
        } else if (instruccion == Tipo.param_s) {
            resultado = "\tparam_s: " + des;
        } else if (instruccion == Tipo.pmb) {
            resultado = "\tpmb " + des;
        } else if (instruccion == Tipo.rtn) {
            resultado = "\trtn " + des + ": " + izq;
        } else if (instruccion == Tipo.skip) {
            resultado = des + ": skip";
        } else if (instruccion == Tipo.in) {
            resultado = "\tin: " + des;
        } else if (instruccion == Tipo.out) {
            resultado = "\tout: " + des;
        } else if (instruccion == Tipo.point) {
            resultado = "\t" + des + " => " + izq;
        } else {
            resultado = null;
        }

        return resultado;
    }
}
