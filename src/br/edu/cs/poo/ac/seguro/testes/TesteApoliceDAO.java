package br.edu.cs.poo.ac.seguro.testes;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;

public class TesteApoliceDAO extends TesteDAO {
    private ApoliceDAO dao = new ApoliceDAO();

    @Override
    protected Class getClasse() {
        return Apolice.class;
    }
    @org.junit.Test
    @Test
    public void teste01() {
        String numero = "00000000";
        Apolice apolice = new Apolice(LocalDate.now(), LocalDate.now().plusYears(1), 1000.0, null, null);
        apolice.setNumero(numero);
        cadastro.incluir(apolice, numero);
        Apolice apol = dao.buscar(numero);
        Assertions.assertNotNull(apol);
    }

    @Test
    public void teste02() {
        String numero = "10000000";
        Apolice apolice = new Apolice(LocalDate.now(), LocalDate.now().plusYears(1), 2000.0, null, null);
        apolice.setNumero(numero);
        cadastro.incluir(apolice, numero);
        Apolice apol = dao.buscar("11000000");
        Assertions.assertNull(apol);
    }

    @Test
    public void teste03() {
        String numero = "20000000";
        Apolice apolice = new Apolice(LocalDate.now(), LocalDate.now().plusYears(1), 3000.0, null, null);
        apolice.setNumero(numero);
        cadastro.incluir(apolice, numero);
        boolean ret = dao.excluir(numero);
        Assertions.assertTrue(ret);
    }

    @Test
    public void teste04() {
        String numero = "30000000";
        Apolice apolice = new Apolice(LocalDate.now(), LocalDate.now().plusYears(1), 4000.0, null, null);
        apolice.setNumero(numero);
        cadastro.incluir(apolice, numero);
        boolean ret = dao.excluir("31000000");
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste05() {
        String numero = "40000000";
        Apolice apolice = new Apolice(LocalDate.now(), LocalDate.now().plusYears(1), 5000.0, null, null);
        apolice.setNumero(numero);
        boolean ret = dao.incluir(apolice);
        Assertions.assertTrue(ret);
        Apolice apol = dao.buscar(numero);
        Assertions.assertNotNull(apol);
    }

    @Test
    public void teste06() {
        String numero = "50000000";
        Apolice apolice = new Apolice(LocalDate.now(), LocalDate.now().plusYears(1), 6000.0, null, null);
        apolice.setNumero(numero);
        cadastro.incluir(apolice, numero);
        boolean ret = dao.incluir(apolice);
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste07() {
        String numero = "60000000";
        Apolice apolice = new Apolice(LocalDate.now(), LocalDate.now().plusYears(1), 7000.0, null, null);
        apolice.setNumero(numero);
        boolean ret = dao.alterar(apolice);
        Assertions.assertFalse(ret);
        Apolice apol = dao.buscar(numero);
        Assertions.assertNull(apol);
    }

    @Test
    public void teste08() {
        String numero = "70000000";
        Apolice apolice = new Apolice(LocalDate.now(), LocalDate.now().plusYears(1), 8000.0, null, null);
        apolice.setNumero(numero);
        cadastro.incluir(apolice, numero);
        apolice = new Apolice(LocalDate.now(), LocalDate.now().plusYears(2), 9000.0, null, null);
        apolice.setNumero(numero);
        boolean ret = dao.alterar(apolice);
        Assertions.assertTrue(ret);
    }
}