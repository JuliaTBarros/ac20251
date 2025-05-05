package br.edu.cs.poo.divisao;

import lombok.Getter;

/**
 * Classe que representa o retorno de operações de divisão,
 * contendo o resultado ou uma mensagem de erro.
 */
@Getter
public class RetornoDivisao {
    private Double resultado;
    private String mensagemErro;

    /**
     * Construtor para a classe RetornoDivisao
     *
     * @param resultado O resultado da divisão, ou null em caso de erro
     * @param mensagemErro A mensagem de erro, ou null em caso de sucesso
     * @throws RuntimeException se ambos os parâmetros forem nulos ou preenchidos
     */
    public RetornoDivisao(Double resultado, String mensagemErro) {
        if (resultado == null && mensagemErro == null) {
            throw new RuntimeException(
                    "Resultado e mensagem de erro não podem ser ambos nulos");
        }
        if (resultado != null && mensagemErro != null) {
            throw new RuntimeException(
                    "Resultado e mensagem de erro não podem ser ambos preenchidos");
        }
        this.resultado = resultado;
        this.mensagemErro = mensagemErro;
    }
}