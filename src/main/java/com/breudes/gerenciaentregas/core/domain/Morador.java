package com.breudes.gerenciaentregas.core.domain;

public class Morador {
    private Long id;
    private String nome;
    private String telefone;
    private String apartamento;
    private String bloco;

    public Morador() {
    }

    public Morador(Long id, String nome, String telefone, String apartamento, String bloco) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.apartamento = apartamento;
        this.bloco = bloco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getApartamento() {
        return apartamento;
    }

    public void setApartamento(String apartamento) {
        this.apartamento = apartamento;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }
}
