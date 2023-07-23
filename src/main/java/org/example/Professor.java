package org.example;

public class Professor {

    private int codigo;
    private String nome;

    public Professor(final int codigo, final String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(final int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }
}
