package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Informe o tamanho da população: ");
        int numberOfLines = scanner.nextInt();
        scanner.close();

        try {
            FileWriter writer = new FileWriter("horarios.csv");

            for (int i = 0; i < numberOfLines; i++) {
                System.out.println("Gerando CSV: Linha " + (i + 1));

                final List<Materia> materias = new ArrayList<>(); //25
                criarMaterias(materias);

                final List<Periodo> periodos = new ArrayList<>(); // 5
                criarPeriodos(periodos);

                final List<Professor> professores = new ArrayList<>(); //10
                criarProfessores(professores);

                final List<Horario> horarios = new ArrayList<>();
                gerarHorarios(materias, professores, horarios); //100

                int conflitos = verificarConflitoHorarios(horarios);
                System.out.println("Conflitos encontrados: " + conflitos);
                System.out.println();

                final List<Horario> horariosEmbaralhadosPorPeriodo = embaralhaPorPeriodo(horarios);
                escreverLinhaCsv(horariosEmbaralhadosPorPeriodo, writer);
            }

            writer.flush();
            writer.close();
            System.out.println("CSV generated successfully!");
        } catch (IOException e) {
            System.out.println("Error generating CSV: " + e.getMessage());
        }
    }

    private static List<Horario> embaralhaPorPeriodo(final List<Horario> horarios) {
        final List<List<Horario>> horariosPorPeriodo = quebraLista(horarios);

        horariosPorPeriodo.forEach(Collections::shuffle);

        return horariosPorPeriodo
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    private static List<List<Horario>> quebraLista(final List<Horario> horarios) {
        List<List<Horario>> sublistas = new ArrayList<>();

        int tamanhoSublista = 20;
        for (int i = 0; i < horarios.size(); i += tamanhoSublista) {
            int endIndex = Math.min(i + tamanhoSublista, horarios.size());
            sublistas.add(horarios.subList(i, endIndex));
        }

        return sublistas;
    }

    private static int verificarConflitoHorarios(final List<Horario> horarios) {
        int quantidadeDeChoques = 0;

        // Percorrer a lista de horários
        for (int i = 0; i < horarios.size() - 1; i++) {

            final Horario horario1 = horarios.get(i);
            final int codigoProfessor1 = horario1.getProfessor().getCodigo();
            final int codigoPeriodo1 = horario1.getMateria().getCodigoPeriodo();
            final int diaHorario1 = i % 5;

            final Horario horario2 = horarios.get(i + 1);
            final int codigoProfessor2 = horario2.getProfessor().getCodigo();
            final int codigoPeriodo2 = horario2.getMateria().getCodigoPeriodo();
            final int diaHorario2 = (i + 1) % 5;

            // Verificar se os professores são os mesmos e se estão no mesmo horário e dia, mas em períodos diferentes
            if (codigoPeriodo1 != codigoPeriodo2
                && codigoProfessor1 == codigoProfessor2
                && diaHorario1 == diaHorario2) {
                quantidadeDeChoques++;
            }
        }

        return quantidadeDeChoques;
    }

    private static void escreverLinhaCsv(final List<Horario> horarios, final FileWriter writer) throws IOException {
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

    private static void gerarHorarios(final List<Materia> materias,
                                      final List<Professor> professores,
                                      final List<Horario> horarios) {
        int limiteInferior = 0;  // Limite inferior (inclusive)
        int limiteSuperior = 10;  // Limite superior (exclusive)

        Random random = new Random();

        for (final Materia materia : materias) {
            // Gera um número inteiro aleatório dentro do limite
            int numeroAleatorio = random.nextInt(limiteSuperior - limiteInferior) + limiteInferior;

            for (int j = 0; j < 4; j++) {
                horarios.add(new Horario(professores.get(numeroAleatorio), materia));
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
