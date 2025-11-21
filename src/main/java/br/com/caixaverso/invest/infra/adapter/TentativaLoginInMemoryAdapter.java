package br.com.caixaverso.invest.infra.adapter;

import br.com.caixaverso.invest.application.port.out.TentativaLoginPort;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class TentativaLoginInMemoryAdapter implements TentativaLoginPort {

    private static final int MAX_TENTATIVAS = 5;
    private static final long BLOQUEIO_SEGUNDOS = 300;

    private final Map<String, TentativaInfo> tentativas = new ConcurrentHashMap<>();

    @Override
    public void registrarFalha(String chave) {
        TentativaInfo info = tentativas.getOrDefault(chave, new TentativaInfo());
        info.tentativas++;
        info.ultimaFalha = Instant.now();
        tentativas.put(chave, info);
    }

    @Override
    public void limpar(String chave) {
        tentativas.remove(chave);
    }

    @Override
    public boolean estaBloqueado(String chave) {
        TentativaInfo info = tentativas.get(chave);

        if (info == null) return false;

        if (info.tentativas < MAX_TENTATIVAS) return false;

        long segundosDesdeUltimaFalha = Instant.now().getEpochSecond() - info.ultimaFalha.getEpochSecond();

        if (segundosDesdeUltimaFalha > BLOQUEIO_SEGUNDOS) {
            tentativas.remove(chave);
            return false;
        }

        return true;
    }

    private static class TentativaInfo {
        int tentativas = 0;
        Instant ultimaFalha;
    }
}

