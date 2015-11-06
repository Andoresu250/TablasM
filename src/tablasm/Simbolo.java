/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablasm;

/**
 *
 * @author Andoresu
 */
public abstract class Simbolo {
    
    public String simbolo;

    public Simbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    
    public boolean isEpsilon(){
        return simbolo.compareTo("&")==0;
    }
    
    @Override
    public abstract boolean equals(Object object);
    
    @Override
    public abstract String toString();
    
}
