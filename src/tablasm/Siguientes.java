package tablasm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

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

    public ArrayList<Siguiente> getSiguientes() {
        return siguientes;
    }
    
    public ArrayList<String> getSiguientes(String noTerminal) {
        for (Siguiente siguiente : siguientes) {
            if(siguiente.noTerminal.compareTo(noTerminal)==0){
                return siguiente.siguientes;
            }
        }
        return null;
    }
    
    public int findByIndex(String nonTerminal){
        for (Siguiente siguiente : siguientes) {
            if(siguiente.noTerminal.compareTo(nonTerminal)==0){
                return siguientes.indexOf(siguiente);
            }
        }
        return -1;
    }
    
    public void setSiguientes(){
        //Regla #2
        for (String produccion : gramatica.producciones) {
            String miProduccion = produccion.split("->")[1];
            for (int i = 0; i < miProduccion.length(); i++) {
                String simbolo = miProduccion.substring(i,i+1);                
                if(Gramatica.isNonTerminal(simbolo)){
                    ArrayList<String> miniPrimero = primeros.miniPrimeros(miProduccion.substring(i+1));
                    if(miniPrimero.size()>0){
                        if(miniPrimero.size()==1 && miniPrimero.get(0).compareTo("&")==0){
                            this.siguientes.get(findByIndex(simbolo)).siguientes.add(produccion.split("->")[0]);
                        }else{
                            this.siguientes.get(findByIndex(simbolo)).siguientes.addAll(miniPrimero);
                            if(miniPrimero.contains("&")){
                                this.siguientes.get(findByIndex(simbolo)).siguientes.add(produccion.split("->")[0]);
                            }
                        }
                    }else{
                        this.siguientes.get(findByIndex(simbolo)).siguientes.add(produccion.split("->")[0]);
                    }
                }
            }
        }
        replace();
    }
        
    
    public void replace(){          
        
        for (Siguiente siguiente : siguientes) {
            siguiente.siguientes = new ArrayList<String>(new LinkedHashSet<String>(siguiente.siguientes));
            siguiente.siguientes.remove(siguiente.noTerminal);                        
            for (int i = 0 ; i < siguiente.siguientes.size(); i++) {
                String simbolo = siguiente.siguientes.get(i);                                
                if(Gramatica.isNonTerminal(simbolo)){                    
                    siguiente.siguientes.addAll(getSiguientes(simbolo));
                    siguiente.siguientes = new ArrayList<String>(new LinkedHashSet<String>(siguiente.siguientes));
                    siguiente.siguientes.remove(simbolo);    
                    siguiente.siguientes.remove(siguiente.noTerminal);            
                    i = -1;
                }
            }
        }              
        for (Siguiente siguiente : siguientes) {
            siguiente.siguientes = new ArrayList<String>(new LinkedHashSet<String>(siguiente.siguientes));
            siguiente.siguientes.remove("&");            
        }
    }
    
}
