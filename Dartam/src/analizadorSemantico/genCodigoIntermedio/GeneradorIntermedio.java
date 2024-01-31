/**
 * Assignatura 21780 - Compiladors
 * Estudis: Grau en Enginyeria Informàtica
 * Itinerari: Intel·ligència Artificial i Computacio
 *
 * Equipo: Arturo, Dani y Marta
 */
package genCodigoIntermedio;

import analizadorSemantico.*;
import analizadorSintactico.ParserSym;
import java.util.ArrayList;
import java.util.Stack;

import analizadorSintactico.symbols.*;
import java.util.HashMap;
import java.util.List;
import jflex.base.Pair;

public class GeneradorIntermedio {

    public TablaSimbolos tablaSimbolos;
    public Generador3Direcciones g3d;
    public TablaVariables tablaVariables;
    public TablaProcedimientos tablaProcedimientos;

    // Description to the function in which we are currently.
    private Pair<String, DescripcionSimbolo> metodoActualmenteSiendoTratado;
    private int variableTratadaActualmente;

    // When checking if a function's parameters are correct, we use this stack to store the declared function's types.
    // We use a stack because we will be taking elements out every time we process them.
    private List<String> errores, symbols;
    private static final boolean DEBUG = true;

    public String getErrors() {
        if (errores.isEmpty()) {
            return "";
        }
        String s = "Localizaciones de los errores multilinea en formato (linea, columna)\n\n";
        for (String e : errores) {
            s += e + "\n";
        }
        return s;
    }

    public String getSymbols() {
        return tablaSimbolos.toString();
    }

    //Nodo padre del resto de nodos...
    public GeneradorIntermedio(SymbolScript scriptElementosAntesDeMain) throws Exception {
        tablaSimbolos = new TablaSimbolos();
        errores = new ArrayList<>();

        //Creamos las 2 tablas, tanto de procedimientos como de variables
        this.tablaProcedimientos = new TablaProcedimientos();
        this.tablaVariables = new TablaVariables();
        g3d = new Generador3Direcciones(this.tablaVariables, this.tablaProcedimientos);
        this.variableTratadaActualmente = -1;

        ArrayList<SymbolDecs> declaraciones = new ArrayList<>();
        ArrayList<SymbolScriptElemento> tuplas = new ArrayList<>();
        ArrayList<SymbolScriptElemento> metodos = new ArrayList<>();
        int idMidDecs = 0, idMidTuplas = 0, idMidMetodos = 0;
        // elementos antes del main
        SymbolScriptElemento elem = scriptElementosAntesDeMain.elemento;
        while (elem != null) {
            switch (elem.getTipo()) {
                case DECS -> {
                    declaraciones.add(elem.declaraciones);
                    idMidDecs++;
                }
                case TUPLA -> {
                    tuplas.add(elem);
                    idMidTuplas++;
                }
                case METODO -> {
                    metodos.add(elem);
                    idMidMetodos++;
                }
            }
            scriptElementosAntesDeMain = scriptElementosAntesDeMain.siguienteElemento;
            elem = scriptElementosAntesDeMain.elemento;
        }
        SymbolMain scriptMainYElementos = scriptElementosAntesDeMain.main;
        // elementos despues del main
        elem = scriptMainYElementos.elemento;

        while (elem != null) {
            switch (elem.getTipo()) {
                case DECS ->
                    declaraciones.add(idMidDecs, elem.declaraciones);
                case TUPLA ->
                    tuplas.add(idMidTuplas, elem);
                case METODO ->
                    metodos.add(idMidMetodos, elem);
            }
            scriptMainYElementos = scriptMainYElementos.siguienteElemento;
            elem = scriptMainYElementos.elemento;
        }
        // metodos especiales
        DescripcionSimbolo dEnter = new DescripcionSimbolo(ParserSym.terminalNames[ParserSym.ENT]);
        tablaSimbolos.poner(ParserSym.terminalNames[ParserSym.ENTER], dEnter);
        DescripcionSimbolo dShow = new DescripcionSimbolo(ParserSym.terminalNames[ParserSym.ENT]);
        tablaSimbolos.poner(ParserSym.terminalNames[ParserSym.SHOW], dShow);
        DescripcionSimbolo dFrom = new DescripcionSimbolo(ParserSym.terminalNames[ParserSym.ENT]);
        tablaSimbolos.poner(ParserSym.terminalNames[ParserSym.FROM], dFrom);
        DescripcionSimbolo dInto = new DescripcionSimbolo(ParserSym.terminalNames[ParserSym.ENT]);
        tablaSimbolos.poner(ParserSym.terminalNames[ParserSym.INTO], dInto);
        // tuplas
        for (SymbolScriptElemento tupla : tuplas) {
            DescripcionSimbolo d = new DescripcionSimbolo(tupla.id, new HashMap<String, DescripcionSimbolo>());
            tablaSimbolos.poner(tupla.id, d);
        }

        for (SymbolScriptElemento tupla : tuplas) {
            procesarDeclaracionTupla(tupla);
        }

        // declaraciones
        for (SymbolDecs decs : declaraciones) {
            procesarDeclaraciones(decs, null);
        }

        // metodos
        for (SymbolScriptElemento metodo : metodos) {
            DescripcionSimbolo d = new DescripcionSimbolo(metodo.tipoRetorno.getTipo());
            tablaSimbolos.poner(metodo.id, d);
        }
        for (SymbolScriptElemento metodo : metodos) {
            procesarDefinicionMetodo(metodo);
        }

        // main
        DescripcionSimbolo d = new DescripcionSimbolo();
        tablaSimbolos.poner(scriptMainYElementos.nombreMain, d);

        procesarMain(scriptMainYElementos);
    }

