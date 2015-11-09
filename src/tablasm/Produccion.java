
package tablasm;

import java.util.ArrayList;
import java.util.List;


public class Produccion {
    
    public NoTerminal noTerminal;
    public ArrayList<Simbolo> simbolos = new ArrayList<>();  
    public boolean produceEpsilon = false;

    public Produccion(NoTerminal noTerminal, String produccion) {
        this.noTerminal = noTerminal;
        for (int i = 0; i < produccion.length(); i++) {
            String simbolo = produccion.substring(i,i+1);
            if(Gram.isNonTerminal(simbolo)){
                simbolos.add(new NoTerminal(simbolo, false));
            }else{
                simbolos.add(new Terminal(simbolo));
            }
        }
        if(simbolos.size()==1 && simbolos.get(0).isEpsilon()){
            produceEpsilon = true;
        }
    }
    
    public Produccion(NoTerminal noTerminal, List<Simbolo> simbolos) {
        this.noTerminal = noTerminal;
        ArrayList<Simbolo> s = new ArrayList<>(simbolos.size());
        s.addAll(simbolos);
        this.simbolos = s;        
        if(s.size()==1 && s.get(0).isEpsilon()){
            produceEpsilon = true;
        }
    }
    
    public Produccion(NoTerminal noTerminal, Simbolo simbolo) {
        this.noTerminal = noTerminal;
        this.simbolos.add(simbolo);
        if(simbolos.size()==1 && simbolos.get(0).isEpsilon()){
            produceEpsilon = true;
        }
    }
    
    public boolean hasTerminales(){
        for (Simbolo simbolo : simbolos) {
            if(simbolo instanceof Terminal){
                return true;
            }
        }
        return false;
    }
    
    public Simbolo getFirst(){
        return this.simbolos.get(0);
    }
    
    public String toString(){
        String s = noTerminal.toString() + "->";
        for (Simbolo simbolo : simbolos) {
            s += simbolo.toString();
        }        
        return s;
    }
    
    public boolean equals(Object object){
        boolean sameSame = false;
        if(object != null && object instanceof Produccion){
            sameSame = this.toString().compareTo(((Produccion) object).toString())==0;
        }
        return sameSame;
    }
}


