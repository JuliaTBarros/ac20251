package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Segurado;

public abstract class SeguradoDAO<T extends Segurado> extends DAOGenerico<T> {
    // A classe abstrata já herda todos os métodos necessários do DAOGenerico
    // e especifica que trabalhará com subtipos de Segurado
}