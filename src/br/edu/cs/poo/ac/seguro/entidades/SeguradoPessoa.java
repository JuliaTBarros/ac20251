package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SeguradoPessoa extends Segurado {
    private String cpf;
    private double renda;

    public SeguradoPessoa(String nome, Endereco endereco, LocalDate dataNascimento, BigDecimal bonus,
                          String cpf, double renda) {
        super(nome, endereco, dataNascimento, bonus);
        this.cpf = cpf;
        this.renda = renda;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getRenda() {
        return renda;
    }

    public void setRenda(double renda) {
        this.renda = renda;
    }

    public LocalDate getDataNascimento() {
        return getDataCriacao();
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        setDataCriacao(dataNascimento);
    }

    @Override
    public boolean isEmpresa() {
        return false; // SeguradoPessoa não é empresa
    }

    @Override
    public String getIdUnico() {
        return cpf;
    }
}