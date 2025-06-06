package br.com.alura.literalura.dto;

public record LivroDTO(Long id,
                       String titulo,
                       String autores,
                       String idioma,
                       int download) {
}