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
    public ArrayList<String> getProducciones(String noTerminal){
        ArrayList<String> producciones = new ArrayList<>();        
        for (String produccion : this.producciones) {            
            String simbolo = produccion.split("->")[0];            
            if(simbolo.compareTo(noTerminal)==0){
                producciones.add(produccion);
            }
        }
        return producciones;
    }
    
    public boolean hasRecursivity(String produccion){
        String noTerminal = produccion.split("->")[0];
        String simbolo = produccion.split("->")[1].substring(0,1);
        if(produccion.split("->")[1].length() > 1){
            if(produccion.split("->")[1].substring(1,2).compareTo("'")==0){
                simbolo += "'";
            }
        }
        return noTerminal.compareTo(simbolo)==0;
    }
    
    public void removeRecursivity(){
        ArrayList<String> nuevaProduccion = new ArrayList<>();
        for (int i = 0; i < producciones.size(); i++) {
            String produccion = producciones.get(i);
             if(hasRecursivity(produccion)){
                String noTerminal = produccion.split("->")[0];
                String noTerminalPrima = noTerminal + "'";
                String miProduccion = noTerminalPrima + "->" + produccion.split("->")[1].substring(1, produccion.split("->")[1].length()) + noTerminalPrima;                
                ArrayList<String> temp = getProducciones(produccion.split("->")[0]);
                temp.remove(produccion);
                producciones.removeAll(temp);
                nuevaProduccion.removeAll(temp);
                if(!temp.isEmpty()){
                    for (int j = 0; j < temp.size(); j++) {
                        temp.set(j, temp.get(j)+noTerminalPrima);
                    }
                }
                nuevaProduccion.addAll(temp);
                nuevaProduccion.add(miProduccion);
                nuevaProduccion.add(noTerminalPrima + "->&");
            }else{
                nuevaProduccion.add(produccion);
            }
        }
        this.producciones = nuevaProduccion;
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
        removeRecursivity();
        String noTerminal;        
        String miExpresion;
        for (String produccion : this.producciones) {
            String[] miProduccion = produccion.split("->");
            noTerminal = miProduccion[0];
            if(!this.noTerminales.contains(noTerminal)){
                this.noTerminales.add(noTerminal);
            }            
            for (int i = 0; i < miProduccion[1].length(); i++) {                
                if(!isNonTerminal(miProduccion[1].substring(i,i+1)) && !this.terminales.contains(miProduccion[1].substring(i,i+1)) && miProduccion[1].substring(i,i+1).compareTo("'")!=0){
                    this.terminales.add(miProduccion[1].substring(i,i+1));
                }
            }
        }
        terminales.remove("&");        
    }
    
}
