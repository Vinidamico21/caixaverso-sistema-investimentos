package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.PerfilRiscoResponseDTO;

public interface CalcularPerfilRiscoUseCase {

    PerfilRiscoResponseDTO calcularPerfil(Long clienteId);

}

