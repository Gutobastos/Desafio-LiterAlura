package br.com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dados_livro")
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int codigoLivro;
    private String titulo;
    private Integer download;

    @ManyToOne
    private Livro livro;

    public Resultado (){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCodigoLivro() {
        return codigoLivro;
    }

    public void setCodigoLivro(int codigoLivro) {
        this.codigoLivro = codigoLivro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getDownload() {
        return download;
    }

    public void setDownload(Integer download) {
        this.download = download;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Resultado(Integer download_count, DadosResults dadosResults) {
        this.titulo = dadosResults.titulo();
        this.download = dadosResults.download();



        try {
            this.download = (dadosResults.download());
        } catch (NumberFormatException ex) {
            this.download = 0;
        }
    }

    @Override
    public String toString() {
        return "Código do Livro= " + codigoLivro +
                ", Título=' " + titulo + '\'' +
                ", Downloads= " + download;
    }
}