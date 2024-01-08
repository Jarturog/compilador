package genCodigoIntermedio;

import java.util.ArrayList;
import java.util.Hashtable;

import analizadorSintactico.symbols.*;
import genCodigoMaquina.Instruction.InstructionType;
import genCodigoMaquina.Instruction;
import analizadorSintactico.ParserSym;

public class GeneradorCIntermedio {

    private ArrayList<Instruction> c3a;

    private Hashtable<String, VTEntry> variableTable;
    private Hashtable<String, PTEntry> procedureTable;
    
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
   
    public GeneradorCIntermedio(SymbolScript script){
        c3a = new ArrayList<>();
        variableTable = new Hashtable<>();
        procedureTable = new Hashtable<>();
        numE = 0;
        numT = 0;
        funcionActual = DEF_FUNCTION; // We use the format function.variable to store and access the variable table
        // This avoids the very real possibility of the user creating a variable of format tN (where N is a natural) 
        // which would cause undesired behaviour
        subnivelActual = 0;
        currentDecLength = -1;
    }
    
    private String nuevaVariable(){
        String t = "t" + numT++;
        VTEntry vte = new VTEntry(t);
        variableTable.put(funcionActual + subnivelActual + t, vte);
        if(tablaProcesosActual != null) tablaProcesosActual.variableTable.put(subnivelActual + t, vte);
        return t;
    }

    private String nuevaEtiqueta(){
        return "e" + numE++;
    }

    private VTEntry getVariable(String t){
        VTEntry vte = variableTable.get(funcionActual + subnivelActual + t);
        int i = subnivelActual-1;

        // We check up until -1 since parameters are in sublevel -1
        while(vte == null && i >= -1) {
            vte = variableTable.get(funcionActual + i-- + t);
        }
        if(vte == null) {
            vte = variableTable.get(DEF_FUNCTION + 0 + t);
        }
        return vte;
    }

    private void eliminarVariable(String t){
        VTEntry vte = variableTable.remove(funcionActual + subnivelActual + t);
        int i = subnivelActual-1;
        while(vte == null && i >= 0) {
            vte = variableTable.remove(funcionActual + i-- + t);
        }
        if(vte == null) {
            vte = variableTable.remove(DEF_FUNCTION + 0 + t);
        }
    }
    
    private void remplazarNombreVariable(String oldKey, String newKey){
        VTEntry vte = getVariable(oldKey);
        eliminarVariable(oldKey);
        variableTable.put(funcionActual + subnivelActual + newKey, vte);
    }

    private String crearEntradaProcedimiento(String procName){
        String internalFunctionName = procName + DEF_FUNCTION;
        PTEntry pte = new PTEntry();
        pte.eStart = nuevaEtiqueta();
        procedureTable.put(internalFunctionName, pte);
        return internalFunctionName;
    }

    private void añadirInstruccion(InstructionType instruction, String left, String right, String destination){
        Instruction i = new Instruction(instruction, left, right, destination);
        c3a.add(i);
    }
    
    private void añadirInstruccion(InstructionType instruction, String left, String destination){
        Instruction i = new Instruction(instruction, left, destination);
        c3a.add(i);
    }
    
    private void añadirInstruccion(InstructionType instruction, String destination){
        Instruction i = new Instruction(instruction, destination);
        c3a.add(i);
    }

    public Hashtable<String, VTEntry> getVariableTable(){
        return variableTable;
    }

    public Hashtable<String, PTEntry> getProcedureTable(){
        return procedureTable;
    }

    public ArrayList<Instruction> getInstructions(){
        return c3a;
    }
    
   /*
        SCRIPT ::= SCRIPT_ELEMENTO:et1 SCRIPT:et2       {: RESULT = new SymbolScript(et1, et2, et1xleft, et1xright); :}
        | MAIN:et                               {: RESULT = new SymbolScript(et, etxleft, etxright); :}
        ;
    */
    
    //A partir de este se generaran el resto
    private void procesar(SymbolScript sScript){
       
       //Generación del primer elemento
       SymbolScriptElemento sse = sScript.elemento;
       if(sse != null){
           procesar(sse);
       }
       
       //Generacion del siguiente, llamada recursiva
       SymbolScript sScript2 = sScript.siguienteElemento;
       if(sScript2 != null){
           procesar(sScript2);
       }
       
       //Generaremos codigo intermedio del main
       SymbolMain main = sScript.main;
       if(main != null){
           procesar(main);
       }
       
    }
    
    /*
    SCRIPT_ELEMENTO ::= KW_METHOD:et1 TIPO_RETORNO:et2 ID:et3 LPAREN PARAMS:et4 RPAREN LKEY BODY:et5 RKEY   {: RESULT = new SymbolScriptElemento(et2, et3, et4, et5, et1xleft, et1xright); :}
        | DECS:et                                                       {: RESULT = new SymbolScriptElemento(et, etxleft, etxright); :}
        | KW_TUPLE:et1 ID:et2 LKEY MIEMBROS_TUPLA:et3 RKEY     {: RESULT = new SymbolScriptElemento(et2, et3, et1xleft, et1xright); :}
        ;
    */
    private void procesar(SymbolScriptElemento se){
        //TODO 2 partes, o generar funcion que es como el de abajo y declarar cosas
    }
    
