package org.example;

public class Periodo {

    private int codigo;
    private String descrição;

    public Periodo(final int codigo, final String descrição) {
        this.codigo = codigo;
        this.descrição = descrição;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(final int codigo) {
        this.codigo = codigo;
    }

    public String getDescrição() {
        return descrição;
    }

    public void setDescrição(final String descrição) {
        this.descrição = descrição;
    }
}
