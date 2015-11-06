package tablasm;


public class NoTerminal extends Simbolo{

    public boolean prima;
    public int produceEpsilon = -1;
    
    public NoTerminal(String simbolo, boolean prima) {
        super(simbolo);
        this.prima = prima;
    }
    
    public boolean compareTo(NoTerminal noTermianl){
        return this.simbolo.toString().compareTo(noTermianl.toString())==0;
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

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;
        if(object != null && object instanceof NoTerminal){
            sameSame = this.simbolo.toString().compareTo(((NoTerminal) object).simbolo.toString())==0;
        }
        return sameSame;
    }
}
