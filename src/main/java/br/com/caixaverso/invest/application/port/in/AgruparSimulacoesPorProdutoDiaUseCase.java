package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.SimulacaoPorProdutoDiaDTO;

import java.util.List;

import br.com.caixaverso.invest.application.dto.PageResponse;

public interface AgruparSimulacoesPorProdutoDiaUseCase {
    PageResponse<SimulacaoPorProdutoDiaDTO> agrupamentoPorProdutoDia(int page, int size);
}