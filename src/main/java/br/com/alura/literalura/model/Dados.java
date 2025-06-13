package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Dados(@JsonAlias("results") List<DadosLivro> dadosLivros,
                    @JsonAlias("authors") List<DadosAutores> dadosAutores,
                    @JsonAlias("languages") List<String> idioma
){}