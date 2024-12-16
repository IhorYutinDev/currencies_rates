package ua.yutin.CurrencyRates.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.yutin.CurrencyRates.AssetsRepository;
import ua.yutin.CurrencyRates.caches.AssetsCache;
import ua.yutin.CurrencyRates.caches.CurrenciesRatesCache;
import ua.yutin.CurrencyRates.models.Asset;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AssetsServiceTest {
    @InjectMocks
    private AssetsService assetsService;

    @Mock
    private AssetsRepository assetsRepository;

    @Mock
    private AssetsCache assetsCache;

    @Mock
    private CurrenciesRatesCache currenciesRatesCache;

    public AssetsServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAssetTest() {
        Asset asset = new Asset("USD");

        when(assetsRepository.save(any(Asset.class))).thenReturn(asset);
        when(assetsCache.getSupportedAssets()).thenReturn(new HashSet<>(List.of("USD")));
        when(assetsCache.addAsset(asset)).thenReturn(asset);

        Asset createdAsset = assetsService.addAsset(asset);

        assertThat(createdAsset).isNotNull();
        assertThat(createdAsset.getName()).isEqualTo("USD");
    }

    @Test
    void getAllAssetsTest() {
        Map<Integer, Asset> regAssets = new HashMap<>();
        regAssets.put(2, new Asset("JPY"));
        regAssets.put(3, new Asset("RON"));
        regAssets.put(4, new Asset("USD"));

        when(assetsCache.getRegisteredAssets()).thenReturn(regAssets);
        when(assetsCache.getSupportedAssets()).thenReturn(new HashSet<>(List.of("USD", "JPY", "RON")));

        List<Asset> allAssets = assetsService.getAllAssets();

        assertThat(allAssets).isNotNull();
        assertThat(allAssets.size()).isEqualTo(3);
    }
}