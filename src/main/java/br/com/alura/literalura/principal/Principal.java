package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.AutoresRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoAPI;
import br.com.alura.literalura.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private final String enderecoAPI = "https://gutendex.com/books/";
    private ConverteDados conversor = new ConverteDados();
    private LivroRepository repositorio;
    private AutoresRepository repositorioAutores;
    private Livro livro = new Livro();
    private Autores autor = new Autores();

    private List<Livro> livros = new ArrayList<>();
    private List<Autores> autores = new ArrayList<>();
    private List<Dados> dados = new ArrayList<>();
    private List<DadosLivro> dadosLivro = new ArrayList<>();
    private List<DadosAutores> dadosAutores = new ArrayList<>();
    private int opcao = 9;

    public Principal(LivroRepository repositorio, AutoresRepository repositorioAutores) {
        this.repositorio = repositorio;
        this.repositorioAutores = repositorioAutores;
    }

    public void exibeMenu() {
        while (opcao != 0) {
            var menu = """
                    ########### LITERALURA ##########
                                                    #
                    1 - BUSCAR LIVRO POR TÍTULO     #
                    2 - BUSCAR TODOS OS LIVROS      #
                    3 - BUSCA PELOS AUTORES         #
                    4 - BUSCA PELOS AUTORES VIVOS   #
                    5 - BUSCAR LIVROS PELO IDIOMA   #
                                                    #
                    0 - SAIR                        #
                                                    #
                    #################################
                    """;
            System.out.println(menu);
            System.out.println("Digite uma das opções: ");
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 0:
                    System.out.println("Saindo...");
                    break;
                case 1:
                    buscarLivroWeb();
                    break;
                case 2:
                    buscarTodosLivrosWeb();
                    break;
                case 3:
//                    buscarPelosAutores();
                    break;
                case 4:
//                    buscarPelosAutoresVivos();
                    break;
                case 5:
//                    buscarLivrosPeloIdioma();
                    break;
            }
        }
    }

    private Dados getDadosPorLivro() {
        System.out.println("Informe o título do livro que deseja buscar: ");
        var tituloDoLivro = leitura.nextLine();
        System.out.println(tituloDoLivro.toUpperCase());
        var json = consumo.obterDadosAPI(enderecoAPI + "?search=" + tituloDoLivro);
        Dados dados = conversor.obterDados(json, Dados.class);
        return dados;
    }

    private Dados getDadosAutores() {
        System.out.println("Buscando dados dos autores o livro: ");
        Livro codigoDoLivro = new Livro(dadosLivro.get(0));
        var json = consumo.obterDadosAPI(enderecoAPI + codigoDoLivro.getCodigo() + "/");
        Dados dados = conversor.obterDados(json, Dados.class);
        return dados;
    }

    private Dados getDadosLivro() {
        System.out.println("Buscando pela lista de livros disponíveis...");
        var json = consumo.obterDadosAPI(enderecoAPI);
        Dados dados= conversor.obterDados(json, Dados.class);
        return dados;
    }

    private void buscarLivroWeb() {
        dadosLivro = getDadosPorLivro().dadosLivros();

        autores = dadosLivro.stream()
                .flatMap(d -> d..stream()
                .map(a -> new Autores(d.dadosAutores(), a)))
                .collect(Collectors.toList());

        livro.setAutores(autor);

        System.out.println("\nLivro: " + dadosLivro);
        System.out.println("Autores: " + dadosAutores);

        salvaDados();
    }

    private void buscarTodosLivrosWeb() {
        dadosLivro = getDadosLivro().dadosLivros();
        dadosLivro.forEach(System.out::println);
    }

    private void salvaDados() {
        System.out.println("\nDeseja salvar esse livro na lista? (S/N)");
        var salvar = leitura.nextLine();

        if (salvar.equalsIgnoreCase("s")) {
            livro = new Livro(dadosLivro.get(0));
            repositorio.save(livro);
            System.out.println("\nLivro salvo na lista com sucesso !!!");
        } else {
            System.out.println("O livro não foi salvo na lista !");
        }
    }


}