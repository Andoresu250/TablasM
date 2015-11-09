
package tablasm;


public class Sig extends Calculo{

    public Sig(NoTerminal noTerminal) {
        super(noTerminal);
    }

    @Override
    public String toString() {
        String s = "SIGUIENTE(" + this.noTerminal.toString() + ") = ";
        for (Simbolo simbolo : simbolos) {
            s += simbolo.toString()+ " ";
        }
        return s;   
    }
    
}
