package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {

    static final int QUANTIDADE_PERIODO = 5;
    static final int QUANTIDADE_DIAS_LETIVOS = 5;
    static final int QUANTIDADE_HORARIO_DIAS_LETIVOS = 4;
    static final int QUANTIDADE_HORARIO_POR_PERIODO = QUANTIDADE_DIAS_LETIVOS * QUANTIDADE_HORARIO_DIAS_LETIVOS;
    static final int TOTAL_HORARIOS = QUANTIDADE_HORARIO_POR_PERIODO * QUANTIDADE_PERIODO;
    static final int TOTAL_GERACOES = 1000;
    public static final double PROBABILIDADE_CRUZAMENTO = 0.99;
    public static final double PROBABILIDADE_MUTACAO = 0.50;

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final List<List<Horario>> individuosPrimordiais = new ArrayList<>();
        final List<List<Horario>> todosOsNovosIndividuosGerados = new ArrayList<>();

        System.out.print("QUAL O TAMANHO DA POPULAÇÃO?: ");

        criaOsIndividuosPrimordiaisEEscreveCsv(individuosPrimordiais, scanner.nextInt());

        List<List<Horario>> novaPopulacao = individuosPrimordiais;

        for (int i = 0; i < TOTAL_GERACOES; i++) {
            novaPopulacao = geraNovaPopulacaoDeHorarios(novaPopulacao);
            todosOsNovosIndividuosGerados.addAll(novaPopulacao);

            System.out.println();
            System.out.println("###########");
            System.out.println("GERAÇÃO: " + (i + 1));
            System.out.println("###########");
            exibeOsConflitos(verificaConflitoDeHorarios(novaPopulacao));
        }

        System.out.println("#############################################");
        System.out.println("MENOR QUANTIDADE DE CONFLITOS ENCONTRADA: " +
            encontraMenorQuantidadeDeConflitosDeTodosIndividuos(todosOsNovosIndividuosGerados));
        System.out.println("#############################################");

        scanner.close();
    }

    private static int encontraMenorQuantidadeDeConflitosDeTodosIndividuos(final List<List<Horario>> todosOsNovosIndividuosGerados) {
        return retornaHorariosOrdenadosPorQuantidadeDeConflitos(todosOsNovosIndividuosGerados).stream()
            .mapToInt(Main::verificarConflitoDeHorarios)
            .min()
            .orElse(0);
    }

    private static void criaOsIndividuosPrimordiaisEEscreveCsv(
        final List<List<Horario>> todosOsHorarios,
        final int tamanhoDaPopulacao) {
        try {
            final FileWriter writer = new FileWriter("horarios.csv");
            final List<Integer> quantidadeDeConflitos = new ArrayList<>();

            for (int i = 0; i < tamanhoDaPopulacao; i++) {
                final List<Materia> materias = new ArrayList<>(); //25
                criarMaterias(materias);

                final List<Periodo> periodos = new ArrayList<>(); // 5
                criarPeriodos(periodos);

                final List<Professor> professores = new ArrayList<>(); //10
                criarProfessores(professores);

                final List<Horario> horarios = new ArrayList<>();
                geraOsHorariosPrimordiais(materias, professores, horarios); //100

                final List<Horario> horariosEmbaralhadosPorPeriodo = embaralhaPorPeriodo(horarios);

                quantidadeDeConflitos.add(verificarConflitoDeHorarios(horariosEmbaralhadosPorPeriodo));
                todosOsHorarios.add(horariosEmbaralhadosPorPeriodo);
                escreveALinhaDoCsv(horariosEmbaralhadosPorPeriodo, writer);
            }

            exibeOsConflitos(quantidadeDeConflitos);

            writer.flush();
            writer.close();
        } catch (final IOException e) {
            System.out.println("Erro ao gerar CSV: " + e.getMessage());
        }
    }

    private static int verificarConflitoDeHorarios(final List<Horario> horarios) {
        final AtomicInteger conflitos = new AtomicInteger();

        // Percorrer a lista de horários
        for (final Horario horario : horarios) {
            horarios.stream()
                .filter(value -> !value.equals(horario))
                .forEach(value -> {
                    if (horario.getDia() == value.getDia()
                        && horario.getPosicao() == value.getPosicao()
                        && horario.getMateria().getCodigoPeriodo() != value.getMateria().getCodigoPeriodo()
                        && horario.getProfessor().getCodigo() == value.getProfessor().getCodigo()) {
                        conflitos.getAndIncrement();
                    }
                });
        }

        return conflitos.get();
    }

    private static List<Integer> verificaConflitoDeHorarios(final List<List<Horario>> novaPopulacao) {
        final List<Integer> conflitos = new ArrayList<>();
        for (int i = 0; i < novaPopulacao.size(); i++) {
            conflitos.add(verificarConflitoDeHorarios(novaPopulacao.get(i)));
        }

        return conflitos;
    }

    private static void exibeOsConflitos(final List<Integer> conflitos) {
        for (int i = 0; i < conflitos.size(); i++) {
            System.out.println("Quantidade de conflitos: " + conflitos.get(i));
            System.out.println();
        }
    }

    private static List<List<Horario>> geraNovaPopulacaoDeHorarios(final List<List<Horario>> horarios) {
        final List<List<Horario>> horariosOrdenados = retornaHorariosOrdenadosPorQuantidadeDeConflitos(horarios);
        final List<List<Horario>> novosHorarios = new ArrayList<>();
        final Random random = new Random();

        for (int i = 0; i < horariosOrdenados.size(); i++) {
            final int metade = horariosOrdenados.size() / 2;

            int indiceAleatorioPrimeiraMetade = random.nextInt(metade);
            int indiceAleatorioSegundaMetade = random.nextInt(((horariosOrdenados.size()) - metade)) + metade;

            final List<Horario> paiAleatorioDaPrimeiraMetade = horariosOrdenados.get(indiceAleatorioPrimeiraMetade);
            final List<Horario> paiAleatorioDaSegundaMetade = horariosOrdenados.get(indiceAleatorioSegundaMetade);

            novosHorarios.add(cruzamentoDeHorarios(paiAleatorioDaPrimeiraMetade, paiAleatorioDaSegundaMetade));
        }

        return novosHorarios;
    }

    private static List<Horario> cruzamentoDeHorarios(final List<Horario> individuoUm, final List<Horario> individuoDois) {
        final List<List<Horario>> filhos = new ArrayList<>();
        final Random random = new Random();
        List<Horario> filhoUm = new ArrayList<>();
        List<Horario> filhoDois = new ArrayList<>();

        if (verificarProbabilidade(PROBABILIDADE_CRUZAMENTO)) {
            int tamanhoMetade = (int) Math.floor((double) QUANTIDADE_PERIODO / 2);
            int primeiraMetade = tamanhoMetade * QUANTIDADE_HORARIO_POR_PERIODO;

            filhoUm.addAll(individuoUm.subList(0, primeiraMetade));
            filhoUm.addAll(individuoDois.subList(primeiraMetade, individuoDois.size()));

            filhoDois.addAll(individuoUm.subList(0, primeiraMetade));
            filhoDois.addAll(individuoUm.subList(primeiraMetade, individuoUm.size()));

            if (verificarProbabilidade(PROBABILIDADE_MUTACAO)) {
                filhoUm = mutacaoDeHorarios(filhoUm);
                filhoDois = mutacaoDeHorarios(filhoDois);
            }

            filhos.add(filhoUm);
            filhos.add(filhoDois);

            return filhos.get(random.nextInt(2));
        }

        filhos.add(individuoUm);
        filhos.add(individuoDois);

        return filhos.get(random.nextInt(2));
    }

    private static boolean verificarProbabilidade(double probabilidade) {
        final Random random = new Random();
        final double randomValue = random.nextDouble(); // Gera um número aleatório entre 0 e 1

        return randomValue <= probabilidade;
    }

    private static List<Horario> mutacaoDeHorarios(final List<Horario> individuo) {
        final Random random = new Random();
        for (int i = 0; i < TOTAL_HORARIOS; i += QUANTIDADE_HORARIO_POR_PERIODO) {
            int ultimoIndexDoPeriodo = (i + QUANTIDADE_HORARIO_POR_PERIODO - 1);

            int primeiroIndex = random.nextInt((ultimoIndexDoPeriodo - i) + 1) + i;
            int segundoIndex = random.nextInt((ultimoIndexDoPeriodo - i) + 1) + i;

            Collections.swap(individuo, primeiroIndex, segundoIndex);
        }

        return individuo;
    }

    private static List<List<Horario>> retornaHorariosOrdenadosPorQuantidadeDeConflitos(final List<List<Horario>> horarios) {
        return horarios.stream()
            .sorted(Comparator.comparingInt(Main::verificarConflitoDeHorarios))
            .collect(Collectors.toList());
    }

    private static List<Horario> embaralhaPorPeriodo(final List<Horario> horarios) {
        final List<List<Horario>> horariosPorPeriodo = retornaOsHorariosDividosPorPeriodos(horarios);

        horariosPorPeriodo.forEach(Collections::shuffle);

        return horariosPorPeriodo
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    private static List<List<Horario>> retornaOsHorariosDividosPorPeriodos(final List<Horario> horarios) {
        final List<List<Horario>> sublistas = new ArrayList<>();

        int tamanhoSublista = 20;
        for (int i = 0; i < horarios.size(); i += tamanhoSublista) {
            int endIndex = Math.min(i + tamanhoSublista, horarios.size());
            sublistas.add(horarios.subList(i, endIndex));
        }

        return sublistas;
    }

    private static void escreveALinhaDoCsv(final List<Horario> horarios, final FileWriter writer) throws IOException {
        final StringBuilder linha = new StringBuilder();

        // Concatena o codigo do professor e o codigo da materia
        for (final Horario horario : horarios) {
            linha.append(horario.getProfessor().getCodigo())
                .append("-")
                .append(horario.getMateria().getCodigo())
                .append(",");
        }

        // Remover a última vírgula da linha, se existir
        if (linha.length() > 0) {
            linha.setLength(linha.length() - 1);
        }

        writer.append(linha.toString());
        writer.append(System.lineSeparator());
    }

    private static void geraOsHorariosPrimordiais(final List<Materia> materias,
                                                  final List<Professor> professores,
                                                  final List<Horario> horarios) {
        final Random random = new Random();
        for (final Materia materia : materias) {
            int codigoProfessorAleatorio = random.nextInt(10);
            for (int j = 0; j < 4; j++) {
                int diaDaSemanaAleatorio = random.nextInt(5);
                int posicaoAleatoriaDoHorario = random.nextInt(4);
                horarios.add(
                    new Horario(
                        professores.get(codigoProfessorAleatorio),
                        materia,
                        diaDaSemanaAleatorio,
                        posicaoAleatoriaDoHorario
                    )
                );
            }
        }
    }

    private static void criarProfessores(final List<Professor> professores) {
        professores.add(new Professor(1, "Jose"));
        professores.add(new Professor(2, "Maria"));
        professores.add(new Professor(3, "João"));
        professores.add(new Professor(4, "Ana"));
        professores.add(new Professor(5, "Pedro"));
        professores.add(new Professor(6, "Marta"));
        professores.add(new Professor(7, "Carlos"));
        professores.add(new Professor(8, "Fernanda"));
        professores.add(new Professor(9, "Paulo"));
        professores.add(new Professor(10, "Keyla"));
    }

    private static void criarMaterias(final List<Materia> materias) {
        materias.add(new Materia(1, 1, "Lógica de Programação"));
        materias.add(new Materia(2, 1, "Banco de Dados"));
        materias.add(new Materia(3, 1, "Programação Orientada a Objetos"));
        materias.add(new Materia(4, 1, "Redes de Computadores"));
        materias.add(new Materia(5, 1, "Sistemas Operacionais"));

        materias.add(new Materia(6, 2, "Engenharia de Software"));
        materias.add(new Materia(7, 2, "Matemática"));
        materias.add(new Materia(8, 2, "Física"));
        materias.add(new Materia(9, 2, "Química"));
        materias.add(new Materia(10, 2, "Biologia"));

        materias.add(new Materia(11, 3, "História"));
        materias.add(new Materia(12, 3, "Geografia"));
        materias.add(new Materia(13, 3, "Português"));
        materias.add(new Materia(14, 3, "Inglês"));
        materias.add(new Materia(15, 3, "Artes"));

        materias.add(new Materia(16, 4, "Educação Física"));
        materias.add(new Materia(17, 4, "Filosofia"));
        materias.add(new Materia(18, 4, "Sociologia"));
        materias.add(new Materia(19, 4, "Economia"));
        materias.add(new Materia(20, 4, "Direito"));

        materias.add(new Materia(21, 5, "Administração"));
        materias.add(new Materia(22, 5, "Marketing"));
        materias.add(new Materia(23, 5, "Finanças"));
        materias.add(new Materia(24, 5, "Gestão de Projetos"));
        materias.add(new Materia(25, 5, "Empreendedorismo"));
    }

    private static void criarPeriodos(final List<Periodo> periodos) {
        periodos.add(new Periodo(1, "Primeiro"));
        periodos.add(new Periodo(2, "Segundo"));
        periodos.add(new Periodo(3, "Terceiro"));
        periodos.add(new Periodo(4, "Quarto"));
        periodos.add(new Periodo(5, "Quinto"));
    }
}
