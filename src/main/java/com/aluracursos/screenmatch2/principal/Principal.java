package com.aluracursos.screenmatch2.principal;

import com.aluracursos.screenmatch2.models.DatosEpisodio;
import com.aluracursos.screenmatch2.models.DatosSerie;
import com.aluracursos.screenmatch2.models.DatosTemporada;
import com.aluracursos.screenmatch2.models.Episodio;
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

    //método para mostrar menú
    public void mostrarMenu() {
        System.out.println("Ingresa el nombre de la serie que deseas encontrar: ");
        var nombreSerie = scanner.nextLine();
        nombreSerie = nombreSerie.replace(" ", "+");
        String url = URL_BASE + nombreSerie + API_KEY;
        var json = consumoAPI.obtenerDatos(url);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //busco los datos de todas las temporadas
        //me creo una lista
        List<DatosTemporada> temporadas = new ArrayList<DatosTemporada>();

        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            // para temporadas
            String url2 = URL_BASE + nombreSerie + "&Season=" + i + API_KEY;
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
        /*List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList()); // lista mutable
        // tabien podria se llamar al .toList() directamente pero se crea una lista inmutable

        //top 5 segun evaluacion
        System.out.println("\n***** TOP 5 episodios: ");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("primer filtro N/A " + e))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .peek(e -> System.out.println("********Sgundo filtro orden(M>n) " + e))
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println("********Tercer titulo Mayusculas " + e))
                .limit(5)
                .forEach(System.out::println);*/

        // convierto datos a lista de episodios
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d)))
                .collect(Collectors.toList());

        /*episodios.forEach(System.out::println);*/

        //busqueda de episodios a partir de algún año
        /* System.out.println("Ingresa el año a partir del cual desaes realizar la búsqueda: ");
        var fecha = scanner.nextInt();
        scanner.nextLine(); //para descartar que hayan errores

        // establezco el formato de la fecha
        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);

        //FORMATEO DE FECHAS
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyyy"); */

        // operaciones intermedias para realizar búsqueda s/episodios
        /*episodios.stream()
                .filter(f -> f.getFechaLanzamiento() != null && f.getFechaLanzamiento().isAfter(fechaBusqueda))
                .forEach(r -> System.out.println(
                        String.format("Temporada: " + r.getTemporada() +
                                " Episodio: " + r.getTitulo() +
                                " Fecha de Lanzamiento: " + r.getFechaLanzamiento().format(dtf))
                )); */

        // buscar título de Episodio que contenga la palabra...
        /*System.out.println("ingrese el Titulo que desea encontrar: ");
        var subsTitulo = scanner.nextLine();

        Optional<Episodio> buscarEpisodio = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(subsTitulo.toUpperCase()))
                .findFirst();
        if (buscarEpisodio.isPresent()) {
            System.out.println("Episodio encontrado " + buscarEpisodio.get());
        } else {
            System.out.println("Episodio no encontrado");
        }*/

        // creo evaluaciones por temporada usando MAP
        Map<Integer, Double> evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporada);

        // agrego clase que proporciona más estadisticas
        DoubleSummaryStatistics  est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println(est);

        // para personalizar la salida
        System.out.println("Media de las evaluaciones: " + est.getAverage());
        System.out.println("Episodio mejor evaluado: " + est.getMax());
        System.out.println("Episodio peor evaluado: " + est.getMin());

        // generador de secuencia de numeros
        Stream.iterate(0,n -> n+1)
                .limit(5)
                .forEach(System.out::println); // salida 1 2 3 4 5

        //transformacion de stream de colecciones en stream de elementos
        List<List<String>> lista = List.of(
                List.of("a", "b"),
                List.of("c", "d")
        );
        Stream<String> stream = lista.stream()
                .flatMap(Collection::stream);

        stream.forEach(System.out::println); //salida,a b c d

        //Reducir streams a un resultado
        List<Integer> numeros = List.of(1,2,3,4,5);
        Optional<Integer> resultado = numeros.stream()
                .reduce(Integer::sum);

        resultado.ifPresent(System.out::println); //salida 15
    }
}
