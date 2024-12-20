package ua.yutin.CurrencyRates.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RateDTO {
    @JsonProperty("base_currency")
    private String baseCurrency;

    @JsonProperty("changed_currency")
    private String changedCurrency;

    private double rate;

    @JsonProperty("inverse_rate")
    private double inverseRate;
}
