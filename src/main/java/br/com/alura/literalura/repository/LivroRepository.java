package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Autores;
import br.com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    Optional<Livro> findByTituloContainingIgnoreCase(String titulo);

    @Query("SELECT l FROM Livro l WHERE l.id > 0")
    List<Livro> buscarLivros(String nomeAutores);

    @Query("SELECT a FROM Livro l, Idioma i JOIN l.autores a WHERE a.nome ILIKE %:nomeAutores%")
    List<Autores> buscarDadosLivrosDosAutores(String nomeAutores);
}
