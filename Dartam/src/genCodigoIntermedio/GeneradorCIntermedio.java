package genCodigoIntermedio;

import java.util.ArrayList;
import java.util.HashMap;

import analizadorSemantico.DescripcionSimbolo;
import analizadorSemantico.TablaSimbolos;
import analizadorSintactico.symbols.*;
import genCodigoMaquina.Instruccion.Tipo;
import genCodigoMaquina.Instruccion;
import analizadorSintactico.ParserSym;

public class GeneradorCIntermedio {

    //Array que guardará el código de 3 direcciones resultante de el procesamiento de cada Simbolo
    private ArrayList<Instruccion> c3a;

    //Tablas de variables y de funciones
    private HashMap<String, EntradaVariable> tablaVariables;
    private HashMap<String, EntradaProcedure> tablaProcedures;

    //Proceso actual en tratamiento
    private EntradaProcedure tablaProcesosActual;
    
    //Funcion actual en tratamiento
    private String funcionActual;
    
    //Subnivel del bloque
    private int subnivelActual;

    //Tipo del de la variable tratada actualmente
    private SymbolTipo tipoActual;
    
    //Indicador de si es constante
    private boolean esConstante;
    
    //Valor de la operacion
    private String valorAsignacion;
    
    static final String ES_FUNCION = ".";

    //Declaraciones actuales
    private String declaracionActual;
    private int longitudDecs;
    
    private ArrayList<String> dimensionesTratar;

    private int numEtiqueta; //Contador de las etiquetas colocadas
    private int numVariable;

    //Método constructor del codigo intermedio
    public GeneradorCIntermedio(SymbolScript script) {
        //Inicializamos código de 3 direcciones
        c3a = new ArrayList<>(); 
        numEtiqueta = 0; //A 0 porque no tenemos etiquetas
        numVariable = 0; //A 0 porque no tenemos variables
        
        //Inicializacion de tablas de variables y procedures
        tablaVariables = new HashMap<>();
        tablaProcedures = new HashMap<>();
        funcionActual = ES_FUNCION;
        
        subnivelActual = 0; //Nivel actual 0, 
        longitudDecs = -1; //No hay declaraciones
        procesar(script);
    }

    //Método para crear nuevas variables, devol
    private String nuevaVariable() {
        String nombre = "t" + numVariable++;
        //Nueva entrada
        EntradaVariable vte = new EntradaVariable(nombre);
        
        //Incorporamos dentro de la tabla
        tablaVariables.put(funcionActual + subnivelActual + nombre, vte); 
        if (tablaProcesosActual != null) {
            tablaProcesosActual.tablaVariables.put(subnivelActual + nombre, vte);
        }
        return nombre; //Devolvemos el nombre de la variable creada
    }

    //La creacion de una nueva etiqueta será unicamente devolver el nombre con el incremental de
    //las etiquetas actuales
    private String nuevaEtiqueta() {
        return "e" + numEtiqueta++; //1+
    }

    //Para recoger una variable lo que haremos serña recorrer la tabla de variables buscando
    private EntradaVariable getVariable(String t) {
        EntradaVariable vte = tablaVariables.get(funcionActual + subnivelActual + t);
        int i = subnivelActual - 1;
        
        //Lo buscamos
        for(; vte == null && i >= -1; i--){
            vte = tablaVariables.get(funcionActual + i-- + t);
        }
        
        //Si no encontramos la variable
        if (vte == null) {
            vte = tablaVariables.get(ES_FUNCION + 0 + t);
        }
        return vte;
    }

    //Para eliminar uan variable de la tabla, lo que haremos serña encontrarla y hacerle remove
    private void eliminarVariable(String t) {
        EntradaVariable vte = tablaVariables.remove(funcionActual + subnivelActual + t);
        int i = subnivelActual - 1;
        
        for (;vte == null && i >= 0;) {
            vte = tablaVariables.remove(funcionActual + i-- + t);
        }
        if (vte == null) {
            vte = tablaVariables.remove(ES_FUNCION + 0 + t);
        }
    }

