package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;

public class SinistroMediator {

    private VeiculoDAO daoVeiculo = new VeiculoDAO();
    private ApoliceDAO daoApolice = new ApoliceDAO();
    private SinistroDAO daoSinistro = new SinistroDAO();
    private static SinistroMediator instancia;

    public static SinistroMediator getInstancia() {
        if (instancia == null)
            instancia = new SinistroMediator();
        return instancia;
    }

    private SinistroMediator() {}

    public String incluirSinistro(DadosSinistro dados, LocalDateTime dataHoraAtual) throws ExcecaoValidacaoDados {
        ExcecaoValidacaoDados excecao = new ExcecaoValidacaoDados();
        List<String> mensagens = excecao.getMensagens();

        // Validação 1: dados não pode ser null
        if (dados == null) {
            mensagens.add("Dados do sinistro devem ser informados");
            throw excecao;
        }

        // Validação 2: data/hora do sinistro não pode ser null
        if (dados.getDataHoraSinistro() == null) {
            mensagens.add("Data/hora do sinistro deve ser informada");
        }

        // Validação 3: data/hora do sinistro deve ser menor que a data/hora atual
        if (dados.getDataHoraSinistro() != null && dados.getDataHoraSinistro().isAfter(dataHoraAtual)) {
            mensagens.add("Data/hora do sinistro deve ser menor que a data/hora atual");
        }

        // Validação 4: placa do veículo não pode ser null nem branco
        if (dados.getPlaca() == null || dados.getPlaca().trim().isEmpty()) {
            mensagens.add("Placa do Veículo deve ser informada");
        }

        // Validação 5: usuário do registro não pode ser null nem branco
        if (dados.getUsuarioRegistro() == null || dados.getUsuarioRegistro().trim().isEmpty()) {
            mensagens.add("Usuário do registro de sinistro deve ser informado");
        }

        // Validação 6: valor do sinistro deve ser maior que zero
        if (dados.getValorSinistro() <= 0) {
            mensagens.add("Valor do sinistro deve ser maior que zero");
        }

        // Validação 7: código do tipo de sinistro deve ser válido
        TipoSinistro tipoSinistro = null;
        try {
            TipoSinistro[] tipos = TipoSinistro.values();
            if (dados.getCodigoTipoSinistro() < 1 || dados.getCodigoTipoSinistro() > tipos.length) {
                mensagens.add("Código do tipo de sinistro inválido");
            } else {
                tipoSinistro = tipos[dados.getCodigoTipoSinistro() - 1];
            }
        } catch (Exception e) {
            mensagens.add("Código do tipo de sinistro inválido");
        }

        // Validação 8: placa deve ser de um veículo cadastrado
        Veiculo veiculo = null;
        if (dados.getPlaca() != null && !dados.getPlaca().trim().isEmpty()) {
            veiculo = daoVeiculo.buscar(dados.getPlaca());
            if (veiculo == null) {
                mensagens.add("Veículo não cadastrado");
            }
        }

        // Validações de apólice
        Apolice apoliceVigente = null;
        if (veiculo != null && dados.getDataHoraSinistro() != null) {
            // Buscar apólice vigente para o veículo
            Apolice[] todasApolices = daoApolice.buscarTodos();
            if (todasApolices != null) {
                for (Apolice apolice : todasApolices) {
                    if (apolice.getVeiculo().getPlaca().equals(dados.getPlaca())) {
                        // Verificar se a apólice está vigente (1 ano a partir da data de início)
                        if (apolice.getDataInicioVigencia() != null) {
                            LocalDateTime inicioVigencia = apolice.getDataInicioVigencia().atStartOfDay();
                            LocalDateTime fimVigencia = inicioVigencia.plusYears(1);

                            if (!dados.getDataHoraSinistro().isBefore(inicioVigencia) &&
                                    dados.getDataHoraSinistro().isBefore(fimVigencia)) {
                                apoliceVigente = apolice;
                                break;
                            }
                        }
                    }
                }
            }

            if (apoliceVigente == null) {
                mensagens.add("Não existe apólice vigente para o veículo");
            }
        }

        // Validação do valor máximo segurado
        if (apoliceVigente != null && apoliceVigente.getValorMaximoSegurado() != null) {
            if (dados.getValorSinistro() > apoliceVigente.getValorMaximoSegurado().doubleValue()) {
                mensagens.add("Valor do sinistro não pode ultrapassar o valor máximo segurado constante na apólice");
            }
        }

        // Se houver erros, lançar exceção
        if (!mensagens.isEmpty()) {
            throw excecao;
        }

        // Calcular sequencial
        int sequencial = 1;
        Sinistro[] sinistrosExistentes = daoSinistro.buscarTodos();
        if (sinistrosExistentes != null) {
            List<Sinistro> sinistrosMesmaApolice = new ArrayList<>();
            for (Sinistro sinistro : sinistrosExistentes) {
                if (sinistro.getNumeroApolice().equals(apoliceVigente.getNumero())) {
                    sinistrosMesmaApolice.add(sinistro);
                }
            }

            if (!sinistrosMesmaApolice.isEmpty()) {
                Collections.sort(sinistrosMesmaApolice, new ComparadorSinistroSequencial());
                sequencial = sinistrosMesmaApolice.get(sinistrosMesmaApolice.size() - 1).getSequencial() + 1;
            }
        }

        // Formar número do sinistro
        String numeroSinistro = "S" + apoliceVigente.getNumero() + String.format("%03d", sequencial);

        // Criar e salvar sinistro
        Sinistro sinistro = new Sinistro(
                numeroSinistro,
                veiculo,
                dados.getDataHoraSinistro(),
                dataHoraAtual,
                dados.getUsuarioRegistro(),
                new BigDecimal(dados.getValorSinistro()),
                tipoSinistro
        );

        sinistro.setNumeroApolice(apoliceVigente.getNumero());
        sinistro.setSequencial(sequencial);

        daoSinistro.incluir(sinistro);

        return numeroSinistro;
    }
}