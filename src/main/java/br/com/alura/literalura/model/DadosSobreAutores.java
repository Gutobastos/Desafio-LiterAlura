package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSobreAutores(
        @JsonAlias("name") String autores,
        @JsonAlias("birth_year") int anoNascimento,
        @JsonAlias("death_year") int anoFalecimento) {
}
