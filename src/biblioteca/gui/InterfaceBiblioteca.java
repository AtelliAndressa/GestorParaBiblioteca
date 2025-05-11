package biblioteca.gui;

import biblioteca.controller.BibliotecaController;
import biblioteca.model.Livro;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InterfaceBiblioteca extends JFrame {
    private BibliotecaController controller;
    private JTextField campoBusca;
    private JTextArea areaResultados;
    private JTextField campoTitulo, campoAutor, campoGenero, campoEditora, campoAno;
    private JButton botaoAdicionar, botaoBuscar, botaoEmprestar, botaoDevolver, botaoListarAtrasados;

    public InterfaceBiblioteca() {
        controller = new BibliotecaController();
        inicializarInterface();
    }

    private void inicializarInterface() {
        setTitle("Sistema de Gestão de Biblioteca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel de entrada de dados
        JPanel painelEntrada = new JPanel(new GridLayout(6, 2, 5, 5));
        painelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelEntrada.add(new JLabel("Título:"));
        campoTitulo = new JTextField();
        painelEntrada.add(campoTitulo);

        painelEntrada.add(new JLabel("Autor:"));
        campoAutor = new JTextField();
        painelEntrada.add(campoAutor);

        painelEntrada.add(new JLabel("Gênero:"));
        campoGenero = new JTextField();
        painelEntrada.add(campoGenero);

        painelEntrada.add(new JLabel("Editora:"));
        campoEditora = new JTextField();
        painelEntrada.add(campoEditora);

        painelEntrada.add(new JLabel("Ano:"));
        campoAno = new JTextField();
        painelEntrada.add(campoAno);

        botaoAdicionar = new JButton("Adicionar Livro");
        painelEntrada.add(botaoAdicionar);

        // Painel de busca
        JPanel painelBusca = new JPanel(new BorderLayout());
        campoBusca = new JTextField();
        botaoBuscar = new JButton("Buscar");
        painelBusca.add(campoBusca, BorderLayout.CENTER);
        painelBusca.add(botaoBuscar, BorderLayout.EAST);

        // Área de resultados
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        JScrollPane painelRolagem = new JScrollPane(areaResultados);

        // Painel de botões
        JPanel painelBotoes = new JPanel();
        botaoEmprestar = new JButton("Emprestar");
        botaoDevolver = new JButton("Devolver");
        botaoListarAtrasados = new JButton("Listar Atrasados");
        painelBotoes.add(botaoEmprestar);
        painelBotoes.add(botaoDevolver);
        painelBotoes.add(botaoListarAtrasados);

        // Adicionando componentes ao frame
        add(painelEntrada, BorderLayout.NORTH);
        add(painelBusca, BorderLayout.CENTER);
        add(painelRolagem, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Configurando eventos
        configurarEventos();
    }

    private void configurarEventos() {
        botaoAdicionar.addActionListener(e -> adicionarLivro());
        botaoBuscar.addActionListener(e -> buscarLivros());
        botaoEmprestar.addActionListener(e -> emprestarLivro());
        botaoDevolver.addActionListener(e -> devolverLivro());
        botaoListarAtrasados.addActionListener(e -> listarLivrosAtrasados());
    }

    private void adicionarLivro() {
        try {
            String titulo = campoTitulo.getText();
            String autor = campoAutor.getText();
            String genero = campoGenero.getText();
            String editora = campoEditora.getText();
            int ano = Integer.parseInt(campoAno.getText());

            controller.adicionarLivro(titulo, autor, genero, editora, ano);
            limparCampos();
            mostrarMensagem("Livro adicionado com sucesso!");
        } catch (NumberFormatException ex) {
            mostrarMensagem("Erro: Ano inválido!");
        }
    }

    private void buscarLivros() {
        String palavraChave = campoBusca.getText();
        if (!palavraChave.isEmpty()) {
            List<Livro> resultados = controller.buscarLivros(palavraChave);
            exibirResultados(resultados);
        }
    }

    private void emprestarLivro() {
        String entrada = JOptionPane.showInputDialog(this, "Digite o ID do livro para empréstimo:");
        if (entrada != null && !entrada.isEmpty()) {
            try {
                int idLivro = Integer.parseInt(entrada);
                if (controller.emprestarLivro(idLivro)) {
                    mostrarMensagem("Livro emprestado com sucesso!");
                } else {
                    mostrarMensagem("Não foi possível emprestar o livro!");
                }
            } catch (NumberFormatException ex) {
                mostrarMensagem("ID inválido!");
            }
        }
    }

    private void devolverLivro() {
        String entrada = JOptionPane.showInputDialog(this, "Digite o ID do livro para devolução:");
        if (entrada != null && !entrada.isEmpty()) {
            try {
                int idLivro = Integer.parseInt(entrada);
                if (controller.devolverLivro(idLivro)) {
                    mostrarMensagem("Livro devolvido com sucesso!");
                } else {
                    mostrarMensagem("Não foi possível devolver o livro!");
                }
            } catch (NumberFormatException ex) {
                mostrarMensagem("ID inválido!");
            }
        }
    }

    private void listarLivrosAtrasados() {
        List<Livro> livrosAtrasados = controller.getLivrosAtrasados();
        exibirResultados(livrosAtrasados);
    }

    private void exibirResultados(List<Livro> livros) {
        StringBuilder sb = new StringBuilder();
        for (Livro livro : livros) {
            sb.append(livro.toString()).append("\n");
        }
        areaResultados.setText(sb.toString());
    }

    private void limparCampos() {
        campoTitulo.setText("");
        campoAutor.setText("");
        campoGenero.setText("");
        campoEditora.setText("");
        campoAno.setText("");
    }

    private void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceBiblioteca gui = new InterfaceBiblioteca();
            gui.setVisible(true);
        });
    }
} 