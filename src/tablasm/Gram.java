package tablasm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Gram {
    public ArrayList<Produccion> originalProducciones = new ArrayList<>();
    public ArrayList<Produccion> producciones = new ArrayList<>();
    public ArrayList<NoTerminal> noTerminales = new ArrayList<>();
    public ArrayList<Terminal> terminales = new ArrayList<>();  
    
    public ArrayList<Prim> primeros = new ArrayList<>();

    public Gram(File gramatica) {
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader(gramatica));
            while((line = br.readLine())!= null){                
                String noTerminal = line.split("->")[0];
                String produccion = line.split("->")[1];
                originalProducciones.add(new Produccion(new NoTerminal(noTerminal, false), produccion));
                producciones.add(new Produccion(new NoTerminal(noTerminal, false), produccion));
                if(!noTerminales.contains(new NoTerminal(noTerminal, false))){
                    noTerminales.add(new NoTerminal(noTerminal, false));
                }
                for (int i = 0; i < produccion.length(); i++) {
                    String simbolo = produccion.substring(i, i+1);
                    if(!isNonTerminal(simbolo)){
                        Terminal ter = new Terminal(simbolo);
                        if(!ter.isEpsilon() && !terminales.contains(ter)){
                            terminales.add(ter);
                        }
                    }
                }
            }
            setProduceEpsilon();
            setPrimeros();
        } catch (Exception e) {
        }
    }
    
    public NoTerminal getNoTerminal(Simbolo simbolo){
        for(NoTerminal nt : noTerminales){
            if(nt.equals(simbolo)){
                return nt;
            }
        }
        return null;
    }
    
    public void setProduceEpsilon(){   
        for(NoTerminal noTerminal : noTerminales){
            ArrayList<Produccion> producciones = getProducciones(noTerminal);
            for (Produccion produccion : producciones) {
                if(produccion.produceEpsilon){
                    noTerminal.produceEpsilon = 1;
                }
            }            
        }
        for(NoTerminal noTerminal : noTerminales){
            if(noTerminal.produceEpsilon==-1){
                ArrayList<Produccion> producciones = getProducciones(noTerminal);                
                boolean noEpsilon = true;
                for (Produccion produccion : producciones) {                    
                    if(produccion.hasTerminales()){
                        noEpsilon = true;                        
                    }else{
                        noEpsilon = false;
                        break;
                    }
                }
                if(noEpsilon){
                    noTerminal.produceEpsilon = 0;
                }
            }                        
        }
        while(InitializeProduceEpsilon()){
            for(NoTerminal noTerminal : noTerminales){
                if(noTerminal.produceEpsilon==-1){
                    ArrayList<Produccion> producciones = getProducciones(noTerminal);                    
                    for (Produccion produccion : producciones) {
                        boolean epsilon = true;
                        for(Simbolo simbolo : produccion.simbolos){
                            if(isNonTerminal(simbolo)){
                                int pe = getNoTerminal(simbolo).produceEpsilon; 
                                if(pe == 0){
                                    epsilon = false;
                                    break;
                                }
                            }else{
                                epsilon = false;
                                break;
                            }
                        }
                        if(epsilon){
                            noTerminal.produceEpsilon = 1;
                        }
                    }
                    if(noTerminal.produceEpsilon==-1){
                        noTerminal.produceEpsilon = 0;
                    }
                }                        
            }
        }
    }
    
    private boolean InitializeProduceEpsilon(){
        for(NoTerminal noTerminal : noTerminales){
            if(noTerminal.produceEpsilon==-1){
                return true;
            }
        }        
        return false;
    }
    
    public void setPrimeros(){
        //Inicializar el arreglo de primeros
        for(NoTerminal noTerminal : noTerminales){
            primeros.add(new Prim(noTerminal));
        }
        for(Prim primero : primeros){
            ArrayList<Produccion> producciones = getProducciones(primero.noTerminal);
            for (Produccion produccion : producciones) {
                for(Simbolo simbolo : produccion.simbolos){
                    if(!primero.simbolos.contains(simbolo)){
                        if(simbolo.isEpsilon() && produccion.simbolos.size()==1){
                            primero.simbolos.add(simbolo);
                        }else{
                            if(!simbolo.isEpsilon()){
                                primero.simbolos.add(simbolo);
                                if(isNonTerminal(simbolo)){                                    
                                    getPrimeros((NoTerminal)simbolo);
                                    int pe = getNoTerminal(simbolo).produceEpsilon;
                                    if(pe==0){
                                        break;
                                    }
                                }else{
                                    break;
                                }
                            }                            
                        }
                    }
                } 
            }
        }
        for(NoTerminal noTerminal : noTerminales){
            if(noTerminal.produceEpsilon==1 && !primeros.get(getIndexPrimero(noTerminal)).simbolos.contains(new Terminal("&"))){
                primeros.get(getIndexPrimero(noTerminal)).simbolos.add(new Terminal("&"));
            }            
        }
    }
    
    private void replace(){
        for(Prim primero : primeros){
            
        }
    }
    
    private int getIndexPrimero(NoTerminal noTerminal){
        int i = -1;
        for(Prim primero : primeros){
            if(primero.noTerminal.equals(noTerminal)){
                i = primeros.indexOf(primero);
            }
        }
        return i;
    }
    
    private void getPrimeros(NoTerminal noTerminal){
        ArrayList<Produccion> producciones = getProducciones(noTerminal);
        int i = getIndexPrimero(noTerminal);
        for (Produccion produccion : producciones) {
            Simbolo simbolo = produccion.getFirst();
            if(!primeros.get(i).simbolos.contains(simbolo)){
                    primeros.get(i).simbolos.add(simbolo);
                }
            if(isNonTerminal(simbolo)){                
                getPrimeros((NoTerminal)simbolo);
            }
        }
        
    }
    
    private ArrayList<Produccion> getProducciones(NoTerminal noTerminal){
        ArrayList<Produccion> producciones = new ArrayList<>();
        for (Produccion produccion : this.producciones) {
            if(produccion.noTerminal.equals(noTerminal)){
                producciones.add(produccion);
            }
        }
        return producciones;
    }
    
    public boolean isNonTerminal(Simbolo s){
        return s instanceof NoTerminal;
    }
    
    public boolean isTerminal(Simbolo s){
        return s instanceof Terminal;
    }
    
    public static boolean isNonTerminal(String s){
        return Character.isUpperCase(s.charAt(0));        
    }  
}
