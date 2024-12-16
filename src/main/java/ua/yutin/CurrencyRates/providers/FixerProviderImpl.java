package ua.yutin.CurrencyRates.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ua.yutin.CurrencyRates.caches.AssetsCache;
import ua.yutin.CurrencyRates.dtos.CurrencyRatesResponse;
import ua.yutin.CurrencyRates.dtos.CurrencyResponse;
import ua.yutin.CurrencyRates.models.Asset;


import java.util.*;
import java.util.stream.Collectors;

@Component
public class FixerProviderImpl implements IExchangeProvider {
    private final AssetsCache assetsCache;
    @Value("${exchange.access_key}")
    private String fixerAccessKey;

    @Value("${exchange.api_url}")
    private String baseExchangeApiUrl;

    @Value("${exchange.base_currency}")
    private String baseCurrency;

    private final RestTemplate restTemplate;

    private static final String GET_SUPPORTED_SYMBOLS_PATH = "/symbols?access_key=";

    @Autowired
    public FixerProviderImpl(RestTemplate restTemplate, @Lazy AssetsCache assetsCache) {
        this.restTemplate = restTemplate;
        this.assetsCache = assetsCache;
    }


    @Override
    public Set<String> getSupportedCurrencies() {
        String url = baseExchangeApiUrl + GET_SUPPORTED_SYMBOLS_PATH + fixerAccessKey;

        CurrencyResponse response = restTemplate.getForObject(url, CurrencyResponse.class);
        if (response != null && response.isSuccess()) {
            return response.getSymbols().keySet();
        }

        return new HashSet<>();
    }

    @Override
    public Map<Asset, Double> getExchangeRates(Set<Asset> assets) {
        String namesString = assets.stream().map(Asset::getName).collect(Collectors.joining(","));

        CurrencyRatesResponse ratesResponse = restTemplate.getForObject(
                baseExchangeApiUrl + "/api/latest?access_key=" +
                        fixerAccessKey + "&base=" + baseCurrency + "&symbols=" + namesString,
                CurrencyRatesResponse.class);

        if (ratesResponse == null) {
            return new HashMap<>();
        }

        if (ratesResponse.isSuccess()) {
            return ratesResponse.getRates().entrySet()
                    .stream()
                    .map(entry -> {
                        Asset asset = assetsCache.getAsset(entry.getKey());
                        return asset == null ? null : Map.entry(asset, entry.getValue());
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        return new HashMap<>();
    }
}
