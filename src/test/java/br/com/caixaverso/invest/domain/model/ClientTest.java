package br.com.caixaverso.invest.domain.model;

import br.com.caixaverso.invest.infra.persistence.entity.Cliente;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void deveCriarClienteComBuilderEGetters() {
        LocalDate dataNascimento = LocalDate.of(1990, 5, 10);
        LocalDateTime dataCriacao = LocalDateTime.of(2024, 1, 1, 10, 0);

        Cliente cliente = Cliente.builder()
                .id(1L)
                .nome("João da Silva")
                .documento("12345678900")
                .email("joao@teste.com")
                .rendaMensal(new BigDecimal("5000.00"))
                .dataNascimento(dataNascimento)
                .dataCriacao(dataCriacao)
                .build();

        assertEquals(1L, cliente.getId());
        assertEquals("João da Silva", cliente.getNome());
        assertEquals("12345678900", cliente.getDocumento());
        assertEquals("joao@teste.com", cliente.getEmail());
        assertEquals(new BigDecimal("5000.00"), cliente.getRendaMensal());
        assertEquals(dataNascimento, cliente.getDataNascimento());
        assertEquals(dataCriacao, cliente.getDataCriacao());
    }

    @Test
    void devePermitirAlterarCamposComSetters() {
        LocalDate dataNascimento = LocalDate.of(1985, 3, 20);
        LocalDateTime dataCriacao = LocalDateTime.of(2023, 12, 31, 23, 59);

        Cliente cliente = new Cliente();
        cliente.setId(2L);
        cliente.setNome("Maria");
        cliente.setDocumento("98765432100");
        cliente.setEmail("maria@teste.com");
        cliente.setRendaMensal(new BigDecimal("8000.00"));
        cliente.setDataNascimento(dataNascimento);
        cliente.setDataCriacao(dataCriacao);

        assertEquals(2L, cliente.getId());
        assertEquals("Maria", cliente.getNome());
        assertEquals("98765432100", cliente.getDocumento());
        assertEquals("maria@teste.com", cliente.getEmail());
        assertEquals(new BigDecimal("8000.00"), cliente.getRendaMensal());
        assertEquals(dataNascimento, cliente.getDataNascimento());
        assertEquals(dataCriacao, cliente.getDataCriacao());
    }

    @Test
    void equalsEDesHashCodeDevemSerVerdadeiroParaObjetosComMesmosDados() {
        Cliente c1 = Cliente.builder()
                .id(1L)
                .nome("João")
                .documento("123")
                .email("joao@teste.com")
                .rendaMensal(new BigDecimal("1000.00"))
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();

        Cliente c2 = Cliente.builder()
                .id(1L)
                .nome("João")
                .documento("123")
                .email("joao@teste.com")
                .rendaMensal(new BigDecimal("1000.00"))
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();

        // mesmo objeto
        assertEquals(c1, c1);

        // objetos diferentes com mesmos dados
        assertEquals(c1, c2);
        assertEquals(c2, c1);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void equalsDeveRetornarFalseQuandoAlgumCampoDiferir() {
        Cliente base = Cliente.builder()
                .id(1L)
                .nome("João")
                .documento("123")
                .email("joao@teste.com")
                .rendaMensal(new BigDecimal("1000.00"))
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();

        // muda somente o id
        Cliente diferente = Cliente.builder()
                .id(2L)
                .nome("João")
                .documento("123")
                .email("joao@teste.com")
                .rendaMensal(new BigDecimal("1000.00"))
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();

        assertNotEquals(base, diferente);
        // hashCodes provavelmente diferentes (não é obrigação, mas em geral serão)
        assertNotEquals(base.hashCode(), diferente.hashCode());
    }

    @Test
    void equalsDeveRetornarFalseParaNullEOutraClasse() {
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nome("João")
                .documento("123")
                .email("joao@teste.com")
                .rendaMensal(new BigDecimal("1000.00"))
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();

        assertNotEquals(null, cliente);
        assertNotEquals("alguma-string", cliente);
    }

    @Test
    void toStringDeveConterCamposPrincipais() {
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nome("João")
                .documento("123")
                .email("joao@teste.com")
                .rendaMensal(new BigDecimal("1000.00"))
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 0, 0))
                .build();

        String str = cliente.toString();

        assertNotNull(str);
        assertTrue(str.contains("João"));
        assertTrue(str.contains("123"));
        assertTrue(str.contains("joao@teste.com"));
    }
}
