package tablasm;


public class NoTerminal {
    
    public String simbolo;
    public boolean prima;

    public NoTerminal(String simbolo, boolean prima) {
        this.simbolo = simbolo;
        this.prima = prima;
    }
    
    public boolean isPrima(){
        return this.prima;
    }
    
    @Override
    public String toString(){
        String simbolo = this.simbolo;
        if(isPrima()){
            simbolo += "'";
        }
        return simbolo;
    }
    
    public boolean compareTo(NoTerminal noTermianl){
        return this.simbolo.toString().compareTo(noTermianl.toString())==0;
    }
}
