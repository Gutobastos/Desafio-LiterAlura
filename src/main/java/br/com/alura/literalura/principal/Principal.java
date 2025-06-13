package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.AutoresRepository;
import br.com.alura.literalura.repository.IdiomasRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoAPI;
import br.com.alura.literalura.service.ConverteDados;

import javax.swing.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

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
    private List<Idioma> listaDeidiomas = new ArrayList<>();
    private List<Dados> dados = new ArrayList<>();
    private List<DadosLivro> dadosLivro = new ArrayList<>();
    private List<DadosAutores> dadosAutores = new ArrayList<>();
    private List<String> dadosIdiomas = new ArrayList<>();

    private Optional<Autores> autoresBusca;
    private Optional<Livro> livroBusca;
    private Optional<Idioma> idiomaBusca;

    private int opcao = -1;
    private int anoAutoresVivos;
    private boolean validacao = true;

    public Principal(LivroRepository repositorio, AutoresRepository repositorioAutores,
                     IdiomasRepository repositoriIdiomas) {
        this.repositorio = repositorio;
        this.repositorioAutores = repositorioAutores;
        this.repositorioIdiomas = repositoriIdiomas;
    }

    public void exibeMenu() {
        while (opcao != 0) {
            var menu = """
                    \n########### LITERALURA ##########
                                                    #
                    1 - BUSCAR LIVRO POR TÍTULO     #
                    2 - BUSCAR TODOS OS LIVROS      #
                    3 - BUSCAR PELOS AUTORES        #
                    4 - BUSCAR PELOS AUTORES VIVOS  #
                    5 - LISTAR LIVROS PELO IDIOMA   #
                                                    #
                    0 - SAIR                        #
                                                    #
                    #################################
                    """;
            System.out.println(menu);
            System.out.println("Digite uma das opções numéricas: ");

            try {
                opcao = leitura.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println("\nOpção inválida, digite uma opção numérica do MENU !");
            }

            leitura.nextLine();
            if (opcao <= 5) {
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
                        buscarPelosAutoresVivos();
                        break;
                    case 5:
                        buscarLivrosPeloIdioma();
                        break;
                }
            } else {
                System.out.println("Favor informar uma das opções númericas !");
            }
        }
    }

    private void listarLivrosBuscados() {
        livros = repositorio.findAll();
        livros.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    private Dados getDadosPorLivro() {
        System.out.println("Informe o título do livro que deseja buscar: ");
        var tituloDoLivro = leitura.nextLine();
        var json = consumo.obterDadosAPI(enderecoAPI + "?search=" +
                tituloDoLivro.toLowerCase().replace(" ", "+"));
        return conversor.obterDados(json, Dados.class);
    }

    private Dados getDadosDoLivro() {
        Livro codigoDoLivro = new Livro(dadosLivro.get(0));
        var json = consumo.obterDadosAPI(enderecoAPI + codigoDoLivro.getCodigo() + "/");
        return conversor.obterDados(json, Dados.class);
    }

    private Dados getDadosLivros() {
        System.out.println("Buscando pela lista de livros disponíveis...");
        var json = consumo.obterDadosAPI(enderecoAPI);
        return conversor.obterDados(json, Dados.class);
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
        dadosIdiomas = getDadosDoLivro().idioma();
        var formatacao = """
                        ######################################################################
                                         RESULTADO DA BUSCA POR TÍTULO NA API

                        Código: %s
                        Downloads: %s
                        Título: %s
                        Idioma: %s
                        Autor(s): %s
                        Ano de Nascimento: %d
                        Ano de Falecimento: %d

                        ######################################################################
                        """.formatted(dadosLivro.get(0).codigo(), dadosLivro.get(0).download(),
                dadosLivro.get(0).titulo(), dadosIdiomas.get(0), dadosAutores.get(0).nome(),
                dadosAutores.get(0).anoNascimento(),
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

    private void buscarTodosLivrosWeb() {
        dadosLivro = getDadosLivros().dadosLivros();
        var indice = 1;
        for (var i = 0 ; i < dadosLivro.size(); i++) {
            var formatacaoGeral = """
                    ######################################################################
                            RESULTADO DA BUSCA DE LIVROS DISPONÍVEIS NA API ( %d )
                    
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
        System.out.println("Informe o nome do autor para buscar na base de dados: ");
        var nomeAutor = leitura.nextLine();
        livros = repositorio.findAll();
        listaDeidiomas = repositorioIdiomas.findAll();

        autoresBusca = repositorioAutores.findFirstByNomeContainingIgnoreCase(nomeAutor);
        if (autoresBusca.isPresent()) {

            List<Autores> relatorioAutores = autoresBusca.stream()
                    .filter(d -> d.getNome().equals(autoresBusca.get().getNome()))
                    .toList();

            for (int i = 0; i < relatorioAutores.size(); i++) {

                var relatorioDosAutores = """
                            \n######################################################################
                                      RESULTADO DA BUSCA POR AUTORES NA BASE DE DADOS ( %d )

                            Nome: %s
                            Nascido em: %d
                            Faleceu em: %d
                            Livro: %s
                            Idioma: %s

                            ######################################################################
                            """.formatted(relatorioAutores.size(), relatorioAutores.get(i).getNome(),
                        relatorioAutores.get(i).getAnoNascimento(),
                        relatorioAutores.get(i).getAnoFalecimento(), livros.get(i).getTitulo(),
                        listaDeidiomas.get(i).getIdioma());

                System.out.println(relatorioDosAutores);
            }
        } else {
            System.out.println("\nAutores não encontrados !");
        }
    }


    private void buscarPelosAutoresVivos() {
        var anoAtual = Year.now().getValue();
        anoAutoresVivos = anoAtual + 1;
        String relatorioDosAutores;

        while (anoAutoresVivos > anoAtual) {
            System.out.println("Informe o ano para saber se os autores estão vivo na base de dados: ");

            try {
                anoAutoresVivos = leitura.nextInt();
            } catch (InputMismatchException ex) {
                anoAutoresVivos = anoAtual + 1;
                System.out.println("Favor informar somente um ano com 4 digitos Ex.: " + anoAtual);
            }
            leitura.nextLine();
        }

        livros = repositorio.findAll();
//        autores = repositorioAutores.findAll();
        listaDeidiomas = repositorioIdiomas.findAll();

        List<Autores> fichaAutoresVivos = repositorioAutores.buscarAutores("");

        List<Autores> relatorioAutoresVivos = fichaAutoresVivos.stream()
                .filter(d -> d.getAnoNascimento() <= anoAutoresVivos && d.getAnoFalecimento() > anoAutoresVivos)
                .sorted(Comparator.comparingInt(Autores::getAnoFalecimento))
                .toList();

        if (relatorioAutoresVivos.isEmpty()) {
            System.out.println("\nNa base de dados não tem autores vivos !");
        }

        var design = """
                    \n######################################################################
                                  RESULTADO DA BUSCA DE AUTORES VIVOS ( %d )
                    _____________________________________________________________________
                    """.formatted(relatorioAutoresVivos.size());
        System.out.println(design);

        for (int i = 0; i < relatorioAutoresVivos.size(); i++) {
            relatorioDosAutores = """                      
                        Nome: %s
                        Nascido em: %d
                        Faleceu em: %d
                        Livro: %s
                        Idioma: %s
                        _____________________________________________________________________
                        """.formatted(relatorioAutoresVivos.get(i).getNome(), relatorioAutoresVivos.get(i).getAnoNascimento(),
                    relatorioAutoresVivos.get(i).getAnoFalecimento(), livros.get(i).getTitulo(),
                    listaDeidiomas.get(i).getIdioma());
            System.out.println(relatorioDosAutores);
        }
        var designFinal = """
                    ######################################################################
                    """;
        System.out.println(designFinal);
    }

    private void buscarLivrosPeloIdioma() {
        listaDeidiomas = repositorioIdiomas.findAll();
        System.out.println("Estatisicas dos livros na base de dados: ");
//
        var tituloDoLivro = "";
        List<Livro> filtroLivro = repositorio.buscarLivros(tituloDoLivro);
        filtroLivro.forEach(l -> System.out.println("Livro: " + l.getTitulo()));

        var nomeDosAutores = "";
        List<Autores> filtroAutores = repositorioAutores.buscarAutores(nomeDosAutores);
        filtroAutores.forEach(a -> System.out.println("Autores: " + a.getNome()));

        var tipoIdioma = "pt";
        List<Idioma> filtroIdioma = repositorioIdiomas.buscarIdiomas(tipoIdioma);
        filtroIdioma.forEach(i -> System.out.println("Idioma: " + i.getIdioma()));

        List<Livro> relatorioLivros = filtroLivro.stream()
                .filter(d -> d.getTitulo() != null)
                .toList();

        List<Idioma> relatorioIdiomasPt = filtroIdioma.stream()
                .filter(d -> d.getIdioma().equals("pt"))
                .toList();

        List<Idioma> relatorioIdiomasEn = filtroIdioma.stream()
                .filter(d -> d.getIdioma().equals("en"))
                .toList();
//
        var estatisticaLivros = """
                        \n######################################################################
                                  ESTATISTICAS DE LIVROS NOS IDIOMAS NO BANCO DE DADOS

                                      LIVROS       |    PORTUGUES   |     INGLES
                        _______________________________________________________________________
                                        %d                  %d               %d
                        ########################################################################
                        """.formatted(relatorioLivros.size(), relatorioIdiomasPt.size(), relatorioIdiomasEn.size());

        System.out.println(estatisticaLivros);
    }

}