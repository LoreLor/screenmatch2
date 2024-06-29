package com.aluracursos.screenmatch2;

import com.aluracursos.screenmatch2.models.DatosEpisodio;
import com.aluracursos.screenmatch2.models.DatosSerie;
import com.aluracursos.screenmatch2.models.DatosTemporada;
import com.aluracursos.screenmatch2.principal.EjemploStreams;
import com.aluracursos.screenmatch2.principal.Principal;
import com.aluracursos.screenmatch2.service.ConsumoAPI;
import com.aluracursos.screenmatch2.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Screenmatch2Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Screenmatch2Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.mostrarMenu();

		//me traigo el ejemplo streams
		EjemploStreams ejemploStreams = new EjemploStreams();
		System.out.println("\n***** Los nombres son: ");
		ejemploStreams.muestraNombres();

		/** DATOS MIGRADOS A PRINCIPAL

		String url = "http://www.omdbapi.com/?t=game+of+thrones&apikey=9886d19c";
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obtenerDatos(url);
		System.out.println(json);

		//instancio mi clase ConvierteDatos
		ConvierteDatos conversor = new ConvierteDatos();
		var datos = conversor.obtenerDatos(json, DatosSerie.class);
		System.out.println(datos);

		//datos para el nuevo path
		String url2 = "http://www.omdbapi.com/?t=game+of+thrones&Season=1&episode=1&apikey=9886d19c";
		json = consumoAPI.obtenerDatos(url2);

		//guardo los datos en una variable
		var datos2 = conversor.obtenerDatos(json, DatosEpisodio.class);
		System.out.println(datos2);

		//me creo una lista
		List<DatosTemporada> temporadas = new ArrayList<DatosTemporada>();

		for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
			// para temporadas
			String url3 = "http://www.omdbapi.com/?t=game+of+thrones&Season="+i+"&apikey=9886d19c";
			json = consumoAPI.obtenerDatos(url3);
			var datos3 = conversor.obtenerDatos(json, DatosTemporada.class);
			//agrego a la lista
			temporadas.add(datos3);
		}
			//para imprimir cada temporada
			temporadas.forEach(System.out::println);
		**/

	}
}
