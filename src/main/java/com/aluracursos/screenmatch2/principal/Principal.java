package com.aluracursos.screenmatch2.principal;

import com.aluracursos.screenmatch2.models.*;
import com.aluracursos.screenmatch2.repository.SerieRepository;
import com.aluracursos.screenmatch2.service.ConsumoAPI;
import com.aluracursos.screenmatch2.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Principal {
    //atributos
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=9886d19c";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSerie = new ArrayList<>();
    private SerieRepository repositorio;

    //constructor de la clase
    public Principal(SerieRepository repository){
        this.repositorio = repository;
    }

    //método para mostrar menú
    public void mostrarMenu() {
       var opcion = -1;
       while (opcion != 0) {
           var menu = """
                   1 - Buscar series
                   2 - Buscar episodios
                   3 - Mostrar series buscadas
                   
                   0 - Salir
                   """;
           System.out.println(menu);
           opcion = scanner.nextInt();
           scanner.nextLine();
           switch (opcion) {
               case 1:
                   buscarSerieWeb();
                   break;
               case 2:
                   buscarEpisodioPorSerie();
                   break;
               case 3:
                   mostrarSeriesBuscadas();
                   break;
               case 0:
                   System.out.println("Cerrando la aplicació....");
                   break;
               default:
                   System.out.println("Opción Inválida");
           }
       }
    }
    //se agrupan series
    private DatosSerie getDatosSerie() {
        System.out.println("Ingresa el nombre de la serie que deseas buscar: ");
        var nombreSerie = scanner.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarSerieWeb(){
        DatosSerie datos = getDatosSerie();
        //datosSerie.add(datos);
        Serie serie = new Serie(datos);
        repositorio.save(serie); //guardo en la tabla
        System.out.println(datos);
    }
    private void buscarEpisodioPorSerie() {
        DatosSerie datosSerie = getDatosSerie();
        List<DatosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i<= datosSerie.totalDeTemporadas(); i++) {
           var json = consumoAPI.obtenerDatos(URL_BASE + datosSerie.titulo().replace(" ", "+") + i + API_KEY);
           DatosTemporada datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
           temporadas.add(datosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void mostrarSeriesBuscadas() {
        List<Serie> series = repositorio.findAll(); /*new ArrayList<>();
        series = datosSerie.stream()
                        .map(s -> new Serie(s))
                        .collect(Collectors.toList()); */
         // agrupo las series según la categoría - género
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}
