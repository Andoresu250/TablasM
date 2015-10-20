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
            ArrayList<String> expresiones = getExpresiones(primero.noTerminal);
            for (String expresion : expresiones) {
               String letra = expresion.substring(3,4);
               if(Gramatica.isNonTerminal(letra)){
                   
                   getPrimero(letra);
               }else{
                   if(!primero.primeros.contains(letra)){
                       primero.primeros.add(letra);
                   }
               }
            }
        }        
    }
    
    public void getPrimero(String noTerminal){
        ArrayList<String> expresiones = getExpresiones(noTerminal);
        int i = 0;
        for (Primero primero : this.primeros) {
            if(primero.noTerminal.compareTo(noTerminal)==0){
                i = this.primeros.indexOf(primero);
            }
        }
        for (String expresion : expresiones) {
            String letra = expresion.substring(3, 4);
            if(Gramatica.isNonTerminal(letra)){
                getPrimero(letra);
            }else{
                if(!this.primeros.get(i).primeros.contains(letra)){
                    this.primeros.get(i).primeros.add(letra);
                }
            }
        }
    }
    
    public ArrayList<String> getExpresiones(String noTerminal){
        ArrayList<String> expresiones = new ArrayList<>();
        String s;
        for (String expresion : this.gramatica.expresiones) {
            s = expresion.substring(0,1);
            if(s.compareTo(noTerminal)==0){
                expresiones.add(expresion);
            }
        }
        return expresiones;
    }
}
