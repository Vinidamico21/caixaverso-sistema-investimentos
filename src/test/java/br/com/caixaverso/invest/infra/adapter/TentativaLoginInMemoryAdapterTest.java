package br.com.caixaverso.invest.infra.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TentativaLoginInMemoryAdapterTest {

    private TentativaLoginInMemoryAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new TentativaLoginInMemoryAdapter();
    }

    private Map<String, ?> getTentativasInternas() {
        try {
            Field field = TentativaLoginInMemoryAdapter.class.getDeclaredField("tentativas");
            field.setAccessible(true);
            return (Map<String, ?>) field.get(adapter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // -------------------------------------------------------------------------
    // TESTE 1 — Falha única NÃO bloqueia
    // -------------------------------------------------------------------------
    @Test
    void testRegistrarFalhaNaoBloqueiaComMenosDe5Tentativas() {
        String chave = "user";

        adapter.registrarFalha(chave);

        assertFalse(adapter.estaBloqueado(chave));
    }

    // -------------------------------------------------------------------------
    // TESTE 2 — Após 5 falhas, deve bloquear
    // -------------------------------------------------------------------------
    @Test
    void testBloqueiaAposCincoTentativas() {
        String chave = "user";

        for (int i = 0; i < 5; i++) adapter.registrarFalha(chave);

        assertTrue(adapter.estaBloqueado(chave));
    }

    // -------------------------------------------------------------------------
    // TESTE 3 — Limpar deve remover tentativas e desbloquear
    // -------------------------------------------------------------------------
    @Test
    void testLimparDesbloqueia() {
        String chave = "user";

        for (int i = 0; i < 5; i++) adapter.registrarFalha(chave);

        assertTrue(adapter.estaBloqueado(chave));

        adapter.limpar(chave);

        assertFalse(adapter.estaBloqueado(chave));
        assertNull(getTentativasInternas().get(chave));
    }

    // -------------------------------------------------------------------------
    // TESTE 4 — Expiração do bloqueio deve liberar login
    // -------------------------------------------------------------------------
    @Test
    void testBloqueioExpiraEAcessoEhLiberado() throws Exception {
        String chave = "user";

        // registra 5 falhas → bloqueado
        for (int i = 0; i < 5; i++) adapter.registrarFalha(chave);
        assertTrue(adapter.estaBloqueado(chave));

        // manipula "ultimaFalha" via reflection para simular tempo passado
        Map<String, ?> tentativas = getTentativasInternas();
        Object info = tentativas.get(chave);

        Field ultimaFalhaField = info.getClass().getDeclaredField("ultimaFalha");
        ultimaFalhaField.setAccessible(true);

        // coloca ultima falha como 10 minutos atrás → expiração automática
        Instant dezMinutosAtras = Instant.now().minusSeconds(600);
        ultimaFalhaField.set(info, dezMinutosAtras);

        // Agora deve liberar
        assertFalse(adapter.estaBloqueado(chave));
    }

    // -------------------------------------------------------------------------
    // TESTE 5 — Tentativa inicial deve criar entrada no mapa
    // -------------------------------------------------------------------------
    @Test
    void testRegistrarFalhaCriaEntradaNoMapa() {
        String chave = "user";

        adapter.registrarFalha(chave);

        Map<String, ?> tentativas = getTentativasInternas();
        assertTrue(tentativas.containsKey(chave));
    }

    // -------------------------------------------------------------------------
    // TESTE 6 — Registrar falha incrementa contador
    // -------------------------------------------------------------------------
    @Test
    void testRegistrarFalhaIncrementaContador() throws Exception {
        String chave = "user";

        adapter.registrarFalha(chave);
        adapter.registrarFalha(chave);

        Map<String, ?> tentativas = getTentativasInternas();
        Object info = tentativas.get(chave);

        Field tentativasField = info.getClass().getDeclaredField("tentativas");
        tentativasField.setAccessible(true);

        int contador = tentativasField.getInt(info);
        assertEquals(2, contador);
    }

    // -------------------------------------------------------------------------
    // TESTE 7 — Chave inexistente nunca está bloqueada
    // -------------------------------------------------------------------------
    @Test
    void testChaveInexistenteNuncaBloqueia() {
        assertFalse(adapter.estaBloqueado("chave-que-nao-existe"));
    }

    // -------------------------------------------------------------------------
    // TESTE 8 — Ao expirar o bloqueio, o registro é removido do mapa
    // -------------------------------------------------------------------------
    @Test
    void testRegistroRemovidoAposExpiracao() throws Exception {
        String chave = "user";

        for (int i = 0; i < 5; i++) adapter.registrarFalha(chave);

        Map<String, ?> tentativas = getTentativasInternas();
        Object info = tentativas.get(chave);

        Field ultimaFalhaField = info.getClass().getDeclaredField("ultimaFalha");
        ultimaFalhaField.setAccessible(true);

        // força expiração (10 minutos atrás)
        ultimaFalhaField.set(info, Instant.now().minusSeconds(600));

        // deve desbloquear E remover do mapa
        assertFalse(adapter.estaBloqueado(chave));
        assertFalse(getTentativasInternas().containsKey(chave));
    }
}

