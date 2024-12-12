package ua.yutin.CurrencyRates.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.yutin.CurrencyRates.caches.AssetsCache;
import ua.yutin.CurrencyRates.caches.CurrenciesRatesCache;
import ua.yutin.CurrencyRates.exceptions.AssetNotCreatedException;
import ua.yutin.CurrencyRates.models.Asset;
import ua.yutin.CurrencyRates.models.AssetEntity;
import ua.yutin.CurrencyRates.providers.IExchangeProvider;
import ua.yutin.CurrencyRates.repositories.AssetsRepository;


import java.util.List;


@Service
public class AssetsService {
    private final AssetsRepository assetsRepository;

    private AssetsCache assetsCache;
    private CurrenciesRatesCache currenciesRatesCache;

    @Autowired
    public AssetsService(AssetsRepository assetsRepository, AssetsCache assetsCache, CurrenciesRatesCache currenciesRatesCache) {
        this.assetsRepository = assetsRepository;
        this.assetsCache = assetsCache;
        this.currenciesRatesCache = currenciesRatesCache;
    }


    public Asset addAsset(Asset asset) {
        if (!assetsCache.getSupportedAssets().contains(asset.getName())) {
            throw new AssetNotCreatedException("Provided not supported asset with name: '" + asset.getName() + "'");
        }

        if (assetsRepository.findByName(asset.getName()) != null) {
            throw new AssetNotCreatedException("Asset with name: '" + asset.getName() + "' already exists");
        }

        Asset addedAsset = assetsCache.addAsset(asset);
        //update after adding - to get fresh rates
        currenciesRatesCache.updateRates();

        return addedAsset;
    }


    public List<AssetEntity> getAllAssets() {
        return assetsCache.getRegisteredAssets().values().stream().map(asset -> new AssetEntity(asset.getName())).toList();
    }
}
