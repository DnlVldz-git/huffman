
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        lista listita = new lista();
        int[] letras = new int[256];
        String[] ruta = new String[256];
        Scanner leer = new Scanner(System.in);
       
        System.out.println("Introduzca la dirección del archivo");
        String direccion = leer.nextLine();
        //"C:\\Users\\dani_\\Desktop\\input.txt"
        
        try(FileReader file_reader = new FileReader(direccion)){
            int caracter_leido = file_reader.read();
            while(caracter_leido!= -1) {
                char caracter = (char) (caracter_leido);
                System.out.print(caracter);
                insertar_caracter(caracter_leido, letras);
                caracter_leido = file_reader.read();
            }
            file_reader.close();
        }catch(IOException ex){
            System.err.println("Error al leer el archivo");
            ex.printStackTrace();

        }
        for(int i = 0; i <256; i++){
            if(letras[i]!=0){
                listita.insertar(letras[i],(char) (i));
            }
        }
        
        System.out.println("\n");
        listita.codigo_arbol();
        System.out.println("\n");
        arbol arbolito = listita.inicio.arbol;
        arbolito.ruta(ruta);
        
        System.out.println("\nIntroduzca la dirección destino de los archivos");
        String destino = leer.nextLine(); 
        
        try(FileReader file_reader = new FileReader(direccion)){
            
            int caracter_leido = file_reader.read();
            File file = new File(destino+"\\"+"output.txt"); //"C:\\Users\\dani_\\Desktop"
            File file2 = new File(destino+"\\"+"tabla.txt"); 
            
            if (!file.exists()) {
                file.createNewFile();
            }
            
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            
            FileWriter fw2 = new FileWriter(file2);
            BufferedWriter bw2 = new BufferedWriter(fw2);
            
            while(caracter_leido!= -1) {
                char caracter = (char) (caracter_leido);
                bw.append(ruta[caracter_leido]);
                caracter_leido = file_reader.read();
            }
            
            for(int i = 0; i<256; i++){
                if(ruta[i]!=null){
                    bw2.append((char) (i)+": "+ruta[i]);
                    bw2.append("\n");
                }
            }
            
            bw2.close();
            bw.close();
            file_reader.close();
            
        }catch(IOException ex){
            System.err.println("Error al leer el archivo");
            ex.printStackTrace();
        }
        System.out.println("\nEl archivo codificado está en la dirección: "+ destino+"\\"+"output.txt");
        System.out.println("\nEl archivo de la tabla está en la dirección: "+ destino+"\\"+"tabla.txt");
        
    }
    
    static void insertar_caracter(int ascii, int[] letras){
        letras[ascii]= letras[ascii]+1; 
    }           
    
}

class lista{
    nodo_lista inicio = null;
    
    public void insertar(int dato, char letra){
        nodo_lista nuevo = nuevo_nodo(dato, letra);
        if(vacio()){
            inicio = nuevo;
        }else if(inicio.arbol.raiz.frec > nuevo.arbol.raiz.frec){
            nuevo.sig = inicio;
            inicio = nuevo;
        }else{
            nodo_lista ant = null;
            nodo_lista aux = inicio;
            
            while(aux != null && aux.arbol.raiz.frec <= nuevo.arbol.raiz.frec){
                ant = aux;
                aux = aux.sig;
            }
            
            ant.sig = nuevo;
            nuevo.sig = aux;            
        }
    }
    
    public void insertar_arbol(arbol groot){
        nodo_lista nuevo = nuevo_arbol(groot);
        
        if(vacio()){
            inicio = nuevo;
        }else if(inicio.arbol.raiz.frec > nuevo.arbol.raiz.frec){
            nuevo.sig = inicio;
            inicio = nuevo;
        }else{
            nodo_lista ant = null;
            nodo_lista aux = inicio;
            
            while(aux != null && aux.arbol.raiz.frec <= nuevo.arbol.raiz.frec){
                ant = aux;
                aux = aux.sig;
            }
            
            ant.sig = nuevo;
            nuevo.sig = aux;            
        }
    }
    
    public arbol combinar(arbol letra1, arbol letra2){
        if(letra1.raiz == null || letra2.raiz == null){
            return letra1;
        }
        arbol combinado = new arbol();
        nodo_arbol raiz_combinada = new nodo_arbol();
        nodo_arbol izq = letra1.raiz;
        nodo_arbol der = letra2.raiz;
        
        raiz_combinada.letra = '^';
        raiz_combinada.frec = izq.frec + der.frec;
        raiz_combinada.izq = izq;
        raiz_combinada.der = der;
        
        combinado.insertar_nodo(raiz_combinada);
        return combinado;
    }
    
