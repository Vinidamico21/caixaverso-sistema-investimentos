package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.domain.model.SimulacaoInvestimento;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulacaoInvestimentoPortTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.SimulacaoInvestimentoPort"
        );

        assertTrue(clazz.isInterface(),
                "SimulacaoInvestimentoPort deve ser uma interface");
    }

    @Test
    void testMethod_salvar_Signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.SimulacaoInvestimentoPort"
        );

        Method method = clazz.getMethod("salvar", SimulacaoInvestimento.class);

        assertEquals(SimulacaoInvestimento.class, method.getReturnType(),
                "O método salvar deve retornar SimulacaoInvestimento");

        assertEquals(1, method.getParameterCount(),
                "O método salvar deve receber exatamente 1 parâmetro");

        assertEquals(SimulacaoInvestimento.class, method.getParameterTypes()[0]);
    }

    @Test
    void testMethod_listar_Signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.SimulacaoInvestimentoPort"
        );

        Method method = clazz.getMethod("listar");

        assertEquals(List.class, method.getReturnType(),
                "O método listar deve retornar List<SimulacaoInvestimento>");

        assertEquals(0, method.getParameterCount(),
                "O método listar não deve receber parâmetros");
    }

    @Test
    void testMethod_listarPorClienteId_Signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.SimulacaoInvestimentoPort"
        );

        Method method = clazz.getMethod("listarPorClienteId", Long.class);

        assertEquals(List.class, method.getReturnType(),
                "listarPorClienteId deve retornar List<SimulacaoInvestimento>");

        assertEquals(1, method.getParameterCount(),
                "listarPorClienteId deve receber exatamente 1 parâmetro");

        assertEquals(Long.class, method.getParameterTypes()[0]);
    }
}

