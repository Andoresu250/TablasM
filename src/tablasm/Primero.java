/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablasm;

import java.util.ArrayList;

/**
 *
 * @author dell
 */
public class Primero {
    public String noTerminal;
    public ArrayList<String> primeros = new ArrayList<>();

    public Primero(String noTerminal) {
        this.noTerminal = noTerminal;
    }
    
    @Override
    public String toString(){
        String s = this.noTerminal + " = [ ";
        for (String primero : this.primeros) {
            s += primero + " ";
        }
        s += "]";
        return s;        
    }    
}
