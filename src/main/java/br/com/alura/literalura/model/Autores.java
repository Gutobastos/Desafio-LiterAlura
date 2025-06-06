package br.com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dados_autores")
public class Autores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int anoNascimento;
    private int anoFalecimento;

    @ManyToOne
    private Livro livro;

    public Autores (){}

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

    public Autores(DadosSobreAutores dadosAutores) {
        this.nome = dadosAutores.autores();
        this.anoNascimento = dadosAutores.anoNascimento();
        this.anoFalecimento = dadosAutores.anoFalecimento();


        try {
            this.anoNascimento = dadosAutores.anoNascimento();
        } catch (NumberFormatException ex) {
            this.anoNascimento = 0;
        }
        try {
            this.anoFalecimento = dadosAutores.anoFalecimento();
        } catch (NumberFormatException ex) {
            this.anoFalecimento = 0;
        }
    }

    @Override
    public String toString() {
        return "Nome='" + nome + '\'' +
                ", Ano de Nascimento=" + anoNascimento +
                ", Ano de Falecimento=" + anoFalecimento;
    }
}