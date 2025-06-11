package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.AutoresRepository;
import br.com.alura.literalura.repository.IdiomasRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoAPI;
import br.com.alura.literalura.service.ConverteDados;
import org.antlr.v4.runtime.atn.LookaheadEventInfo;

import java.util.*;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private final String enderecoAPI = "https://gutendex.com/books/";
    private ConverteDados conversor = new ConverteDados();
    private LivroRepository repositorio;
    private AutoresRepository repositorioAutores;
    private IdiomasRepository repositorioIdiomas;
    private Livro livro = new Livro();
    private Autores autor = new Autores();
    private Idioma idioma = new Idioma();

    private List<Livro> livros = new ArrayList<>();
    private List<Autores> autores = new ArrayList<>();
    private List<Dados> dados = new ArrayList<>();
    private List<DadosLivro> dadosLivro = new ArrayList<>();
    private List<DadosAutores> dadosAutores = new ArrayList<>();
    private List<String> dadosIdiomas = new ArrayList<>();
    private int opcao = 9;

    public Principal(LivroRepository repositorio, AutoresRepository repositorioAutores, IdiomasRepository repositoriIdiomas) {
        this.repositorio = repositorio;
        this.repositorioAutores = repositorioAutores;
        this.repositorioIdiomas = repositoriIdiomas;
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
            if (opcao <= 5){
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
                        buscarPelosAutores();
                        break;
                    case 4:
//                    buscarPelosAutoresVivos();
                        break;
                    case 5:
//                    buscarLivrosPeloIdioma();
                        break;
                }
            } else {
                System.out.println("Favor informar uma das opções númericas !");
            }
        }
    }


    private Dados getDadosPorLivro() {
        System.out.println("Informe o título do livro que deseja buscar: ");
        var tituloDoLivro = leitura.nextLine();
        var json = consumo.obterDadosAPI(enderecoAPI + "?search=" + tituloDoLivro.toLowerCase().replace(" ", "+"));
        Dados dados = conversor.obterDados(json, Dados.class);
        return dados;
    }

    private Dados getDadosDoLivro() {
        Livro codigoDoLivro = new Livro(dadosLivro.get(0));
        var json = consumo.obterDadosAPI(enderecoAPI + codigoDoLivro.getCodigo() + "/");
        Dados dados = conversor.obterDados(json, Dados.class);
        return dados;
    }

    private Dados getDadosLivros() {
        System.out.println("Buscando pela lista de livros disponíveis...");
        var json = consumo.obterDadosAPI(enderecoAPI);
        Dados dados= conversor.obterDados(json, Dados.class);
        return dados;
    }

    private void salvaDados() {
        System.out.println("\nDeseja salvar esse livro na lista? (S/N)");
        var salvar = leitura.nextLine();

        if (salvar.equalsIgnoreCase("s")) {
            livro = new Livro(dadosLivro.get(0));
            autor = new Autores(dadosAutores.get(0));
            idioma = new Idioma(dadosIdiomas.get(0));
            repositorio.save(livro);
            repositorioAutores.save(autor);
            repositorioIdiomas.save(idioma);
            System.out.println("\nLivro salvo na lista com sucesso !!!");
        } else {
            System.out.println("O livro não foi salvo na lista !");
        }
    }

    private void buscarLivroWeb() {
        dadosLivro = getDadosPorLivro().dadosLivros();
        System.out.println("Buscando dados do livro... Aguarde... ");
        dadosAutores = getDadosDoLivro().dadosAutores();
        dadosIdiomas = getDadosDoLivro().languages();
        var formatacao = """
                        ######################################################################
                                              RESULTADO DA BUSCA POR TÍTULO

                        Código: %s
                        Downloads: %s
                        Título: %s
                        Idioma: %s
                        Autor(s): %s
                        Ano de Nascimento: %d
                        Ano de Falecimento: %d

                        ######################################################################
                        """.formatted(dadosLivro.get(0).codigo(), dadosLivro.get(0).download(),
                dadosLivro.get(0).titulo(), dadosIdiomas.get(0), dadosAutores.get(0).nome(), dadosAutores.get(0).anoNascimento(),
                dadosAutores.get(0).anoFalecimento());

        Optional<Livro> livroInformado = repositorio.findByTituloContainingIgnoreCase(dadosLivro.get(0).titulo());
        if (livroInformado.isPresent()) {
            System.out.println(formatacao);
        } else {
            System.out.println(formatacao);
            System.out.println("Novo livro encontrado na API, mas não está na base de dados !");
            salvaDados();
        }
    }

    private void listarLivrosBuscados() {
        livros = repositorio.findAll();
        livros.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    private void buscarTodosLivrosWeb() {
        dadosLivro = getDadosLivros().dadosLivros();
        var indice = 1;
        for (var i = 0 ; i <= dadosLivro.size(); i++) {
            var formatacaoGeral = """
                    ######################################################################
                                  RESULTADO DA BUSCA DE LIVROS DISPONÍVEIS(%d)
                    
                    Código: %s
                    Downloads: %s
                    Título: %s
                    
                    ######################################################################
                    """.formatted(indice++, dadosLivro.get(i).codigo(), dadosLivro.get(i).download(),
                    dadosLivro.get(i).titulo());
            System.out.println(formatacaoGeral);
        }
    }

    private void buscarPelosAutores() {

    }


}