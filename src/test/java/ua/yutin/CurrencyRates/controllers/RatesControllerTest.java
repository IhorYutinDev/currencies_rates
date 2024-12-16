package ua.yutin.CurrencyRates.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import ua.yutin.CurrencyRates.dtos.RateDTO;
import ua.yutin.CurrencyRates.models.Rate;
import ua.yutin.CurrencyRates.services.RatesService;
import ua.yutin.CurrencyRates.utils.RatesMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RatesController.class)
public class RatesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class RatesServiceTestConfig {
        @Bean
        public RatesService ratesService() {
            return Mockito.mock(RatesService.class);
        }

        @Bean
        public RatesMapper ratesMapper() {
            return Mockito.mock(RatesMapper.class);
        }
    }

    @Autowired
    private RatesService ratesService;

    @Autowired
    private RatesMapper ratesMapper;


    @BeforeEach
    public void setUp() {
        Rate rate = new Rate(2.0, 1);
        when(ratesService.getRate("USD")).thenReturn(rate);

        RateDTO dto = new RateDTO("EUR", "USD", 2.0, 0.5);
        when(ratesMapper.mapToDTO(rate)).thenReturn(dto);
    }

    @Test
    public void testGetRates() throws Exception {
        mockMvc.perform(get("/rest/rates/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base_currency").value("EUR"))
                .andExpect(jsonPath("$.changed_currency").value("USD"))
                .andExpect(jsonPath("$.rate").value(2.0))
                .andExpect(jsonPath("$.inverse_rate").value(0.5));
    }
}

