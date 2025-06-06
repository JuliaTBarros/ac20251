package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.PrecoAno;
import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class ApoliceMediator {
    private static ApoliceMediator instancia;
    private SeguradoPessoaDAO daoSegPes;
    private SeguradoEmpresaDAO daoSegEmp;
    private VeiculoDAO daoVel;
    private ApoliceDAO daoApo;
    private SinistroDAO daoSin;

    private ApoliceMediator() {
        daoSegPes = new SeguradoPessoaDAO();
        daoSegEmp = new SeguradoEmpresaDAO();
        daoVel = new VeiculoDAO();
        daoApo = new ApoliceDAO();
        daoSin = new SinistroDAO();
    }

    public static ApoliceMediator getInstancia() {
        if (instancia == null) {
            instancia = new ApoliceMediator();
        }
        return instancia;
    }

    public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
        if (dados == null) {
            return new RetornoInclusaoApolice(null, "Dados do veículo devem ser informados");
        }

        String msgErro = validarTodosDadosVeiculo(dados);
        if (msgErro != null) {
            return new RetornoInclusaoApolice(null, msgErro);
        }

        String cpfOuCnpj = dados.getCpfOuCnpj();
        boolean isCpf = cpfOuCnpj.length() == 11;
        Segurado segurado = null;

        if (isCpf) {
            segurado = daoSegPes.buscar(cpfOuCnpj);
            if (segurado == null) {
                return new RetornoInclusaoApolice(null, "CPF inexistente no cadastro de pessoas");
            }
        } else {
            segurado = daoSegEmp.buscar(cpfOuCnpj);
            if (segurado == null) {
                return new RetornoInclusaoApolice(null, "CNPJ inexistente no cadastro de empresas");
            }
        }

        String numeroApolice;
        int anoAtual = LocalDate.now().getYear();
        if (isCpf) {
            numeroApolice = anoAtual + "000" + cpfOuCnpj + dados.getPlaca();
        } else {
            numeroApolice = anoAtual + cpfOuCnpj + dados.getPlaca();
        }

        Apolice apoliceExistente = daoApo.buscar(numeroApolice);
        if (apoliceExistente != null) {
            return new RetornoInclusaoApolice(null, "Apólice já existente para ano atual e veículo");
        }

        Veiculo veiculo = daoVel.buscar(dados.getPlaca());
        CategoriaVeiculo categoria = CategoriaVeiculo.values()[dados.getCodigoCategoria() - 1];

        if (veiculo == null) {
            veiculo = new Veiculo(dados.getPlaca(), dados.getAno(), segurado, categoria);
            daoVel.incluir(veiculo);
        } else {
            veiculo.setProprietario(segurado);
            daoVel.alterar(veiculo);
        }

        BigDecimal valorMaximoSegurado = dados.getValorMaximoSegurado().setScale(2, RoundingMode.HALF_UP);
        BigDecimal vpa = valorMaximoSegurado.multiply(new BigDecimal("0.03")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal vpb;

        // Utiliza o método isEmpresa() para verificar se é empresa e o método isEhLocadoraDeVeiculos()
        // caso seja uma empresa locadora de veículos
        if (segurado.isEmpresa() && ((SeguradoEmpresa) segurado).isEhLocadoraDeVeiculos()) {
            vpb = vpa.multiply(new BigDecimal("1.2")).setScale(2, RoundingMode.HALF_UP);
        } else {
            vpb = vpa;
        }

        BigDecimal bonus = segurado.getBonus();
        BigDecimal bonusDividido = bonus.divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP);
        BigDecimal vpc = vpb.subtract(bonusDividido).setScale(2, RoundingMode.HALF_UP);

        BigDecimal valorPremio = vpc.compareTo(BigDecimal.ZERO) > 0 ? vpc : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorFranquia = vpb.multiply(new BigDecimal("1.3")).setScale(2, RoundingMode.HALF_UP);

        LocalDate dataInicioVigencia = LocalDate.now();
        Apolice apolice = new Apolice(numeroApolice, veiculo, valorFranquia, valorPremio, valorMaximoSegurado, dataInicioVigencia);
        daoApo.incluir(apolice);

        Sinistro[] sinistros = daoSin.buscarTodos();
        boolean temSinistroAnoAnterior = false;

        if (sinistros != null) {
            for (Sinistro sinistro : sinistros) {
                if (sinistro != null && sinistro.getVeiculo() != null) {
                    boolean mesmoProprietario = false;

                    // Verificar se é o mesmo proprietário utilizando polimorfismo
                    if (sinistro.getVeiculo().getProprietario() != null) {
                        if (!sinistro.getVeiculo().getProprietario().isEmpresa() && segurado instanceof SeguradoPessoa) {
                            // Ambos são pessoa física
                            mesmoProprietario = ((SeguradoPessoa) sinistro.getVeiculo().getProprietario()).getCpf()
                                    .equals(((SeguradoPessoa) segurado).getCpf());
                        } else if (sinistro.getVeiculo().getProprietario().isEmpresa() && segurado instanceof SeguradoEmpresa) {
                            // Ambos são pessoa jurídica
                            mesmoProprietario = ((SeguradoEmpresa) sinistro.getVeiculo().getProprietario()).getCnpj()
                                    .equals(((SeguradoEmpresa) segurado).getCnpj());
                        }
                    }

                    if (mesmoProprietario &&
                            sinistro.getDataHoraSinistro().getYear() == dataInicioVigencia.getYear() - 1) {
                        temSinistroAnoAnterior = true;
                        break;
                    }
                }
            }
        }

        if (!temSinistroAnoAnterior) {
            BigDecimal bonusAdicional = valorPremio.multiply(new BigDecimal("0.3")).setScale(2, RoundingMode.HALF_UP);
            segurado.creditarBonus(bonusAdicional);

            // Atualiza o segurado no DAO correspondente
            if (!segurado.isEmpresa()) {
                daoSegPes.alterar((SeguradoPessoa) segurado);
            } else {
                daoSegEmp.alterar((SeguradoEmpresa) segurado);
            }
        }

        return new RetornoInclusaoApolice(numeroApolice, null);
    }

    public Apolice buscarApolice(String numero) {
        return daoApo.buscar(numero);
    }

    public String excluirApolice(String numero) {
        if (StringUtils.ehNuloOuBranco(numero)) {
            return "Número deve ser informado";
        }

        Apolice apolice = daoApo.buscar(numero);
        if (apolice == null) {
            return "Apólice inexistente";
        }

        Sinistro[] sinistros = daoSin.buscarTodos();
        if (sinistros != null) {
            int anoApolice = apolice.getDataInicioVigencia().getYear();
            String placaVeiculo = apolice.getVeiculo().getPlaca();

            for (Sinistro sinistro : sinistros) {
                if (sinistro != null && sinistro.getVeiculo() != null) {
                    boolean mesmoVeiculo = placaVeiculo.equals(sinistro.getVeiculo().getPlaca());
                    boolean mesmoAno = sinistro.getDataHoraSinistro().getYear() == anoApolice;

                    if (mesmoVeiculo && mesmoAno) {
                        return "Existe sinistro cadastrado para o veículo em questão e no mesmo ano da apólice";
                    }
                }
            }
        }

        daoApo.excluir(numero);
        return null;
    }

    private String validarTodosDadosVeiculo(DadosVeiculo dados) {
        if (StringUtils.ehNuloOuBranco(dados.getCpfOuCnpj())) {
            return "CPF ou CNPJ deve ser informado";
        }

        String cpfOuCnpj = dados.getCpfOuCnpj();
        if (cpfOuCnpj.length() == 11) {
            if (!ValidadorCpfCnpj.ehCpfValido(cpfOuCnpj)) {
                return "CPF inválido";
            }
        } else if (cpfOuCnpj.length() == 14) {
            if (!ValidadorCpfCnpj.ehCnpjValido(cpfOuCnpj)) {
                return "CNPJ inválido";
            }
        } else {
            return "CPF ou CNPJ inválido";
        }

        if (StringUtils.ehNuloOuBranco(dados.getPlaca())) {
            return "Placa do veiculo deve ser informada";
        }

        if (dados.getAno() < 2020 || dados.getAno() > 2025) {
            return "Ano tem que estar entre 2020 e 2025, incluindo estes";
        }

        if (dados.getCodigoCategoria() < 1 || dados.getCodigoCategoria() > 5) {
            return "Categoria inválida";
        }

        return validarCpfCnpjValorMaximo(dados);
    }

    private String validarCpfCnpjValorMaximo(DadosVeiculo dados) {
        if (dados.getValorMaximoSegurado() == null) {
            return "Valor máximo segurado deve ser informado";
        }

        BigDecimal valorMaximoPermitido = obterValorMaximoPermitido(dados.getAno(), dados.getCodigoCategoria());
        BigDecimal valorMinimo = valorMaximoPermitido.multiply(new BigDecimal("0.75")).setScale(2, RoundingMode.HALF_UP);

        if (dados.getValorMaximoSegurado().compareTo(valorMinimo) < 0 ||
                dados.getValorMaximoSegurado().compareTo(valorMaximoPermitido) > 0) {
            return "Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria";
        }

        return null;
    }

    private BigDecimal obterValorMaximoPermitido(int ano, int codigoCat) {
        CategoriaVeiculo categoria = CategoriaVeiculo.values()[codigoCat - 1];
        PrecoAno[] precosAnos = categoria.getPrecosAnos();

        for (PrecoAno precoAno : precosAnos) {
            if (precoAno.getAno() == ano) {
                return new BigDecimal(Double.toString(precoAno.getPreco())).setScale(2, RoundingMode.HALF_UP);
            }
        }

        return BigDecimal.ZERO;
    }
}