package org.example;

public class Horario {
    private Professor professor;
    private Materia materia;
    private int dia;

    public Horario(final Professor professor, final Materia materia, final int dia) {
        this.professor = professor;
        this.materia = materia;
        this.dia = dia;
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
}