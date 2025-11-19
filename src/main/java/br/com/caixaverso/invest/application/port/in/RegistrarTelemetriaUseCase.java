package br.com.caixaverso.invest.application.port.in;

public interface RegistrarTelemetriaUseCase {

    void registrar(
            Long clienteId,
            String endpoint,
            String metodoHttp,
            boolean sucesso,
            int statusHttp,
            int duracaoMs
    );

}