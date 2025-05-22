package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Registro;
import java.io.Serializable;

public abstract class DAOGenerico<T extends Registro> {
    private CadastroObjetos cadastro;

    public DAOGenerico() {
        cadastro = new CadastroObjetos(getClasseEntidade());
    }

    // Método abstrato que deve ser implementado pelas subclasses
    public abstract Class getClasseEntidade();

    public T buscar(String id) {
        return (T) cadastro.buscar(id);
    }

    public boolean incluir(T registro) {
        if (buscar(registro.getIdUnico()) != null) {
            return false;
        } else {
            cadastro.incluir(registro, registro.getIdUnico());
            return true;
        }
    }

    public boolean alterar(T registro) {
        if (buscar(registro.getIdUnico()) == null) {
            return false;
        } else {
            cadastro.alterar(registro, registro.getIdUnico());
            return true;
        }
    }

    public boolean excluir(String id) {
        if (buscar(id) == null) {
            return false;
        } else {
            cadastro.excluir(id);
            return true;
        }
    }

    public T[] buscarTodos() {
        Serializable[] objs = cadastro.buscarTodos();
        if (objs == null) {
            return null;
        }

        T[] registros = (T[]) java.lang.reflect.Array.newInstance(getClasseEntidade(), objs.length);
        for (int i = 0; i < objs.length; i++) {
            registros[i] = (T) objs[i];
        }
        return registros;
    }
}