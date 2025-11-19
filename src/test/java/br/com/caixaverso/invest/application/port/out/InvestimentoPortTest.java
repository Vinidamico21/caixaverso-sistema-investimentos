package br.com.caixaverso.invest.application.port.out;

import br.com.caixaverso.invest.infra.persistence.entity.Investimento;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvestimentoPortTest {

    @Test
    void testInterfaceExists() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.InvestimentoPort"
        );

        assertTrue(clazz.isInterface(),
                "InvestimentoPort deve ser uma interface");
    }

    @Test
    void testMethod_findByClienteId_Signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.InvestimentoPort"
        );

        Method method = clazz.getMethod("findByClienteId", Long.class);

        assertEquals(List.class, method.getReturnType(),
                "findByClienteId deve retornar List<Investimento>");

        assertEquals(1, method.getParameterCount());
        assertEquals(Long.class, method.getParameterTypes()[0]);
    }

    @Test
    void testMethod_findByClienteIdAndPeriodo_Signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.InvestimentoPort"
        );

        Method method = clazz.getMethod(
                "findByClienteIdAndPeriodo",
                Long.class,
                LocalDate.class
        );

        assertEquals(List.class, method.getReturnType(),
                "findByClienteIdAndPeriodo deve retornar List<Investimento>");

        assertEquals(2, method.getParameterCount());
        assertEquals(Long.class, method.getParameterTypes()[0]);
        assertEquals(LocalDate.class, method.getParameterTypes()[1]);
    }

    @Test
    void testMethod_save_Signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.InvestimentoPort"
        );

        Method method = clazz.getMethod("save", Investimento.class);

        assertEquals(Investimento.class, method.getReturnType(),
                "save deve retornar Investimento");

        assertEquals(1, method.getParameterCount());
        assertEquals(Investimento.class, method.getParameterTypes()[0]);
    }

    @Test
    void testMethod_findAll_Signature() throws Exception {
        Class<?> clazz = Class.forName(
                "br.com.caixaverso.invest.application.port.out.InvestimentoPort"
        );

        Method method = clazz.getMethod("findAll");

        assertEquals(List.class, method.getReturnType(),
                "findAll deve retornar List<Investimento>");

        assertEquals(0, method.getParameterCount(),
                "findAll não deve ter parâmetros");
    }
}

