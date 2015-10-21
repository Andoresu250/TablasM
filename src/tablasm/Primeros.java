/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablasm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 *
 * @author dell
 */
public class Primeros {
    private Gramatica gramatica;
    private ArrayList<Primero> primeros = new ArrayList<>();    

    public Primeros(Gramatica gramatica) {
        this.gramatica = gramatica;        
        for (String noTerminal : this.gramatica.noTerminales) {
            this.primeros.add(new Primero(noTerminal));
        }        
    }  

    public ArrayList<Primero> getPrimeros() {
        return primeros;
    }
    
    public void setPrimeros(){
        for (Primero primero : primeros) {
            ArrayList<String> producciones = getProducciones(primero.noTerminal);
            for (String produccion : producciones) {
               String simbolo = produccion.split("->")[1].substring(0, 1);
               if(produccion.split("->")[1].length()>1){
                   if(produccion.split("->")[1].substring(1,2).compareTo("'")==0){
                       simbolo += "'";
                   }
               }                
               if(Gramatica.isNonTerminal(simbolo)){
                   if(!primero.primeros.contains(simbolo)){
                       //getPrimeroR(simbolo);
                       primero.primeros.add(simbolo);
                   }
                   //getPrimeroR(simbolo);
               }else{
                   if(!primero.primeros.contains(simbolo)){
                       primero.primeros.add(simbolo);
                   }
               }
            }
        }
        Collections.reverse(primeros);
        for (Primero primero : primeros) {
            for (int i = 0 ; i < primero.primeros.size(); i++) {
                String simbolo = primero.primeros.get(i);
                if(Gramatica.isNonTerminal(simbolo)){
                    primero.primeros.addAll(getPrimero(simbolo));
                    primero.primeros.remove(simbolo);    
                    i = -1;
                }
            }
        }
        Collections.reverse(primeros);
        for (Primero primero : primeros) {
            primero.primeros = new ArrayList<String>(new LinkedHashSet<String>(primero.primeros));
        }
        
    }    
    
    public ArrayList<String> getPrimero(String noTerminal){        
        for (Primero primero : this.primeros) {
            if(primero.noTerminal.compareTo(noTerminal)==0){
                return primero.primeros;
            }
        }
        return  null;
    }
    
    public void getPrimeroR(String noTerminal){
        ArrayList<String> producciones = getProducciones(noTerminal);
        int i = 0;
        for (Primero primero : this.primeros) {
            if(primero.noTerminal.compareTo(noTerminal)==0){
                i = this.primeros.indexOf(primero);
            }
        }
        for (String produccion : producciones) {
            String simbolo = produccion.split("->")[1].substring(0, 1);
            if(produccion.split("->")[1].length()>1){
                if(produccion.split("->")[1].substring(1,2).compareTo("'")==0){
                    simbolo += "'";
                }
            }   
            if(Gramatica.isNonTerminal(simbolo)){
                getPrimeroR(simbolo);
            }else{
                if(!this.primeros.get(i).primeros.contains(simbolo)){
                    this.primeros.get(i).primeros.add(simbolo);
                }
            }
        }
    }
    
    public ArrayList<String> getProducciones(String noTerminal){
        ArrayList<String> producciones = new ArrayList<>();        
        for (String produccion : this.gramatica.producciones) {            
            String simbolo = produccion.split("->")[0];            
            if(simbolo.compareTo(noTerminal)==0){
                producciones.add(produccion);
            }
        }
        return producciones;
    }
}