    //TODO: Revisar asignaciones
    private void procesarDeclaraciones(SymbolDecs decs, DescripcionSimbolo declaracionTipoTupla) throws Exception {
        SymbolDecAsigLista dec = decs.iddecslista;

        while (dec != null) {
            SymbolTipo tipo = decs.tipo;
            boolean error = false;
            DescripcionSimbolo descripcionTupla = null;
            String id = dec.id;
            SymbolOperand valorAsignado = (dec.asignacion == null) ? null : dec.asignacion.operando;
            int variable = 0; //Contendra el numero de la variable creada

            if (tipo.isArray() || tipo.isTupla()) {
                if (tipo.isTupla()) {
                    String tuplaName = tipo.getTipo();
                    tuplaName = tuplaName.substring(tuplaName.indexOf(" ") + 1);
                    descripcionTupla = tablaSimbolos.consulta(tuplaName);
                    variable = this.g3d.nuevaVariable(TipoReferencia.var, tipo.getTipo(), false, true);
                    this.variableTratadaActualmente = variable;
                }
                if (tipo.isArray()) {
                    variable = this.g3d.nuevaVariable(TipoReferencia.var, tipo.getTipo(), true, false);
                    this.variableTratadaActualmente = variable;
                }
            } else if (valorAsignado != null) {
                variable = this.g3d.nuevaVariable(TipoReferencia.var, tipo.getTipo(), false, false);
                this.variableTratadaActualmente = variable;
            }

            //No hays errores
            if (!error) {
                DescripcionSimbolo d;
                if (tipo.isArray()) {
                    d = new DescripcionSimbolo(tipo.getTipo() + " " + tipo.dimArray.getEmptyBrackets(), tipo.dimArray.getDimensiones(), descripcionTupla);
                } else {
                    d = new DescripcionSimbolo(tipo.getTipo(), decs.isConst, valorAsignado != null, descripcionTupla);
                }

                //Añadiendo un miembro de tupla
                if (declaracionTipoTupla != null) {
                    declaracionTipoTupla.anyadirMiembro(id, d);
                } else { //Declaracion de dato

                    SymbolOperand valor = (dec.asignacion == null) ? null : dec.asignacion.operando;
                    tablaSimbolos.poner(id, d);
                }
            }
            dec = dec.siguienteDeclaracion;
        }
    }

    private void procesarBody(SymbolBody body) throws Exception {
        while (body != null) {
            SymbolMetodoElemento elem = body.metodo;
            switch (elem.getTipo()) {
                case INSTR ->
                    procesarInstruccion(elem.instruccion);
                case IF ->
                    procesarIf(elem.iff);
                case LOOP ->
                    procesarLoop(elem.loop);
                case SWITCH ->
                    procesarSwitch(elem.sw);
            }
            body = body.siguienteMetodo;
        }
    }

