package biblioteca.model;

import biblioteca.database.LivroDAO;
import java.time.LocalDate;
import java.util.List;

public class Biblioteca {
    private LivroDAO livroDAO;
    private static final int DIAS_EMPRESTIMO = 14;

    public Biblioteca() {
        this.livroDAO = new LivroDAO();
        this.livroDAO.criarTabela();
    }

    public void adicionarLivro(Livro livro) {
        livroDAO.adicionarLivro(livro);
    }

    public List<Livro> buscarLivros(String palavraChave) {
        return livroDAO.buscarLivros(palavraChave);
    }

    public boolean emprestarLivro(int idLivro) {
        return livroDAO.emprestarLivro(idLivro);
    }

    public boolean devolverLivro(int idLivro) {
        return livroDAO.devolverLivro(idLivro);
    }

    public List<Livro> getLivrosAtrasados() {
        return livroDAO.getLivrosAtrasados();
    }

    public List<Livro> getTodosLivros() {
        return livroDAO.getTodosLivros();
    }
} 