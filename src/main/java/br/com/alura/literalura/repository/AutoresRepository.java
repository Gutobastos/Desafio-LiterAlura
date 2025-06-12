package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutoresRepository extends JpaRepository<Autores, Long>  {

    Optional<Autores> findFirstByNomeContainingIgnoreCase(String nomeAutor);
}
