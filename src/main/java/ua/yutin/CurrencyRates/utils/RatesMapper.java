package ua.yutin.CurrencyRates.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.yutin.CurrencyRates.caches.AssetsCache;
import ua.yutin.CurrencyRates.dtos.RateDTO;
import ua.yutin.CurrencyRates.models.Rate;

@Component
public class RatesMapper {
    @Value("${exchange.base_currency}")
    private String baseCurrency;
    private AssetsCache assetsCache;

    @Autowired
    public RatesMapper(AssetsCache assetsCache) {
        this.assetsCache = assetsCache;
    }

    public RateDTO mapToDTO(Rate rate) {
        return new RateDTO(baseCurrency, assetsCache.getAsset(rate.getAssetId()).getName(), 1 / rate.getValue(), rate.getValue());
    }
}
