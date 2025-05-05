package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Apolice implements Serializable {
    private String numero;
    private Veiculo veiculo;
    private BigDecimal valorFranquia;
    private BigDecimal valorPremio;
    private BigDecimal valorMaximoSegurado;
    private LocalDate dataInicioVigencia;

    // Constructor without dataInicioVigencia - for backward compatibility
    public Apolice(String numero, Veiculo veiculo, BigDecimal valorFranquia, BigDecimal valorPremio, BigDecimal valorMaximoSegurado) {
        this.numero = numero;
        this.veiculo = veiculo;
        this.valorFranquia = valorFranquia;
        this.valorPremio = valorPremio;
        this.valorMaximoSegurado = valorMaximoSegurado;
        this.dataInicioVigencia = LocalDate.now(); // Default to current date
    }

    // Full constructor with all fields
    public Apolice(String numero, Veiculo veiculo, BigDecimal valorFranquia, BigDecimal valorPremio,
                   BigDecimal valorMaximoSegurado, LocalDate dataInicioVigencia) {
        this.numero = numero;
        this.veiculo = veiculo;
        this.valorFranquia = valorFranquia;
        this.valorPremio = valorPremio;
        this.valorMaximoSegurado = valorMaximoSegurado;
        this.dataInicioVigencia = dataInicioVigencia;
    }
}