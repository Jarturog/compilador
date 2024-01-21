package genCodigoIntermedio;

import java.util.ArrayList;
import java.util.HashMap;

import analizadorSemantico.DescripcionSimbolo;
import analizadorSemantico.TablaSimbolos;
import analizadorSintactico.symbols.*;
import genCodigoMaquina.Instruction.InstructionType;
import genCodigoMaquina.Instruction;
import analizadorSintactico.ParserSym;

public class GeneradorCIntermedio {

    private ArrayList<Instruction> c3a;

    private HashMap<String, VTEntry> variableTable;
    private HashMap<String, PTEntry> procedureTable;

    private PTEntry tablaProcesosActual;
    private String funcionActual;
    private int subnivelActual;
    private SymbolTipo tipoActual;
    private boolean esConstante;
    private String valorAsignacion;
    static final String DEF_FUNCTION = ".";

    private String declaracionActual; // Variable used in array declaration
    private int currentDecLength;
    private ArrayList<String> dimensionsToCheck;

    private int numE;
    private int numT;

    public GeneradorCIntermedio(SymbolScript script) {
        c3a = new ArrayList<>();
        variableTable = new HashMap<>();
        procedureTable = new HashMap<>();
        numE = 0;
        numT = 0;
        funcionActual = DEF_FUNCTION; // We use the format function.variable to store and access the variable table
        // This avoids the very real possibility of the user creating a variable of format tN (where N is a natural) 
        // which would cause undesired behaviour
        subnivelActual = 0;
        currentDecLength = -1;
        procesar(script);
    }

    private String nuevaVariable() {
        String t = "t" + numT++;
        VTEntry vte = new VTEntry(t);
        variableTable.put(funcionActual + subnivelActual + t, vte);
        if (tablaProcesosActual != null) {
            tablaProcesosActual.variableTable.put(subnivelActual + t, vte);
        }
        return t;
    }

    private String nuevaEtiqueta() {
        return "e" + numE++;
    }

    private VTEntry getVariable(String t) {
        VTEntry vte = variableTable.get(funcionActual + subnivelActual + t);
        int i = subnivelActual - 1;

        // We check up until -1 since parameters are in sublevel -1
        while (vte == null && i >= -1) {
            vte = variableTable.get(funcionActual + i-- + t);
        }
        if (vte == null) {
            vte = variableTable.get(DEF_FUNCTION + 0 + t);
        }
        return vte;
    }

    private void eliminarVariable(String t) {
        VTEntry vte = variableTable.remove(funcionActual + subnivelActual + t);
        int i = subnivelActual - 1;
        while (vte == null && i >= 0) {
            vte = variableTable.remove(funcionActual + i-- + t);
        }
        if (vte == null) {
            vte = variableTable.remove(DEF_FUNCTION + 0 + t);
        }
    }

    private void remplazarNombreVariable(String oldKey, String newKey) {
        VTEntry vte = getVariable(oldKey);
        eliminarVariable(oldKey);
        variableTable.put(funcionActual + subnivelActual + newKey, vte);
    }

    private String crearEntradaProcedimiento(String procName) {
        String internalFunctionName = procName + DEF_FUNCTION;
        PTEntry pte = new PTEntry();
        pte.eStart = nuevaEtiqueta();
        procedureTable.put(internalFunctionName, pte);
        return internalFunctionName;
    }

    private void añadirInstruccion(InstructionType instruction, String left, String right, String destination) {
        Instruction i = new Instruction(instruction, left, right, destination);
        c3a.add(i);
    }

    private void añadirInstruccion(InstructionType instruction, String left, String destination) {
        Instruction i = new Instruction(instruction, left, destination);
        c3a.add(i);
    }

    private void añadirInstruccion(InstructionType instruction, String destination) {
        Instruction i = new Instruction(instruction, destination);
        c3a.add(i);
    }

    public HashMap<String, VTEntry> getVariableTable() {
        return variableTable;
    }

    public HashMap<String, PTEntry> getProcedureTable() {
        return procedureTable;
    }

    public ArrayList<Instruction> getInstructions() {
        return c3a;
    }

