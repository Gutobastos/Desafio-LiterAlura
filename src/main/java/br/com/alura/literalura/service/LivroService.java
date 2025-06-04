package br.com.alura.literalura.service;

import br.com.alura.literalura.dto.LivroDTO;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivroService {
//
//    @Autowired
//    private LivroRepository repositorio;

//    private List<LivroDTO> converteDados(List<Livro> series){
//        return series.stream()
//                .map(l -> new LivroDTO(l.getId(), l.getTitulo(), l.getTotalTemporadas(), l.getAvaliacao(),
//                        l.getGenero(), l.getAtores(), l.getPoster(), l.getSinopse()))
//                .collect(Collectors.toList());
//    }

//    public List<LivroDTO> obterTodasAsSeries(){
//        return converteDados(repositorio.findAll());
//    }

//    public List<LivroDTO> obterTop5Series() {
//        return converteDados(repositorio.findTop5SeriesByOrderByAvaliacaoDesc());
//    }
//
//    public List<LivroDTO> obterLancamentos() {
//        return converteDados(repositorio.lancamentosMaisRecentes());
//    }
//
//    public LivroDTO obterPorId(Long id) {
//        Optional<Livro> serie = repositorio.findById(id);
//
//        if (serie.isPresent()) {
//            Livro l = livro.get();
//            return new LivroDTO(l.getId(), l.getTitulo(), l.getTotalTemporadas(), l.getAvaliacao(),
//                    l.getGenero(), l.getAtores(), l.getPoster(), l.getSinopse());
//        }
//        return null;
//    }
//
//    public List<LivroDTO> obterTodasTemporadas(Long id) {
//        Optional<Livro> livro = repositorio.findById(id);
//
//        if (livro.isPresent()) {
//            livro l = livro.get();
//            return l.getEpisodios().stream()
//                    .map(e -> new LivroDTO(l.getTemporada(), l.getNumeroEpisodio(), l.getTitulo()))
//                    .collect(Collectors.toList());
//        }
//        return null;
//    }
//
//    public List<LivroDTO> obterTemporadasPorNumero(Long id, Long numero) {
//        return repositorio.obterEpisodiosPorTemporada(id, numero)
//                .stream()
//                .map(l -> new EpisodioDTO(l.getTemporada(), l.getNumeroEpisodio(), l.getTitulo()))
//                .collect(Collectors.toList());
//
//    }

}
