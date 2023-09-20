/**
 * Copia de los apuntes del profesor
 */
public class AFNToAFD {

    boolean tk_identificat;
    int anticipacio, primer, darrer, resultat;
    int estat, estataacceptacio, estatanticipacio, einicial;
    char[] buffer;
    public final static int id = 1, instr = 2, comentari = 4, error = 5, tk_ident = 10, tk_instr = 20, tk_oprel = 40, tk_error = 50, buit = -999;
    public final static String LT = "", LE = "";
    String atribut;
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    public int T(int e, char c){
        return 1;
    }
    
    public int reconeix(int e){
        return 0;
    }
    
    public String substring (int c1, int c2) {
        return "";
    }
    
    public boolean esFinal(int estat){
        return true;
    }
    
    public Token llegirToken() {
        tk_identificat = false;
        while (!tk_identificat) {
            anticipacio = primer;
            darrer = primer;
            estat = T(einicial, buffer[anticipacio]);
            estataacceptacio = error;
            while (estat != buit) {
                if (esFinal(estat)) {
                    estataacceptacio = estat;
                    darrer = anticipacio;
                }
                anticipacio = següent(anticipacio);
                estat = T(estat, buffer[anticipacio]);
            }
            switch (reconeix(estatanticipacio)) {
                case id:
                    tk_identificat = true;
                    resultat = tk_ident;
                    atribut = substring(primer, darrer);
                    primer = següent(darrer);
                    break;
                case instr:
                    tk_identificat = true;
                    resultat = tk_instr;
                    atribut = null;
                    primer = següent(darrer);
                    break;
                case oprel.LT:
                    tk_identificat = true;
                    resultat = tk_oprel;
                    atribut = LT;
                    primer = següent(darrer);
                    break;
                case oprel.LE:
                    tk_identificat = true;
                    resultat = tk_oprel;
                    atribut = LE;
                    primer = següent(darrer);
                    break;
                case comentari:
                    primer = següent(darrer);
                    break;
                case error:
                    tk_identificat = true;
                    resultat = tk_error;
                    atribut = null;
                    primer = següent(darrer);
                    break;
            }
        }
        return new Token(resultat, atribut);
    }
    
    public int següent(int pos) {
        return (pos + 1) % buffer.length;
    }
    
    class Token {
        public Token(int resultat, String atribut){
            
        }
    }
    
    class oprel {
        public static final int LT = -1, LE = -2;
    }
}
