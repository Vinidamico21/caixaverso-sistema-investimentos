package br.com.caixaverso.invest.application.port.in;

import br.com.caixaverso.invest.application.dto.AuthRequestDTO;
import br.com.caixaverso.invest.application.dto.AuthResponseDTO;

public interface AuthUseCase {

    AuthResponseDTO autenticar(AuthRequestDTO request);

}