package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Sinistro;

public class SinistroDAO extends DAOGenerico<Sinistro> {
    @Override
    public Class getClasseEntidade() {
        return Sinistro.class;
    }
}