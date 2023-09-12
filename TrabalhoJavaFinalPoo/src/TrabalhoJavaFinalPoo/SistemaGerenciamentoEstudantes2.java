package TrabalhoJavaFinalPoo;

import java.sql.SQLException;
import java.util.List;

public class SistemaGerenciamentoEstudantes2 {
    
    
    private BancoDeDados2 bancoDeDados;

    public SistemaGerenciamentoEstudantes2() {
        try {
            bancoDeDados = new BancoDeDados2();
            bancoDeDados.criarTabelas();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados ou criar tabelas: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) throws SQLException {
        SistemaGerenciamentoEstudantes2 gerenciamento = new SistemaGerenciamentoEstudantes2();

        Menu2 menu = new Menu2(gerenciamento, gerenciamento.bancoDeDados);
        menu.exibirMenu();
    }
    
    public List<String> listarCursos() throws SQLException {
        return bancoDeDados.listarCursos();
    }

    public List<Estudante2> listarEstudantes() throws SQLException {
        return bancoDeDados.listarEstudantes();
    }

    public void adicionarEstudante(Estudante2 estudante) throws SQLException {
        bancoDeDados.inserirEstudante(estudante);
    }

    public void removerEstudante(int id) throws SQLException {
        bancoDeDados.removerEstudante(id);
    }

    public void editarEstudante(int id, String novoNome, int novoIdCurso) throws SQLException {
        bancoDeDados.atualizarNomeEstudante(id, novoNome, novoIdCurso);
    }

    
}
