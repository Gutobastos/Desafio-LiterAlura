package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosResults (@JsonAlias("id") int codigoLivro,
                            @JsonAlias("title") String titulo,
                            @JsonAlias("download_count") int download
){
}
