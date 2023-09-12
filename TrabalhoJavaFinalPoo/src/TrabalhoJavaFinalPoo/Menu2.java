package TrabalhoJavaFinalPoo;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Menu2 {
    private Scanner scanner = new Scanner(System.in);
    private SistemaGerenciamentoEstudantes2 gerenciamento;
    private BancoDeDados2 bancoDeDados;

    public Menu2(SistemaGerenciamentoEstudantes2 gerenciamento, BancoDeDados2 bancoDeDados) {
        this.gerenciamento = gerenciamento;
        this.bancoDeDados = bancoDeDados;
    }

    public void exibirMenu() {
        int opcao;

        do {
            System.out.println("Menu de opções:");
            System.out.println("1 - Cadastrar Curso");
            System.out.println("2 - Adicionar Estudante");
            System.out.println("3 - Editar Estudante");
            System.out.println("4 - Remover Estudante");
            System.out.println("5 - Listar Estudantes");
            System.out.println("6 - Listar Cursos");
            System.out.println("7 - Encerrar/Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {

                case 1:
                    cadastrarCursos();
                    break;
                case 2:
                    adicionarEstudante();
                    break;
                case 3:
                    editarEstudante();
                    break;
                case 4:
                    removerEstudante();
                    break;
                case 5:
                    listarEstudantes();
                    break;
                case 6:
                    listarCursos();
                    break;
                case 7:
                    System.out.println("Programa encerrado! Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 7);

        scanner.close();
    }
    
    
    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                int valor = Integer.parseInt(scanner.nextLine());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite um número inteiro válido, referente ao ID.");
            }
        }
    }

    private void adicionarEstudante() {
        System.out.print("Nome do estudante: ");
        String nome = scanner.nextLine();

        List<String> cursosDisponíveis;
        try {
            cursosDisponíveis = bancoDeDados.listarCursos();

            if (cursosDisponíveis.isEmpty()) {
                System.out.println("Nenhum curso cadastrado. Não é possível adicionar o estudante.");
                return;
            }

            System.out.println("Cursos disponíveis:");
            for (int i = 0; i < cursosDisponíveis.size(); i++) {
                System.out.println((i + 1) + " - " + cursosDisponíveis.get(i));
            }

            int escolhaCurso = lerInteiro("Escolha o número do curso: ");

            if (escolhaCurso >= 1 && escolhaCurso <= cursosDisponíveis.size()) {
                String nomeCursoEscolhido = cursosDisponíveis.get(escolhaCurso - 1);
                int idCursoEscolhido = bancoDeDados.getIdCursoPeloNome(nomeCursoEscolhido);
                Estudante2 estudante = new Estudante2(nome, nomeCursoEscolhido, idCursoEscolhido);
                gerenciamento.adicionarEstudante(estudante);
            } else {
                System.out.println("Escolha de curso inválida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar estudante: " + e.getMessage());
        }
    }

    private void editarEstudante() {
        List<Estudante2> estudantes;
        try {
            estudantes = gerenciamento.listarEstudantes();
        } catch (SQLException e) {
            System.out.println("Erro ao listar estudantes: " + e.getMessage());
            return;
        }

        if (estudantes.isEmpty()) {
            System.out.println("Nenhum estudante cadastrado para editar.");
            return;
        }

        System.out.println("Lista de Estudantes:");
        for (Estudante2 estudante : estudantes) {
            System.out.println("ID: " + estudante.getIdAluno() + " - Nome: " + estudante.getNomeAluno());
        }

        int id = lerInteiro("Digite o ID do estudante a ser editado: ");

        Estudante2 estudanteExistente = buscarEstudante(id, estudantes);
        if (estudanteExistente == null) {
            System.out.println("Estudante não encontrado.");
            return;
        }

        System.out.print("Novo nome do estudante: ");
        String novoNome = scanner.nextLine();

        List<String> cursosDisponíveis;
        try {
            cursosDisponíveis = bancoDeDados.listarCursos();
            if (cursosDisponíveis.isEmpty()) {
                System.out.println("Nenhum curso cadastrado. Não é possível editar o curso do estudante.");
            } else {
                System.out.println("Cursos disponíveis:");
                for (int i = 0; i < cursosDisponíveis.size(); i++) {
                    System.out.println((i + 1) + " - " + cursosDisponíveis.get(i));
                }

                int novoIdCurso = -1;
                while (novoIdCurso == -1) {
                    System.out.print("Escolha o número do novo curso (ou pressione Enter para manter o curso atual): ");
                    String escolhaCursoStr = scanner.nextLine();
                    if (escolhaCursoStr.isEmpty()) {
                        novoIdCurso = estudanteExistente.getIdCurso();
                    } else {
                        try {
                            int escolhaCurso = Integer.parseInt(escolhaCursoStr);
                            if (escolhaCurso >= 1 && escolhaCurso <= cursosDisponíveis.size()) {
                                String nomeCursoEscolhido = cursosDisponíveis.get(escolhaCurso - 1);
                                novoIdCurso = bancoDeDados.getIdCursoPeloNome(nomeCursoEscolhido);
                            } else {
                                System.out.println("Escolha de curso inválida. O curso atual será mantido.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida. Digite um número inteiro válido.");
                        }
                    }
                }

                try {
                    gerenciamento.editarEstudante(id, novoNome, novoIdCurso);
                    System.out.println("Estudante atualizado com sucesso!");
                } catch (SQLException e) {
                    System.out.println("Erro ao editar estudante: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar cursos: " + e.getMessage());
        }
    }
    private void removerEstudante() {
        List<Estudante2> estudantes;
        try {
            estudantes = gerenciamento.listarEstudantes();
        } catch (SQLException e) {
            System.out.println("Erro ao listar estudantes: " + e.getMessage());
            return;
        }

        if (estudantes.isEmpty()) {
            System.out.println("Nenhum estudante cadastrado para remover.");
            return;
        }

        System.out.println("Lista de Estudantes:");
        for (Estudante2 estudante : estudantes) {
            System.out.println("ID: " + estudante.getIdAluno() + " - Nome: " + estudante.getNomeAluno());
        }

        int id = lerInteiro("Digite o ID do estudante a ser removido: ");

        Estudante2 estudanteExistente = buscarEstudante(id, estudantes);
        if (estudanteExistente == null) {
            System.out.println("Estudante não encontrado.");
            return;
        }

        try {
            gerenciamento.removerEstudante(id);
            System.out.println("Estudante removido com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao remover estudante: " + e.getMessage());
        }
    }

    private void listarEstudantes() {
        List<Estudante2> estudantes;
        try {
            estudantes = gerenciamento.listarEstudantes();
        } catch (SQLException e) {
            System.out.println("Erro ao listar estudantes: " + e.getMessage());
            return;
        }

        if (estudantes.isEmpty()) {
            System.out.println("Nenhum estudante cadastrado.");
        } else {
            System.out.println("Lista de Estudantes:");
            for (Estudante2 estudante : estudantes) {
                System.out.println(estudante);
            }
        }
    }

    private void listarCursos() {
        List<String> cursosDisponíveis;
        try {
            cursosDisponíveis = bancoDeDados.listarCursos();

            if (cursosDisponíveis.isEmpty()) {
                System.out.println("Nenhum curso cadastrado.");
            } else {
                System.out.println("Lista de Cursos:");
                for (String curso : cursosDisponíveis) {
                    System.out.println(curso);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar cursos: " + e.getMessage());
        }
    }

    private void cadastrarCursos() {
        System.out.print("Nome do curso: ");
        String nomeCurso = scanner.nextLine();

        try {
            bancoDeDados.inserirCurso(nomeCurso);
            
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar curso: " + e.getMessage());
        }
    }

    private Estudante2 buscarEstudante(int id, List<Estudante2> estudantes) {
        for (Estudante2 estudante : estudantes) {
            if (estudante.getIdAluno() == id) {
                return estudante;
            }
        }
        return null;
    }
}