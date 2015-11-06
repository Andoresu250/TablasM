package tablasm;

import java.util.ArrayList;
import javax.swing.JOptionPane;


public class TablaM {
    
    Gramatica gramatica;
    Primeros primeros;
    Siguientes siguientes;
    String[][] tablaM;
    
    public String getProduccion(String noTerminal, String terminal){
        int i = this.gramatica.noTerminales.indexOf(noTerminal);
        int j = this.gramatica.terminales.indexOf(terminal);
        if(j==-1){
            return "";
        }
        return this.tablaM[i][j];
    }
    
    public String invertirCadena(String produccion){
        String s = "";
        boolean sw = false;
        for (int i = produccion.length(); i > 0; i--) {
            String l = produccion.substring(i-1, i);
            if(sw){
                s += l + "'";
                sw = false;
            }else{
                if(l.compareTo("'")==0){
                    sw = true;                
                }else{
                    s += l;
                }
            }  
        }
        return s;
    }
    
    public String[][] probarCadena(String cadena){
        String[][] m = new String[20][3];
        for (int i = 0; i < m.length; i++) {
            m[i][0] = m[i][1] = m[i][2] = "";
        }
        m[0][0] = "$" + gramatica.noTerminales.get(0);
        m[0][1] = cadena + "$";
        boolean sw = true;
        int i = 0;
        while(sw){
            if(m[i][0].compareTo("$")==0 && m[i][0].compareTo(m[i][1])==0){
                sw = false;
                break;
            }
            String pila = m[i][0].substring(m[i][0].length()-1);
            if(pila.compareTo("'")==0){
                pila = m[i][0].substring(m[i][0].length()-2);
            }
            String entrada = m[i][1].substring(0,1);
            if(Gramatica.isNonTerminal(pila)){
                String produccion = getProduccion(pila, entrada);
                if(produccion.compareTo("")==0){
                    JOptionPane.showMessageDialog(null, "La cadena " + cadena + " No es producida por la GIC");
                    String[][] analisis = new String[i+1][3];
                    for (int j = 0; j < analisis.length; j++) {
                        analisis[j][0] = m[j][0];
                        analisis[j][1] = m[j][1];
                        analisis[j][2] = m[j][2];
                    }
                    return analisis;
                }
                m[i][2] = produccion;
                i++;
                String produccionInversa = produccion.split("->")[1];
                produccionInversa = invertirCadena(produccionInversa);
                produccionInversa = produccionInversa.replaceAll("&", "");
                m[i][0] = m[i-1][0].substring(0,m[i-1][0].length()-pila.length()) + produccionInversa;
                m[i][1] = m[i-1][1] ;                
            }else{
                if(pila.compareTo(entrada)==0){
                    i++;
                    m[i][0] = m[i-1][0].substring(0,m[i-1][0].length()-1);
                    m[i][1] = m[i-1][1].substring(1);
                }else{
                    JOptionPane.showMessageDialog(null, "La cadena " + cadena + " No es producida por la GIC");
                    String[][] analisis = new String[i+1][3];
                    for (int j = 0; j < analisis.length; j++) {
                        analisis[j][0] = m[j][0];
                        analisis[j][1] = m[j][1];
                        analisis[j][2] = m[j][2];
                    }
                    return analisis;
                }                
            }
            
        }
        String[][] analisis = new String[i+1][3];
        for (int j = 0; j < analisis.length; j++) {
            analisis[j][0] = m[j][0];
            analisis[j][1] = m[j][1];
            analisis[j][2] = m[j][2];
        }
        return analisis;
    }

    public TablaM(Gramatica gramatica, Primeros primeros, Siguientes siguientes) {
        this.gramatica = gramatica;
        this.primeros = primeros;
        this.siguientes = siguientes;
        this.tablaM = new String[gramatica.noTerminales.size()][gramatica.terminales.size()+2];
        for (int i = 0; i < this.tablaM.length; i++) {
            for (int j = 0; j < this.tablaM[i].length; j++) {                    
                this.tablaM[i][j]="";
            }                
        }
        for (int i = 1; i < this.tablaM.length; i++) {
            this.tablaM[i][0] = gramatica.noTerminales.get(i-1);                
        }
        setTablaM();
    }
    
    public void setTablaM(){
        for (String produccion : gramatica.producciones) {
            String noTerminal = produccion.split("->")[0];
            String miProduccion = produccion.split("->")[1];
            ArrayList<String> misPrimeros = primeros.miniPrimeros(miProduccion);
            if(!misPrimeros.contains("&")){
                for (String miPrimero : misPrimeros) {
                    this.tablaM[getRowIndex(noTerminal)][getColumnIndex(miPrimero)] = produccion;
                }
            
            }else{
                ArrayList<String> misSiguientes = siguientes.getSiguientes(noTerminal);
                for (String miSiguiente : misSiguientes) {
                    this.tablaM[getRowIndex(noTerminal)][getColumnIndex(miSiguiente)] = produccion;
                }
            }
        }
    }
    
    public int getColumnIndex(String simbolo){
        for (String  terminal: gramatica.terminales) {
            if(simbolo.compareTo(terminal)==0){
                return gramatica.terminales.indexOf(terminal);
            }
        }
        return -1;
    }
    
    public int getRowIndex(String simbolo){
        for (String  terminal: gramatica.noTerminales) {
            if(simbolo.compareTo(terminal)==0){
                return gramatica.noTerminales.indexOf(terminal);
            }
        }
        
        return -1;
    }
    
    public static void main(String[] args) {
        View vista = new View();
        vista.show();   
        String s = "1";        
    }
    
}
