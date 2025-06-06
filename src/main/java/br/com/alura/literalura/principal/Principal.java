package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.DadosResults;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.model.Resultado;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoAPI;
import br.com.alura.literalura.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private final String enderecoAPI = "https://gutendex.com/books";
    private ConverteDados conversor = new ConverteDados();
    LivroRepository repositorio;

    private List<Livro> livros = new ArrayList<>();
    private List<DadosLivro> dadosLivros = new ArrayList<>();
    private List<DadosResults> dadosResultsList = new ArrayList<>();
    private int opcao = 9;

    public Principal(LivroRepository repositorio) {
        this.repositorio = repositorio;
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

    private DadosLivro getDadosLivroGeral() {
        System.out.println("Buscando todos os livros disponíveis: ");
        System.out.println("Buscando...");
        var json = consumo.obterDadosAPI(enderecoAPI + "/");
        System.out.println("\nDADOS DA API: " + json + "\n");
        DadosLivro dados = conversor.obterDados(json, DadosLivro.class);
        return dados;
    }

    private DadosLivro getDadosLivro() {
        System.out.println("Digite o nome da série para busca");
        var tituloLivro = "casmurro";
        var json = consumo.obterDadosAPI(enderecoAPI + "/?search=" + tituloLivro.replace(" ", "+"));
        System.out.println("\nDADOS DA API: " + json + "\n");
        DadosLivro dados = conversor.obterDados(json, DadosLivro.class);
        return dados;
    }

    private void buscarLivroWeb() {
        dadosLivros.add(getDadosLivro());
        List<DadosLivro> resultados = dadosLivros.stream()
                .flatMap(d -> d.dadosDoLivro()
                        .stream().map(r -> new DadosLivro(d.dadosDoLivro(), d.respostaAutores())))
                .collect(Collectors.toList());
        resultados.forEach(System.out::println);

    }

    private void buscarTodosLivrosWeb() {
        List<DadosLivro> dadosLivros = new ArrayList<>();
//        dadosLivros.add(getDadosLivroGeral());
        List<DadosLivro> resultados = dadosLivros.stream()
                .flatMap(d -> d.dadosDoLivro()
                        .stream().map(r -> new DadosLivro(d.dadosDoLivro(), d.respostaAutores())))
                .collect(Collectors.toList());
        resultados.forEach(System.out::println);

    }

}