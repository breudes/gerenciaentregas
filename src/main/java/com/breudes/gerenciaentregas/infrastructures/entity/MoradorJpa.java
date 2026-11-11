package com.breudes.gerenciaentregas.infrastructures.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "moradores")
public class MoradorJpa implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String telefone;
    private String email;
    @Column(nullable = false)
    private String apartamento;
    @Column(nullable = false)
    private String bloco;

    public MoradorJpa() {
    }

    public MoradorJpa(Long id, String nome, String telefone, String apartamento, String bloco) {
        this(id, nome, telefone, null, apartamento, bloco);
    }

    public MoradorJpa(Long id, String nome, String telefone, String email, String apartamento, String bloco) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "MoradorJpa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", apartamento='" + apartamento + '\'' +
                ", bloco='" + bloco + '\'' +
                '}';
    }
}
