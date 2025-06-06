package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(@JsonAlias("results") List<DadosResults> dadosDoLivro,
                         @JsonAlias("authors") List<DadosAutores> respostaAutores
) {}
