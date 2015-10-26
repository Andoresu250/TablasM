package tablasm;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TablaM {
    
    Gramatica gramatica;
    Primeros primeros;
    Siguientes siguientes;
    String[][] tablaM;

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
    }
    
}
