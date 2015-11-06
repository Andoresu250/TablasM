package tablasm;

public class Prim extends Calculo{

    public Prim(NoTerminal noTermianl) {
        super(noTermianl);
    }

    @Override
    public String toString() {
        String s = "PRIMERO(" + this.noTerminal.toString() + ") = ";
        for (Simbolo simbolo : simbolos) {
            s += simbolo.toString()+ " ";
        }
        return s;   
    }
    
}
