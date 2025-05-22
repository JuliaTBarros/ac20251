package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaDAO extends SeguradoDAO<SeguradoPessoa> {
    @Override
    public Class getClasseEntidade() {
        return SeguradoPessoa.class;
    }

    // Método específico de busca que usa o método buscar da superclasse
    public SeguradoPessoa buscar(String cpf) {
        return super.buscar(cpf);
    }
}