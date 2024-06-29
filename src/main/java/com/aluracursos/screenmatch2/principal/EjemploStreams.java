package com.aluracursos.screenmatch2.principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    //metodo
    public void muestraNombres(){
        List<String> nombres = Arrays.asList("Brenda", "Luis", "Brenda", "Genesis");
        //uso de stream para anidar operaciones y funciones lambda
        nombres.stream()
                .sorted()
                //.limit(2)
                .filter(n -> n.startsWith("L"))
                .map(n -> n.toUpperCase())
                .forEach(n -> System.out.println(n));
    }
}