    private void procesarInstruccion(SymbolInstr instr) throws Exception {
        switch (instr.getTipo()) {
            case SymbolInstr.ASIGS ->
                procesarAsignaciones(instr.asigs);
            case SymbolInstr.DECS ->
                procesarDeclaraciones(instr.decs, null);
            case SymbolInstr.FCALL ->
                procesarLlamadaFuncion(instr.fcall);
            case SymbolInstr.RET ->
                procesarReturn(instr.ret);
            case SymbolInstr.SWAP ->
                procesarSwap(instr.swap);
        }
    }

    private void procesarAsignaciones(SymbolAsigs asigs) throws Exception {
        while (asigs != null) {
            SymbolAsig asig = asigs.asig;
            DescripcionSimbolo d = tablaSimbolos.consulta(asig.id);
            String tipoValor = procesarOperando(asig.valor);
            String tipoVariable = null;
            switch (asig.getTipo()) {
                case PRIMITIVA -> {
                    tipoVariable = d.getTipo();
                    d.asignarValor();
                }
                case ARRAY -> {
                    String idx = procesarOperando(asig.dim);
                    tipoVariable = d.getTipo();
                    tablaSimbolos.ponerIndice(asig.id, d);
                }
                case TUPLA -> {
                    DescripcionSimbolo miembro = d.getMember(asig.miembro);
                    tipoVariable = miembro.getTipo();
                    tablaSimbolos.ponerCampo(asig.id, asig.miembro, miembro);
                    miembro.asignarValor();
                }
            }
            asigs = asigs.siguienteAsig;
        }

    }

    private void procesarLlamadaFuncion(SymbolFCall fcall) throws Exception {
        String nombre = (String) fcall.methodName.value;
        DescripcionSimbolo ds = tablaSimbolos.consulta(nombre);
        ArrayList<Pair<String, DescripcionSimbolo>> params = ds.getTiposParametros();
        SymbolOperandsLista opLista = fcall.operandsLista;
        
        for (Pair<String, DescripcionSimbolo> tipoParam : params) {
            
            SymbolOperand op = opLista.operand;
            String tipoOp = procesarOperando(op);

            opLista = opLista.siguienteOperando;
        }
    }

    private void procesarReturn(SymbolReturn ret) throws Exception {
        String tipo = metodoActualmenteSiendoTratado.snd.getTipo();
        SymbolOperand op = ret.op;
        if (op == null) {
            
                return;

        }
        Object tipoOp = procesarOperando(op);


        //TODO: revisar
        this.g3d.generarInstruccion(TipoInstruccion.RETURN.getDescripcion(), new Operador(metodoActualmenteSiendoTratado.fst), null, null);
    }

    private void procesarSwap(SymbolSwap swap) {
        //Todo correcto!
        this.g3d.generarInstruccion(TipoInstruccion.SWAP.getDescripcion(), new Operador(swap.op2), new Operador(swap.op1), new Operador(swap.op2));
        this.g3d.generarInstruccion(TipoInstruccion.SWAP.getDescripcion(), new Operador(swap.op1), new Operador(swap.op2), new Operador(swap.op1));

    }

    private void procesarIf(SymbolIf cond) throws Exception {
        String tipoCond = procesarOperando(cond.cond);
        //Creamos la etiqueta
        String efi = this.g3d.nuevaEtiqueta(); //Nueva etiqueta para el if
        String e = this.g3d.nuevaEtiqueta();
        String eElse = this.g3d.nuevaEtiqueta();

        //Llegados aquí si la condicion es falsa salta, sino ejecuta
        this.g3d.generarInstruccion(TipoInstruccion.IFEQ.getDescripcion(), new Operador(cond.cond.getReferencia()), new Operador(Tipo.INT, 0), new Operador(e)); //Si es falso saltaremos

        tablaSimbolos.entraBloque();
        procesarBody(cond.cuerpo);
        tablaSimbolos.salirBloque();
        //Si se cumplio la condicion no continuares revisando el resto
        this.g3d.generarInstruccion(TipoInstruccion.GOTO.getDescripcion(), null, null, new Operador(efi));

        this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(e));
        SymbolElifs elifs = cond.elifs;

