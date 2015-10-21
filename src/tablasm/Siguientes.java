package tablasm;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andoresu
 */
public class Siguientes {
    private Gramatica gramatica;
    private ArrayList<Siguiente> siguientes = new ArrayList<>();    
    private Primeros primeros;
    
    public Siguientes(Gramatica gramatica, Primeros primeros) {
        this.gramatica = gramatica;        
        this.primeros = primeros;
        for (String noTerminal : this.gramatica.noTerminales) {
            this.siguientes.add(new Siguiente(noTerminal));
        }        
        siguientes.get(0).siguientes.add("$");
    }
    
    
    
    
    
}
