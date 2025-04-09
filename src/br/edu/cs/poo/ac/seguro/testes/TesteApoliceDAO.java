package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;
import java.lang.String;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;

public class TesteApoliceDAO extends TesteDAO {
    private ApoliceDAO dao = new ApoliceDAO();

    protected Class getClasse() {
        return Apolice.class;
    }

    @Test
    public void teste01() {
        String numero = "00000000";
        cadastro.incluir(new Apolice(numero,null, BigDecimal.valueOf(1000.0),
                BigDecimal.valueOf(5000.0), BigDecimal.valueOf(50000.0)), numero);
        Apolice apolice = dao.buscar(numero);
        Assertions.assertNotNull(apolice);
    }

    @Test
    public void teste02() {
        String numero = "10000000";
        cadastro.incluir(new Apolice(numero, null, BigDecimal.valueOf(1000.0),
                BigDecimal.valueOf(5000.0), BigDecimal.valueOf(50000.0)), numero);
        Apolice apolice = dao.buscar("11000000");
        Assertions.assertNull(apolice);
    }

    @Test
    public void teste03() {
        String numero = "20000000";
        cadastro.incluir(new Apolice(numero, null, BigDecimal.valueOf(1000.0),
                BigDecimal.valueOf(5000.0), BigDecimal.valueOf(50000.0)), numero);
        boolean ret = dao.excluir(numero);
        Assertions.assertTrue(ret);
    }

    @Test
    public void teste04() {
        String numero = "30000000";
        cadastro.incluir(new Apolice(numero, null, BigDecimal.valueOf(1000.0),
                BigDecimal.valueOf(5000.0), BigDecimal.valueOf(50000.0)), numero);
        boolean ret = dao.excluir("31000000");
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste05() {
        String numero = "40000000";
        boolean ret = dao.incluir(new Apolice(numero, null, BigDecimal.valueOf(1000.0),
                BigDecimal.valueOf(5000.0), BigDecimal.valueOf(50000.0)));
        Assertions.assertTrue(ret);
        Apolice apolice = dao.buscar(numero);
        Assertions.assertNotNull(apolice);
    }

    @Test
    public void teste06() {
        String numero = "50000000";
        Apolice apolice = new Apolice(numero, null, BigDecimal.valueOf(1000.0),
                BigDecimal.valueOf(5000.0), BigDecimal.valueOf(50000.0));
        cadastro.incluir(apolice, numero);
        boolean ret = dao.incluir(apolice);
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste07() {
        String numero = "60000000";
        boolean ret = dao.alterar(new Apolice(numero, null, BigDecimal.valueOf(1000.0),
                BigDecimal.valueOf(5000.0), BigDecimal.valueOf(50000.0)));
        Assertions.assertFalse(ret);
        Apolice apolice = dao.buscar(numero);
        Assertions.assertNull(apolice);
    }

    @Test
    public void teste08() {
        String numero = "70000000";
        Apolice apolice = new Apolice(numero, null, BigDecimal.valueOf(1000.0),
                BigDecimal.valueOf(5000.0), BigDecimal.valueOf(50000.0));
        cadastro.incluir(apolice, numero);
        apolice = new Apolice(numero, null, BigDecimal.valueOf(2000.0),
                BigDecimal.valueOf(6000.0), BigDecimal.valueOf(60000.0));
        boolean ret = dao.alterar(apolice);
        Assertions.assertTrue(ret);
    }
}