package ua.yutin.CurrencyRates.dtos;

import lombok.Data;

import java.util.Map;


@Data
public class CurrencyResponse {
    private boolean success;

    private Map<String, String> symbols;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, String> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, String> symbols) {
        this.symbols = symbols;
    }
}
