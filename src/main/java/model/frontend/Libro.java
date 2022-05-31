package model.frontend;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import model.frontend.MultipleFSR;

public class Libro implements Comparable<Libro>{
    private String Nombre;
    private double Score;

    public Libro(String nombre, double score){
        this.Nombre = nombre;
        this.Score = score;
    }
    public Libro(){

    }

    public void setNombre(String nombre){
        this.Nombre = nombre;
    }
    public void setScore(double score){
        this.Score = score;
    }
    public String getNombre(){
        return this.Nombre;
    }
    public double getScore(){
        return this.Score;
    }

    public Libro[] getLibros(List<FrontendSearchResponse> ListaFSP){
        FrontendSearchResponse Todos = new FrontendSearchResponse();
        

        String[] frase = new String[ListaFSP.get(0).getResponseFrase().length + 
                                    ListaFSP.get(1).getResponseFrase().length +
                                    ListaFSP.get(2).getResponseFrase().length];
        int[] ocurrencias = new int[ListaFSP.get(0).getResponseOcurrencias().length + 
                                    ListaFSP.get(1).getResponseOcurrencias().length +
                                    ListaFSP.get(2).getResponseOcurrencias().length];
        double[] tf = new double[ListaFSP.get(0).getResponseTf().length + 
                                ListaFSP.get(1).getResponseTf().length +
                                ListaFSP.get(2).getResponseTf().length];
        double[] itf = new double[ListaFSP.get(0).getResponseItf().length + 
                                ListaFSP.get(1).getResponseItf().length +
                                ListaFSP.get(2).getResponseItf().length];
        String[] libros = new String[ListaFSP.get(0).getResponseLibro().length + 
                                    ListaFSP.get(1).getResponseLibro().length +
                                    ListaFSP.get(2).getResponseLibro().length];

        
        int index = 0;


        for (String item: ListaFSP.get(0).getResponseFrase()) {
            frase[index++] = item;
        }
        for (String item: ListaFSP.get(1).getResponseFrase()) {
            frase[index++] = item;
        }
        for (String item: ListaFSP.get(2).getResponseFrase()) {
            frase[index++] = item;
        }

        index = 0;

        for (int item: ListaFSP.get(0).getResponseOcurrencias()) {
            ocurrencias[index++] = item;
        }
        for (int item: ListaFSP.get(1).getResponseOcurrencias()) {
            ocurrencias[index++] = item;
        }
        for (int item: ListaFSP.get(2).getResponseOcurrencias()) {
            ocurrencias[index++] = item;
        }

        index = 0;

        for (double item: ListaFSP.get(0).getResponseTf()) {
            tf[index++] = item;
        }
        for (double item: ListaFSP.get(1).getResponseTf()) {
            tf[index++] = item;
        }
        for (double item: ListaFSP.get(2).getResponseTf()) {
            tf[index++] = item;
        }

        index = 0;

        for (double item: ListaFSP.get(0).getResponseItf()) {
            itf[index++] = item;
        }
        for (double item: ListaFSP.get(1).getResponseItf()) {
            itf[index++] = item;
        }
        for (double item: ListaFSP.get(2).getResponseItf()) {
            itf[index++] = item;
        }

        index = 0;

        for (String item: ListaFSP.get(0).getResponseLibro()) {
            libros[index++] = item;
        }
        for (String item: ListaFSP.get(1).getResponseLibro()) {
            libros[index++] = item;
        }
        for (String item: ListaFSP.get(2).getResponseLibro()) {
            libros[index++] = item;
        }



        Todos.setResponseFrase(frase);
        Todos.setResponseOcurrencias(ocurrencias);
        Todos.setResponseTf(tf);
        Todos.setResponseItf(itf);
        Todos.setResponseLibro(libros);
        Todos.setFraseCompleta(ListaFSP.get(0).getFraseCompleta());
        //System.out.println("-------------------LISTA-------------------");
        // for (String string : Todos.getResponseFrase()) {
        //     System.out.println(string);
        // }
        // for (int string : Todos.getResponseOcurrencias()) {
        //     System.out.println(string);
        // }
        // System.out.println("-------------------LISTA-------------------");
        // for (String librosss : Todos.getResponseLibro()) {
        //     System.out.println(librosss);
        // }
        // FrontendSearchResponse aux7 = Todos.SortValues();
        // System.out.println(aux7.toString());
        
        //System.out.println(Todos.toString());


        // for(int i=0;i<ListaFSP.size();i++){
        //     frase = 
        // }
        String[] tamFrase = Todos.getFraseCompleta().split(" ");
        Libro[] ResultLibros = new Libro[Todos.getResponseLibro().length/tamFrase.length];

        
        index = 0;
        for(int i=0;i<Todos.getResponseLibro().length-tamFrase.length+1;i+=tamFrase.length){
            ResultLibros[index] = new Libro();
            ResultLibros[index].setNombre(Todos.getResponseLibro()[i]);

            // double Pf = Todos.getResponseTf()[i]*Todos.getResponseItf()[i] + 
            //             Todos.getResponseTf()[i+1]*Todos.getResponseItf()[i+1] + 
            //             Todos.getResponseTf()[i+2]*Todos.getResponseItf()[i+2];
            double Pf = 0;

            
            // System.out.println("------------------------------------------------------------------------------Tamanio de la frase start--------------------------");
            // System.out.println(tamFrase.length);
            // System.out.println(Todos.getFraseCompleta());
            // System.out.println("------------------------------------------------------------------------------Tamanio de la frase end--------------------------");

            for(int j=0; j<tamFrase.length; j++){
                Pf = Pf + Todos.getResponseTf()[i+j]*Todos.getResponseItf()[i+j];
            }
            
            ResultLibros[index].setScore(Pf);
            //System.out.println(ResultLibros[index].getNombre());
            //System.out.println(ResultLibros[index].getScore());

            index++;
        }

        List<Libro> Libros_list = Arrays.asList(ResultLibros);
        List<Libro> newList = Libros_list.stream().distinct().collect(Collectors.toList());
        //Collections.sort(Libros_list);
        Collections.sort(newList, Comparator.comparingDouble(Libro::getScore));
        Collections.reverse(newList);

        Libro[] libros_final = new Libro[newList.size()];
        libros_final = newList.toArray(libros_final);
        
        return libros_final;
        // for(int i=0;i<ResultLibros.length;i++){ 
        //     System.out.println(ResultLibros[i].getNombre());
        //     System.out.println(ResultLibros[i].getScore());
        // }

        

        // System.out.println(Todos.size());
    }

    public int compareTo(Libro u1)
    {
        return this.getNombre().compareTo(u1.getNombre());
    }



}