        String elseIf2 = this.g3d.nuevaEtiqueta();
        while (elifs != null) {

            SymbolElif elif = elifs.elif;
            tipoCond = procesarOperando(elif.cond);
            if (elifs.siguienteElif == null && cond.els != null) {
                //Llegados aquí si la condicion es falsa salta, sino ejecuta
                this.g3d.generarInstruccion(TipoInstruccion.IFEQ.getDescripcion(), new Operador(cond.cond.getReferencia()), new Operador(Tipo.INT, 0), new Operador(eElse)); //Si es falso saltaremos
            } else if (elifs.siguienteElif != null) {
                //Llegados aquí si la condicion es falsa salta, sino ejecuta
                this.g3d.generarInstruccion(TipoInstruccion.IFEQ.getDescripcion(), new Operador(cond.cond.getReferencia()), new Operador(Tipo.INT, 0), new Operador(elseIf2)); //Si es falso saltaremos
            } else {
                //Llegados aquí si la condicion es falsa salta, sino ejecuta
                this.g3d.generarInstruccion(TipoInstruccion.IFEQ.getDescripcion(), new Operador(cond.cond.getReferencia()), new Operador(Tipo.INT, 0), new Operador(efi)); //Si es falso saltaremos
            }

            tablaSimbolos.entraBloque();
            procesarBody(elif.cuerpo);
            tablaSimbolos.salirBloque();

            //Como ha ejecutado saltamos al fin del if
            this.g3d.generarInstruccion(TipoInstruccion.GOTO.getDescripcion(), null, null, new Operador(efi)); //Si es falso saltaremos

            elifs = elifs.siguienteElif;
            if (elifs != null) { //Si existe creamos la siguiente etiqueta
                this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(elseIf2));
                elseIf2 = this.g3d.nuevaEtiqueta();
            }
        }

        if (cond.els != null) {
            tablaSimbolos.entraBloque();
            procesarBody(cond.els.cuerpo);
            tablaSimbolos.salirBloque();
        }

        //Fin del bloque if
        this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(efi));
    }

    private void procesarLoop(SymbolLoop loop) throws Exception {
        // no importa que sea while o do while

        //Creamos la etiqueta para volver al bucle si se cumple la condicion
        String inicioLoop = this.g3d.nuevaEtiqueta();
        this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(inicioLoop));

        String salirLoop = this.g3d.nuevaEtiqueta();
        tablaSimbolos.entraBloque();
        SymbolLoopCond loopCond = loop.loopCond;
        if (loopCond.decs != null) {
            procesarDeclaraciones(loopCond.decs, null);
        }
        String tipoCond = procesarOperando(loopCond.cond);
        if (loopCond.asig != null) {
            procesarAsignaciones(loopCond.asig);
        }

        //En el caso de no cumplirse la condicion saltaremos al fin del bucle
        this.g3d.generarInstruccion(TipoInstruccion.IFEQ.getDescripcion(), new Operador(loopCond.cond.getReferencia()), new Operador(Tipo.INT, 0), new Operador(salirLoop));

        procesarBody(loop.cuerpo);
        tablaSimbolos.salirBloque();

        this.g3d.generarInstruccion(TipoInstruccion.GOTO.getDescripcion(), null, null, new Operador(inicioLoop)); //Saltaremos al mismo bucle
        this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(salirLoop)); //Etiqueta para fin de bucle

    }

    private void procesarSwitch(SymbolSwitch sw) throws Exception {
        String fiSwitch = this.g3d.nuevaEtiqueta();
        String pred = ""; //Guardaremos la etiqueta para el default del swtich

        tablaSimbolos.entraBloque();
        Object tipo1 = procesarOperando(sw.cond);
        SymbolCaso caso = sw.caso;
        String cases = this.g3d.nuevaEtiqueta(); //Etiquetas para los casos
        while (caso != null) {
            Object tipo2 = procesarOperando(caso.cond);
            if (caso.caso != null) { //Tenemos mas casos, saltamos al siguiente ya que no cumplimos condicion
                cases = this.g3d.nuevaEtiqueta();
                this.g3d.generarInstruccion(TipoInstruccion.IFEQ.getDescripcion(), new Operador(caso.cond.getReferencia()), new Operador(Tipo.INT, 0), new Operador(cases));
            } else if (sw.pred != null) { //No hay siguiente caso pero hay un default, saltamos al default
                pred = this.g3d.nuevaEtiqueta();
                this.g3d.generarInstruccion(TipoInstruccion.IFEQ.getDescripcion(), new Operador(caso.cond.getReferencia()), new Operador(Tipo.INT, 0), new Operador(pred));
            } else { //Swithc solo con cases y no hay mas saltamos al final del bloque switch
                this.g3d.generarInstruccion(TipoInstruccion.IFEQ.getDescripcion(), new Operador(caso.cond.getReferencia()), new Operador(Tipo.INT, 0), new Operador(fiSwitch));
            }

            procesarBody(caso.cuerpo);
            //Se proceso el body, por lo que la condicion era correcta, saltamos al final del switch
            this.g3d.generarInstruccion(TipoInstruccion.GOTO.getDescripcion(), null, null, new Operador(fiSwitch));

            caso = caso.caso;

            //Para el siguiente case
            this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(cases));

        }
        if (sw.pred != null) {
            this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(pred));
            procesarBody(sw.pred.cuerpo);
        }
        tablaSimbolos.salirBloque();
        this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(fiSwitch));
    }

    private void procesarDefinicionMetodo(SymbolScriptElemento metodo) throws Exception {

        //Añadimos el metodo
        this.g3d.añadirFuncion(metodo.id);
        String fEtiqueta = this.g3d.nuevaEtiqueta(); //Creamos la etiqeuta de la funcion

        //Añadimos la funcion a la tabla
        int numeroProcedure = this.g3d.nuevoProcedimiento(metodo.id, this.tablaSimbolos.getProfundidad(), fEtiqueta);

        tablaSimbolos.entraBloque();
        metodoActualmenteSiendoTratado = new Pair(metodo.id, tablaSimbolos.consulta(metodo.id));
        procesarParametros(metodo.parametros.paramsLista);

        this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(fEtiqueta));
        this.g3d.generarInstruccion(TipoInstruccion.INIT.getDescripcion(), null, null, new Operador(metodo.id));

        procesarBody(metodo.cuerpo);

        this.g3d.eliminarFuncion();
        this.g3d.generarInstruccion(TipoInstruccion.RETURN.getDescripcion(), new Operador(metodo.id), null, new Operador(metodo.getReferencia()));

        tablaSimbolos.salirBloque();
    }

    private void procesarParametros(SymbolParamsLista params) {

        String idFuncion = this.g3d.funcionActual(); //Los parametros pertenecen a este metodo
        PData data = this.g3d.getProcedimeinto(idFuncion);

        while (params != null) {
            int variable;
            String tipo = params.tipoParam.getTipo();
            if (params.tipoParam.isTupla()) {
                variable = this.g3d.nuevaVariable(TipoReferencia.param, params.tipoParam.getTipo(), false, true);
            } else if (params.tipoParam.isArray()) {
                variable = this.g3d.nuevaVariable(TipoReferencia.param, params.tipoParam.getTipo(), true, false);
            } else {
                variable = this.g3d.nuevaVariable(TipoReferencia.param, params.tipoParam.getTipo(), false, false);
            }

            //tablaSimbolos.posaparam(metodoActualmenteSiendoTratado.fst, nombreParam, dParam);
//                tablaSimbolos.poner(nombreParam, dParam);
//                dFuncion.anyadirParametro(nombreParam, dParam);
            data.añadirParametro(variable, tipo); //Le añadimos el parametro

            params = params.siguienteParam;
        }
    }

    private void procesarDeclaracionTupla(SymbolScriptElemento tupla) throws Exception {
        SymbolMiembrosTupla miembros = tupla.miembrosTupla;
        DescripcionSimbolo d = tablaSimbolos.consulta(tupla.id);

        String identificador = tupla.id; //Identificador

        //Como nuestras tuplas pueden tener datos de diferentes tipo, tipo=null
        int varNumero = this.g3d.nuevaVariable(TipoReferencia.var, null, false, true);

        while (miembros != null) {
            procesarDeclaraciones(miembros.decs, d);
            miembros = miembros.siguienteDeclaracion;
        }
    }

    private void procesarMain(SymbolMain main) throws Exception {
        SymbolBody body = main.main;

        this.g3d.añadirFuncion("main");
        String etiquetaMain = this.g3d.nuevaEtiqueta(); //Etiqueta main

        //Lo añadimos a la tabla de funciones TODO: Revisar tema de profundidad!!
        int numeroProcedure = this.g3d.nuevoProcedimiento("main", 0, etiquetaMain);

        tablaSimbolos.entraBloque();
        metodoActualmenteSiendoTratado = new Pair(main.nombreMain, tablaSimbolos.consulta(main.nombreMain));
        String tipo = ParserSym.terminalNames[ParserSym.STRING] + " " + main.lBracket + main.rBracket;
        //DescripcionSimbolo d = new DescripcionSimbolo(tipo, false, true, null);
        //tablaSimbolos.poner(main.nombreArgumentos, d);

        this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(etiquetaMain));
        this.g3d.generarInstruccion(TipoInstruccion.INIT.getDescripcion(), null, null, new Operador("main"));

        procesarBody(body);

        this.g3d.eliminarFuncion();

        tablaSimbolos.salirBloque();

    }

    /**
     * Devuelve el tipo, array o struct al que pertenece el operando. Null si no
     * se respetan los tipos.
     *
     * @param op
     * @return
     */
    private String procesarOperando(SymbolOperand op) throws Exception {
        if (DEBUG) {
            //System.out.println(op.toString());
        }
        switch (op.getTipo()) {
            case ATOMIC_EXPRESSION -> { //Se asignan valores string, bool... o alguno con id

                SymbolAtomicExpression literal = op.atomicExp;
                String tipo = literal.tipo;
                if (!tipo.equals(ParserSym.terminalNames[ParserSym.ID])) {

                    //Copiamos el valor x -> A
                    this.g3d.generarInstruccion(TipoInstruccion.COPY.getDescripcion(), new Operador(op.getReferencia()), null, new Operador(this.variableTratadaActualmente));

                    return tipo; // si no es ID
                }
                String nombreID = (String) literal.value;
                DescripcionSimbolo ds = tablaSimbolos.consulta(nombreID);
                //Copiamos el valor de la variable B -> A
                int variableAsignado = this.tablaVariables.recibirNumeroVariable(nombreID);
                this.g3d.generarInstruccion(TipoInstruccion.COPY.getDescripcion(), new Operador(variableAsignado), null, new Operador(this.variableTratadaActualmente));

                return ds.getTipo();
            }
            case FCALL -> { //TODO: Mirar llamadaFuncion que sera donde se asigne el valor a la variable si es asi
                SymbolFCall fcall = op.fcall;
                procesarLlamadaFuncion(fcall);
                DescripcionSimbolo ds = tablaSimbolos.consulta((String) fcall.methodName.value);
                return ds.getTipo();
            }
            case OP_BETWEEN_PAREN -> { //Se encaragara de continuar procesandose
                return procesarOperando(op.op);
            }
            case UNARY_EXPRESSION -> {
                SymbolUnaryExpression exp = op.unaryExp;
                String tipo = procesarOperando(exp.op);

                int operation;
                if (exp.isLeftUnaryOperator()) {
                    SymbolLUnaryOperator operator = exp.leftOp;
                    operation = operator.unaryOperator;
                } else {
                    SymbolRUnaryOperator operator = exp.rightOp;
                    operation = operator.unaryOperator;
                }

                //R_OPERATOR -> SOLO INC Y DEC
                if (ParserSym.OP_INC == operation || ParserSym.OP_DEC == operation) {
                    String s = "incremento";
                    if (ParserSym.OP_DEC == operation) {
                        s = "decremento";
                    }

                    procesarAsignaciones(new SymbolAsigs(new SymbolAsig(!exp.isLeftUnaryOperator(), operation, exp.op.toString(), exp.value, exp.xleft, exp.xright), exp.xleft, exp.xright));

                    //En el caso de no haber errores se eesta haciendo un a++ o un a-- por lo que actualizaremos la variable a la que se haga referencia
                    int numeroVariable = this.tablaVariables.recibirNumeroVariable((String) op.value); //TODO: Revisar
                    if (numeroVariable != -1) { //Variable encontrada
                        if (s.equals("incremento")) { //Incrementamos variable
                            this.g3d.generarInstruccion(TipoInstruccion.ADD.getDescripcion(), new Operador(Tipo.INT, 1), new Operador(numeroVariable), new Operador(numeroVariable));
                        } else if (s.equals("decremento")) { //Decrementamos variable
                            this.g3d.generarInstruccion(TipoInstruccion.SUB.getDescripcion(), new Operador(Tipo.INT, 1), new Operador(numeroVariable), new Operador(numeroVariable));
                        }
                    } else { //Insertamos directamente en la varaible a la que hacia referencia anteriormente

                        //TODO: mirar que el valor pasado como op.getReferencia() sea en verdad el valor a incrementar
                        numeroVariable = this.g3d.nuevaVariable(TipoReferencia.var, tipo, false, false);
                        if (s.equals("incremento")) { //Incrementamos variable
                            this.g3d.generarInstruccion(TipoInstruccion.ADD.getDescripcion(), new Operador(Tipo.INT, 1), new Operador(op.getReferencia()), new Operador(numeroVariable));
                        } else if (s.equals("decremento")) { //Decrementamos variable
                            this.g3d.generarInstruccion(TipoInstruccion.SUB.getDescripcion(), new Operador(Tipo.INT, 1), new Operador(op.getReferencia()), new Operador(numeroVariable));
                        }
                    }

                    //Ahora si, podemos pasar la variable incrementada o decrementada a su origen si es necesario
                    //TODO: Incorporar alguna manera de indicar que ya hemos finalizado con una varaible para no asignar valores a variables pasadas
                    this.g3d.generarInstruccion(TipoInstruccion.COPY.getDescripcion(), new Operador(numeroVariable), null, new Operador(this.variableTratadaActualmente));
                }

                //SOLO L_OPERATIONS
                if (exp.isLeftUnaryOperator()) {
                    return tipo;
                }

                if (tipo.equals(ParserSym.terminalNames[ParserSym.ENT]) && (ParserSym.OP_INC != operation && ParserSym.OP_DEC != operation)) {
                    return ParserSym.terminalNames[ParserSym.REAL]; // !!! se puede operar entero con OP_PCT
                }

                return tipo;
            }
            case BINARY_EXPRESSION -> {
                SymbolBinaryExpression exp = op.binaryExp;

                String tipo1 = procesarOperando(exp.op1);

                String tipo2 = procesarOperando(exp.op2);

                boolean unoIntOtroDouble = (tipo1.equals(ParserSym.terminalNames[ParserSym.REAL]) && tipo2.equals(ParserSym.terminalNames[ParserSym.ENT]))
                        || (tipo2.equals(ParserSym.terminalNames[ParserSym.REAL]) && tipo1.equals(ParserSym.terminalNames[ParserSym.ENT]));

                SymbolBinaryOperator operator = exp.bop;
                if (unoIntOtroDouble) {

                    //---->Se puede hacer operacion
                    TipoInstruccion ti = recibirTipoInstruccion(operator.nombreOperador());
                    int var = this.g3d.nuevaVariable(TipoReferencia.var, ParserSym.terminalNames[ParserSym.REAL], false, false);
                    //TODO: Mirar lo de la referecia
                    this.g3d.generarInstruccion(ti.getDescripcion(), new Operador(exp.op1.getReferencia()), new Operador(exp.op2.getReferencia()), new Operador(this.variableTratadaActualmente));

                    return ParserSym.terminalNames[ParserSym.REAL];

                }
                if (operator.doesOperationResultInBoolean()) {

                    //---->se peude hacer operacion
                    TipoInstruccion ti = recibirTipoInstruccion(operator.nombreOperador());
                    int var = this.g3d.nuevaVariable(TipoReferencia.var, ParserSym.terminalNames[ParserSym.PROP], false, false);
                    //TODO: Mirar lo de la referecia
                    this.g3d.generarInstruccion(ti.getDescripcion(), new Operador(exp.op1.getReferencia()), new Operador(exp.op2.getReferencia()), new Operador(this.variableTratadaActualmente));

                    return ParserSym.terminalNames[ParserSym.PROP];
                }

                //---->se puede hacer operacion
                TipoInstruccion ti = recibirTipoInstruccion(operator.nombreOperador());
                int var = this.g3d.nuevaVariable(TipoReferencia.var, tipo1, false, false);
                //TODO: Mirar lo de la referecia
                this.g3d.generarInstruccion(ti.getDescripcion(), new Operador(exp.op1.getReferencia()), new Operador(exp.op2.getReferencia()), new Operador(this.variableTratadaActualmente));

                return tipo1; // en caso contrario resulta en el mismo tipo
            }
            case CONDITIONAL_EXPRESSION -> {
                SymbolConditionalExpression exp = op.conditionalExp;
                String tipoCond = procesarOperando(exp.cond);
                String etiquetaFin = this.g3d.nuevaEtiqueta();
                String etiquetaFalse = this.g3d.nuevaEtiqueta();

                this.g3d.generarInstruccion(TipoInstruccion.IFEQ.getDescripcion(), new Operador(exp.cond.getReferencia()), new Operador(Tipo.INT, 1), new Operador(etiquetaFalse));

                String tipo1 = procesarOperando(exp.caseTrue);

                this.g3d.generarInstruccion(TipoInstruccion.GOTO.getDescripcion(), null, null, new Operador(etiquetaFin));

                this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(etiquetaFalse));
                String tipo2 = procesarOperando(exp.caseFalse);

                this.g3d.generarInstruccion(TipoInstruccion.SKIP.getDescripcion(), null, null, new Operador(etiquetaFin));
                return tipo1;
            }
            case IDX_ARRAY -> {
                SymbolOperand arr = op.op;
                String tipoArr = procesarOperando(arr);
                

                String aux = tipoArr;
                if (aux != null && aux.startsWith(ParserSym.terminalNames[ParserSym.KW_TUPLE])) {
                    aux = aux.substring(aux.indexOf(" ") + 1);
                }

                String idArray = "";
                
                    tipoArr = tipoArr.substring(0, tipoArr.length() - 3);
                    if (tipoArr.startsWith(ParserSym.terminalNames[ParserSym.KW_TUPLE])) {
                        String tipo = tipoArr.substring(tipoArr.indexOf(" ") + 1);
                        idArray = tipo;

                    }
                
                SymbolOperand idx = op.idxArr;
                String tipoIdx = procesarOperando(idx);

                // comprobacion de que el entero sea positivo???
                //TODO: Esto funcionaria unicamente para a = b[i], no para b[i] = a
                this.g3d.generarInstruccion(TipoInstruccion.IND_VAL.getDescripcion(), new Operador(idArray), new Operador(idx.getReferencia()), new Operador(this.getErrors()));

                return tipoArr;
            }
            case MEMBER_ACCESS -> { // TODO: 
                SymbolOperand tupla = op.op;
                String tipoTupla = procesarOperando(tupla);

                String nombre = (String) tupla.value;//tipoTupla.substring(tipoTupla.indexOf(" ") + 1);
                DescripcionSimbolo ds = tablaSimbolos.consulta(nombre);

                DescripcionSimbolo miembro = ds.getMember(op.member);

                return miembro.getTipo();
            }
            case CASTING -> {
                SymbolOperand operando = op.op;
                String casting = op.casting.getTipo();
                String tipo = procesarOperando(operando);
                int var = this.g3d.nuevaVariable(TipoReferencia.var, tipo, false, false);
                this.g3d.generarInstruccion(TipoInstruccion.CAST.getDescripcion(), new Operador(tipo), new Operador(casting), new Operador(var));

                //TODO: Revisar si se asigna a otra variable o que
                this.g3d.generarInstruccion(TipoInstruccion.ASIGN.getDescripcion(), new Operador(var), null, new Operador(this.variableTratadaActualmente));

                return casting; // casting posible entre int <-> char <-> double <-> int y de char -> string

            }
        }
        return null; // error, no es ninguno de los casos
    }

    private TipoInstruccion recibirTipoInstruccion(String s) {
        if (s.equals("OP_ADD")) {
            return TipoInstruccion.ADD;
        } else if (s.equals("OP_SUB")) {
            return TipoInstruccion.SUB;
        } else if (s.equals("OP_MUL")) {
            return TipoInstruccion.MUL;
        } else if (s.equals("OP_DIV")) {
            return TipoInstruccion.DIV;
        } else if (s.equals("OP_MOD")) {
            return TipoInstruccion.MOD;
        } else if (s.equals("OP_POT")) {
            return TipoInstruccion.POT;
        } else if (s.equals("OP_EQ")) {
            return TipoInstruccion.IFEQ;
        } else if (s.equals("OP_BEQ")) {
            return TipoInstruccion.IFGE;
        } else if (s.equals("OP_BT")) {
            return TipoInstruccion.IFGT;
        } else if (s.equals("OP_LEQ")) {
            return TipoInstruccion.IFLE;
        } else if (s.equals("OP_LT")) {
            return TipoInstruccion.IFLT;
        } else if (s.equals("OP_NEQ")) {
            return TipoInstruccion.NOT;
        } else if (s.equals("OP_AND")) {
            return TipoInstruccion.AND;
        } else if (s.equals("OP_OR")) {
            return TipoInstruccion.OR;
        } else {
            return null;
        }
    }

}
