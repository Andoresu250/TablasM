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
    
    public Primero findPrimero(String noTerminal){
        for (Primero primero : primeros) {
            if(primero.noTerminal.compareTo(noTerminal)==0){
                return primero;                
            }
        }       
        return null;
    }
    
    public boolean hasEpsilon(String simbolo){
        if(Gramatica.isNonTerminal(simbolo)){            
            return findPrimero(simbolo).primeros.contains("&");
        }else{
            return "&".compareTo(simbolo)==0;
        }        
    } 
    
    public ArrayList<String> miniPrimeros(String produccion){
        ArrayList<String> primeros = new ArrayList<>();
        boolean hasEpsilon = true;
        for (int i = 0; i < produccion.length(); i++) {
            String simbolo = produccion.substring(i,i+1);
            if(produccion.length()-1>i){
                if(produccion.substring(i+1,i+2).compareTo("'")==0){
                    simbolo += "'";
                    i++;
                }
            }
            if(Gramatica.isNonTerminal(simbolo)){
                primeros.addAll(getPrimeros(simbolo));
                if(!hasEpsilon(simbolo)){
                    hasEpsilon = false;
                    primeros = new ArrayList<String>(new LinkedHashSet<String>(primeros));
                    primeros.remove("&");
                    return primeros;
                }
            }else{
                primeros.add(simbolo);
                if(simbolo.compareTo("&")!=0){
                    hasEpsilon = false;
                    primeros = new ArrayList<String>(new LinkedHashSet<String>(primeros));
                    primeros.remove("&");
                    return primeros;
                }
            }
        }
        primeros = new ArrayList<String>(new LinkedHashSet<String>(primeros));
        return primeros;
    }
    
    public void setPrimeros(){
        //Inicializar primeros
        String s = gramatica.producciones.toString();
        System.out.println(s);
        for (Primero primero : primeros) {
            ArrayList<String> producciones = getProducciones(primero.noTerminal);
            for (String produccion : producciones) {
               String simbolo = produccion.split("->")[1].substring(0, 1);
               if(produccion.split("->")[1].length()>1){
                   if(produccion.split("->")[1].substring(1,2).compareTo("'")==0){
                       simbolo += "'";
                   }
                }  
                if(!primero.primeros.contains(simbolo)){              
                    primero.primeros.add(simbolo);
                    if(Gramatica.isNonTerminal(simbolo)){
                        getPrimeroR(simbolo);
                    }
                }
            }
        }
        //añadir Prim(y) a Prim(x)
        replace();
        //añadir Primeros teniendo en cuenta los &
        for (Primero primero : primeros) {
            ArrayList<String> producciones = getProducciones(primero.noTerminal);
            for (String produccion : producciones) {
               String simbolo = produccion.split("->")[1].substring(0, 1);
               if(produccion.split("->")[1].length()>1){
                   if(produccion.split("->")[1].substring(1,2).compareTo("'")==0){
                       simbolo += "'";                       
                   }
                }  
                if(hasEpsilon(simbolo)){
                    for (int i = simbolo.length(); i < produccion.split("->")[1].length(); i++) {
                        simbolo = produccion.split("->")[1].substring(i,i+1);                        
                        primero.primeros.add(simbolo);
                        if(produccion.split("->")[1].length()-1>i){
                            if(produccion.split("->")[1].substring(i+1,i+2).compareTo("'")==0){
                                simbolo += "'";
                                i++;
                            }
                        }
                        if(hasEpsilon(simbolo)){
                             if(!primero.primeros.contains(simbolo)){              
                                primero.primeros.add(simbolo);                                
                            }
                        }else
                            break;
                    }
                }
            }
        }
        replace();
        //remover & invalidos
         for (Primero primero : primeros) {
            ArrayList<String> producciones = getProducciones(primero.noTerminal);
            boolean hasEpsilon = true;
            for (String produccion : producciones) {
               String simbolo = produccion.split("->")[1].substring(0, 1);
               if(produccion.split("->")[1].length()>1){
                   if(produccion.split("->")[1].substring(1,2).compareTo("'")==0){
                       simbolo += "'";
                   }
                }
               if(producciones.toString().contains("->&")){                   
                   break;
               }else{
                    if(!hasEpsilon(simbolo)){
                        hasEpsilon = false;
                        break;
                    }
               }
            }
            if(!hasEpsilon){
                primero.primeros.remove("&");
            }
         }
    }    
    
    public ArrayList<String> getPrimeros(String noTerminal){        
        for (Primero primero : this.primeros) {
            if(primero.noTerminal.compareTo(noTerminal)==0){
                return primero.primeros;
            }
        }
        return  null;
    }
    
    public void replace(){
        Collections.reverse(primeros);
        for (Primero primero : primeros) {
            for (int i = 0 ; i < primero.primeros.size(); i++) {
                String simbolo = primero.primeros.get(i);
                if(Gramatica.isNonTerminal(simbolo)){
                    primero.primeros.addAll(getPrimeros(simbolo));
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
                if(!this.primeros.get(i).primeros.contains(simbolo)){
                    this.primeros.get(i).primeros.add(simbolo);
                }
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
