package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutoresRepository extends JpaRepository<Autores, Long>  {

    Optional<Autores> findFirstByNomeContainingIgnoreCase(String nomeAutor);

    @Query("SELECT a FROM Autores a WHERE a.id > 0")
    List<Autores> buscarAutores(String nomeDosAutores);
}
