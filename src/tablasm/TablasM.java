/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tablasm;

import java.io.File;
import java.io.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author dell
 */
public class TablasM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("TXT File","txt");
        fc.setFileFilter(filtro);
        fc.showOpenDialog(fc);
        File archivoGramatica = fc.getSelectedFile();
        if(archivoGramatica!=null){
            Gramatica miGramatica = new Gramatica(archivoGramatica);
            System.out.println(miGramatica.expresiones.toString());
            System.out.println(miGramatica.noTerminales.toString());
            System.out.println(miGramatica.terminales.toString());
            Primeros primeros = new Primeros(miGramatica);
            primeros.setPrimeros();
            for (Primero primero : primeros.getPrimeros()) {
                System.out.println(primero.toString());
            }
        }else{
            System.out.println("Operacion invalida");
        }
    }
    
}