    private void remplazarNombreVariable(String anterior, String nueva) {
        //Buscamos la variable
        EntradaVariable vte = getVariable(anterior);
        
        //La eliminamos
        eliminarVariable(anterior);
        
        //Insertamos la nueva variable pero con la informacion de la anterior
        tablaVariables.put(funcionActual + subnivelActual + nueva, vte);
    }

    private String crearEntradaProcedimiento(String nombre) {
        String nombreFuncion = nombre + ES_FUNCION;
        EntradaProcedure entrada = new EntradaProcedure();
        entrada.eInicio = nuevaEtiqueta();
        tablaProcedures.put(nombreFuncion, entrada);
        return nombreFuncion;
    }

    //Creacion de una nueva instruccion de 3 direcciones
    private void añadirInstruccion(Tipo tipo, String izq, String der, String des) {
        Instruccion i = new Instruccion(tipo, izq, der, des);
        c3a.add(i);
    }

    //Creacion de una nueva instruccion de 3 direcciones
    private void añadirInstruccion(Tipo tipo, String izq, String des) {
        Instruccion i = new Instruccion(tipo, izq, des);
        c3a.add(i);
    }

     //Creacion de una nueva instruccion de 3 direcciones
    private void añadirInstruccion(Tipo tipo,String des) {
        Instruccion i = new Instruccion(tipo, des);
        c3a.add(i);
    }
    
    
    public HashMap<String, EntradaVariable> getTablaVariables() {
        return tablaVariables;
    }

    public HashMap<String, EntradaProcedure> getTablaProcedures() {
        return tablaProcedures;
    }

