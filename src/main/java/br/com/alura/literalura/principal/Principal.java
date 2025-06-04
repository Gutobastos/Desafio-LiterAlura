package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.service.ConsumoAPI;
import br.com.alura.literalura.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private final String enderecoAPI = "https://gutendex.com/books";
    private ConverteDados conversor = new ConverteDados();

    private List<Livro> livros = new ArrayList<>();
    private List<DadosLivro> dadosLivros = new ArrayList<>();
    private int opcao = 9;

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
                    buscarTodosLivroWeb();
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
        }
    }

    private void buscarLivroWeb() {
        DadosLivro dados = getDadosLivro();
        dadosLivros.add(dados);
        System.out.println(dadosLivros + "\n");
    }

    private DadosLivro getDadosLivro() {
        System.out.println("Digite o nome do título do livro: ");
        var nomeLivro = "casmurro";


        var json = consumo.obterDadosAPI(enderecoAPI + "/?search=" + nomeLivro.replace(" ", "+") + "&?languages=pt");
        System.out.println(json);
        DadosLivro dados = conversor.obterDados(json, DadosLivro.class);
        return dados;
    }

    private void buscarTodosLivroWeb() {
        System.out.println("\nBuscando todos os livros...");
        var json = consumo.obterDadosAPI(enderecoAPI + "/");
        System.out.println(json);
    }

    private void buscarPelosAutores() {
    }

    private void buscarPelosAutoresVivos() {
    }

    private void buscarLivrosPeloIdioma() {
    }

}
