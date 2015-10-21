/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablasm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author dell
 */
public class Gramatica {
    public ArrayList<String> producciones = new ArrayList<>();
    public ArrayList<String> terminales = new ArrayList<>();
    public ArrayList<String> noTerminales = new ArrayList<>();
   
    public static boolean isNonTerminal(String s){
        return Character.isUpperCase(s.charAt(0));        
    }    

    public Gramatica(File miGramatica) {
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader(miGramatica));
            while((line = br.readLine())!=null){                  
                this.producciones.add(line);
            }
        } catch (Exception e) {
            
        }
        String noTerminal;        
        String miExpresion;
        for (String produccion : this.producciones) {
            String[] miProduccion = produccion.split("->");
            noTerminal = miProduccion[0];
            if(!this.noTerminales.contains(noTerminal)){
                this.noTerminales.add(noTerminal);
            }            
            for (int i = 0; i < miProduccion[1].length(); i++) {                
                if(!isNonTerminal(miProduccion[1].substring(i,i+1)) && !this.terminales.contains(miProduccion[1].substring(i,i+1))){
                    this.terminales.add(miProduccion[1].substring(i,i+1));
                }
            }
        }
        
    }
    
}
