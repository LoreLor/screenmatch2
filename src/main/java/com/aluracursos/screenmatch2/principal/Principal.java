package com.aluracursos.screenmatch2.principal;

import com.aluracursos.screenmatch2.models.DatosEpisodio;
import com.aluracursos.screenmatch2.models.DatosSerie;
import com.aluracursos.screenmatch2.models.DatosTemporada;
import com.aluracursos.screenmatch2.service.ConsumoAPI;
import com.aluracursos.screenmatch2.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    //atributos
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=9886d19c";
    private ConvierteDatos conversor = new ConvierteDatos();

    //método para mostrar menú
    public void mostrarMenu(){
        System.out.println("Ingresa el nombre de la serie que deseas encontrar: ");
        var nombreSerie = scanner.nextLine();
        nombreSerie = nombreSerie.replace(" ","+");
        String url = URL_BASE+nombreSerie+API_KEY;
        var json = consumoAPI.obtenerDatos(url);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //busco los datos de todas las temporadas
        //me creo una lista
        List<DatosTemporada> temporadas = new ArrayList<DatosTemporada>();

        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            // para temporadas
            String url2 = URL_BASE+nombreSerie+"&Season="+i+API_KEY;
            json = consumoAPI.obtenerDatos(url2);
            var datos2 = conversor.obtenerDatos(json, DatosTemporada.class);
            //agrego a la lista
            temporadas.add(datos2);
        }
        //para imprimir cada temporada
        /* temporadas.forEach(System.out::println); */

        // mostrar sólo el título de los episodios de cada temporada - forma dura
        /*for (int i = 0; i < datos.totalDeTemporadas() ; i++) {
            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            //itero para traer los nompres de los episodios
            for (int j = 0; j < episodiosTemporada.size() ; j++) {
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }*/
        // se resume en una linea de código
        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //convierto toda la info en lista del tipo DatosEpisodio
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList()); // lista mutable
        // tabien podria se llamar al .toList() directamente pero se crea una lista inmutable

        //top 5 segun evaluacion
        System.out.println("\n***** TOP 5 episodios: ");
        datosEpisodios.stream()
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion))
                .limit(5)
                .forEach(System.out::println);
    }
}
