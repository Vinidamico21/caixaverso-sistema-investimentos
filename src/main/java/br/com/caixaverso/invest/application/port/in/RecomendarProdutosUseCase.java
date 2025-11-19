package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.RecomendacaoResponseDTO;
import br.com.caixaverso.invest.application.dto.ProdutoRecomendadoDTO;

import java.util.List;

public interface RecomendarProdutosUseCase {

    RecomendacaoResponseDTO recomendarParaCliente(Long clienteId);

    List<ProdutoRecomendadoDTO> recomendarPorPerfil(String perfilTexto);

}