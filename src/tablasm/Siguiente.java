/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablasm;

import java.util.ArrayList;

/**
 *
 * @author Andoresu
 */
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
