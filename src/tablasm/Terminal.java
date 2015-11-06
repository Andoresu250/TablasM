package tablasm;


public class Terminal extends Simbolo{
    
    public Terminal(String simbolo) {
        super(simbolo);
    }

    @Override
    public String toString() {
        return this.simbolo;
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;
        if(object != null && object instanceof Terminal){
            sameSame = this.simbolo.toString().compareTo(((Terminal) object).simbolo.toString())==0;
        }
        return sameSame;
    }
}
