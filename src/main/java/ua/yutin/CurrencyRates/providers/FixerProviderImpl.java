package ua.yutin.CurrencyRates.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.yutin.CurrencyRates.entities.CurrencyRatesResponse;
import ua.yutin.CurrencyRates.entities.CurrencyResponse;
import ua.yutin.CurrencyRates.models.Asset;
import ua.yutin.CurrencyRates.repositories.AssetsRepository;
import ua.yutin.CurrencyRates.utils.RequestsSender;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FixerProviderImpl implements IExchangeProvider {
    @Value("${exchange.access_key}")
    private String fixerAccessKey;

    @Value("${exchange.api_url}")
    private String baseExchangeApiUrl;

    @Value("${exchange.base_currency}")
    private String baseCurrency;

    private final RequestsSender requestsSender;
    private final AssetsRepository assetsRepository;
    //move to properties?
    private static final String GET_SUPPORTED_SYMBOLS_PATH = "/symbols?access_key=";

    @Autowired
    public FixerProviderImpl(RequestsSender requestsSender, AssetsRepository assetsRepository) {
        this.requestsSender = requestsSender;
        this.assetsRepository = assetsRepository;
    }


    @Override
    public Set<String> getSupportedCurrencies() {
        String url = baseExchangeApiUrl + GET_SUPPORTED_SYMBOLS_PATH + fixerAccessKey;

        CurrencyResponse response = requestsSender.getForObject(url, CurrencyResponse.class);
        if (response != null && response.isSuccess()) {
            return response.getSymbols().keySet();
        }

        return new HashSet<>();
    }

    @Override
    public Map<Asset, Double> getExchangeRates(Set<Asset> assets) {
        String namesString = assets.stream().map(Asset::getName).collect(Collectors.joining(","));

        CurrencyRatesResponse ratesResponse = requestsSender.getForObject(
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
                        Asset asset = assetsRepository.findByName(entry.getKey());
                        return asset == null ? null : Map.entry(asset, entry.getValue());
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        return new HashMap<>();
    }
}
