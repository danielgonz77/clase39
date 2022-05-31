package model.frontend;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
//import java.util.Arrays;
import java.util.Random;

public class Buscador {
    
    String[] frase_inicial;
    int[] nt;
    String[] frase_final;
    int[] ocurrencias;
    double[] tf;
    double[] itf;
    String[] libro;
    String server;
    String frase_completa_original;
    
    //double[] itf;
    double palabras_totales;
    
    public Buscador(String[] frase, String server){
        this.frase_inicial = frase;
        this.server = server;
        this.frase_completa_original = "";
        for (String string : frase) {
            this.frase_completa_original += string;
            this.frase_completa_original += " ";
        }
    }

    public void SearchOcurrencias(){

        // FileReader fileReader = new FileReader("src/main/resources/file.txt");
        // BufferedReader bufferedReader = new BufferedReader(fileReader);
        // Stream<String> lines = bufferedReader.lines();
        String[] pathnames;
        String pathdir = "src/main/resources/LIBROS_TXT/";

        File f = new File("src/main/resources/LIBROS_TXT");

        pathnames = f.list();


        int contador_libros = 0;
        double aux1 = 0;
        double aux2 = 0;

        if(pathnames.length % 3 != 0){
            aux1 =  Math.floor((pathnames.length)/3);
            System.out.println("!=0 aux1= " + aux1);
            if(aux1 % 2 != 0){
                aux2 =  Math.floor((pathnames.length-aux1)/2);
                //System.out.println("!=0 aux2= " + aux2);
            }else{
                aux2 = pathnames.length/2;
                //System.out.println("==0 aux2= " + aux2);
            }
        }else{
            aux1 = pathnames.length/3;
            //System.out.println("==0 aux1= " + aux1);
        }
        double aux3 = pathnames.length - aux1 - aux2;
        // System.out.println("\n\n-------------------AUX-------------------\n\n");
        // System.out.println(aux1 + ", " + aux2 + ", " + aux3);
        System.out.println(server);
        String[] nombre_libros = null;

        if(server.contains("34.168.153.230")){
            nombre_libros = Arrays.copyOfRange(pathnames, 0, (int)aux1); //0-14
        }else if(server.contains("107.178.223.194")){
            nombre_libros = Arrays.copyOfRange(pathnames, (int)aux1, (int)aux1 + (int)aux2); // 15-29
        }else if(server.contains("34.125.217.185")){
            nombre_libros = Arrays.copyOfRange(pathnames, (int)aux1 + (int)aux2, pathnames.length); //30-45
        }

        this.ocurrencias = new int[nombre_libros.length*frase_inicial.length];
        this.tf = new double[nombre_libros.length*frase_inicial.length];
        this.itf = new double[nombre_libros.length*frase_inicial.length];
        this.libro = new String[nombre_libros.length*frase_inicial.length];
        this.frase_final = new String[nombre_libros.length*frase_inicial.length];
        this.nt = new int[frase_inicial.length];
        
        for(int i=0; i<frase_inicial.length; i++){
            this.nt[i] = 0;
        }

        


        for (String pathname : nombre_libros) {
            System.out.println(pathname);
            this.palabras_totales = 0;
            //File f1 = new File("src/main/resources/LIBROS_TXT/Adler,_Elizabeth__1991_._La_esmeralda_de_los_Ivanoff_[10057].txt");
            File f1 = new File(pathdir+pathname);
            String[] words = null;
            FileReader fr;

            // System.out.println(this.ocurrencias.length);
            // System.out.println(this.tf.length);
            // System.out.println(this.libro.length);
            // System.out.println(this.frase_final.length);
            
            // System.out.println("frase.length + " + frase.length + "\n");
            // for(int i=0; i<frase.length; i++){
            //     System.out.println(frase[i]);
            // }
            for(int i=0; i<frase_inicial.length; i++){
                //System.out.println("\n\n------------inicia try " + i + "------------\n\n");
                try {
                    //System.out.println("dentro");
                    fr = new FileReader(f1, StandardCharsets.ISO_8859_1);
                    BufferedReader br = new BufferedReader(fr); 
                    String s;     
                    int count_palabras=0;
                    while((s=br.readLine())!=null)   
                    {
                        words=s.split(" ");  
        
                        for (String word : words){
                            if (word.toLowerCase().contains(this.frase_inicial[i].toLowerCase()))   
                            {
                                count_palabras++;   
                            }
                            this.palabras_totales++;
                        }
                    }
        
                    fr.close();

                    if(count_palabras!=0){
                        this.nt[i]++;
                    }
                    //System.out.println("Contador libros: " + contador_libros);

                    this.ocurrencias[contador_libros] = count_palabras;
                    this.tf[contador_libros] = this.ocurrencias[contador_libros]/this.palabras_totales;
                    this.frase_final[contador_libros] = this.frase_inicial[i];
                    this.libro[contador_libros] = pathname;
                    //this.tf[contador_libros] =Math.log10(a)

                    System.out.println("\nPalabra: " + this.frase_final[contador_libros] + "\nOcurrecias: " + this.ocurrencias[contador_libros] + 
                    "\nLibro: " + this.libro[contador_libros] + "\ntf: " + this.tf[contador_libros] + "\n");
                    
                    contador_libros++;

                    //System.out.println("Ahora el contador de libros vale: " + contador_libros);
                    
                    //return "Palabra: " + this.frase[i] + " Ocurrencias: " + count_palabras + "\n";
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        
        //System.out.println("-----------Salio-----------");

    }

    public String[] getFrase(){
        return this.frase_final;
    }
    public double[] getTf(){
        return this.tf;
    }
    public int[] getOcurrencias(){
        return this.ocurrencias;
    }
    public String[] getLibros(){
        return this.libro;
    }

    public String getFraseCompleta(){
        return this.frase_completa_original;
    }

    public double[] getItf(){
        for(int i=0; i<this.tf.length; i+=this.frase_inicial.length){
            for(int l=0; l<this.frase_inicial.length; l++){
                this.itf[i+l] = Math.log10(this.getLibros().length/ this.nt[l]);
            }
        }

        return this.itf;
    }


    public String toString(){
        String cadena = "";

        for(int i=0; i<this.frase_final.length;i++){

            cadena = cadena + "\nPalabra: " + this.frase_final[i] + "\nOcurrecias: " + this.ocurrencias[i] + 
            "\nLibro: " + this.libro[i] + "\ntf: " + this.tf[i] + "\n\n";

        }

        return cadena;
    }

    // public double[] SearchTf(){

    //     for (int i=0; i<this.ocurrencias.length; i++) {
    //         this.tf[i] = this.ocurrencias[i]/this.palabras_totales;
    //     }
    //     return this.tf;
    // }



}
