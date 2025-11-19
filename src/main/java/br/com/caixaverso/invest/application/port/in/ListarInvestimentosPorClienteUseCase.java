package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.InvestimentoResponseDTO;

import java.util.List;

public interface ListarInvestimentosPorClienteUseCase {

    List<InvestimentoResponseDTO> listarPorCliente(String clienteIdRaw);

}