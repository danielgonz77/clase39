
package model.frontend;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FrontendSearchResponse{
        private String[] frase;
        private int[] ocurrencias;
        private double[] tf;
        private double[] itf;
        private String[] libros;
        private String server;
        private String frase_completa;
        // private String[] ip;
        // private String[] libro;

        public FrontendSearchResponse(String[] frase, int[] ocurrencias, double[] tf, double[] itf, String[] libros) {
            this.frase = frase;
            this.ocurrencias = ocurrencias;
            this.tf = tf;
            this.itf = itf;
            this.libros = libros;
            this.frase_completa = "";
        }

        public FrontendSearchResponse(){

        }


        public void setResponseFrase(String[] frase) {
            this.frase = frase;
        }

        public void setResponseOcurrencias(int[] ocurrencias) {
            this.ocurrencias = ocurrencias;
        }

        public void setResponseTf(double[] tf) {
            this.tf = tf;
        }

        public void setResponseItf(double[] itf) {
            this.itf = itf;
        }

        public void setResponseLibro(String[] libro) {
            this.libros = libro;
        }

        public void setServer(String server){
            this.server = server;
        }

        public void setFraseCompleta(String frase){
            this.frase_completa = frase;
        }

        public String[] getResponseFrase() {
            return this.frase;
        }

        public int[] getResponseOcurrencias() {
            return this.ocurrencias;
        }

        public double[] getResponseTf() {
            return this.tf;
        }

        public double[] getResponseItf() {
            return this.itf;
        }

        public String[] getResponseLibro() {
            return this.libros;
        }

        public String getServer(){
            return this.server;
        }

        public String getFraseCompleta(){
            return this.frase_completa;
        }

        // public FrontendSearchResponse SortValues(){
        //     double[] aux1 = this.itf;
        //     double[] aux2 = this.itf;
        //     double[] aux3 = this.tf;
        //     String[] aux4 = this.frase;
        //     int[] aux5 = this.ocurrencias;
        //     String[] aux6 = this.libros;

        //     Arrays.sort(aux1);
        //     int[] orden = new int[aux1.length];

        //     for(int i=0;i<aux1.length;i++){
        //         orden[i] = Arrays.asList(aux1).indexOf(this.itf[i]);
        //     }

        //     for(int i=0;i<aux1.length;i++){
        //         this.itf[i] = aux2[orden[i]];
        //         this.tf[i] = aux3[orden[i]];
        //         this.frase[i] = aux4[orden[i]];
        //         this.ocurrencias[i] = aux5[orden[i]];
        //         this.libros[i] = aux6[orden[i]];
        //     }   
            
        //     return new FrontendSearchResponse(this.frase, this.ocurrencias, this.tf, this.itf, this.libros);
            
        // }



        public String toString(){
            String salida = "";
            for (int i=0; i<frase.length; i++) {
                salida = salida + "\nPalabra: " + this.frase[i] + "\nOcurrencias: " + this.ocurrencias[i] +
                "\ntf: " + this.tf[i] + "\nitf: " + this.itf[i] + "\nLibro: " + this.libros[i] + "\n";
            }
            return salida;
        }


}
