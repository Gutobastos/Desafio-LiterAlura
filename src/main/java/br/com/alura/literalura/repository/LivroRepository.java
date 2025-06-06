package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long>  {

    Optional<Livro> findByTituloContainingIgnoreCase(String tituloLivro);

//    Optional<Livro> findByTituloContainingIgnoreCase(String nomeLivro);
//
//    List<Livro> findByAutoresContainingIgnoreCase(String nomeAtores, Double avaliacao);
//
//    List<Livro> findByIdioma(Livro livro);
//
//    @Query ("SELECT l FROM Livro l WHERE l.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
//    List<Livro> seriesPorTemporadaEAvaliacao(int totalTemporadas, Double avaliacao);
//
//    @Query("SELECT l FROM Livro l JOIN l.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
//    List<Livro> episodiosPorTrecho(String trechoEpisodio);
//
//    @Query("SELECT l FROM Livro l JOIN l.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC limit 5")
//    List<Livro> topEpisodiosPorSerie(Livro serie);
//
//    @Query("SELECT l FROM Livro l JOIN l.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
//    List<Livro> episodiosPorSerieEAno(Livro serie, int anoLancamento);
//
//    @Query("SELECT l FROM Livro l " +
//            "JOIN l.episodios e " +
//            "GROUP BY l " +
//            "ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
//    List<Livro> lancamentosMaisRecentes();
//
//    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
//    List<Livro> obterEpisodiosPorTemporada(Long id, Long numero);
}
