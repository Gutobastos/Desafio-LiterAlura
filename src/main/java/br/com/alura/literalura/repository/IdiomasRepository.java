package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IdiomasRepository extends JpaRepository<Idioma, Long>  {

    Optional<Idioma> findByIdiomaContainingIgnoreCase(String idioma);

    @Query("SELECT i FROM Idioma i WHERE i.id > 0")
    List<Idioma> buscarIdiomas(String tipoIdioma);
}
