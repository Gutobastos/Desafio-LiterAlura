package br.com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private List<String> resultado;

    private String titulo;

    private String nome;

    private int anoNascimento;

    private int anoFalecimento;

    private String idioma;

    private int download;

    public Livro(){}

    public Livro(DadosResults dadosResults){
        this.titulo = dadosResults.titulo();
        this.download = dadosResults.download();
    }

    public Livro(DadosAutores dadosAutores){
        this.nome = dadosAutores.nome();
        this.anoNascimento = dadosAutores.anoNascimento();
        this.anoFalecimento = dadosAutores.anoFalecimento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getResultado() {
        return resultado;
    }

    public void setResultado(List<String> resultado) {
        this.resultado = resultado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    @Override
    public String toString() {
        return "titulo= '" + resultado + '\'' +
                ", autores= '" + nome + '\'' +
                ", idioma= '" + idioma + '\'' +
                ", download= " + download;
    }
}