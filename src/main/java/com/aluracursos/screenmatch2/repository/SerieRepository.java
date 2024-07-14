package com.aluracursos.screenmatch2.repository;

import com.aluracursos.screenmatch2.models.Categoria;
import com.aluracursos.screenmatch2.models.Episodio;
import com.aluracursos.screenmatch2.models.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository <Serie, Long> {
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);
     // para traer las 5 mejores evaluaciones
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(Categoria categoria);
    //List<Serie> findByTotalTemporadaslessThanEqualAndEvaluacionGreaterThanEqual(int totalTemporadas, Double evaluacion);
    @Query("SELECT s FROM Serie s WHERE s.totalDeTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion") // los 2 ptos hacen referencia al parametro
    List<Serie> seriesPorTemparadaYEvaluacion(int totalDeTemporadas, Double evaluacion);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5 ")
    List<Episodio> top5Episodios(Serie serie);
}
