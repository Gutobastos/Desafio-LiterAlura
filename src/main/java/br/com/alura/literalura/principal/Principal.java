package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.service.ConsumoAPI;
import br.com.alura.literalura.service.ConverteDados;

import java.time.Year;
import java.util.*;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private final String enderecoAPI = "https://gutendex.com/books/";
    private ConverteDados conversor = new ConverteDados();
    private LivroRepository repositorio;
    private Livro livro = new Livro();
    private List<Autores> autores = new ArrayList<>();
    private List<Idioma> listaDeidiomas = new ArrayList<>();
    private List<DadosLivro> dadosLivro = new ArrayList<>();
    private List<DadosAutores> dadosAutores = new ArrayList<>();
    private List<String> dadosIdiomas = new ArrayList<>();
    private int opcao = -1;
    private int anoAutoresVivos;

    public Principal(LivroRepository repositorio) {
        this.repositorio = repositorio;
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
            opcao = -1;
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

    private List getFichaTecnicaLivro() {
        System.out.println("\nInforme o nome dos Autores para a busca ? ");
        var nomeAutores = leitura.nextLine();
        List<Autores> livrosEncontrados = repositorio.buscarDadosLivrosDosAutores(nomeAutores);
        var fichaTecnica = new ArrayList<>();
        for (int i = 0; i < livrosEncontrados.size(); i++){
            fichaTecnica.add(livrosEncontrados.get(i).getNome());
            fichaTecnica.add(livrosEncontrados.get(i).getAnoNascimento());
            fichaTecnica.add(livrosEncontrados.get(i).getAnoFalecimento());
            fichaTecnica.add(livrosEncontrados.get(i).getLivro().getTitulo());
            fichaTecnica.add(livrosEncontrados.get(i).getLivro().getIdiomas().get(0).getIdioma());
        }
        return fichaTecnica;
    }

    private Dados getDadosPorLivro() {
        System.out.println("Informe o título do livro que deseja buscar: ");
        var tituloDoLivro = leitura.nextLine();
        var json = consumo.obterDadosAPI(enderecoAPI + "?search=" +
                tituloDoLivro.toLowerCase().replace(" ", "%20"));
        return conversor.obterDados(json, Dados.class);
    }

    private Dados getDadosDoLivro() {
        Livro codigoDoLivro = new Livro(dadosLivro.get(0));
        var json = consumo.obterDadosAPI(enderecoAPI + codigoDoLivro.getCodigo() + "/");
        return conversor.obterDados(json, Dados.class);
    }

    private Dados getDadosLivros() {
        System.out.println("Buscando pela lista de livros disponíveis na API...");
        var json = consumo.obterDadosAPI(enderecoAPI);
        return conversor.obterDados(json, Dados.class);
    }

    private void salvaDados() {
        System.out.println("\nDeseja salvar esse livro na lista? (S/N)");
        var salvar = leitura.nextLine();
        if (salvar.equalsIgnoreCase("s")) {
            livro = new Livro(dadosLivro.get(0));
            autores = dadosAutores.stream()
                    .flatMap(da -> dadosAutores.stream()
                            .map(Autores::new))
                    .toList();
            listaDeidiomas = dadosIdiomas.stream()
                    .flatMap(di -> dadosIdiomas.stream()
                            .map(Idioma::new))
                    .toList();
            livro.setAutores(autores);
            livro.setIdiomas(listaDeidiomas);
            repositorio.save(livro);
            System.out.println("\nLivro salvo na lista com sucesso !!!");
        } else {
            System.out.println("O livro não foi salvo na lista !");
        }
    }

    private void buscarLivroWeb() {
        dadosLivro = getDadosPorLivro().dadosLivros();
        System.out.println("Buscando dados do livro... Aguarde... ");
        if (!dadosLivro.isEmpty()) {
            dadosAutores = getDadosDoLivro().dadosAutores();
            dadosIdiomas = getDadosDoLivro().idioma();
            var formatacao = """
                        \n######################################################################
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
        } else {
            System.out.println("Livro não encontrado na API !");
        }
    }

    private void buscarTodosLivrosWeb() {
        dadosLivro = getDadosLivros().dadosLivros();
        var indice = 1;
        System.out.printf("Foram encontrado(s) %d Livros... Listando...", dadosLivro.size());
        for (var i = 0 ; i < dadosLivro.size(); i++) {
            var formatacaoGeral = """
                    \n######################################################################
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
        var fichaDoAutorBuscado = getFichaTecnicaLivro();
        if (!fichaDoAutorBuscado.isEmpty()) {

            var designAutores = """
                    \n######################################################################
                              RESULTADO DA BUSCA POR AUTORES NA BASE DE DADOS
                    
                    Nome: %s
                    Ano de Nascimento: %d
                    Ano de Falecimento: %d
                    Livro: %s
                    Idioma: %s
                    
                    ######################################################################
                    """.formatted(fichaDoAutorBuscado.get(0), fichaDoAutorBuscado.get(1),
                    fichaDoAutorBuscado.get(2), fichaDoAutorBuscado.get(3), fichaDoAutorBuscado.get(4));
            System.out.println(designAutores);
        } else {
            System.out.println("Autores não encontrados da base de dados !");
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
        var fichaAutoresVivos = repositorio.buscarDadosLivrosDosAutores("");

        var relatorioAutoresVivos = fichaAutoresVivos.stream()
                .filter(d -> d.getAnoNascimento() <= anoAutoresVivos && d.getAnoFalecimento() > anoAutoresVivos)
                .sorted(Comparator.comparingInt(Autores::getAnoFalecimento))
                .toList();

        if (relatorioAutoresVivos.isEmpty()) {
            System.out.println("\nNa base de dados não tem autores vivos !");
        } else {
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
                        relatorioAutoresVivos.get(i).getAnoFalecimento(), relatorioAutoresVivos.get(i).getLivro().getTitulo(),
                        relatorioAutoresVivos.get(i).getLivro().getIdiomas().get(0).getIdioma());
                System.out.println(relatorioDosAutores);
            }
        }
        var designFinal = """
                                                FIM DA LISTA
                    ######################################################################
                    """;
        System.out.println(designFinal);
    }

    private void buscarLivrosPeloIdioma() {
        System.out.println("Estatisicas dos livros na base de dados: ");
        var tituloDoLivro = "";
        var filtroLivro = repositorio.buscarLivros(tituloDoLivro);
        var relatorioLivros = filtroLivro.stream()
                .filter(d -> d.getTitulo() != null)
                .toList();
        var relatorioIdiomasPt = filtroLivro.stream()
                .filter(d -> d.getIdiomas().get(0).getIdioma().equals("pt"))
                .toList();
        var relatorioIdiomasEn = filtroLivro.stream()
                .filter(d -> d.getIdiomas().get(0).getIdioma().equals("en"))
                .toList();
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