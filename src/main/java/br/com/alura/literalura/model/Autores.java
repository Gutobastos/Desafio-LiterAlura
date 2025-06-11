package br.com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int anoNascimento;
    private int anoFalecimento;

    @ManyToOne
    private Livro livro;

    public Autores(){}

    public Autores(DadosAutores dadosAutores){
        this.nome = dadosAutores.nome();
        this.anoNascimento = dadosAutores.anoNascimento();
        this.anoFalecimento = dadosAutores.anoFalecimento();
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(int anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public int getAnoFalecimento() {
        return anoFalecimento;
    }

    public void setAnoFalecimento(int anoFalecimento) {
        this.anoFalecimento = anoFalecimento;
    }

    @Override
    public String toString() {
        return "nome=' " + nome + '\'' +
                ", anoNascimento= " + anoNascimento +
                ", anoFalecimento= " + anoFalecimento;
    }

}