    /*
        MAIN ::= KW_METHOD:pos KW_VOID KW_MAIN:nombre LPAREN KW_STRING LBRACKET:l RBRACKET:r KW_ARGS:args RPAREN LKEY BODY:et RKEY  {: RESULT = new SymbolMain(nombre, args, l, r, et, posxleft, posxright); :}
        | MAIN:et1 SCRIPT_ELEMENTO:et2     {: RESULT = new SymbolMain(et1,et2, et1xleft, et1xright); :}
        ;
    */
    private void procesar(SymbolMain main){
        //Primer caso, si tenemos un main y luego scriptElemento
        if(main.main != null){
            procesar(main.siguienteElemento);
            procesar(main.elemento);
        }else{ //Caso donde nuestro main es una funcion normal y corriente
            String nombre = main.nombreMain; 
            funcionActual = this.crearEntradaProcedimiento(nombre);
            
            
            //TODO: los argumentos son un array []argumentos!, pero bueno
            //Gestionamos parametros 
            SymbolParams parametros = null;
            
            //Recibimos la tabla de procedimientos de esta funcion
            PTEntry tabla = this.procedureTable.get(nombre);
            this.tablaProcesosActual = tabla; //Cambiamos a la actual
            
            //Tratando parametros
            if(parametros != null){
                procesar(parametros);
                
                //Ahora la tabla de dicha funcion tiene incorporado cuantos parametros tiene 
                tabla.numParams = parametros.paramsLista.numParametros;
            }
            
            añadirInstruccion(InstructionType.skip, tabla.eStart);
            añadirInstruccion(InstructionType.pmb, nombre);
            
            //Tratamiento del body
            SymbolBody cuerpo = main.main;
            if(cuerpo != null){
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
    }
    
    /*
        BODY ::= METODO_ELEMENTO:et1 BODY:et2   {: RESULT = new SymbolBody(et1, et2, et1xleft, et1xright); :}
        |                               {: RESULT = new SymbolBody(); :}
        ;

    */
    private void procesar(SymbolBody cuerpo){
        if(cuerpo != null){
            
            //Procesamos el actual
            if(cuerpo.metodo != null){
                procesar(cuerpo.metodo);
            }
            
            //procesamos los siguientes
            SymbolBody cuerpo2 = cuerpo.siguienteMetodo;
            if(cuerpo2 != null){
                procesar(cuerpo2);
            }
            
        }
    }
    
    /*
        TIPO ::= TIPO_PRIMITIVO:t                       {: RESULT = new SymbolTipo(t, txleft, txright); :}
        | TIPO_PRIMITIVO:t DIMENSIONES:d        {: RESULT = new SymbolTipo(t, d, txleft, txright); :}
        | KW_TUPLE:t ID:i                       {: RESULT = new SymbolTipo(i, txleft, txright); :}
        | KW_TUPLE:t ID:i DIMENSIONES:d         {: RESULT = new SymbolTipo(i, d, txleft, txright); :}
        ;
    */
    private void procesar(SymbolTipo tipo){
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
    private void procesar(SymbolTipoPrimitivo tipoPrimitivo){
        //TODO: Posiblemente tratarlos unicamente donde se usen declaraciones
    }
    
    
    /*
        PARAMS ::= PARAMSLISTA:et                             {: RESULT = new SymbolParams(et, etxleft, etxright); :}   
        |                                             {: RESULT = new SymbolParams(); :}
        ;
    */
    private void procesar(SymbolParams params){
        if(params != null){ //Ya que podemos tener params como lambda
            procesar(params.paramsLista);
        }
    }
    
    /*
        PARAMSLISTA ::= TIPO:et1 ID:id COMMA PARAMSLISTA:sig           {: RESULT = new SymbolParamsLista(et1, id, sig, et1xleft, et1xright); :}
        | TIPO:et ID:id                          {: RESULT = new SymbolParamsLista(et, id, etxleft, etxright); :}
        ;
    */
    private void procesar(SymbolParamsLista paramsLista){
        //Crearemos una nueva variable
        String variable = this.nuevaVariable();
        VTEntry entrada = this.getVariable(variable);
        
        //Como este parametro vendra de una funcion o llamada el tablaProcesosActual != null
        this.tablaProcesosActual.params.add(variable); //Añadimos el parametro
        
        //Ahora meterelos el tipo dentro de la variable
        if(paramsLista.param.idTupla != null){ //Es una tupla
            entrada.dimensions.add(nuevaVariable());
        }else{
            //Tipo primitivo
            switch(paramsLista.param.tipo.tipo){ //Almacenaremos el tipo de variable que es
                case "STRING":
                    entrada.type = ParserSym.STRING;
                    break;
                case "PROP":
                    entrada.type = ParserSym.PROP;
                    break;
                case "ENT":
                    entrada.type = ParserSym.ENT;
                    break;
                case "REAL":
                    entrada.type = ParserSym.REAL;
                    break;
                case "CAR":
                    entrada.type = ParserSym.CAR;
                    break;
            }
        }
        
        //Ahora generamos por si siguen habiendo mas parametros
        SymbolParamsLista siguiente = paramsLista.siguienteParam;
        if(siguiente != null){
            procesar(siguiente);
        }
         
        
    }
    
    
    /*
        DECS ::= KW_CONST:et1 TIPO:et2 DEC_ASIG_LISTA:et3 ENDINSTR     {: RESULT = new SymbolDecs(true,et2,et3, et1xleft, et1xright); :}
                | TIPO:et1 DEC_ASIG_LISTA:et2 ENDINSTR                 {: RESULT = new SymbolDecs(false, et1,et2, et1xleft, et1xright); :}
        ; 
    */
    private void procesar(SymbolDecs declaraciones){
        if(declaraciones.isConst){ //Si es una declaración de varaibles constantes
            this.esConstante = true;
            this.tipoActual = declaraciones.tipo;
            procesar(declaraciones.iddecslista);
        }else{ //Si no son constantes
            this.esConstante = true;
            this.tipoActual = declaraciones.tipo;
            procesar(declaraciones.iddecslista);
        }
            
    }
    
    
    /*
        DEC_ASIG_LISTA ::= ID:et1 ASIG_BASICO:et2 COMMA DEC_ASIG_LISTA:et3       {: RESULT = new SymbolDecAsigLista(et1,et2,et3, et1xleft, et1xright); :}
        | ID:et1 ASIG_BASICO:et2                                   {: RESULT = new SymbolDecAsigLista(et1,et2, et1xleft, et1xright); :}
        ;

    */
    private void procesar(SymbolDecAsigLista dal){
        String v = nuevaVariable();
        VTEntry entrada = getVariable(v);
        this.declaracionActual = dal.id;
        
        if(this.esConstante && !entrada.initialValue.equals("0")){ //Las variables son constantes y no se les a asignado nadas
            procesar(dal.asignacion); //Vamos a procesarlo y recoger el valor de lo que asignamos
            entrada.initialValue = this.valorAsignacion;
            entrada.isConstant = true;
        }else{
            procesar(dal.asignacion); //Vamos a procesarlo y recoger el valor de lo que asignamos
            entrada.initialValue = this.valorAsignacion;
            entrada.isConstant = false;
        }
        
        if(dal.siguienteDeclaracion != null){
            procesar(dal.siguienteDeclaracion);
        }      
    }
    
    
    /*
        DIMENSIONES ::= LBRACKET:l OPERAND:et1 RBRACKET:r DIMENSIONES:et2   {: RESULT = new SymbolDimensiones(et1, et2, l, r, et1xleft, et1xright); :}
            | LBRACKET:l OPERAND:et1 RBRACKET:r                         {: RESULT = new SymbolDimensiones(et1, l, r, et1xleft, et1xright); :}
            ;
    */
    private void procesar(SymbolDimensiones dim){
        //TODO
    }
    
    /*
        ASIG_BASICO ::= AS_ASSIGN OPERAND:et   {: RESULT = new SymbolAsigBasico(et, etxleft, etxright); :}
        |                       {: RESULT = new SymbolAsigBasico(); :}
        ;
    */
    private void procesar(SymbolAsigBasico asignBasico){
        if(asignBasico.operando != null){ //Primer caso
            procesar(asignBasico.operando);
        }
    }
    
    
    /*
        METODO_ELEMENTO ::= INSTR:et    {: RESULT = new SymbolMetodoElemento(et, etxleft, etxright); :}
        | LOOP:et               {: RESULT = new SymbolMetodoElemento(et, etxleft, etxright); :}
        | IF:et                 {: RESULT = new SymbolMetodoElemento(et, etxleft, etxright); :}
        | SWITCH:et             {: RESULT = new SymbolMetodoElemento(et, etxleft, etxright); :}
        ;
    */
    private void procesar(SymbolMetodoElemento metodo){
        if(metodo.instruccion != null){
            procesar(metodo.instruccion);
        }else if(metodo.loop != null){
            procesar(metodo.loop);
        }else if(metodo.iff != null){
            procesar(metodo.iff);
        }else{
            procesar(metodo.sw);
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
    private void procesar(SymbolInstr instr){
        if(instr.ret != null){
            procesar(instr.ret);
        }else if(instr.fcall != null){
            procesar(instr.fcall);
        }else if(instr.decs != null){
            procesar(instr.decs);
        }else if(instr.asigs != null){
            procesar(instr.asigs);
        }else{
            procesar(instr.swap);
        }
    }
    
    /*
        FCALL ::= METODO_NOMBRE:et1 LPAREN OPERANDS_LISTA:et2 RPAREN   {: RESULT = new SymbolFCall(et1, et2, et1xleft, et1xright); :}
                | METODO_NOMBRE:et1 LPAREN RPAREN   {: RESULT = new SymbolFCall(et1, et1xleft, et1xright); :}
                ;
    */
    private void procesar(SymbolFCall fcall){
        String nombre = (String)fcall.methodName.value;
        SymbolOperandsLista operandos = fcall.operandsLista;
        if(operandos != null){
            generate(operandos);
        }
        
        String etiqueta = nuevaVariable();
        if(nombre == null){
            nombre = ""+fcall.methodName.specialMethod;
        }
        añadirInstruccion(InstructionType.call, nombre, etiqueta);
        fcall.setReferencia(etiqueta);
    }
    
    /*
        OPERANDS_LISTA ::= OPERAND:et COMMA OPERANDS_LISTA:ol     {: RESULT = new SymbolOperandsLista(et, ol, etxleft, etxright); :}
        | OPERAND:et                                    {: RESULT = new SymbolOperandsLista(et, etxleft, etxright); :}
        ;
    */
    private void procesar(SymbolOperandsLista opl){
        
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
    private void procesar(SymbolReturn ret){
        String t;
        SymbolOperand op = ret.op;
        if(op != null){
            generate(op);
            t = op.getReferencia();
        }else{
            t = "0";
        }
        
        String nombre = funcionActual.replace(".", "");
        añadirInstruccion(InstructionType.rtn, t, nombre);
    }
    
    private void generate(SymbolSwap swap){
    
    }
    
    private void generate(SymbolDecs decs){
    
    }
        
    //FALTA POR MIRAR
    private void generate(SymbolBody insBody){
    
    }
    
    private void generate(SymbolOperand op){
        
    }
    
    private void generate(SymbolOperandsLista opL){
        
    }
    
    private void generate(SymbolTipo tipo){
        
    }
    
    private void procesar(SymbolMetodoElemento me){
        
    }
        
        
        
    
    
}
    
    /*
    private void generate(SymbolArrSuff arrSuff){
        // if first 
        //     t1 = i = arrSuff.reference = getIndex().reference;
        // else 
        //     t2 = t1 * d2 
        //     t3 = t2 + i2
        // end if
        // Our grammar does not allow us to differentiate between the first and the middle array suffixes
        // So we can generalize it by saying
        // foreach n
        //      t(n+1) = t(n) * d(n)
        //      t(n+2) = t(n+1) + i(n)
        // Where t(1) = 0. This is why if it's the first time calling this function, tn is 0
        String tn = arrSuff.reference == null ? "0" : arrSuff.reference; // Why tn is this symbol's reference is further explained in a few lines
        //String tn = index.reference;
        String dimensions = "0";
        if(dimensionsToCheck.size() != 0) {
            dimensions = dimensionsToCheck.remove(0);
        }
        String tn1 = nuevaVariableiable();
        añadirInstruccion(InstructionType.prod, tn, ""+dimensions, tn1); // tn1 = tn * d
        SymbolOperation index = arrSuff.getIndex();
        generate(index);
        String in = index.reference;
        String tn2 = nuevaVariableiable();
        añadirInstruccion(InstructionType.add, tn1, in, tn2); // tn2 = tn1 + in
        
        // Next iteration
        SymbolArrSuff next = arrSuff.getNext();
        if(next != null){
            next.reference = tn2; // We pass tn2 as a reference. This is a "cheat" to allow communication between father and son, but is
            // technically a devirtualization of the idea of the reference. 
            // This is also the reason why at the start of this function we had to check if the reference was null or not, as only the first iteration
            // should be null
            generate(next);
            tn2 = next.reference;
        }

        arrSuff.reference = tn2;
    }


    private void generate(SymbolAssign assign){
        SymbolVar var = assign.getVariable();
        generate(var);
        String t = var.reference;

        SymbolOperation rSide = assign.getRightSide();
        generate(rSide);
        String t1 = rSide.reference;

        if(rSide.type.isType(KW_ARRAY)){
            añadirInstruccion(InstructionType.point, t1, t);
        } else añadirInstruccion(InstructionType.copy, t1, t);
    }

    private void generate(SymbolDec dec){
        String t = nuevaVariableiable();
        VTEntry vte = getVariable(t);
        currentDec = dec.variableName;
        if(dec.isConstant){
            // añadirInstruccion(InstructionType.copy, dec.getValue().getSemanticValue().toString(), t);
            vte.initialValue = dec.getValue().getSemanticValue().toString();
            replaceVarTableKey(t, dec.variableName);
            return;
        }
        replaceVarTableKey(t, dec.variableName);
        SymbolOperation value = dec.getValue();
        if(value != null){
            if(value.isConstant) {
                Object v = value.getSemanticValue();
                if(v instanceof Boolean) v = (Boolean) v ? VTEntry.TRUE : VTEntry.FALSE;
                vte.initialValue = v.toString();
            } else {
                generate(value);
                if(!t.equals(value.reference)) añadirInstruccion(InstructionType.copy, value.reference, t);
            }
        }
        dec.reference = t;
        currentDec = null;
        currentDecLength = -1;
    }

  
    private void generate(SymbolDecs decs){
        // No code is generated here, equal to its semantic.generate() equivalent
        SymbolBase dec = decs.getDeclaration();
        if(dec instanceof SymbolDec) generate((SymbolDec) dec);
        else if (dec instanceof SymbolFunc) {
            String eContDecs = nuevaEtiqueta();
            añadirInstruccion(InstructionType.go_to, eContDecs); // We must skip the function during executions unless called
            generate((SymbolFunc) dec);
            añadirInstruccion(InstructionType.skip, eContDecs);
        }
        
        SymbolDecs nextDecs = decs.getNext();
        if (nextDecs != null) generate(nextDecs);
    }


    private void generate(SymbolElse sElse){
        SymbolIf nextIf = sElse.getIf();
        SymbolInstrs instrs = sElse.getInstructions();
        if(nextIf != null) generate(nextIf);
        else if(instrs != null) {
            generate(instrs);
        }
    }


    private void generate(SymbolFor sFor){
        subnivelActual++;
        
        SymbolBase init = sFor.getInit();
        if(init instanceof SymbolDec) generate((SymbolDec) init);
        else if(init instanceof SymbolAssign) generate((SymbolAssign) init);
        else if(init instanceof SymbolSwap) generate((SymbolSwap) init);
        else if(init instanceof SymbolFuncCall) generate((SymbolFuncCall) init);

        String eStart = nuevaEtiqueta();
        añadirInstruccion(InstructionType.skip, eStart);

        SymbolOperation cond = sFor.getCondition();
        generate(cond);
        String tCond = cond.reference;

        String eEnd = nuevaEtiqueta();
        añadirInstruccion(InstructionType.if_EQ, tCond, "0", eEnd);

        SymbolInstrs instrs = sFor.getInstructions();
        generate(instrs);

        SymbolBase end = sFor.getFinal();
        if(end instanceof SymbolDec) generate((SymbolDec) end);
        else if(end instanceof SymbolAssign) generate((SymbolAssign) end);
        else if(end instanceof SymbolSwap) generate((SymbolSwap) end);
        else if(end instanceof SymbolFuncCall) generate((SymbolFuncCall) end);

        añadirInstruccion(InstructionType.go_to, eStart);
        añadirInstruccion(InstructionType.skip, eEnd);

        subnivelActual--;
    }


    private void generate(SymbolFunc func){
        String name = func.getFunctionName();

        funcionActual = createPTEntry(name);

        SymbolArgs args = func.getArgs();
        PTEntry pte = procedureTable.get(funcionActual);
        tablaProcesosActual = pte;

        if(args != null) {
            generate(args);
            pte.numParams = args.getNArgs();
        }
        
        añadirInstruccion(InstructionType.skip, pte.eStart);
        añadirInstruccion(InstructionType.pmb, name);
        // pte.tReturn = nuevaVariableiable();
        SymbolInstrs instrs = func.getInstructions();
        if(instrs != null) generate(instrs);

        // If no return was found, we must put it at the end
        // añadirInstruccion(InstructionType.rtn, "0", name);
        if(func.getType().isType(KW_VOID)) 
            añadirInstruccion(InstructionType.rtn, "0", name);
        tablaProcesosActual.eEnd = nuevaEtiqueta();
        añadirInstruccion(InstructionType.skip, tablaProcesosActual.eEnd);

        funcionActual = DEF_FUNCTION;
        tablaProcesosActual = null;
    }

 
    private void generate(SymbolFuncCall functionCall){
        String funcName = functionCall.getFunctionName();
        SymbolParams params = functionCall.getParams();
        if(params != null) generate(params);

        String t = nuevaVariableiable();
        añadirInstruccion(InstructionType.call, funcName, t);
        functionCall.reference = t;
    }

 
    private void generate(SymbolIf sIf){
        // We change the current function so as to denote that this is a different block, but we must restore it after.
        subnivelActual++;

        SymbolOperation cond = sIf.getCondition();
        generate(cond);
        String tCond = cond.reference;

        String eElse = nuevaEtiqueta();
        añadirInstruccion(InstructionType.if_EQ, tCond, "0", eElse);

        SymbolInstrs instrs = sIf.getInstructions();
        generate(instrs);

        añadirInstruccion(InstructionType.skip, eElse);
        
        SymbolElse sElse = sIf.getElse();
        if(sElse != null) generate(sElse);
        subnivelActual--;
    }

    private void generate(SymbolIn in){
        SymbolVar oper = in.getVariable();
        generate(oper);
        String t = oper.reference;
        añadirInstruccion(InstructionType.in, t);
    }

    private void generate(SymbolInstr instruction){
        switch(instruction.getInstructionType()){
            case instDeclaration:
                generate((SymbolDec) instruction);
                return;
            case instAssignation:
                generate((SymbolAssign) instruction);
                return;
            case instSwap:
                generate((SymbolSwap) instruction);
                return;
            case instFunctionCall:
                generate((SymbolFuncCall) instruction);
                return;
            case instReturn:
                generate((SymbolReturn) instruction);
                return;
            case instIf:
                generate((SymbolIf) instruction);
                return;
            case instLoop:
                generate((SymbolLoop) instruction);
                return;
            case instFor:
                generate((SymbolFor) instruction);
                return;
            case instIn:
                generate((SymbolIn) instruction);
                return;
            case instOut:
                generate((SymbolOut) instruction);
                return;
            default:
                // Nothing to do
        }
    }


    private void generate(SymbolInstrs instructions){
        generate(instructions.getInstruction());
        SymbolInstrs next = instructions.getNext();
        if(next != null) generate(next);
    }


    private void generate(SymbolList list){
        String t;
        VTEntry vte = null;
        // This is to deal with the indexing of the lists
        if(currentDec != null){
            vte = getVariable(currentDec);
            t = vte.tName;
        } else  {
            t = nuevaVariableiable();
            currentDec = t;
        }
        String right = "";

        int length = list.type.arrayLength;
        // We store the dimensions of the list inside of the variable table
        if(vte != null && length != VTEntry.UNKNOWN){
            if(vte.dimensions.size() <= list.type.getArrayDepth()){
                vte.dimensions.add(""+length);
            }
        }

        SymbolOperation value = list.getValue();
        if(!value.isConstant) { 
            generate(value);
            right = value.reference;
        } else if(value.getSemanticValue() instanceof Character){
            char cVal = (Character) value.getSemanticValue();
            right = "" + (int) cVal; // We store ASCII value
            getVariable(currentDec).type = ParserSym.KW_CHAR;
        } else if(value.getSemanticValue() instanceof Boolean){
            boolean bVal = (Boolean) value.getSemanticValue();
            // We store true or false in bits, not in Boolean
            right = "" + (bVal ? VTEntry.TRUE : VTEntry.FALSE);
        } else if(value.getSemanticValue() instanceof Integer){
            right = "" + (int) value.getSemanticValue();
        }
        
        // If the depth is 1 it means we are now on the "ground level" of the array and we can start to assign
        if(list.type.getArrayDepth() == 1){
            currentDecLength++;
            // We must store the generated values as an index of the first one, t.
            int size = VTEntry.CHAR_BYTES;
            if(value.type.isType(ParserSym.KW_INT)) size = VTEntry.INTEGER_BYTES;
            int displacement = currentDecLength * size;
            añadirInstruccion(InstructionType.ind_ass, ""+displacement, right, getVariable(currentDec).tName);
        }

        SymbolList next = list.getNext();
        if(next != null && next.getValue() != null) {
            generate(next);
        }

        list.reference = t;
    }

    private void generate(SymbolLoop loop){
        subnivelActual++;

        String eStart = nuevaEtiqueta();
        añadirInstruccion(InstructionType.skip, eStart);

        SymbolOperation cond = loop.getCondition();
        generate(cond);
        String tCond = cond.reference;

        String eEnd = nuevaEtiqueta();
        añadirInstruccion(InstructionType.if_EQ, tCond, "0", eEnd);

        SymbolInstrs instrs = loop.getInstructions();
        generate(instrs);

        añadirInstruccion(InstructionType.go_to, eStart);
        añadirInstruccion(InstructionType.skip, eEnd);

        subnivelActual--;
    }

    private void generate(SymbolMain main){
        SymbolInstrs instrs = main.getInstructions();
        if(instrs != null) {
            // We prepare the procedure table and insert the info about the main procedure
            funcionActual = createPTEntry("main");
            tablaProcesosActual = procedureTable.get(funcionActual);
            String eMain = tablaProcesosActual.eStart;

            añadirInstruccion(InstructionType.skip, eMain);
            añadirInstruccion(InstructionType.pmb, "main");
            generate(instrs);

            funcionActual = DEF_FUNCTION; // Reset current function
            tablaProcesosActual = null;
        }
    }

    private void generate(SymbolOperand operand){
        String t = "";

        Object value = operand.getValue();

        if(!operand.isLeaf()){
            SymbolOperation operation = (SymbolOperation) operand.getValue();
            generate(operation);
            t = operation.reference;
        } else if(value instanceof SymbolValue){
            SymbolValue sValue = (SymbolValue) operand.getValue();
            generate(sValue);
            t = sValue.reference;
        } else if(value instanceof Integer){
            t = nuevaVariableiable();
            añadirInstruccion(InstructionType.copy, value.toString(), t);
            //getVariable(t).occupation = VTEntry.INTEGER_BYTES;
        } else if(value instanceof Character){
            t = nuevaVariableiable();
            char cVal = (Character) value;
            // We store the ASCII value
            añadirInstruccion(InstructionType.copy, "" + (int) cVal, t);
            getVariable(t).type = ParserSym.KW_CHAR;
        } else if(value instanceof Boolean){
            t = nuevaVariableiable();
            boolean bVal = (Boolean) value;
            // We store true or false in bits, not in Boolean
            añadirInstruccion(InstructionType.copy, "" + (bVal ? VTEntry.TRUE : VTEntry.FALSE), t);
        }

        if(!operand.isConstant && operand.isNegated()){
            if(operand.type.isType(ParserSym.KW_INT)){
                añadirInstruccion(InstructionType.neg, t, t);

            } else {
                añadirInstruccion(InstructionType.not, t, t);
            }
        }
        operand.reference = t;
    }

    private void generate(SymbolOperation operation){
        String t;

        SymbolOperand lValue = operation.getLValue();
        generate(lValue);
        t = lValue.reference;
        SymbolOp op = operation.getOperation();
        SymbolOperand rValue = operation.getRValue();
        
        // If nothing else is needed we don't keep going.
        if(op == null || rValue == null){
            operation.reference = t;
            return;
        }

        generate(rValue);
        t = nuevaVariableiable();
        String eTrue, eFalse;
        switch (op.operation) {
            case VTEntry.ADD:
                añadirInstruccion(InstructionType.add, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.SUB:
                añadirInstruccion(InstructionType.sub, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.PROD:
                añadirInstruccion(InstructionType.prod, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.DIV:
                añadirInstruccion(InstructionType.div, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.MOD:
                añadirInstruccion(InstructionType.mod, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.OR:
                añadirInstruccion(InstructionType.or, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.AND:    
                añadirInstruccion(InstructionType.and, lValue.reference, rValue.reference, t);
                break;
            case VTEntry.IS_EQUAL:
                eTrue = nuevaEtiqueta();
                eFalse = nuevaEtiqueta();
                añadirInstruccion(InstructionType.if_EQ, lValue.reference, rValue.reference, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.FALSE, t);
                añadirInstruccion(InstructionType.go_to, eFalse);
                añadirInstruccion(InstructionType.skip, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.TRUE, t);
                añadirInstruccion(InstructionType.skip, eFalse);
                break;
            case VTEntry.BIGGER:
                eTrue = nuevaEtiqueta();
                eFalse = nuevaEtiqueta();
                añadirInstruccion(InstructionType.if_GT, lValue.reference, rValue.reference, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.FALSE, t);
                añadirInstruccion(InstructionType.go_to, eFalse);
                añadirInstruccion(InstructionType.skip, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.TRUE, t);
                añadirInstruccion(InstructionType.skip, eFalse);
                break;
            case VTEntry.BEQ:
                eTrue = nuevaEtiqueta();
                eFalse = nuevaEtiqueta();
                añadirInstruccion(InstructionType.if_GE, lValue.reference, rValue.reference, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.FALSE, t);
                añadirInstruccion(InstructionType.go_to, eFalse);
                añadirInstruccion(InstructionType.skip, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.TRUE, t);
                añadirInstruccion(InstructionType.skip, eFalse);
                break;
            case VTEntry.LESSER:
                eTrue = nuevaEtiqueta();
                eFalse = nuevaEtiqueta();
                añadirInstruccion(InstructionType.if_LT, lValue.reference, rValue.reference, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.FALSE, t);
                añadirInstruccion(InstructionType.go_to, eFalse);
                añadirInstruccion(InstructionType.skip, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.TRUE, t);
                añadirInstruccion(InstructionType.skip, eFalse);
                break;
            case VTEntry.LEQ:
                eTrue = nuevaEtiqueta();
                eFalse = nuevaEtiqueta();
                añadirInstruccion(InstructionType.if_LE, lValue.reference, rValue.reference, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.FALSE, t);
                añadirInstruccion(InstructionType.go_to, eFalse);
                añadirInstruccion(InstructionType.skip, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.TRUE, t);
                añadirInstruccion(InstructionType.skip, eFalse);
                break;
            case VTEntry.NEQ:
                eTrue = nuevaEtiqueta();
                eFalse = nuevaEtiqueta();
                añadirInstruccion(InstructionType.if_NE, lValue.reference, rValue.reference, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.FALSE, t);
                añadirInstruccion(InstructionType.go_to, eFalse);
                añadirInstruccion(InstructionType.skip, eTrue);
                añadirInstruccion(InstructionType.copy, ""+VTEntry.TRUE, t);
                añadirInstruccion(InstructionType.skip, eFalse);
                break;
        }

        operation.reference = t;
    }

    private void generate(SymbolOut out){
        SymbolOperation oper = out.getValue();
        generate(oper);
        String t = oper.reference;
        añadirInstruccion(InstructionType.out, t);
    }

    private void generate(SymbolParams params){
        SymbolParams next = params.getNext();
        if(next != null) generate(next);

        SymbolOperation oper = params.getValue();
        generate(oper);
        String t = oper.reference;
        
        if(oper.type.isType(KW_ARRAY)){
            añadirInstruccion(InstructionType.param_c, t);
            SymbolTypeVar type = oper.type;
            // Since we need to know the dimensions of the arrays but parameters are not known in compilation time,
            // we pass them as parameters which the function's preamble will take care of during assembly code generation.
            while(type != null && type.arrayLength != VTEntry.UNKNOWN) {
                añadirInstruccion(InstructionType.param_s, ""+type.arrayLength);
                type = type.getBaseType();
            }
        } else añadirInstruccion(InstructionType.param_s, t);
    }


    private void generate(SymbolReturn sReturn){
        String t;
        SymbolOperation oper = sReturn.getValue();
        if(oper != null){
            generate(oper);
            t = oper.reference;
        } else t = "0";
        
        String name = funcionActual.replace(".", "");
        añadirInstruccion(InstructionType.rtn, t, name);
    }


    private void generate(SymbolSwap swap){
        SymbolVar var1 = swap.getVariable1();
        generate(var1);
        String t1 = var1.reference;

        String t = nuevaVariableiable();
        añadirInstruccion(InstructionType.copy, t1, t);

        SymbolVar var2 = swap.getVariable2();
        generate(var2);
        String t2 = var2.reference;

        añadirInstruccion(InstructionType.copy, t2, t1);
        añadirInstruccion(InstructionType.copy, t, t2);
    }


    private void generate(SymbolValue value){
        Object val = value.getSemanticValue();
        String t = "";
        if(val instanceof SymbolVar) {
            SymbolVar var = (SymbolVar) val;
            generate(var);
            t = var.reference;
        } else if(val instanceof SymbolFuncCall) {
            SymbolFuncCall fcall = (SymbolFuncCall) val;
            generate(fcall);
            t = fcall.reference;
        } else if(val instanceof SymbolList) {
            SymbolList list = (SymbolList) val;
            // If a list is not declared (like as a parameter for out) then generate list will change currentDec, but not restore it afterwards.
            String prevDec = currentDec; 
            generate(list);
            t = list.reference;
            // We calculate the total dimensions on this variable, starting by the first level's length
            VTEntry vte = getVariable(currentDec);
            currentDec = prevDec; // We restore the previous value of currentDec
            SymbolTypeVar subLevel = list.type;
            // We go through each sublevel so that we can calculate the occupation accordingly.
            while(subLevel.getArrayDepth() > vte.dimensions.size()){
                vte.dimensions.add(""+subLevel.arrayLength);
                subLevel = subLevel.getBaseType();
            }
        } else if(val instanceof Integer) {
            int intValue = (Integer) val; 
            t = nuevaVariableiable();
            añadirInstruccion(InstructionType.copy, "" + intValue, t);
        } else if(val instanceof Boolean) {
            boolean boolValue = (Boolean) val;
            t = nuevaVariableiable();
            añadirInstruccion(InstructionType.copy, "" + (boolValue ? VTEntry.TRUE : VTEntry.FALSE), t);
        } else if(val instanceof Character) {
            char cValue = (Character) val;
            t = nuevaVariableiable();
            añadirInstruccion(InstructionType.copy, "" + cValue, t);
            getVariable(t).type = ParserSym.KW_CHAR;
        }
        value.reference = t;
    }


    private void generate(SymbolVar var){
        VTEntry vte = getVariable(var.getId());
        String t = vte.tName;

        SymbolArrSuff arrSuff = var.getArrSuff();
        if(arrSuff != null){
            dimensionsToCheck = vte.cloneDimensions();
            generate(arrSuff);
            String tSuffix = arrSuff.reference;
            int nBytes = VTEntry.CHAR_BYTES;
            if(vte.type == ParserSym.KW_INT){
                nBytes = VTEntry.INTEGER_BYTES;
            }
            // Here should go 
            // tn = tm - b 
            // but b is always 0 in our language, as all arrays start with 0
            // so we don't have to include it in this case.
            // t' = tSuff * nbytes
            // t'' = t[t']
            // t <- t''
            String tPrima = nuevaVariableiable();
            añadirInstruccion(InstructionType.prod, tSuffix, "" + nBytes, tPrima);
            String arrayOrigin = t;
            t = nuevaVariableiable();
            añadirInstruccion(InstructionType.ind_val, arrayOrigin, tPrima, t);
        }

        var.reference = t;
    }

    private String nuevaVariableiable(){
        String t = "t" + numT++;
        VTEntry vte = new VTEntry(t);
        variableTable.put(funcionActual + subnivelActual + t, vte);
        if(tablaProcesosActual != null) tablaProcesosActual.variableTable.put(subnivelActual + t, vte);
        return t;
    }

    private String nuevaEtiqueta(){
        return "e" + numE++;
    }

    private VTEntry getVariable(String t){
        VTEntry vte = variableTable.get(funcionActual + subnivelActual + t);
        int i = subnivelActual-1;

        // We check up until -1 since parameters are in sublevel -1
        while(vte == null && i >= -1) {
            vte = variableTable.get(funcionActual + i-- + t);
        }
        if(vte == null) {
            vte = variableTable.get(DEF_FUNCTION + 0 + t);
        }
        return vte;
    }

    private void eliminarVariable(String t){
        VTEntry vte = variableTable.remove(funcionActual + subnivelActual + t);
        int i = subnivelActual-1;
        while(vte == null && i >= 0) {
            vte = variableTable.remove(funcionActual + i-- + t);
        }
        if(vte == null) {
            vte = variableTable.remove(DEF_FUNCTION + 0 + t);
        }
    }
    
    private void replaceVarTableKey(String oldKey, String newKey){
        VTEntry vte = getVariable(oldKey);
        eliminarVariable(oldKey);
        variableTable.put(funcionActual + subnivelActual + newKey, vte);
    }

    private String createPTEntry(String procName){
        String internalFunctionName = procName + DEF_FUNCTION;
        PTEntry pte = new PTEntry();
        pte.eStart = nuevaEtiqueta();
        procedureTable.put(internalFunctionName, pte);
        return internalFunctionName;
    }

    private void añadirInstruccion(InstructionType instruction, String left, String right, String destination){
        Instruction i = new Instruction(instruction, left, right, destination);
        c3a.add(i);
    }
    
    private void añadirInstruccion(InstructionType instruction, String left, String destination){
        Instruction i = new Instruction(instruction, left, destination);
        c3a.add(i);
    }
    
    private void añadirInstruccion(InstructionType instruction, String destination){
        Instruction i = new Instruction(instruction, destination);
        c3a.add(i);
    }

    public String toString(){
        String s = "";
        for(Instruction i : c3a){
            s += i + "\n";
        }
        return s;
    }*/
