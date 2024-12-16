package ua.yutin.CurrencyRates.dtos;

import lombok.Data;

import java.util.Map;


@Data
public class CurrencyResponse {
    private boolean success;
    private Map<String, String> symbols;
}
