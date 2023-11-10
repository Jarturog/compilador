import java.util.ArrayList;
import java.util.Stack;

/**
 * Copia de los apuntes del profesor
 */
public class AFNToAFD {

    public static final int buit = -999;
    public static final char lambda = (char) -999;
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    public ArrayList<Integer> lambdaClausura(ArrayList<Integer> T){
        ArrayList<Integer> C = new ArrayList<Integer>(T);
        Stack<Integer> p = new Stack();
        for (int i = 0; i < C.size(); i++) {
            p.push(T.get(i));
        }
        while(!p.empty()){
            Integer s = p.pop();
            ArrayList<Integer> q;
            q = moviment(s, lambda);
            for (int i = 0; i < q.size(); i++) {
                C.add(q.get(i));
                p.push(q.get(i));
            }
        }
        return C;
    }
    
    public ArrayList<Integer> moviment(int estat, char simbol){
        return null;
    }
}
