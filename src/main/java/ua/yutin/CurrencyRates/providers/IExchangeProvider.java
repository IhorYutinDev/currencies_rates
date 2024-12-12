package ua.yutin.CurrencyRates.providers;

import ua.yutin.CurrencyRates.models.Asset;

import java.util.Map;
import java.util.Set;

public interface IExchangeProvider {
    Set<String> getSupportedCurrencies();
    Map<Asset, Double> getExchangeRates(Set<Asset> names);
}
