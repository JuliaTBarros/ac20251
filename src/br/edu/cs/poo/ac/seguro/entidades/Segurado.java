package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public abstract class Segurado implements Registro {
    private String nome;
    private Endereco endereco;
    private LocalDate dataCriacao;
    private BigDecimal bonus;

    public Segurado(String nome, Endereco endereco, LocalDate dataCriacao, BigDecimal bonus) {
        this.nome = nome;
        this.endereco = endereco;
        this.dataCriacao = dataCriacao;
        this.bonus = bonus;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    protected LocalDate getDataCriacao() {
        return dataCriacao;
    }

    protected void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public int getIdade() {
        return Period.between(dataCriacao, LocalDate.now()).getYears();
    }

    public void creditarBonus(BigDecimal valor) {
        this.bonus = this.bonus.add(valor);
    }

    public void debitarBonus(BigDecimal valor) {
        this.bonus = this.bonus.subtract(valor);
    }

    // Método abstrato para verificar se é empresa
    public abstract boolean isEmpresa();

    // Implementação do método da interface Registro
    // Cada subclasse deve implementar seu próprio getIdUnico()
    @Override
    public abstract String getIdUnico();
}