package br.com.caixaverso.invest.domain.port;


import br.com.caixaverso.invest.domain.model.PesosRecomendacao;
import java.util.List;


public interface PesosRecomendacaoPort {
    List<PesosRecomendacao> listarPorPerfil(String perfil);
}