package biblioteca.controller;

import biblioteca.model.Biblioteca;
import biblioteca.model.Livro;
import java.util.List;

public class BibliotecaController {
    private Biblioteca biblioteca;
    
    public BibliotecaController() {
        this.biblioteca = new Biblioteca();
    }
    
    public void adicionarLivro(String titulo, String autor, String genero, String editora, int ano) {
        Livro livro = new Livro(0, titulo, autor, genero, editora, ano);
        biblioteca.adicionarLivro(livro);
    }
    
    public List<Livro> buscarLivros(String palavraChave) {
        return biblioteca.buscarLivros(palavraChave);
    }
    
    public boolean emprestarLivro(int idLivro) {
        return biblioteca.emprestarLivro(idLivro);
    }
    
    public boolean devolverLivro(int idLivro) {
        return biblioteca.devolverLivro(idLivro);
    }
    
    public List<Livro> getLivrosAtrasados() {
        return biblioteca.getLivrosAtrasados();
    }
    
    public List<Livro> getTodosLivros() {
        return biblioteca.getTodosLivros();
    }
} 