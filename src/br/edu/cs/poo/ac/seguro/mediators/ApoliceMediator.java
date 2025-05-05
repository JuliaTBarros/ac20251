package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.PrecoAno;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class ApoliceMediator {
    private static final ApoliceMediator INSTANCIA = new ApoliceMediator();
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
        return INSTANCIA;
    }

    public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
        // Validar dados do veículo
        String mensagemErro = validarTodosDadosVeiculo(dados);
        if (mensagemErro != null) {
            return new RetornoInclusaoApolice(null, mensagemErro);
        }

        // Verifica se é CPF ou CNPJ
        boolean isCpf = dados.getCpfOuCnpj().length() <= 11;

        // Gerar número da apólice
        String numeroApolice;
        if (isCpf) {
            numeroApolice = LocalDate.now().getYear() + "000" + dados.getCpfOuCnpj() + dados.getPlaca();
        } else {
            numeroApolice = LocalDate.now().getYear() + dados.getCpfOuCnpj() + dados.getPlaca();
        }

        // Verificar se já existe apólice para este veículo no ano atual
        Apolice apoliceExistente = daoApo.buscar(numeroApolice);
        if (apoliceExistente != null) {
            return new RetornoInclusaoApolice(null, "Apólice já existente para ano atual e veículo");
        }

        // Buscar segurado por CPF ou CNPJ
        SeguradoPessoa seguradoPessoa = null;
        SeguradoEmpresa seguradoEmpresa = null;
        if (isCpf) {
            seguradoPessoa = daoSegPes.buscar(dados.getCpfOuCnpj());
            if (seguradoPessoa == null) {
                return new RetornoInclusaoApolice(null, "CPF inexistente no cadastro de pessoas");
            }
        } else {
            seguradoEmpresa = daoSegEmp.buscar(dados.getCpfOuCnpj());
            if (seguradoEmpresa == null) {
                return new RetornoInclusaoApolice(null, "CNPJ inexistente no cadastro de empresas");
            }
        }

        // Converter código de categoria para enum CategoriaVeiculo
        CategoriaVeiculo categoria;
        switch (dados.getCodigoCategoria()) {
            case 1:
                categoria = CategoriaVeiculo.BASICO;
                break;
            case 2:
                categoria = CategoriaVeiculo.INTERMEDIARIO;
                break;
            case 3:
                categoria = CategoriaVeiculo.LUXO;
                break;
            case 4:
                categoria = CategoriaVeiculo.SUPER_LUXO;
                break;
            case 5:
                categoria = CategoriaVeiculo.ESPORTIVO;
                break;
            default:
                return new RetornoInclusaoApolice(null, "Categoria inválida");
        }

        // Buscar ou criar veículo
        Veiculo veiculo = daoVel.buscar(dados.getPlaca());
        if (veiculo == null) {
            // Criar novo veículo
            veiculo = new Veiculo(dados.getPlaca(), dados.getAno(), seguradoEmpresa, seguradoPessoa, categoria);
            daoVel.incluir(veiculo);
        } else {
            // Atualizar veículo existente
            veiculo.setCategoria(categoria);
            veiculo.setAno(dados.getAno());
            if (isCpf) {
                veiculo = new Veiculo(dados.getPlaca(), dados.getAno(), null, seguradoPessoa, categoria);
            } else {
                veiculo = new Veiculo(dados.getPlaca(), dados.getAno(), seguradoEmpresa, null, categoria);
            }
            daoVel.alterar(veiculo);
        }

        // Calcular valor do prêmio e franquia
        BigDecimal valorMaximoSegurado = dados.getValorMaximoSegurado().setScale(2, RoundingMode.HALF_UP);

        // Passo A: 3% do valor máximo segurado
        BigDecimal vpa = valorMaximoSegurado.multiply(new BigDecimal("0.03")).setScale(2, RoundingMode.HALF_UP);

        // Passo B
        BigDecimal vpb;
        if (seguradoEmpresa != null && seguradoEmpresa.isEhLocadoraDeVeiculos()) {
            vpb = vpa.multiply(new BigDecimal("1.2")).setScale(2, RoundingMode.HALF_UP);
        } else {
            vpb = vpa;
        }

        // Buscar bônus do segurado
        BigDecimal bonus = BigDecimal.ZERO;
        if (isCpf && seguradoPessoa != null) {
            bonus = seguradoPessoa.getBonus();
        } else if (seguradoEmpresa != null) {
            bonus = seguradoEmpresa.getBonus();
        }

        // Passo C
        BigDecimal vpc = vpb.subtract(bonus.divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);

        // Passo D
        BigDecimal valorPremio = vpc.compareTo(BigDecimal.ZERO) > 0 ? vpc : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        // Valor da franquia (130% do VPB)
        BigDecimal valorFranquia = vpb.multiply(new BigDecimal("1.3")).setScale(2, RoundingMode.HALF_UP);

        // Criar e incluir apólice
        LocalDate dataInicio = LocalDate.now();
        Apolice apolice = new Apolice(numeroApolice, veiculo, valorFranquia, valorPremio, valorMaximoSegurado, dataInicio);
        daoApo.incluir(apolice);

        // Verificar se o segurado deve receber bônus
        boolean temSinistro = false;
        Sinistro[] sinistros = daoSin.buscarTodos();
        if (sinistros != null) {
            for (Sinistro sinistro : sinistros) {
                if (sinistro != null) {
                    Veiculo veiculoSinistro = sinistro.getVeiculo();
                    int anoSinistro = sinistro.getDataHoraSinistro().getYear();
                    if (veiculoSinistro != null &&
                            ((isCpf && veiculoSinistro.getProprietarioPessoa() != null &&
                                    veiculoSinistro.getProprietarioPessoa().getCpf().equals(dados.getCpfOuCnpj())) ||
                                    (!isCpf && veiculoSinistro.getProprietarioEmpresa() != null &&
                                            veiculoSinistro.getProprietarioEmpresa().getCnpj().equals(dados.getCpfOuCnpj()))) &&
                            anoSinistro == dataInicio.getYear() - 1) {
                        temSinistro = true;
                        break;
                    }
                }
            }
        }

        if (!temSinistro) {
            BigDecimal novoBonus = valorPremio.multiply(new BigDecimal("0.3")).setScale(2, RoundingMode.HALF_UP);
            if (isCpf && seguradoPessoa != null) {
                seguradoPessoa.creditarBonus(novoBonus);
                daoSegPes.alterar(seguradoPessoa);
            } else if (seguradoEmpresa != null) {
                seguradoEmpresa.creditarBonus(novoBonus);
                daoSegEmp.alterar(seguradoEmpresa);
            }
        }

        return new RetornoInclusaoApolice(numeroApolice, null);
    }

    public Apolice buscarApolice(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return null;
        }
        return daoApo.buscar(numero);
    }

    public String excluirApolice(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return "Número deve ser informado";
        }
        Apolice apolice = daoApo.buscar(numero);
        if (apolice == null) {
            return "Apólice inexistente";
        }
        Sinistro[] sinistros = daoSin.buscarTodos();
        if (sinistros != null) {
            int anoApolice = apolice.getDataInicioVigencia().getYear();
            Veiculo veiculoApolice = apolice.getVeiculo();
            for (Sinistro sinistro : sinistros) {
                if (sinistro != null) {
                    LocalDateTime dataSinistro = sinistro.getDataHoraSinistro();
                    Veiculo veiculoSinistro = sinistro.getVeiculo();
                    if (dataSinistro != null && veiculoSinistro != null &&
                            dataSinistro.getYear() == anoApolice &&
                            veiculoSinistro.equals(veiculoApolice)) {
                        return "Existe sinistro cadastrado para o veículo em questão e no mesmo ano da apólice";
                    }
                }
            }
        }
        daoApo.excluir(numero);
        return null;
    }

    private String validarTodosDadosVeiculo(DadosVeiculo dados) {
        if (dados == null) {
            return "Dados do veículo devem ser informados";
        }
        if (dados.getCpfOuCnpj() == null || dados.getCpfOuCnpj().trim().isEmpty()) {
            return "CPF ou CNPJ deve ser informado";
        }
        String cpfCnpj = dados.getCpfOuCnpj().trim();
        if (cpfCnpj.length() <= 11) {
            if (!ValidadorCpfCnpj.ehCpfValido(cpfCnpj)) {
                return "CPF inválido";
            }
        } else {
            if (!ValidadorCpfCnpj.ehCnpjValido(cpfCnpj)) {
                return "CNPJ inválido";
            }
        }
        if (dados.getPlaca() == null || dados.getPlaca().trim().isEmpty()) {
            return "Placa do veículo deve ser informada";
        }
        int ano = dados.getAno();
        if (ano < 2020 || ano > 2025) {
            return "Ano tem que estar entre 2020 e 2025, incluindo estes";
        }
        if (dados.getValorMaximoSegurado() == null) {
            return "Valor máximo segurado deve ser informado";
        }
        int codigoCat = dados.getCodigoCategoria();
        if (codigoCat < 1 || codigoCat > 3) {
            return "Categoria inválida";
        }
        // Verificar existência de CPF/CNPJ no cadastro antes da faixa de valor
        String msgExist = validarCpfCnpjValorMaximo(dados);
        if (msgExist != null) {
            return msgExist;
        }
        BigDecimal valorMaximoBaseado = obterValorMaximoPermitido(ano, codigoCat);
        BigDecimal limiteInferior = valorMaximoBaseado.multiply(new BigDecimal("0.75")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal limiteSuperior = valorMaximoBaseado;
        if (dados.getValorMaximoSegurado().compareTo(limiteInferior) < 0 ||
                dados.getValorMaximoSegurado().compareTo(limiteSuperior) > 0) {
            return "Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria";
        }
        return null;
    }

    private String validarCpfCnpjValorMaximo(DadosVeiculo dados) {
        String cpfCnpj = dados.getCpfOuCnpj().trim();
        if (cpfCnpj.length() <= 11) {
            SeguradoPessoa seguradoPessoa = daoSegPes.buscar(cpfCnpj);
            if (seguradoPessoa == null) {
                return "CPF inexistente no cadastro de pessoas";
            }
        } else {
            SeguradoEmpresa seguradoEmpresa = daoSegEmp.buscar(cpfCnpj);
            if (seguradoEmpresa == null) {
                return "CNPJ inexistente no cadastro de empresas";
            }
        }
        return null;
    }

    private BigDecimal obterValorMaximoPermitido(int ano, int codigoCat) {
        BigDecimal valorBase;
        switch (codigoCat) {
            case 1:
                valorBase = new BigDecimal("50000.00");
                break;
            case 2:
                valorBase = new BigDecimal("60000.00");
                break;
            case 3:
                valorBase = new BigDecimal("80000.00");
                break;
            default:
                return BigDecimal.ZERO;
        }
        int anoAtual = LocalDate.now().getYear();
        int anosDeUso = anoAtual - ano;
        BigDecimal fatorDepreciacao = BigDecimal.ONE.subtract(new BigDecimal("0.05").multiply(new BigDecimal(anosDeUso)));
        return valorBase.multiply(fatorDepreciacao).setScale(2, RoundingMode.HALF_UP);
    }
}
