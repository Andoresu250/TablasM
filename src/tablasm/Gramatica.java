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
    public ArrayList<String> expresiones = new ArrayList<>();
    public ArrayList<String> terminales = new ArrayList<>();
    public ArrayList<String> noTerminales = new ArrayList<>();
   
    public static boolean isNonTerminal(String s){
        return Character.isUpperCase(s.charAt(0));        
    }    

    public Gramatica(File miGramatica) {
        BufferedReader br = null;
        try {
            String line;
            String e;
            String[] alternancias;
            br = new BufferedReader(new FileReader(miGramatica));
            while((line = br.readLine())!=null){  
                e = line.substring(3,line.length());
                alternancias = e.split("\\|");
                if(e.length()>1){
                    for (int i = 0; i < alternancias.length; i++) {
                        this.expresiones.add(line.substring(0,3)+alternancias[i]);                        
                    }
                }else
                    this.expresiones.add(line);
            }
        } catch (Exception e) {
            
        }
        String noTerminal;        
        String miExpresion;
        for (String expresion : this.expresiones) {
            noTerminal = expresion.substring(0,1);
            if(!this.noTerminales.contains(noTerminal)){
                this.noTerminales.add(noTerminal);
            }
            miExpresion = expresion.split(">")[1];
            for (int i = 0; i < miExpresion.length(); i++) {                
                if(!isNonTerminal(miExpresion.substring(i,i+1)) && !this.terminales.contains(miExpresion.substring(i,i+1))){
                    this.terminales.add(miExpresion.substring(i,i+1));
                }
            }
        }
        
    }
    
}