    public void codigo_arbol(){
        if(inicio != null){
            nodo_lista aux1 = inicio;
            nodo_lista aux2 = inicio.sig;
            
            while(aux2 != null){
                arbol arbolito = combinar(aux1.arbol, aux2.arbol);
                this.insertar_arbol(arbolito);
                inicio = inicio.sig.sig;
                aux1 = inicio;
                aux2 = inicio.sig;
            }
        }
    }
    
    public void recorrer(){
        nodo_lista aux = inicio;
        while( aux != null ){
            nodo_arbol raiz = aux.arbol.raiz;
            System.out.print(raiz.letra + ": " + raiz.frec + " ");
            aux = aux.sig;
        }
    }
    
    public boolean vacio(){
        return inicio == null;
    }
    
    public nodo_lista nuevo_nodo(int dato, char letra){
        arbol arbolito = new arbol();
        arbolito.insertar(dato, letra);
        
        nodo_lista nuevo = new nodo_lista();
        nuevo.arbol = arbolito;
        nuevo.sig = null;
        return nuevo;
    }
    
    public nodo_lista nuevo_arbol(arbol arbolito ){       
        nodo_lista nuevo = new nodo_lista();
        nuevo.arbol = arbolito;
        nuevo.sig = null;
        return nuevo;
    }
}

class arbol{
    
    nodo_arbol raiz = null;
    public boolean bandera = false;
    
    public void insertar(int dato, char letra){
        nodo_arbol nuevo = crear_nodo(dato, letra);
        
        if(raiz == null){
            raiz = nuevo;
        }else{
            nodo_arbol aux  = raiz;
            nodo_arbol ant = null;
            while(aux!=null){
                ant = aux;
                if(nuevo.frec > aux.frec){
                    aux = aux.der;
                }else{
                    aux = aux.izq;
                }    
            }
            if(nuevo.frec > ant.frec){
                ant.der = nuevo;
            }else{
                ant.izq = nuevo;
            }
        }
    }
    
    public void ruta(String[] ruta){
        in_orden(raiz, "", "", ruta);
    }

    public void in_orden(nodo_arbol aux, String ruta, String num, String[] rutas){
        ruta = ruta+num;
        if (aux != null) {
            in_orden(aux.izq, ruta, "0", rutas);
            in_orden(aux.der, ruta, "1", rutas);
            if(aux.der==null&&aux.izq==null){
                rutas[(int) (aux.letra)]= ruta; 
            }
        } 
        
    }
    
    public void insertar_nodo(nodo_arbol nuevo){
        raiz = nuevo;
    }
    
    public nodo_arbol crear_nodo(int dato, char letra){
        nodo_arbol nuevo = new nodo_arbol();
        nuevo.letra = letra;
        nuevo.frec = dato;
        nuevo.izq = null;
        nuevo.der = null;
        
        return nuevo;
    }
    
}

class cola{
    
    nodo_cola inicio = null;
    nodo_cola fin = null;
    
    public nodo_cola crear_nodo(nodo_arbol dato){
        nodo_cola nuevo = new nodo_cola();
        nuevo.prev = null;
        nuevo.sig = null;
        nuevo.dato = dato;
        return nuevo;
    }
    

    public void meter(nodo_arbol dato){
        nodo_cola nuevo = crear_nodo(dato);
        if(vacio()){
            inicio = nuevo;
            fin = nuevo;
        }
        else{
            nuevo.prev = fin;
            fin.sig = nuevo;
            fin = nuevo;
        }
    }
    

    public nodo_arbol sacar(){
        nodo_arbol resultado = inicio.dato;
        if(inicio == fin){
            inicio = null;
            fin = null;
        }else{
            inicio = inicio.sig;
            inicio.prev = null;
        }
        
        return resultado;
    }
    
    public boolean vacio(){
        return (inicio == null) && (fin == null);
    }
}

class nodo_lista{
    arbol arbol;
    nodo_lista sig;
}

class nodo_arbol{
    int frec;
    char letra;
    nodo_arbol izq;
    nodo_arbol der;
}

class nodo_cola{
    nodo_arbol dato;
    nodo_cola prev;
    nodo_cola sig;
}
