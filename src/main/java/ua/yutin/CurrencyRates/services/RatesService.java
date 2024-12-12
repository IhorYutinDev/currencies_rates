package ua.yutin.CurrencyRates.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ua.yutin.CurrencyRates.caches.AssetsCache;
import ua.yutin.CurrencyRates.caches.CurrenciesRatesCache;
import ua.yutin.CurrencyRates.exceptions.RatesNotFoundException;
import ua.yutin.CurrencyRates.models.Asset;
import ua.yutin.CurrencyRates.models.Rate;
import ua.yutin.CurrencyRates.models.RateEntity;
import ua.yutin.CurrencyRates.providers.IExchangeProvider;
import ua.yutin.CurrencyRates.repositories.RatesRepository;

import java.util.Optional;

@Service
public class RatesService {
    private CurrenciesRatesCache currenciesRatesCache;
    private AssetsCache assetsCache;

    @Value("${exchange.base_currency}")
    private String baseCurrency;


    @Autowired
    public RatesService(CurrenciesRatesCache currenciesRatesCache, AssetsCache assetsCache) {
        this.currenciesRatesCache = currenciesRatesCache;
        this.assetsCache = assetsCache;
    }

    public RateEntity getRate(String name) {
        Asset asset = assetsCache.getAsset(name);
        if (asset == null) {
            throw new RatesNotFoundException("Currency with name: '" + name + "' is not registered");
        }

        Rate rate = currenciesRatesCache.getRate(name);
        if (rate != null) {
            return new RateEntity(baseCurrency, asset.getName(), 1 / rate.getValue(), rate.getValue());
        } else {
            throw new RatesNotFoundException("Not found rates for currency with name: '" + name + "'");
        }
    }
}
