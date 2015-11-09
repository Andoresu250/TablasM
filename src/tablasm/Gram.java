package tablasm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Gram {
    public ArrayList<Produccion> originalProducciones = new ArrayList<>();
    public ArrayList<Produccion> producciones = new ArrayList<>();
    public ArrayList<NoTerminal> noTerminales = new ArrayList<>();
    public ArrayList<Terminal> terminales = new ArrayList<>();  
    
    public ArrayList<Prim> primeros = new ArrayList<>();
    public ArrayList<Sig> siguientes = new ArrayList<>();
    
    private static final String[] letras = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    public Gram(File gramatica) {
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader(gramatica));
            System.out.println("Leyendo archivo...");
            while((line = br.readLine())!= null){                
                String noTerminal = line.split("->")[0];
                String produccion = line.split("->")[1];
                originalProducciones.add(new Produccion(new NoTerminal(noTerminal, false), produccion));
                producciones.add(new Produccion(new NoTerminal(noTerminal, false), produccion));
                if(!noTerminales.contains(new NoTerminal(noTerminal, false))){
                    noTerminales.add(new NoTerminal(noTerminal, false));
                }
                for (int i = 0; i < produccion.length(); i++) {
                    String simbolo = produccion.substring(i, i+1);
                    if(!isNonTerminal(simbolo)){
                        Terminal ter = new Terminal(simbolo);
                        if(!ter.isEpsilon() && !terminales.contains(ter)){
                            terminales.add(ter);
                        }
                    }
                }
            }
            System.out.println("leido.");
            System.out.println("Factorizando...");
            factorize();
            System.out.println("Factorizado.");
            System.out.println("Elimiando recursividad...");
            removeRecursivity();            
            System.out.println("Eliminada.");            
            setProduceEpsilon();
            System.out.println("Calculando Primeros...");
            setPrimeros();
            System.out.println("Primeros listos");
            System.out.println("Calculando Siguientes");
            setSiguientes();
            System.out.println("Siguientes Listos");
        } catch (Exception e) {
        }
    }
    
    private boolean hasRecursivity(Produccion produccion){
        return produccion.noTerminal.equals(produccion.simbolos.get(0));
    }
    
    private void removeRecursivity(){        
        ArrayList<Produccion> producciones = new ArrayList<>();
        for (int i = 0; i < this.producciones.size(); i++) {
            Produccion produccion = this.producciones.get(i);
            if(hasRecursivity(produccion)){
                NoTerminal noTerminalPrima = new NoTerminal(produccion.noTerminal.toString(), true);                
                ArrayList<Simbolo> nuevosSimbolos = (ArrayList<Simbolo>) produccion.simbolos.clone();
                List<Simbolo> asd = nuevosSimbolos.subList(1, nuevosSimbolos.size());   
                asd.add(noTerminalPrima);
                Produccion nuevaProduccion = new Produccion(noTerminalPrima, asd);
                ArrayList<Produccion> produccionesT = (ArrayList<Produccion>) getProducciones(produccion.noTerminal).clone();
                produccionesT.remove(produccion);
                this.producciones.removeAll(produccionesT);
                if(produccionesT.isEmpty()){
                    produccionesT.add(new Produccion(produccion.noTerminal, noTerminalPrima));
                }else{
                    for (int j = 0; j < produccionesT.size(); j++) {
                        Produccion p = produccionesT.get(j);
                        p.simbolos.add(noTerminalPrima);
                        produccionesT.set(j, p);
                    }
                }
                producciones.addAll(produccionesT);
                producciones.add(nuevaProduccion);
                producciones.add(new Produccion(noTerminalPrima, new Terminal("&")));
            }else{
                producciones.add(produccion);
            }
        }
        producciones = deleteRepeatP(producciones);
        this.producciones = producciones;
        this.noTerminales.clear();
        for(Produccion p : this.producciones){                         
            if(!noTerminales.contains(p.noTerminal)){
                noTerminales.add(p.noTerminal);                    
            }
        }    
    }
    
    private void factorize(){
        int i = 0;
        while( i < noTerminales.size()){
            while(!doIt(noTerminales.get(i))){  
                i = 0;
            }
            i++;
        } 
        this.noTerminales.clear();
        for(Produccion p : this.producciones){                         
            if(!noTerminales.contains(p.noTerminal)){
                noTerminales.add(p.noTerminal);                    
            }
        }  
    }
    
    private boolean doIt(NoTerminal noTerminal){
        ArrayList<Produccion> producciones = (ArrayList<Produccion>) getProducciones(noTerminal).clone();
        if(producciones.size()==1){
            return true;
        }
        for (Produccion produccion : producciones) {
            ArrayList<Produccion> factorizables = new ArrayList<>();
            ArrayList<Produccion> noFactorizables = new ArrayList<>();
            factorizables.add(produccion);
            Simbolo alfa1 = produccion.simbolos.get(0);
            for (Produccion produccionF : producciones) {
                if(!produccion.equals(produccionF)){
                    Simbolo alfa2 = produccionF.simbolos.get(0);
                    if(alfa1.equals(alfa2)){
                        factorizables.add(produccionF);
                    }else{
                        noFactorizables.add(produccionF);
                    }
                }
            }
            if(factorizables.size()==1){
                factorizables.clear();                
            }else{
                NoTerminal nuevoNoTerminal = newNonTerminal();
                ArrayList<Simbolo> nuevosSimbolos = new ArrayList<>();
                nuevosSimbolos.add(alfa1);
                nuevosSimbolos.add(nuevoNoTerminal);
                Produccion nuevaProduccion = new Produccion(noTerminal, nuevosSimbolos);
                ArrayList<Produccion> nuevasProducciones = new ArrayList<>();
                nuevasProducciones.add(nuevaProduccion);
                nuevasProducciones.addAll(noFactorizables);
                for(Produccion factorizable : factorizables){
                    List<Simbolo> simbolos = factorizable.simbolos.subList(1, factorizable.simbolos.size());
                    if(simbolos.isEmpty()){
                        nuevasProducciones.add(new Produccion(nuevoNoTerminal, new Terminal("&")));
                    }else{
                        nuevasProducciones.add(new Produccion(nuevoNoTerminal, simbolos));
                    }
                }
                producciones.removeAll(factorizables);
                producciones.removeAll(noFactorizables);
                this.producciones.addAll(this.producciones.indexOf(produccion), nuevasProducciones);
                this.producciones.removeAll(factorizables);
                this.producciones = deleteRepeatP(this.producciones);
                return false;
            }
        }
        return true;
    }
    
    public NoTerminal getNoTerminal(Simbolo simbolo){
        for(NoTerminal nt : noTerminales){
            if(nt.equals(simbolo)){
                return nt;
            }
        }
        return null;
    }
    
    public void setProduceEpsilon(){   
        for(NoTerminal noTerminal : noTerminales){
            ArrayList<Produccion> producciones = getProducciones(noTerminal);
            for (Produccion produccion : producciones) {
                if(produccion.produceEpsilon){
                    noTerminal.produceEpsilon = 1;
                }
            }            
        }
        for(NoTerminal noTerminal : noTerminales){
            if(noTerminal.produceEpsilon==-1){
                ArrayList<Produccion> producciones = getProducciones(noTerminal);                
                boolean noEpsilon = true;
                for (Produccion produccion : producciones) {                    
                    if(produccion.hasTerminales()){
                        noEpsilon = true;                        
                    }else{
                        noEpsilon = false;
                        break;
                    }
                }
                if(noEpsilon){
                    noTerminal.produceEpsilon = 0;
                }
            }                        
        }
        while(InitializeProduceEpsilon()){
            for(NoTerminal noTerminal : noTerminales){
                if(noTerminal.produceEpsilon==-1){
                    ArrayList<Produccion> producciones = getProducciones(noTerminal);                    
                    for (Produccion produccion : producciones) {
                        boolean epsilon = true;
                        boolean sw = true;
                        for(Simbolo simbolo : produccion.simbolos){
                            if(isNonTerminal(simbolo)){
                                int pe = getNoTerminal(simbolo).produceEpsilon; 
                                if(pe == 0){
                                    epsilon = false;
                                    break;
                                }else{
                                    if(pe == -1){
                                        sw = false;
                                    }
                                }
                            }else{
                                epsilon = false;
                                break;
                            }
                        }
                        if(epsilon && sw){
                            noTerminal.produceEpsilon = 1;
                        }
                    }
                    if(noTerminal.produceEpsilon==-1){
                        noTerminal.produceEpsilon = 0;
                    }
                }                        
            }
        }
    }
    
    private boolean InitializeProduceEpsilon(){
        for(NoTerminal noTerminal : noTerminales){
            if(noTerminal.produceEpsilon==-1){
                return true;
            }
        }        
        return false;
    }
        
    public void setPrimeros(){
        //Inicializar el arreglo de primeros
        for(NoTerminal noTerminal : noTerminales){
            primeros.add(new Prim(noTerminal));
        }
        //Encontrar Primeros para cada no terminal
        for(NoTerminal noTerminal : noTerminales){
            getPrimerosR(noTerminal);
        }
        //Agregar epsilon
        for(NoTerminal noTerminal : noTerminales){
            if(noTerminal.produceEpsilon==1 && !primeros.get(getIndexPrimero(noTerminal)).simbolos.contains(new Terminal("&"))){
                primeros.get(getIndexPrimero(noTerminal)).simbolos.add(new Terminal("&"));
            }            
        }
        //Remplazar 
        replacePrimeros();
    }
    
    public ArrayList<Simbolo> primerosParciales(List<Simbolo> s){
        ArrayList<Simbolo> simbolos = new ArrayList<>(s.size());
        simbolos.addAll(s);
        ArrayList<Simbolo> primeros = new ArrayList<>();
        for (Simbolo simbolo : simbolos) {
             if(isNonTerminal(simbolo)){                 
                 primeros.addAll(getPrimero((NoTerminal)simbolo).simbolos);
                 NoTerminal nt = getNoTerminal(simbolo);                 
                 if(nt.produceEpsilon==0){
                     primeros = deleteRepeat(primeros);
                     primeros.remove(new Terminal("&"));
                     return primeros;
                 }
             }else{
                 primeros.add(simbolo);
                 if(!simbolo.isEpsilon()){
                     primeros = deleteRepeat(primeros);
                     primeros.remove(new Terminal("&"));
                     return primeros;
                 }
             }
        }
        primeros = deleteRepeat(primeros);
        return primeros;
    }
    
    public void setSiguientes(){
        for(NoTerminal noTerminal : noTerminales){
            siguientes.add(new Sig(noTerminal));
        }
        siguientes.get(0).simbolos.add(new Terminal("$"));
        for(Produccion produccion : producciones){            
            for (int i = 0; i < produccion.simbolos.size(); i++) {
                Simbolo simbolo = produccion.simbolos.get(i);
                if(isNonTerminal(simbolo)){                                        
                    ArrayList<Simbolo> primeros = primerosParciales(produccion.simbolos.subList(i+1, produccion.simbolos.size()));                                        
                    if(primeros.size() > 0){
                        if(primeros.size()==1 && primeros.get(0).isEpsilon()){
                            siguientes.get(findIndex((NoTerminal)simbolo)).simbolos.add(produccion.noTerminal);
                        }else{
                            siguientes.get(findIndex((NoTerminal)simbolo)).simbolos.addAll(primeros);
                            if(primeros.contains(new Terminal("&"))){
                                this.siguientes.get(findIndex((NoTerminal)simbolo)).simbolos.add(produccion.noTerminal);
                            }
                        }
                    }else{
                        siguientes.get(findIndex((NoTerminal)simbolo)).simbolos.add(produccion.noTerminal);
                    }
                }
            }
        }
        replaceSiguientes();
    }
    
    private void replaceSiguientes(){
        for(Sig siguiente :  siguientes){
            siguiente.simbolos = deleteRepeat(siguiente.simbolos);
            siguiente.simbolos.remove(siguiente.noTerminal);
        }
        for(Sig siguiente :  siguientes){
            for (int i = 0; i < siguiente.simbolos.size(); i++) {
                Simbolo simbolo = siguiente.simbolos.get(i);
                if(isNonTerminal(simbolo)){                                        
                    siguiente.simbolos.addAll(getSiguientes((NoTerminal)simbolo));
                    siguiente.simbolos = deleteRepeat(siguiente.simbolos);
                    siguiente.simbolos.remove(siguiente.noTerminal);
                    siguiente.simbolos.remove(simbolo);
                    siguiente.simbolos.remove(new Terminal("&"));
                    i = -1;
                }
            }
            siguiente.simbolos = deleteRepeat(siguiente.simbolos);
        }
    }
    
    public ArrayList<Simbolo> getSiguientes(NoTerminal noTerminal){
        for(Sig siguiente : siguientes){
            if(siguiente.noTerminal.equals(noTerminal)){
                return siguiente.simbolos;
            }
        }
        return null;
    }
    
    private int findIndex(NoTerminal noTerminal){
        for(Sig siguiente : siguientes){
            if(siguiente.noTerminal.equals(noTerminal)){
                return siguientes.indexOf(siguiente);
            }
        }
        return -1;
    }
    
    private void replacePrimeros(){
        for(Prim primero : primeros){
            if(primero.simbolos.contains(primero.noTerminal)){
                primero.simbolos.remove(primero.noTerminal);
            }            
        }
        
        for(Prim primero : primeros){            
            for (int i = 0; i < primero.simbolos.size(); i++) {
                Simbolo simbolo = primero.simbolos.get(i);
                if(isNonTerminal(simbolo)){
                    primero.simbolos.remove(simbolo);
                    ArrayList<Simbolo> simbolosTemp = getPrimero((NoTerminal)simbolo).simbolos;
                    simbolosTemp = deleteRepeat(simbolosTemp);
                    simbolosTemp.remove(new Terminal("&"));
                    primero.simbolos.addAll(simbolosTemp);
                    i = -1;
                }                
            }
            primero.simbolos = deleteRepeat(primero.simbolos);
        }        
    }
    
    private ArrayList<Simbolo> deleteRepeat(ArrayList<Simbolo> simbolos){
        ArrayList<Simbolo> nuevosSimbolos = new ArrayList<>();
        for (Simbolo simbolo : simbolos) {
            if(!nuevosSimbolos.contains(simbolo)){
                nuevosSimbolos.add(simbolo);
            }
        }
        return nuevosSimbolos;
    }
    
    private ArrayList<Produccion> deleteRepeatP(ArrayList<Produccion> producciones){
        ArrayList<Produccion> nuevosSimbolos = new ArrayList<>();
        for (Produccion produccion : producciones) {
            if(!nuevosSimbolos.contains(produccion)){
                nuevosSimbolos.add(produccion);
            }
        }
        return nuevosSimbolos;
    }
    
    public Prim getPrimero(NoTerminal noTerminal){
        for(Prim primero : primeros){
            if(primero.noTerminal.equals(noTerminal)){
                return primero;
            }
        }
        return null;
    }   
    
    private int getIndexPrimero(NoTerminal noTerminal){
        int i = -1;
        for(Prim primero : primeros){
            if(primero.noTerminal.equals(noTerminal)){
                i = primeros.indexOf(primero);
            }
        }
        return i;
    }    
    
    private void getPrimerosR(NoTerminal noTerminal){
        ArrayList<Produccion> producciones = getProducciones(noTerminal);
        int i = getIndexPrimero(noTerminal);
        for (Produccion produccion : producciones) {
            for(Simbolo simbolo : produccion.simbolos){
                if(!primeros.get(i).simbolos.contains(simbolo)){
                    if(isNonTerminal(simbolo)){
                        NoTerminal nt = getNoTerminal(simbolo);
                        if(nt.produceEpsilon==1){
                            primeros.get(i).simbolos.add(simbolo);
                            getPrimerosR((NoTerminal)simbolo);
                        }else{
                            primeros.get(i).simbolos.add(simbolo);
                            break;
                        }
                    }else{
                        if(!simbolo.isEpsilon()){
                            primeros.get(i).simbolos.add(simbolo);
                            break;
                        }
                    }
                }
            }
        }   
    }
    
    private ArrayList<Produccion> getProducciones(NoTerminal noTerminal){
        ArrayList<Produccion> producciones = new ArrayList<>();
        for (Produccion produccion : this.producciones) {
            if(produccion.noTerminal.equals(noTerminal)){
                producciones.add(produccion);
            }
        }
        return producciones;
    }
    
    public boolean isNonTerminal(Simbolo s){
        return s instanceof NoTerminal;
    }
    
    public boolean isTerminal(Simbolo s){
        return s instanceof Terminal;
    }
    
    public static boolean isNonTerminal(String s){
        return Character.isUpperCase(s.charAt(0));        
    }      

    public boolean contains(ArrayList<NoTerminal> noTerminales, NoTerminal n) {
        for (NoTerminal noTerminal : noTerminales) {
            if(noTerminal.toString().compareTo(n.toString())==0){
                return true;
            }
        }
        return false;
    }
    
    public boolean contains(ArrayList<Terminal> Terminales, Terminal t) {
        for (Terminal terminal : Terminales) {
            if(terminal.equals(t)){
                return true;
            }
        }
        return false;
    }
    
    public NoTerminal newNonTerminal(){
        for(String letra : letras){
            NoTerminal nt = new NoTerminal(letra, false);
            if(!noTerminales.contains(nt)){
                noTerminales.add(nt);
                return nt;
            }            
        }
        return null;
    } 
    
}