    /*
        SCRIPT ::= SCRIPT_ELEMENTO:et1 SCRIPT:et2       {: RESULT = new SymbolScript(et1, et2, et1xleft, et1xright); :}
        | MAIN:et                               {: RESULT = new SymbolScript(et, etxleft, etxright); :}
        ;
     */
    //A partir de este se generaran el resto
    private void procesar(SymbolScript sScript) {
        ArrayList<SymbolDecs> declaraciones = new ArrayList<>();
        ArrayList<SymbolScriptElemento> tuplas = new ArrayList<>();
        ArrayList<SymbolScriptElemento> metodos = new ArrayList<>();
        int idMidDecs = 0, idMidTuplas = 0, idMidMetodos = 0;
        // elementos antes del main
        SymbolScriptElemento elem = sScript.elemento;
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
            sScript = sScript.siguienteElemento;
            elem = sScript.elemento;
        }
        SymbolMain scriptMainYElementos = sScript.main;
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
        // tuplas
        for (SymbolScriptElemento tupla : tuplas) {
            procesar(tupla);
        }
        // declaraciones
        for (SymbolDecs decs : declaraciones) {
            procesar(decs);
        }
        // metodos
        for (SymbolScriptElemento metodo : metodos) {
            procesar(metodo);
        }
        // main
        procesar(scriptMainYElementos);
    }

    /*
    SCRIPT_ELEMENTO ::= KW_METHOD:et1 TIPO_RETORNO:et2 ID:et3 LPAREN PARAMS:et4 RPAREN LKEY BODY:et5 RKEY   {: RESULT = new SymbolScriptElemento(et2, et3, et4, et5, et1xleft, et1xright); :}
        | DECS:et                                                       {: RESULT = new SymbolScriptElemento(et, etxleft, etxright); :}
        | KW_TUPLE:et1 ID:et2 LKEY MIEMBROS_TUPLA:et3 RKEY     {: RESULT = new SymbolScriptElemento(et2, et3, et1xleft, et1xright); :}
        ;
     */
    private void procesar(SymbolScriptElemento se) {
        //TODO 2 partes, o generar funcion que es como el de abajo y declarar cosas
    }

    /*
        MAIN ::= KW_METHOD:pos KW_VOID KW_MAIN:nombre LPAREN KW_STRING LBRACKET:l RBRACKET:r KW_ARGS:args RPAREN LKEY BODY:et RKEY  {: RESULT = new SymbolMain(nombre, args, l, r, et, posxleft, posxright); :}
        | MAIN:et1 SCRIPT_ELEMENTO:et2     {: RESULT = new SymbolMain(et1,et2, et1xleft, et1xright); :}
        ;
     */
    private void procesar(SymbolMain main) {
        //Primer caso, si tenemos un main y luego scriptElemento
        if (main.main == null) {
            return; // error
        }
        //Caso donde nuestro main es una funcion normal y corriente
        String nombre = main.nombreMain;
        funcionActual = this.crearEntradaProcedimiento(nombre);

        //TODO: los argumentos son un array []argumentos!, pero bueno
        //Gestionamos parametros 
        SymbolParams parametros = null;

        //Recibimos la tabla de procedimientos de esta funcion
        PTEntry tabla = this.procedureTable.get(nombre);
        this.tablaProcesosActual = tabla; //Cambiamos a la actual

        //Tratando parametros
        if (parametros != null) { // ----------------------------------------------------------------------------------------------
            procesar(parametros);

            //Ahora la tabla de dicha funcion tiene incorporado cuantos parametros tiene 
            tabla.numParams = parametros.paramsLista.numParametros;
        }

        añadirInstruccion(InstructionType.skip, tabla.eStart);
        añadirInstruccion(InstructionType.pmb, nombre);

        //Tratamiento del body
        SymbolBody cuerpo = main.main;
        if (cuerpo != null) {
            procesar(cuerpo);
        }

        //Al ser el main solo devolvera void, por lo que no hace nada
        añadirInstruccion(InstructionType.rtn, "0", nombre);

        //Etiqueta para el final de la funcion
        this.tablaProcesosActual.eEnd = nuevaEtiqueta();
        añadirInstruccion(InstructionType.skip, tablaProcesosActual.eEnd);

        //Ahora reseteamos la funcion actual y la tabla de procesos actual
        this.funcionActual = DEF_FUNCTION;
        this.tablaProcesosActual = null;
        
    }

    /*
        BODY ::= METODO_ELEMENTO:et1 BODY:et2   {: RESULT = new SymbolBody(et1, et2, et1xleft, et1xright); :}
        |                               {: RESULT = new SymbolBody(); :}
        ;

     */
    private void procesar(SymbolBody cuerpo) {
        while (cuerpo != null) {
            SymbolMetodoElemento elem = cuerpo.metodo;

            switch (elem.getTipo()) {
                case SymbolMetodoElemento.INSTR ->
                    procesar(elem.instruccion);
                case SymbolMetodoElemento.IF ->
                    procesar(elem.iff);
                case SymbolMetodoElemento.LOOP ->
                    procesar(elem.loop);
                case SymbolMetodoElemento.SWITCH ->
                    procesar(elem.sw);
            }
            cuerpo = cuerpo.siguienteMetodo;
        }
    }

    /*
        TIPO ::= TIPO_PRIMITIVO:t                       {: RESULT = new SymbolTipo(t, txleft, txright); :}
        | TIPO_PRIMITIVO:t DIMENSIONES:d        {: RESULT = new SymbolTipo(t, d, txleft, txright); :}
        | KW_TUPLE:t ID:i                       {: RESULT = new SymbolTipo(i, txleft, txright); :}
        | KW_TUPLE:t ID:i DIMENSIONES:d         {: RESULT = new SymbolTipo(i, d, txleft, txright); :}
        ;
     */
    private void procesar(SymbolTipo tipo) {
        //TODO: Posiblemente tratarlos unicamente donde se usen declaraciones
    }

    /*
    TIPO_PRIMITIVO ::= KW_BOOL:et              {: RESULT = new SymbolTipoPrimitivo(et, etxleft, etxright); :}
        | KW_INT:et                        {: RESULT = new SymbolTipoPrimitivo(et, etxleft, etxright); :}
        | KW_DOUBLE:et                     {: RESULT = new SymbolTipoPrimitivo(et, etxleft, etxright); :}
        | KW_CHAR:et                       {: RESULT = new SymbolTipoPrimitivo(et, etxleft, etxright); :}
        | KW_STRING:et                     {: RESULT = new SymbolTipoPrimitivo(et, etxleft, etxright); :}
        ;
     */
    private void procesar(SymbolTipoPrimitivo tipoPrimitivo) {
        //TODO: Posiblemente tratarlos unicamente donde se usen declaraciones
    }

    /*
        PARAMS ::= PARAMSLISTA:et                             {: RESULT = new SymbolParams(et, etxleft, etxright); :}   
        |                                             {: RESULT = new SymbolParams(); :}
        ;
     */
    private void procesar(SymbolParams params) {
        if (params != null) { //Ya que podemos tener params como lambda
            procesar(params.paramsLista);
        }
    }

    /*
        PARAMSLISTA ::= TIPO:et1 ID:id COMMA PARAMSLISTA:sig           {: RESULT = new SymbolParamsLista(et1, id, sig, et1xleft, et1xright); :}
        | TIPO:et ID:id                          {: RESULT = new SymbolParamsLista(et, id, etxleft, etxright); :}
        ;
     */
    private void procesar(SymbolParamsLista paramsLista) {
        //Crearemos una nueva variable
        String variable = this.nuevaVariable();
        VTEntry entrada = this.getVariable(variable);

        //Como este parametro vendra de una funcion o llamada el tablaProcesosActual != null
        this.tablaProcesosActual.params.add(variable); //Añadimos el parametro

        //Ahora meterelos el tipo dentro de la variable
        if (paramsLista.param.idTupla != null) { //Es una tupla
            entrada.dimensions.add(nuevaVariable());
        } else {
            //Tipo primitivo
            switch (paramsLista.param.tipo.getTipo()) { //Almacenaremos el tipo de variable que es
                case SymbolTipoPrimitivo.STRING -> entrada.type = ParserSym.STRING;
                case SymbolTipoPrimitivo.PROP -> entrada.type = ParserSym.PROP;
                case SymbolTipoPrimitivo.ENT -> entrada.type = ParserSym.ENT;
                case SymbolTipoPrimitivo.REAL -> entrada.type = ParserSym.REAL;
                case SymbolTipoPrimitivo.CAR -> entrada.type = ParserSym.CAR;
            }
            //Almacenaremos el tipo de variable que es
        }

        //Ahora generamos por si siguen habiendo mas parametros
        SymbolParamsLista siguiente = paramsLista.siguienteParam;
        if (siguiente != null) {
            procesar(siguiente);
        }

    }

    /*
        DECS ::= KW_CONST:et1 TIPO:et2 DEC_ASIG_LISTA:et3 ENDINSTR     {: RESULT = new SymbolDecs(true,et2,et3, et1xleft, et1xright); :}
                | TIPO:et1 DEC_ASIG_LISTA:et2 ENDINSTR                 {: RESULT = new SymbolDecs(false, et1,et2, et1xleft, et1xright); :}
        ; 
     */
    /*
        DEC_ASIG_LISTA ::= ID:et1 ASIG_BASICO:et2 COMMA DEC_ASIG_LISTA:et3       {: RESULT = new SymbolDecAsigLista(et1,et2,et3, et1xleft, et1xright); :}
        | ID:et1 ASIG_BASICO:et2                                   {: RESULT = new SymbolDecAsigLista(et1,et2, et1xleft, et1xright); :}
        ;

     */
    private void procesar(SymbolDecs decs) {
        SymbolDecAsigLista dec = decs.iddecslista;
        while (dec != null) {
            String v = nuevaVariable();
            VTEntry entrada = getVariable(v);

            this.declaracionActual = dec.id;
            if(decs.isConst){
                entrada.initialValue = (String)dec.value;
                replaceVarTableKey(v, dec.id);
                return;
            }
            replaceVarTableKey(v, dec.id);
            SymbolOperand value = dec.value;
            if(value != null){
                if(value.isConstant) {
                    Object val = value.value;
                    if(val instanceof Boolean) val = (Boolean) val ? Constants.TRUE : Constants.FALSE;
                    entrada.initialValue = val.toString();
                } else {
                    procesar(value);
                    if(!v.equals(value.getReferencia())) añadirInstruccion(InstructionType.copy, value.getReferencia(), v);
                }
            }
            dec.setReferencia(v);
            this.declaracionActual = null;
            currentDecLength = -1;
            dec = dec.siguienteDeclaracion;
        }
    }

    


    /*
        DIMENSIONES ::= LBRACKET:l OPERAND:et1 RBRACKET:r DIMENSIONES:et2   {: RESULT = new SymbolDimensiones(et1, et2, l, r, et1xleft, et1xright); :}
            | LBRACKET:l OPERAND:et1 RBRACKET:r                         {: RESULT = new SymbolDimensiones(et1, l, r, et1xleft, et1xright); :}
            ;
     */
    private void procesar(SymbolDimensiones dim) {
        //TODO
    }

    /*
        ASIG_BASICO ::= AS_ASSIGN OPERAND:et   {: RESULT = new SymbolAsigBasico(et, etxleft, etxright); :}
        |                       {: RESULT = new SymbolAsigBasico(); :}
        ;
     */
    private void procesar(SymbolAsigBasico asignBasico) {
        if (asignBasico.operando != null) { //Primer caso
            procesar(asignBasico.operando);
        }
    }

    /*
        INSTR ::= FCALL:et ENDINSTR    {: RESULT = new SymbolInstr(et,etxleft, etxright); :} 
        | RETURN:et            {: RESULT = new SymbolInstr(et,etxleft, etxright); :} 
        | DECS:et              {: RESULT = new SymbolInstr(et,etxleft, etxright); :}
        | ASIGS:et             {: RESULT = new SymbolInstr(et,etxleft, etxright); :}
        | SWAP:et              {: RESULT = new SymbolInstr(et,etxleft, etxright); :}
        ;
     */
    private void procesar(SymbolInstr instr) {
        switch (instr.getTipo()) {
            case SymbolInstr.ASIGS ->
                procesar(instr.asigs);
            case SymbolInstr.DECS ->
                procesar(instr.decs);
            case SymbolInstr.FCALL ->
                procesar(instr.fcall);
            case SymbolInstr.RET ->
                procesar(instr.ret);
            case SymbolInstr.SWAP ->
                procesar(instr.swap);
        }
    }

    /*
        FCALL ::= METODO_NOMBRE:et1 LPAREN OPERANDS_LISTA:et2 RPAREN   {: RESULT = new SymbolFCall(et1, et2, et1xleft, et1xright); :}
                | METODO_NOMBRE:et1 LPAREN RPAREN   {: RESULT = new SymbolFCall(et1, et1xleft, et1xright); :}
                ;
     */
    private void procesar(SymbolFCall fcall) {
        String nombre = (String) fcall.methodName.value;
        SymbolOperandsLista operandos = fcall.operandsLista;
        if (operandos != null) {
            procesar(operandos);
        }

        String etiqueta = nuevaVariable();
        if (nombre == null) {
            nombre = "" + fcall.methodName.specialMethod;
        }
        añadirInstruccion(InstructionType.call, nombre, etiqueta);
        fcall.setReferencia(etiqueta);
    }

    /*
        OPERANDS_LISTA ::= OPERAND:et COMMA OPERANDS_LISTA:ol     {: RESULT = new SymbolOperandsLista(et, ol, etxleft, etxright); :}
        | OPERAND:et                                    {: RESULT = new SymbolOperandsLista(et, etxleft, etxright); :}
        ;
     */
    private void procesar(SymbolOperandsLista opl) {

    }

    //POR COMPLETAR
    /* private void generate(SymbolLoop func){
        String nombre = func.nombreMetodo;
        if(nombre != null){
            funcionActual = crearEntradaProcedimiento(nombre);
        }else{
            funcionActual = crearEntradaProcedimiento(""+func.methodName.specialMethod);
        }
        
        SymbolOperandsLista opL = fcall.operandsLista;
        PTEntry pte = procedureTable.get(funcionActual);
        tablaProcesosActual = pte;
        
        if(opL != null){
            generate(opL);
            pte.numParams = opL.numOperandos;
        }
        
        añadirInstruccion(InstructionType.skip, pte.eStart);
        añadirInstruccion(InstructionType.pmb, nombre);
        
       // SymbolCuerpo 
    }*/
 /*
        RETURN ::= KW_RETURN ENDINSTR                               {: :} //Con este que hacemos?
                   | KW_RETURN OPERAND:et ENDINSTR                     {: RESULT = new SymbolReturn(et, etxleft, etxright); :}
                    ;
     */
    private void procesar(SymbolReturn ret) {
        String t;
        SymbolOperand op = ret.op;
        if (op != null) {
            procesar(op);
            t = op.getReferencia();
        } else {
            t = "0";
        }

        String nombre = funcionActual.replace(".", "");
        añadirInstruccion(InstructionType.rtn, t, nombre);
    }

    /* 
        SWAP ::= ID:et1 OP_SWAP ID:et2 ENDINSTR           {: RESULT = new SymbolSwap(et1, et2, et1xleft, et1xright); :}
        ;

    
     */
    private void procesar(SymbolSwap swap) {
        String variable1 = swap.op1;
        String variable2 = swap.op2;

        //Lo que haremos será intercambiar el contendio de referencia entre las 2
        remplazarNombreVariable(variable1, variable2);
        remplazarNombreVariable(variable2, variable1);
    }

    /*
        ASIGS ::= ASIG:et1 COMMA ASIGS:et2                              {: RESULT = new SymbolAsigs(et1, et2, et1xleft, et1xright); :}
        | ASIG:et ENDINSTR                                      {: RESULT = new SymbolAsigs(et, etxleft, etxright); :}
        ;
     */
    private void procesar(SymbolAsigs asigs) {
        while (asigs != null) {
            procesar(asigs.asig);
            asigs = asigs.siguienteAsig;
        }
    }

    /*
        ASIG ::= ID:et ASIG_OP:aop OPERAND:val                                  {: RESULT = new SymbolAsig(et, aop, val, etxleft, etxright); :}
        | ID:et1 LBRACKET OPERAND:et2 RBRACKET ASIG_OP:aop OPERAND:val  {: RESULT = new SymbolAsig(et1, et2, aop, val, et1xleft, et1xright); :}
        | ID:et1 OP_MEMBER ID:et2 ASIG_OP:aop OPERAND:val               {: RESULT = new SymbolAsig(et1, et2, aop, val, et1xleft, et1xright); :}
        | ID:et1 OP_INC:et2             {: RESULT = new SymbolAsig(true, ParserSym.OP_INC, et1, et2, et1xleft, et1xright); :}  %prec PREC_R_U_EXP
        | ID:et1 OP_DEC:et2             {: RESULT = new SymbolAsig(true, ParserSym.OP_DEC, et1, et2, et1xleft, et1xright); :}  %prec PREC_R_U_EXP
        | OP_INC:et1 ID:et2             {: RESULT = new SymbolAsig(false, ParserSym.OP_INC, et2, et1, et1xleft, et1xright); :} %prec PREC_L_U_EXP
        | OP_DEC:et1 ID:et2             {: RESULT = new SymbolAsig(false, ParserSym.OP_DEC, et2, et1, et1xleft, et1xright); :} %prec PREC_L_U_EXP
        ;
     */
    private void procesar(SymbolAsig asig) {
        // TODO: asignaciones...
    }

    private void procesar(SymbolOperand op) {

    }

    /*
        LOOP ::= KW_LOOP:et1 LOOP_COND:et2 LKEY BODY:et3 RKEY                   {: RESULT = new SymbolLoop(et2, et3, et1xleft, et1xright); :}
        | KW_DO:et1 LKEY BODY:et2 RKEY LOOP_COND:et3 ENDINSTR           {: RESULT = new SymbolLoop(et2, et3, et1xleft, et1xright); :}
        ;

    
     */
    private void procesar(SymbolLoop bucle) {

        //Ahora estamos dentro de un nuevo bloque
        this.subnivelActual += 1; //Estamos dentro!

        //Creamos la etiqueta de inicio
        String etiquetaInicio = nuevaEtiqueta();
        añadirInstruccion(InstructionType.skip, etiquetaInicio);

        SymbolLoopCond condicion = bucle.loopCond;
        procesar(condicion);
        String c = condicion.getReferencia();

        String etiquetaFin = nuevaEtiqueta();
        //Etiqueta de final de bucle
        añadirInstruccion(InstructionType.if_EQ, c, "0", etiquetaFin);

        //Ahora toda el contendido del for hay que procesarlo
        SymbolBody cuerpo = bucle.cuerpo;
        procesar(cuerpo);

        añadirInstruccion(InstructionType.go_to, etiquetaInicio);
        añadirInstruccion(InstructionType.skip, etiquetaFin);

        //Ahora hemos salido del bucle, por lo que ascendemos un nivel
        this.subnivelActual -= 1;

    }

    /*
    LOOP_COND ::= OPERAND:et                                                {: RESULT = new SymbolLoopCond(et, etxleft, etxright); :}
        | DECS:et1 ENDINSTR OPERAND:et2 ENDINSTR ASIGS:et3            {: RESULT = new SymbolLoopCond(et1, et2, et3, et1xleft, et1xright); :}
        ;
     */
    private void procesar(SymbolLoopCond cond) {
        if (cond.decs != null) {
            //Procesamos cada una de las partes
            procesar(cond.decs);

            procesar(cond.cond);

            procesar(cond.asig);

        } else {
            procesar(cond.cond);
        }
    }

    /*
    IF ::= KW_IF OPERAND:et1 LKEY BODY:et2 RKEY ELIFS:et3 ELSE:et4          {: RESULT = new SymbolIf(et1, et2, et3, et4, et1xleft, et1xright); :}
        ;

     */
    private void procesar(SymbolIf si) {
        //Ahora estamos dentro de un nuevo bloque
        this.subnivelActual += 1; //Estamos dentro!

        //Creamos la etiqueta de inicio
        String etiquetaInicio = nuevaEtiqueta();
        añadirInstruccion(InstructionType.skip, etiquetaInicio);

        SymbolOperand condicion = si.cond;
        procesar(condicion);
        String c = condicion.getReferencia();

        String etiquetaFin = nuevaEtiqueta();
        //Etiqueta de final de bucle
        añadirInstruccion(InstructionType.if_EQ, c, "0", etiquetaFin);

        //Ahora toda el contendido del for hay que procesarlo
        SymbolBody cuerpo = si.cuerpo;
        procesar(cuerpo);

        añadirInstruccion(InstructionType.skip, etiquetaFin);

        //Si tenemos un elseif
        if (si.elifs != null) {
            procesar(si.elifs);
        }

        //Si tenemos else
        if (si.els != null) {
            procesar(si.els);
        }

        //Ahora hemos salido del bucle, por lo que ascendemos un nivel
        this.subnivelActual -= 1;
    }

    /*
        ELIFS ::= ELIF:et1 ELIFS:et2                        {: RESULT = new SymbolElifs(et1, et2, et1xleft, et1xright); :}
        |                                           {: RESULT = new SymbolElifs(); :}
        ;
     */
    private void procesar(SymbolElifs elifs) {
        if (elifs.elif != null) {
            procesar(elifs.elif); //Procesamos el actual
            procesar(elifs.elifs); //Procesamos el siguiente
        }
    }

    /*
        ELIF ::= KW_ELIF OPERAND:et1 LKEY BODY:et2 RKEY     {: RESULT = new SymbolElif(et1, et2, et1xleft, et1xright); :}
        ;
     */
    private void procesar(SymbolElif elif) {

        //Creamos la etiqueta de inicio
        String etiquetaInicio = nuevaEtiqueta();
        añadirInstruccion(InstructionType.skip, etiquetaInicio);

        SymbolOperand condicion = elif.cond;
        procesar(condicion);
        String c = condicion.getReferencia();

        String etiquetaFin = nuevaEtiqueta();
        //Etiqueta de final de bucle
        añadirInstruccion(InstructionType.if_EQ, c, "0", etiquetaFin);

        //Ahora toda el contendido del for hay que procesarlo
        SymbolBody cuerpo = elif.cuerpo;
        procesar(cuerpo);

        añadirInstruccion(InstructionType.skip, etiquetaFin);

    }

    /*
        ELSE ::= KW_ELSE:pos LKEY BODY:et RKEY                  {: RESULT = new SymbolElse(et, posxleft, posxright); :}
        |                                           {: RESULT = new SymbolElse(); :}
        ;

     */
    private void procesar(SymbolElse symElse) {
        if (symElse.cuerpo != null) {
            procesar(symElse.cuerpo);
        }
    }

    /*
        
        SWITCH ::= KW_SWITCH OPERAND:et1 RKEY CASO:et2 PRED:et3 LKEY {: RESULT = new SymbolSwitch(et1, et2, et3, et1xleft, et1xright); :}
        ;   
     */
    private void procesar(SymbolSwitch swi) {

        this.subnivelActual += 1;
        //Creamos la etiqueta de inicio
        String etiquetaInicio = nuevaEtiqueta();
        añadirInstruccion(InstructionType.skip, etiquetaInicio);

        SymbolOperand condicion = swi.cond;
        procesar(condicion);
        String c = condicion.getReferencia();

        String etiquetaFin = nuevaEtiqueta();
        //Etiqueta de final de bucle
        añadirInstruccion(InstructionType.if_EQ, c, "0", etiquetaFin);

        procesar(swi.caso);
        procesar(swi.pred);

        añadirInstruccion(InstructionType.skip, etiquetaFin);

        this.subnivelActual -= 1;

    }

    /*
        CASO ::= CASO:et1 KW_CASE OPERAND:et2 ARROW BODY:et3 {: RESULT = new SymbolCaso(et1, et2, et3, et1xleft, et1xright); :}
        |                                            {: RESULT = new SymbolCaso(); :}
        ;
     */
    private void procesar(SymbolCaso caso) {
        if (caso != null) {
            procesar(caso.caso);
            
            String etiquetaInicio = nuevaEtiqueta();
            añadirInstruccion(InstructionType.skip, etiquetaInicio);

            SymbolOperand condicion = caso.cond;
            procesar(condicion);
            String c = condicion.getReferencia();

            String etiquetaFin = nuevaEtiqueta();
            //Etiqueta de final de bucle
            añadirInstruccion(InstructionType.if_EQ, c, "0", etiquetaFin);

            //Procesamos el cuerpo
            procesar(caso.cuerpo);

            añadirInstruccion(InstructionType.skip, etiquetaFin);
        }
    }

    /*
        PRED ::= KW_CASE KW_DEFAULT ARROW BODY:et            {: RESULT = new SymbolPred(et, etxleft, etxright); :}
        ;
     */
    private void procesar(SymbolPred pred) {
        if (pred.cuerpo != null) {
            procesar(pred.cuerpo);
        }
    }

}