package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaMediator {
    private static SeguradoPessoaMediator instancia;
    private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
    private SeguradoPessoaDAO seguradoPessoaDAO = new SeguradoPessoaDAO();

    private SeguradoPessoaMediator() {
    }

    public static SeguradoPessoaMediator getInstancia() {
        if (instancia == null) {
            instancia = new SeguradoPessoaMediator();
        }
        return instancia;
    }

    public String validarCpf(String cpf) {
        if (StringUtils.ehNuloOuBranco(cpf)) {
            return "CPF deve ser informado";
        }

        if (cpf.length() != 11) {
            return "CPF deve ter 11 caracteres";
        }

        if (!ValidadorCpfCnpj.ehCpfValido(cpf)) {
            return "CPF com dígito inválido";
        }

        return null;
    }

    public String validarRenda(double renda) {
        if (renda < 0) {
            return "Renda deve ser maior ou igual à zero";
        }

        return null;
    }

    public String validarSeguradoPessoa(SeguradoPessoa seg) {
        // Validação dos atributos da superclasse
        String msgErro = seguradoMediator.validarNome(seg.getNome());
        if (msgErro != null) {
            return msgErro;
        }

        msgErro = seguradoMediator.validarEndereco(seg.getEndereco());
        if (msgErro != null) {
            return msgErro;
        }

        msgErro = seguradoMediator.validarDataCriacao(seg.getDataNascimento());
        if (msgErro != null) {
            return "Data do nascimento deve ser informada";
        }

        // Validação dos atributos específicos
        msgErro = validarCpf(seg.getCpf());
        if (msgErro != null) {
            return msgErro;
        }

        msgErro = validarRenda(seg.getRenda());
        if (msgErro != null) {
            return msgErro;
        }

        return null;
    }

    public String incluirSeguradoPessoa(SeguradoPessoa seg) {
        String msgErro = validarSeguradoPessoa(seg);
        if (msgErro != null) {
            return msgErro;
        }

        SeguradoPessoa segExistente = seguradoPessoaDAO.buscar(seg.getCpf());
        if (segExistente != null) {
            return "CPF do segurado pessoa já existente";
        }

        boolean ret = seguradoPessoaDAO.incluir(seg);
        if (!ret) {
            return "Erro ao incluir segurado pessoa";
        }

        return null;
    }

    public String alterarSeguradoPessoa(SeguradoPessoa seg) {
        String msgErro = validarSeguradoPessoa(seg);
        if (msgErro != null) {
            return msgErro;
        }

        SeguradoPessoa segExistente = seguradoPessoaDAO.buscar(seg.getCpf());
        if (segExistente == null) {
            return "CPF do segurado pessoa não existente";
        }

        boolean ret = seguradoPessoaDAO.alterar(seg);
        if (!ret) {
            return "Erro ao alterar segurado pessoa";
        }

        return null;
    }

    public String excluirSeguradoPessoa(String cpf) {
        SeguradoPessoa segExistente = seguradoPessoaDAO.buscar(cpf);
        if (segExistente == null) {
            return "CPF do segurado pessoa não existente";
        }

        boolean ret = seguradoPessoaDAO.excluir(cpf);
        if (!ret) {
            return "Erro ao excluir segurado pessoa";
        }

        return null;
    }

    public SeguradoPessoa buscarSeguradoPessoa(String cpf) {
        return seguradoPessoaDAO.buscar(cpf);
    }
}