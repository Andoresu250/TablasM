package tablasm;

import java.util.ArrayList;

public abstract class Calculo {
    public NoTerminal noTerminal;
    public ArrayList<Simbolo> simbolos = new ArrayList<>();

    public Calculo(NoTerminal noTerminal) {
        this.noTerminal = noTerminal;
    }
    
    @Override
    public abstract String toString();
}
