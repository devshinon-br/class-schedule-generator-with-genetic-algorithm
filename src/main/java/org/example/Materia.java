package org.example;

public class Materia {

    private int codigo;
    private int codigoPeriodo;
    private String nome;

    public Materia(final int codigo, final int codigoPeriodo, final String nome) {
        this.codigo = codigo;
        this.codigoPeriodo = codigoPeriodo;
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(final int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoPeriodo() {
        return codigoPeriodo;
    }

    public void setCodigoPeriodo(final int codigoPeriodo) {
        this.codigoPeriodo = codigoPeriodo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }
}
