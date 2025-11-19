package br.com.caixaverso.invest.application.dto;

import br.com.caixaverso.invest.application.dto.request.SimularInvestimentoRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SimularInvestimentoRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // -------------------------------------------------------
    //  Testes padrões do DTO
    // -------------------------------------------------------

    @Test
    void testNoArgsConstructor() {
        SimularInvestimentoRequest dto = new SimularInvestimentoRequest();

        assertNotNull(dto);
        assertNull(dto.getClienteId());
        assertNull(dto.getValor());
        assertNull(dto.getPrazoMeses());
        assertNull(dto.getTipoProduto());
    }

    @Test
    void testAllArgsConstructor() {
        SimularInvestimentoRequest dto = new SimularInvestimentoRequest(
                1L,
                new BigDecimal("1000"),
                12,
                "CDB"
        );

        assertEquals(1L, dto.getClienteId());
        assertEquals(new BigDecimal("1000"), dto.getValor());
        assertEquals(12, dto.getPrazoMeses());
        assertEquals("CDB", dto.getTipoProduto());
    }

    @Test
    void testBuilder() {
        SimularInvestimentoRequest dto = SimularInvestimentoRequest.builder()
                .clienteId(55L)
                .valor(new BigDecimal("2500"))
                .prazoMeses(6)
                .tipoProduto("Tesouro Direto")
                .build();

        assertEquals(55L, dto.getClienteId());
        assertEquals(new BigDecimal("2500"), dto.getValor());
        assertEquals(6, dto.getPrazoMeses());
        assertEquals("Tesouro Direto", dto.getTipoProduto());
    }

    @Test
    void testGettersAndSetters() {
        SimularInvestimentoRequest dto = new SimularInvestimentoRequest();

        dto.setClienteId(77L);
        dto.setValor(new BigDecimal("5000"));
        dto.setPrazoMeses(24);
        dto.setTipoProduto("LCI");

        assertEquals(77L, dto.getClienteId());
        assertEquals(new BigDecimal("5000"), dto.getValor());
        assertEquals(24, dto.getPrazoMeses());
        assertEquals("LCI", dto.getTipoProduto());
    }

    @Test
    void testEqualsAndHashCode() {
        SimularInvestimentoRequest dto1 = new SimularInvestimentoRequest(
                1L, new BigDecimal("1000"), 12, "CDB"
        );

        SimularInvestimentoRequest dto2 = new SimularInvestimentoRequest(
                1L, new BigDecimal("1000"), 12, "CDB"
        );

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SimularInvestimentoRequest dto = SimularInvestimentoRequest.builder()
                .clienteId(1L)
                .valor(new BigDecimal("1000"))
                .prazoMeses(12)
                .tipoProduto("CDB")
                .build();

        String str = dto.toString();

        assertTrue(str.contains("1"));
        assertTrue(str.contains("1000"));
        assertTrue(str.contains("12"));
        assertTrue(str.contains("CDB"));
    }

    // -------------------------------------------------------
    //  Testes de VALIDAÇÃO REAL (Hibernate Validator)
    // -------------------------------------------------------

    @Test
    void testValidationFailsWhenFieldsNullOrInvalid() {

        SimularInvestimentoRequest dto = new SimularInvestimentoRequest(
                null,                   // clienteId → inválido
                new BigDecimal("-10"),  // valor negativo → inválido
                -5,                     // prazoMeses negativo → inválido
                null                    // tipoProduto → inválido
        );

        Set<ConstraintViolation<SimularInvestimentoRequest>> violations =
                validator.validate(dto);

        assertEquals(4, violations.size());
    }

    @Test
    void testValidationPassesWithValidData() {

        SimularInvestimentoRequest dto = new SimularInvestimentoRequest(
                10L,
                new BigDecimal("2000"),
                24,
                "Tesouro Selic"
        );

        Set<ConstraintViolation<SimularInvestimentoRequest>> violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}

