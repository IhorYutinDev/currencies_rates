package ua.yutin.CurrencyRates.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.yutin.CurrencyRates.caches.AssetsCache;
import ua.yutin.CurrencyRates.caches.CurrenciesRatesCache;
import ua.yutin.CurrencyRates.models.Asset;
import ua.yutin.CurrencyRates.models.Rate;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.Mockito.when;

public class RatesServiceTest {
    @InjectMocks
    private RatesService ratesService;

    @Mock
    private CurrenciesRatesCache currenciesRatesCache;

    @Mock
    private AssetsCache assetsCache;


    public RatesServiceTest() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getRateTest() {

        Asset asset = new Asset("USD");
        asset.setId(4);

        when(assetsCache.getAsset("USD")).thenReturn(asset);
        when(currenciesRatesCache.getRate("USD")).thenReturn(new Rate(88.8, 4));

        Rate rate = ratesService.getRate("USD");

        assertThat(rate).isNotNull();
        assertThat(rate.getAssetId()).isEqualTo(4);
        assertThat(rate.getValue()).isEqualTo(88.8);
    }
}
