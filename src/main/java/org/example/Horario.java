package org.example;

public class Horario {
    private Professor professor;
    private Materia materia;

    public Horario(final Professor professor, final Materia materia) {
        this.professor = professor;
        this.materia = materia;
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
}