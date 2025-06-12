package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdiomasRepository extends JpaRepository<Idioma, Long>  {

    Optional<Idioma> findByIdiomaContainingIgnoreCase(String idioma);
}
