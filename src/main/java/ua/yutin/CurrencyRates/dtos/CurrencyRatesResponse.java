package ua.yutin.CurrencyRates.dtos;

import lombok.Data;

import java.util.Map;

@Data
public class CurrencyRatesResponse {
    private boolean success;
    private long timestamp;
    private String base;
    private String date;
    private Map<String, Double> rates;
}
