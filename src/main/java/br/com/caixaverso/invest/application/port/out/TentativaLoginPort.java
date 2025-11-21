package br.com.caixaverso.invest.application.port.out;

public interface TentativaLoginPort {
    void registrarFalha(String chave);
    void limpar(String chave);
    boolean estaBloqueado(String chave);
}

