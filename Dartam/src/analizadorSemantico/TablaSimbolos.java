package analizadorSemantico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TablaSimbolos {
    
    // The main structure of the symbol table is a HashMap of the information of each symbol.
    private HashMap<String, DescripcionSimbolo> TablaSimbolos;

    private int currentLevel;
    private ArrayList<Integer> ambitsTable;
    private ArrayList<TableEntry> expansionTable;

    public TablaSimbolos(){
        TablaSimbolos = new HashMap<>();
        currentLevel = 0;
        ambitsTable = new ArrayList<>();
        ambitsTable.add(currentLevel, 0);
        expansionTable = new ArrayList<>();
    }

    /**
     * Inserts the variable to the TablaSimbolos. Variable must be unique in the current level, otherwise SemanticException is thrown.
     * @param variable
     * @param desc
     * @throws SemanticException
     */
    public void insertVariable(String variable, DescripcionSimbolo desc) {
        // We search for the string (which functions as the hashmap key) in the symbol table
        DescripcionSimbolo oldVarDesc = TablaSimbolos.get(variable);
        if(oldVarDesc != null){
            //if(oldVarDesc.declaredLevel == currentLevel) throw new SemanticException("Variable '" + variable + "' already declared in this level");
            // We add the old obscured variable to the expansion table.
            int idxe = ambitsTable.get(currentLevel); // idxe = ambitsTable[currentLevel]
            idxe++;
            ambitsTable.set(currentLevel, idxe); // ambitsTable[currentLevel] = idxe
            expansionTable.add(new TableEntry(variable, oldVarDesc)); // expansionTable[idxe] = description of the obscured variable
        }
        desc.declaredLevel = currentLevel;
        TablaSimbolos.put(variable, desc);
    }

    public DescripcionSimbolo getDescription(String variable) {
        return TablaSimbolos.get(variable);
    }

    public void empty(){
        currentLevel = 0;
        TablaSimbolos.clear();
        ambitsTable.clear();
        expansionTable.clear();
    }

    public void enterBlock(){
        int prevAmbit = ambitsTable.get(currentLevel);
        currentLevel++;
        ambitsTable.add(prevAmbit);
    }

    public void exitBlock() {
        //if(LenguaG.DEBUGGING) System.out.println(this);

        //if(currentLevel == 0) throw new CompilerException(" !!! Compiler error !!! Tried to exit last block in symbol table.");
        int to = ambitsTable.get(currentLevel); // Finishing index: indicates the last variable added to the expansion table.
        ambitsTable.remove(currentLevel);       // we decrement the current level
        currentLevel--;
        int from = ambitsTable.get(currentLevel); // Starting index: first variable added to the expansion table in this level
        for(TableEntry te : expansionTable.subList(from, to)){
            TablaSimbolos.replace(te.variable, te.desc); // We restore the previous value
        }
        expansionTable.subList(from, to).clear(); // We clear the expansion table
        
        // Removal of symbols from the previous level
        Iterator<HashMap.Entry<String, DescripcionSimbolo>> it = TablaSimbolos.entrySet().iterator();
        while(it.hasNext()){
            if(it.next().getValue().declaredLevel > currentLevel) it.remove();
        }
    }

    @Override
    public String toString(){
        String st = "Current level: " + currentLevel + "\n\n";
        //st += "Symbol Table: " + TablaSimbolos.toString() + "\n";
        st += "Symbol Table: \n";
        StringBuilder sb = new StringBuilder();
        Iterator<HashMap.Entry<String, DescripcionSimbolo>> it = TablaSimbolos.entrySet().iterator();
        while(it.hasNext()){
            HashMap.Entry<String, DescripcionSimbolo> next = it.next();
            sb.append(next.getKey());
            sb.append(":\n\t");
            sb.append(next.getValue());
            sb.append("\n");
        }
        st += sb + "\n";
        st += "Ambit table:" + ambitsTable.toString() + "\n";
        st += "Expansion table:" + expansionTable.toString() + "\n";
        return st;
    }

    // private class to simplify entry to expansion table.
    private class TableEntry {
        String variable;
        DescripcionSimbolo desc;

        public TableEntry(String variable, DescripcionSimbolo desc){
            this.variable = variable;
            this.desc = desc;
        }

        @Override
        public String toString(){
            String te = "Variable: " + variable + "\n";
            te += "Description: " + desc + "\n";
            return te;
        }
    }
}