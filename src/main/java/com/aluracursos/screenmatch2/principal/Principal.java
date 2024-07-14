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
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

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
                   4 - Buscar serie por título
                   5 - Buscar Top 5
                   6 - Series por Categorías
                   7 - filtrar series por temporadas y evaluación
                   8 - Buscar episodios por titulo
                   9 - Top 5 episodios por Serie
                   
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
               case 4:
                   buscarSeriesPorTitulo();
                   break;
               case 5:
                   buscarSeriesTop5();
                   break;
               case 6:
                   buscarSeriePorCategoria();
                   break;
               case 7:
                   filtrarSeriesPorTemporadaYEvaluacion();
                   break;
               case 8:
                   buscarEpisodiosPorTitulo();
                   break;
               case 9:
                   buscarTop5Episodios();
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
        //DatosSerie datosSerie = getDatosSerie(); este método trae los datos directos de api
        mostrarSeriesBuscadas(); //trae los datos de db
        System.out.println("ingresa el nombre de la serie para ver los episodios: ");
        var serieBuscada = scanner.nextLine();

        // creo un Optional
        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(serieBuscada.toLowerCase()))
                .findFirst();

        if(serie.isPresent()){
           var serieEncontrada =  serie.get();
            System.out.println("serieEncontrada" + serieEncontrada);
            List<DatosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i<= serieEncontrada.getTotalDeTemporadas(); i++) {
                var json = consumoAPI.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporada datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            // necesito una lista de episodios
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            // guardo en db
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }

    }
    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll(); /*new ArrayList<>();
        series = datosSerie.stream()
                        .map(s -> new Serie(s))
                        .collect(Collectors.toList()); */
         // agrupo las series según la categoría - género
        System.out.println("series " +series);
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
    private void buscarSeriesPorTitulo() {
        System.out.println("ingresa el nombre de la serie que deseas buscar: ");
        var nombreSerie = scanner.nextLine();
        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if(serieBuscada.isPresent()) {
            System.out.println("La serie buscada es: " + serieBuscada.get());
        } else {
            System.out.println("Serie no encontrada");
        }
    }
    private void buscarSeriesTop5(){
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println(
                "Nombre de la Serie: " + s.getTitulo() +
                " Evaluación: " + s.getEvaluacion())
        );
    }
    private void buscarSeriePorCategoria() {
        System.out.println("Ingrese la categoría/género serie que deseas buscar: ");
        var generoSerie = scanner.nextLine();
        //transformo el géneo a enum Categoria
        var categoria = Categoria.fromEsp(generoSerie);
        List<Serie> seriesPorcategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de las categorias  " + seriesPorcategoria);
    }
    public void filtrarSeriesPorTemporadaYEvaluacion(){
        System.out.println("¿Filtrar séries con cuántas temporadas? ");
        var totalDeTemporadas = scanner.nextInt();
        scanner.nextLine();
        System.out.println("¿Com evaluación apartir de cuál valor? ");
        var evaluacion = scanner.nextDouble();
        scanner.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemparadaYEvaluacion(totalDeTemporadas,evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s -> System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }
    private void  buscarEpisodiosPorTitulo(){
        System.out.println("Escribe el nombre del episodio que deseas buscar");
        var nombreEpisodio = scanner.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie: %s Temporada %s Episodio %s Evaluación %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));

    }
    private void buscarTop5Episodios() {
        buscarSeriesPorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Serie: %s - Temporada %s - Episodio %s - Evaluación %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));

        }
    }
}
