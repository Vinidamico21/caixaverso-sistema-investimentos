package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;
import br.com.caixaverso.invest.application.dto.response.PageResponse;

public interface ListarInvestimentosPorClienteUseCase {

    PageResponse<InvestimentoResponseDTO> listarPorCliente(String clienteIdRaw, int page, int size);

}