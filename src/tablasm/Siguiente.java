package tablasm;

import java.util.ArrayList;

public class Siguiente {
    public String noTerminal;
    public ArrayList<String> siguientes = new ArrayList<>();

    public Siguiente(String noTerminal) {
        this.noTerminal = noTerminal;
    }
    
    @Override
    public String toString(){
        String s = "SIGUIENTE(" + this.noTerminal + ") = ";
        for (String siguiente : this.siguientes) {
            s += siguiente + " ";
        }        
        return s;        
    }  
}
