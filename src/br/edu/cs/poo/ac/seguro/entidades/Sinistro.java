package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sinistro implements Registro {
    private String numero;
    private Veiculo veiculo;
    private LocalDateTime dataHoraSinistro;
    private LocalDateTime dataHoraRegistro;
    private String usuarioRegistro;
    private BigDecimal valorSinistro;
    private TipoSinistro tipo;

    // Novos atributos adicionados (não incluídos no construtor AllArgsConstructor)
    private int sequencial;
    private String numeroApolice;

    // Constructor for the original 7 parameters (maintains compatibility)
    public Sinistro(String numero, Veiculo veiculo, LocalDateTime dataHoraSinistro,
                    LocalDateTime dataHoraRegistro, String usuarioRegistro,
                    BigDecimal valorSinistro, TipoSinistro tipo) {
        this.numero = numero;
        this.veiculo = veiculo;
        this.dataHoraSinistro = dataHoraSinistro;
        this.dataHoraRegistro = dataHoraRegistro;
        this.usuarioRegistro = usuarioRegistro;
        this.valorSinistro = valorSinistro;
        this.tipo = tipo;
    }

    // Constructor for all parameters including new ones
    public Sinistro(String numero, Veiculo veiculo, LocalDateTime dataHoraSinistro,
                    LocalDateTime dataHoraRegistro, String usuarioRegistro,
                    BigDecimal valorSinistro, TipoSinistro tipo, int sequencial,
                    String numeroApolice) {
        this.numero = numero;
        this.veiculo = veiculo;
        this.dataHoraSinistro = dataHoraSinistro;
        this.dataHoraRegistro = dataHoraRegistro;
        this.usuarioRegistro = usuarioRegistro;
        this.valorSinistro = valorSinistro;
        this.tipo = tipo;
        this.sequencial = sequencial;
        this.numeroApolice = numeroApolice;
    }

    @Override
    public String getIdUnico() {
        return numero;
    }
}