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
            String s1 = this.toString();
            String s2 = ((NoTerminal)object).toString();
            sameSame = s1.compareTo(s2)==0;
        }
        return sameSame;
    }
}
