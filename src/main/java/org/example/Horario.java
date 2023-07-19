package org.example;

public class Horario {
    private Professor professor;
    private Materia materia;
    private int posicao;
    private int dia;

    public Horario(final Professor professor, final Materia materia, final int dia, final int posicao) {
        this.professor = professor;
        this.materia = materia;
        this.dia = dia;
        this.posicao = posicao;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(final Professor professor) {
        this.professor = professor;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(final Materia materia) {
        this.materia = materia;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(final int dia) {
        this.dia = dia;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(final int posicao) {
        this.posicao = posicao;
    }
}