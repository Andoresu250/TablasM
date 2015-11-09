package tablasm;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;



public class TablaM {
    
    Gram gram;
    Produccion[][] tM;
    
    public Produccion getProduccion(NoTerminal noTerminal, Terminal terminal){
        int i = gram.noTerminales.indexOf(noTerminal);
        int j = gram.terminales.indexOf(terminal) - 1;
        if(i==-1 || j==-1){
            return null;
        }
        return tM[i][j];
    }    
    
    public ArrayList<Simbolo> invertir(ArrayList<Simbolo> simbolos){
        ArrayList<Simbolo> temp = (ArrayList<Simbolo>) simbolos.clone();
        Collections.reverse(temp);
        return temp;
    }
    
    public String[][] probar(String s){        
        ArrayList<ArrayList> pilaF = new ArrayList<>();
        ArrayList<ArrayList> entradaF = new ArrayList<>();
        ArrayList<Produccion> salidaF = new ArrayList<>();
        
        ArrayList<Simbolo> pila = new ArrayList<>();
        pila.add(new Terminal("$"));
        pila.add(gram.noTerminales.get(0));
        pilaF.add(pila);        
        
        ArrayList<Simbolo> entrada = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            entrada.add(new Terminal(s.substring(i, i+1)));
        }
        entrada.add(new Terminal("$"));        
        entradaF.add(entrada);        
        
        int i = 0;
        while(true){
            if(pilaF.get(i).size()==1 && pilaF.get(i).get(0).equals(new Terminal("$")) && entradaF.get(i).size()==1 && entradaF.get(i).get(0).equals(new Terminal("$"))){                
                break;
            }
            Simbolo simboloPila = (Simbolo)pilaF.get(i).get(pilaF.get(i).size()-1);            
            Simbolo simboloEntrada = (Simbolo)entradaF.get(i).get(0);
            if(gram.isNonTerminal(simboloPila)){
                Produccion produccion = getProduccion((NoTerminal)simboloPila, (Terminal)simboloEntrada);
                if(produccion==null){
                    JOptionPane.showMessageDialog(null, "La cadena " + s + " No es producida por la GIC");
                    salidaF.add(produccion);
                    return toMatrix(pilaF, entradaF, salidaF);
                }
                salidaF.add(produccion);
                i++;                
                ArrayList<Simbolo> miProduccion = (ArrayList<Simbolo>) produccion.simbolos.clone();
                miProduccion.remove(new Terminal("&"));
                ArrayList<Simbolo> pilaT = (ArrayList<Simbolo>) pilaF.get(i-1).clone();                                
                pilaT.remove(pilaT.size()-1);
                pilaT.addAll(invertir(miProduccion));                
                pilaF.add(pilaT);                
                
                ArrayList<Simbolo> entradaT = (ArrayList<Simbolo>) entradaF.get(i-1).clone();                
                entradaF.add(entradaT);                
            }else{
                if(simboloEntrada.equals(simboloPila)){
                    i++;
                    ArrayList<Simbolo> pilaT = (ArrayList<Simbolo>) pilaF.get(i-1).clone();     
                    ArrayList<Simbolo> entradaT = (ArrayList<Simbolo>) entradaF.get(i-1).clone();
                    pilaT.remove(pilaT.size()-1);
                    entradaT.remove(0);
                    pilaF.add(pilaT);
                    entradaF.add(entradaT);
                    salidaF.add(null);                    
                }else{
                    JOptionPane.showMessageDialog(null, "La cadena " + s + " No es producida por la GIC");
                    salidaF.add(null);
                    return toMatrix(pilaF, entradaF, salidaF);
                }
            }
        }
        salidaF.add(null);
        return toMatrix(pilaF, entradaF, salidaF);
    }   
      
    public String arrayToString(ArrayList<Object> a){
        String s = "";
        for (Object a1 : a) {
            s += a1.toString();
        }
        return s;
    }
    
    public String[][] toMatrix(ArrayList<ArrayList> pilaF, ArrayList<ArrayList> entradaF, ArrayList<Produccion> salidaF){
        String[][] m = new String[pilaF.size()][3];
        for (int i = 0; i < m.length; i++) {
            m[i][0] = m[i][1] = m[i][2] = "";
        }
        for (int i = 0; i < m.length; i++) {
            m[i][0] = arrayToString(pilaF.get(i));
            m[i][1] = arrayToString(entradaF.get(i));
            if(salidaF.get(i)==null){
                m[i][2] = "";
            }else{
                m[i][2] = salidaF.get(i).toString();
            }
        }
        return m;
    }    
    
    public TablaM(Gram gram) {
        this.gram = gram;   
        this.gram.terminales.add(new Terminal("$"));
        this.tM = new Produccion[gram.noTerminales.size()][gram.terminales.size()];
        for (int i = 0; i < this.tM.length; i++) {
            for (int j = 0; j < this.tM[i].length; j++) {                    
                this.tM[i][j] = null;
            }                
        }
        setTablaM();
        
    }       
    
     public void setTablaM(){
         for(Produccion produccion : gram.producciones){             
             NoTerminal noTerminal = gram.getNoTerminal(produccion.noTerminal);  
             ArrayList<Simbolo> primeros = gram.primerosParciales(produccion.simbolos);
             if(!primeros.contains(new Terminal("&"))){
                 for(Simbolo simbolo : primeros){
                     tM[getRowIndex(noTerminal)][getColumnIndex((Terminal)simbolo)] = produccion;
                 }
             }else{
                 for(Simbolo simbolo : gram.getSiguientes(noTerminal)){
                     tM[getRowIndex(noTerminal)][getColumnIndex((Terminal)simbolo)] = produccion;
                 }
             }
         }        
    }     
     
     public String[][] tablaMToString(){
         String[][] m = new String[gram.noTerminales.size()][gram.terminales.size()];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {                    
                m[i][j]="";
            }                
        }
        for (int i = 0; i < m.length; i++) {
            m[i][0] = gram.noTerminales.get(i).toString();
        }
        for (int i = 0; i < m.length; i++) {
            for (int j = 1; j < m[i].length; j++) {
                if(tM[i][j-1]==null){
                    m[i][j] = "";
                }else{
                    m[i][j]=tM[i][j-1].toString();
                }
            }                
        }        
         return m;
     }       
    
    public int getColumnIndex(Terminal simbolo){
        for(Terminal terminal : gram.terminales){
            if(terminal.equals(simbolo)){
                return gram.terminales.indexOf(terminal);
            }
        }
        return -1;
    }   
    
    public int getRowIndex(NoTerminal simbolo){
        for(NoTerminal noTerminal : gram.noTerminales){
            if(noTerminal.equals(simbolo)){
                return gram.noTerminales.indexOf(noTerminal);
            }
        }
        return -1;
    }
    
    public static void printMatrix(Object[][] m) {
        try {
            int rows = m.length;
            int columns = m[0].length;
            String str = "|\t";

            for (int i = 0; i < rows; i++) {
                
                for (int j = 0; j < columns; j++) {
                    if(m[i][j]==null){
                        str += " " + "\t";
                    }else{
                        str += m[i][j].toString() + "\t";
                    }
                }
                System.out.println(str + "|");
                str = "|\t";
            }

        } catch (Exception e) {
            System.out.println("Matrix is empty!!");
        }
    }
    
    public static void main(String[] args) {
        View vista = new View();
        vista.show();   
    }
    
}
