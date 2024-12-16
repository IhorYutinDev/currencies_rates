package ua.yutin.CurrencyRates.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.yutin.CurrencyRates.dtos.AssetDTO;
import ua.yutin.CurrencyRates.models.Asset;
import ua.yutin.CurrencyRates.services.AssetsService;
import ua.yutin.CurrencyRates.utils.AssetsMapper;


import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AssetsController.class, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AssetsMapper.class))
@Import(AssetsControllerTest.MockAssetsServiceConfig.class)
public class AssetsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AssetsService assetsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AssetsMapper assetsMapper;

    private AssetDTO dto;
    private Asset asset;

    @BeforeEach
    void setUp() {
        dto = new AssetDTO("USD");
        asset = new Asset("USD");
        when(assetsService.addAsset(any(Asset.class))).thenReturn(asset);
    }

    @Test
    void addAssetTest() throws Exception {
        when(assetsService.addAsset(asset)).thenReturn(asset); // Логика сервиса

        mockMvc.perform(post("/rest/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))) // Передача корректного JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void getAllAssetsTest() throws Exception {
        when(assetsService.getAllAssets()).thenReturn(Arrays.asList(new Asset("USD"), new Asset("EUR")));

        mockMvc.perform(get("/rest/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[1].currency").value("EUR"));
    }

    @TestConfiguration
    static class MockAssetsServiceConfig {
        @Bean
        public AssetsService assetsService() {
            return Mockito.mock(AssetsService.class);
        }
    }
}
