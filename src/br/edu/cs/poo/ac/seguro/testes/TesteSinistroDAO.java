package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;

public class TesteSinistroDAO extends TesteDAO {
    private SinistroDAO dao = new SinistroDAO();

    protected Class getClasse() {
        return Sinistro.class;
    }

    @Test
    public void teste01() {
        String numero = "00000000";
        cadastro.incluir(new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(),
                "usuarioTeste", BigDecimal.valueOf(5000.0), TipoSinistro.COLISAO), numero);
        Sinistro sinistro = dao.buscar(numero);
        Assertions.assertNotNull(sinistro);
    }

    @Test
    public void teste02() {
        String numero = "10000000";
        cadastro.incluir(new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(),
                "usuarioTeste", BigDecimal.valueOf(5000.0), TipoSinistro.COLISAO), numero);
        Sinistro sinistro = dao.buscar("11000000");
        Assertions.assertNull(sinistro);
    }

    @Test
    public void teste03() {
        String numero = "20000000";
        cadastro.incluir(new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(),
                "usuarioTeste", BigDecimal.valueOf(5000.0), TipoSinistro.COLISAO), numero);
        boolean ret = dao.excluir(numero);
        Assertions.assertTrue(ret);
    }

    @Test
    public void teste04() {
        String numero = "30000000";
        cadastro.incluir(new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(),
                "usuarioTeste", BigDecimal.valueOf(5000.0), TipoSinistro.COLISAO), numero);
        boolean ret = dao.excluir("31000000");
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste05() {
        String numero = "40000000";
        boolean ret = dao.incluir(new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(),
                "usuarioTeste", BigDecimal.valueOf(5000.0), TipoSinistro.COLISAO));
        Assertions.assertTrue(ret);
        Sinistro sinistro = dao.buscar(numero);
        Assertions.assertNotNull(sinistro);
    }

    @Test
    public void teste06() {
        String numero = "50000000";
        Sinistro sinistro = new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(),
                "usuarioTeste", BigDecimal.valueOf(5000.0), TipoSinistro.COLISAO);
        cadastro.incluir(sinistro, numero);
        boolean ret = dao.incluir(sinistro);
        Assertions.assertFalse(ret);
    }

    @Test
    public void teste07() {
        String numero = "60000000";
        boolean ret = dao.alterar(new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(),
                "usuarioTeste", BigDecimal.valueOf(5000.0), TipoSinistro.COLISAO));
        Assertions.assertFalse(ret);
        Sinistro sinistro = dao.buscar(numero);
        Assertions.assertNull(sinistro);
    }

    @Test
    public void teste08() {
        String numero = "70000000";
        Sinistro sinistro = new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(),
                "usuarioTeste", BigDecimal.valueOf(5000.0), TipoSinistro.COLISAO);
        cadastro.incluir(sinistro, numero);
        sinistro = new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(),
                "usuarioTeste2", BigDecimal.valueOf(7000.0), TipoSinistro.FURTO);
        boolean ret = dao.alterar(sinistro);
        Assertions.assertTrue(ret);
    }
}