    public ArrayList<Instruccion> getC3A() {
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
        EntradaProcedure tabla = this.tablaProcedures.get(nombre);
        this.tablaProcesosActual = tabla; //Cambiamos a la actual

        //Tratando parametros
        if (parametros != null) { // ----------------------------------------------------------------------------------------------
            procesar(parametros);

            //Ahora la tabla de dicha funcion tiene incorporado cuantos parametros tiene 
            tabla.numeroParametros = parametros.paramsLista.numParametros;
        }

        añadirInstruccion(Tipo.skip, tabla.eInicio);
        añadirInstruccion(Tipo.pmb, nombre);

        //Tratamiento del body
        SymbolBody cuerpo = main.main;
        if (cuerpo != null) {
            procesar(cuerpo);
        }

        //Al ser el main solo devolvera void, por lo que no hace nada
        añadirInstruccion(Tipo.rtn, "0", nombre);

        //Etiqueta para el final de la funcion
        this.tablaProcesosActual.eFin = nuevaEtiqueta();
        añadirInstruccion(Tipo.skip, tablaProcesosActual.eFin);

        //Ahora reseteamos la funcion actual y la tabla de procesos actual
        this.funcionActual = ES_FUNCION;
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
        EntradaVariable entrada = this.getVariable(variable);

        //Como este parametro vendra de una funcion o llamada el tablaProcesosActual != null
        this.tablaProcesosActual.parametros.add(variable); //Añadimos el parametro

        //Ahora meterelos el tipo dentro de la variable
        if (paramsLista.param.idTupla != null) { //Es una tupla
            entrada.dimensiones.add(nuevaVariable());
        } else {
            //Tipo primitivo
            switch (paramsLista.param.tipo.getTipo()) { //Almacenaremos el tipo de variable que es
                case SymbolTipoPrimitivo.STRING -> entrada.tipo = ParserSym.STRING;
                case SymbolTipoPrimitivo.PROP -> entrada.tipo = ParserSym.PROP;
                case SymbolTipoPrimitivo.ENT -> entrada.tipo = ParserSym.ENT;
                case SymbolTipoPrimitivo.REAL -> entrada.tipo = ParserSym.REAL;
                case SymbolTipoPrimitivo.CAR -> entrada.tipo = ParserSym.CAR;
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
        //TODO:
    }

    


    /*
        DIMENSIONES ::= LBRACKET:l OPERAND:et1 RBRACKET:r DIMENSIONES:et2   {: RESULT = new SymbolDimensiones(et1, et2, l, r, et1xleft, et1xright); :}
            | LBRACKET:l OPERAND:et1 RBRACKET:r                         {: RESULT = new SymbolDimensiones(et1, l, r, et1xleft, et1xright); :}
            ;
     */
    private void procesar(SymbolDimensiones dim) {
        //TODO:
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
        añadirInstruccion(Tipo.call, nombre, etiqueta);
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
        PTEntry pte = tablaProcedures.get(funcionActual);
        tablaProcesosActual = pte;
        
        if(opL != null){
            generate(opL);
            pte.numParams = opL.numOperandos;
        }
        
        añadirInstruccion(Tipo.skip, pte.eStart);
        añadirInstruccion(Tipo.pmb, nombre);
        
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
        añadirInstruccion(Tipo.rtn, t, nombre);
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
        // TODO: operandos
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
        añadirInstruccion(Tipo.skip, etiquetaInicio);

        SymbolLoopCond condicion = bucle.loopCond;
        procesar(condicion);
        String c = condicion.getReferencia();

        String etiquetaFin = nuevaEtiqueta();
        //Etiqueta de final de bucle
        añadirInstruccion(Tipo.if_EQ, c, "0", etiquetaFin);

        //Ahora toda el contendido del for hay que procesarlo
        SymbolBody cuerpo = bucle.cuerpo;
        procesar(cuerpo);

        añadirInstruccion(Tipo.go_to, etiquetaInicio);
        añadirInstruccion(Tipo.skip, etiquetaFin);

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
        añadirInstruccion(Tipo.skip, etiquetaInicio);

        SymbolOperand condicion = si.cond;
        procesar(condicion);
        String c = condicion.getReferencia();

        String etiquetaFin = nuevaEtiqueta();
        //Etiqueta de final de bucle
        añadirInstruccion(Tipo.if_EQ, c, "0", etiquetaFin);

        //Ahora toda el contendido del for hay que procesarlo
        SymbolBody cuerpo = si.cuerpo;
        procesar(cuerpo);

        añadirInstruccion(Tipo.skip, etiquetaFin);

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
        añadirInstruccion(Tipo.skip, etiquetaInicio);

        SymbolOperand condicion = elif.cond;
        procesar(condicion);
        String c = condicion.getReferencia();

        String etiquetaFin = nuevaEtiqueta();
        //Etiqueta de final de bucle
        añadirInstruccion(Tipo.if_EQ, c, "0", etiquetaFin);

        //Ahora toda el contendido del for hay que procesarlo
        SymbolBody cuerpo = elif.cuerpo;
        procesar(cuerpo);

        añadirInstruccion(Tipo.skip, etiquetaFin);

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
        añadirInstruccion(Tipo.skip, etiquetaInicio);

        SymbolOperand condicion = swi.cond;
        procesar(condicion);
        String c = condicion.getReferencia();

        String etiquetaFin = nuevaEtiqueta();
        //Etiqueta de final de bucle
        añadirInstruccion(Tipo.if_EQ, c, "0", etiquetaFin);

        procesar(swi.caso);
        procesar(swi.pred);

        añadirInstruccion(Tipo.skip, etiquetaFin);

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
            añadirInstruccion(Tipo.skip, etiquetaInicio);

            SymbolOperand condicion = caso.cond;
            procesar(condicion);
            String c = condicion.getReferencia();

            String etiquetaFin = nuevaEtiqueta();
            //Etiqueta de final de bucle
            añadirInstruccion(Tipo.if_EQ, c, "0", etiquetaFin);

            //Procesamos el cuerpo
            procesar(caso.cuerpo);

            añadirInstruccion(Tipo.skip, etiquetaFin);
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