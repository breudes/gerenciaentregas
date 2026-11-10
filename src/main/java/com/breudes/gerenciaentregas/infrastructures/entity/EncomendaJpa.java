package com.breudes.gerenciaentregas.infrastructures.entity;

import com.breudes.gerenciaentregas.infrastructures.enums.StatusEncomendaJpa;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "encomendas")
public class EncomendaJpa implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String descricao;
    @ManyToOne(cascade = CascadeType.ALL)
    private MoradorJpa moradorDestinatarioJpa;
    @ManyToOne(cascade = CascadeType.ALL)
    private MoradorJpa moradorRecebedorJpa;
    private LocalDateTime dataDeCadastro;
    private LocalDateTime dataDeEntrega;
    private boolean ativo;
    @Enumerated
    private StatusEncomendaJpa status;

    public EncomendaJpa() {
    }

    public EncomendaJpa(Long id, String descricao,
                        MoradorJpa moradorDestinatarioJpa, MoradorJpa moradorRecebedorJpa,
                        LocalDateTime dataDeCadastro, LocalDateTime dataDeEntrega, boolean ativo, StatusEncomendaJpa status) {
        this.id = id;
        this.descricao = descricao;
        this.moradorDestinatarioJpa = moradorDestinatarioJpa;
        this.moradorRecebedorJpa = moradorRecebedorJpa;
        this.dataDeCadastro = dataDeCadastro;
        this.dataDeEntrega = dataDeEntrega;
        this.ativo = ativo;
        this.status = status;
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

    public MoradorJpa getMoradorDestinatarioJpa() {
        return moradorDestinatarioJpa;
    }

    public void setMoradorDestinatarioJpa(MoradorJpa moradorDestinatarioJpa) {
        this.moradorDestinatarioJpa = moradorDestinatarioJpa;
    }

    public MoradorJpa getMoradorRecebedorJpa() {
        return moradorRecebedorJpa;
    }

    public void setMoradorRecebedorJpa(MoradorJpa moradorRecebedorJpa) {
        this.moradorRecebedorJpa = moradorRecebedorJpa;
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

    public StatusEncomendaJpa getStatus() {
        return status;
    }

    public void setStatus(StatusEncomendaJpa status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EncomendaJpa{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", moradorDestinatarioJpa=" + moradorDestinatarioJpa +
                ", moradorRecebedorJpa=" + moradorRecebedorJpa +
                ", dataDeCadastro=" + dataDeCadastro +
                ", dataDeEntrega=" + dataDeEntrega +
                ", ativo=" + ativo +
                ", status=" + status +
                '}';
    }
}
