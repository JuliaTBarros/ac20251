package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaMediator {
    private static SeguradoEmpresaMediator instancia;
    private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
    private SeguradoEmpresaDAO seguradoEmpresaDAO = new SeguradoEmpresaDAO();

    private SeguradoEmpresaMediator() {
    }

    public static SeguradoEmpresaMediator getInstancia() {
        if (instancia == null) {
            instancia = new SeguradoEmpresaMediator();
        }
        return instancia;
    }

    public String validarCnpj(String cnpj) {
        if (StringUtils.ehNuloOuBranco(cnpj)) {
            return "CNPJ deve ser informado";
        }

        if (cnpj.length() != 14) {
            return "CNPJ deve ter 14 caracteres";
        }

        if (!ValidadorCpfCnpj.ehCnpjValido(cnpj)) {
            return "CNPJ com dígito inválido";
        }

        return null;
    }

    public String validarFaturamento(double faturamento) {
        if (faturamento <= 0) {
            return "Faturamento deve ser maior que zero";
        }

        return null;
    }

    public String validarSeguradoEmpresa(SeguradoEmpresa seg) {
        String msgErro = seguradoMediator.validarNome(seg.getNome());
        if (msgErro != null) {
            return msgErro;
        }

        msgErro = seguradoMediator.validarEndereco(seg.getEndereco());
        if (msgErro != null) {
            return msgErro;
        }

        msgErro = seguradoMediator.validarDataCriacao(seg.getDataAbertura());
        if (msgErro != null) {
            return "Data da abertura deve ser informada";
        }

        msgErro = validarCnpj(seg.getCnpj());
        if (msgErro != null) {
            return msgErro;
        }

        msgErro = validarFaturamento(seg.getFaturamento());
        if (msgErro != null) {
            return msgErro;
        }

        return null;
    }

    public String incluirSeguradoEmpresa(SeguradoEmpresa seg) {
        String msgErro = validarSeguradoEmpresa(seg);
        if (msgErro != null) {
            return msgErro;
        }

        SeguradoEmpresa segExistente = seguradoEmpresaDAO.buscar(seg.getCnpj());
        if (segExistente != null) {
            return "CNPJ do segurado empresa já existente";
        }

        boolean ret = seguradoEmpresaDAO.incluir(seg);
        if (!ret) {
            return "Erro ao incluir segurado empresa";
        }

        return null;
    }

    public String alterarSeguradoEmpresa(SeguradoEmpresa seg) {
        String msgErro = validarSeguradoEmpresa(seg);
        if (msgErro != null) {
            return msgErro;
        }

        SeguradoEmpresa segExistente = seguradoEmpresaDAO.buscar(seg.getCnpj());
        if (segExistente == null) {
            return "CNPJ do segurado empresa não existente";
        }

        boolean ret = seguradoEmpresaDAO.alterar(seg);
        if (!ret) {
            return "Erro ao alterar segurado empresa";
        }

        return null;
    }

    public String excluirSeguradoEmpresa(String cnpj) {
        SeguradoEmpresa segExistente = seguradoEmpresaDAO.buscar(cnpj);
        if (segExistente == null) {
            return "CNPJ do segurado empresa não existente";
        }

        boolean ret = seguradoEmpresaDAO.excluir(cnpj);
        if (!ret) {
            return "Erro ao excluir segurado empresa";
        }

        return null;
    }

    public SeguradoEmpresa buscarSeguradoEmpresa(String cnpj) {
        return seguradoEmpresaDAO.buscar(cnpj);
    }
}