package ua.yutin.CurrencyRates.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RateEntity {
    @JsonProperty("base-currency")
    private String baseCurrency;

    @JsonProperty("changed-currency")
    private String changedCurrency;

    private double rate;

    @JsonProperty("inverse_rate")
    private double inverseRate;
}
