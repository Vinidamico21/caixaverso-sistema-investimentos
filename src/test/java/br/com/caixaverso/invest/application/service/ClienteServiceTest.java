package br.com.caixaverso.invest.application.service;

import br.com.caixaverso.invest.application.port.out.ClientePort;
import br.com.caixaverso.invest.infra.persistence.entity.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    ClientePort clientePort;

    @InjectMocks
    ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setup() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Fulano Teste");
    }

    // ---------------------------------------------------------------------
    // TESTE DO MÉTODO buscarCliente()
    // ---------------------------------------------------------------------

    @Test
    void testBuscarCliente_Sucesso() {
        when(clientePort.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.buscarCliente(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Fulano Teste", result.getNome());

        verify(clientePort, times(1)).findById(1L);
    }

    @Test
    void testBuscarCliente_ClienteNaoEncontrado() {
        when(clientePort.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> clienteService.buscarCliente(1L)
        );

        assertEquals("Cliente não encontrado", ex.getMessage());
        verify(clientePort, times(1)).findById(1L);
    }

    // ---------------------------------------------------------------------
    // TESTE DO MÉTODO existe()
    // ---------------------------------------------------------------------

    @Test
    void testExiste_True() {
        when(clientePort.findById(1L)).thenReturn(Optional.of(cliente));

        boolean exists = clienteService.existe(1L);

        assertTrue(exists);
        verify(clientePort).findById(1L);
    }

    @Test
    void testExiste_False() {
        when(clientePort.findById(1L)).thenReturn(Optional.empty());

        boolean exists = clienteService.existe(1L);

        assertFalse(exists);
        verify(clientePort).findById(1L);
    }
}

