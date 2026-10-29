package com.breudes.gerenciaentregas.core.domain;

import java.time.LocalDateTime;

public class Encomenda {
    private Long id;
    private String descricao;
    private Morador moradorDestinatario;
    private Morador moradorRecebedor;
    private LocalDateTime dataDeCadastro;
    private LocalDateTime dataDeEntrega;
    private boolean ativo;

    public Encomenda() {
    }

    public Encomenda(Long id, String descricao, Morador moradorDestinatario, Morador moradorRecebedor, LocalDateTime dataDeCadastro, LocalDateTime dataDeEntrega, boolean ativo) {
        this.id = id;
        this.descricao = descricao;
        this.moradorDestinatario = moradorDestinatario;
        this.moradorRecebedor = moradorRecebedor;
        this.dataDeCadastro = dataDeCadastro;
        this.dataDeEntrega = dataDeEntrega;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Morador getMoradorDestinatario() {
        return moradorDestinatario;
    }

    public void setMoradorDestinatario(Morador moradorDestinatario) {
        this.moradorDestinatario = moradorDestinatario;
    }

    public Morador getMoradorRecebedor() {
        return moradorRecebedor;
    }

    public void setMoradorRecebedor(Morador moradorRecebedor) {
        this.moradorRecebedor = moradorRecebedor;
    }

    public LocalDateTime getDataDeCadastro() {
        return dataDeCadastro;
    }

    public void setDataDeCadastro(LocalDateTime dataDeCadastro) {
        this.dataDeCadastro = dataDeCadastro;
    }

    public LocalDateTime getDataDeEntrega() {
        return dataDeEntrega;
    }

    public void setDataDeEntrega(LocalDateTime dataDeEntrega) {
        this.dataDeEntrega = dataDeEntrega;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
