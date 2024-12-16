package ua.yutin.CurrencyRates.services;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.yutin.CurrencyRates.caches.AssetsCache;
import ua.yutin.CurrencyRates.caches.CurrenciesRatesCache;
import ua.yutin.CurrencyRates.exceptions.AssetNotCreatedException;
import ua.yutin.CurrencyRates.models.Asset;
import ua.yutin.CurrencyRates.AssetsRepository;


import java.util.List;

@Log4j2
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
        if (assetsCache.getSupportedAssets().isEmpty()){
            assetsCache.updateSupportedAssets();
        }

        if (!assetsCache.getSupportedAssets().contains(asset.getName())) {
            log.warn("Failed to add asset. Provided not supported currency: {}", asset.getName());
            throw new AssetNotCreatedException("Provided not supported asset with name: '" + asset.getName() + "'");
        }

        if (assetsCache.getAsset(asset.getName()) != null) {
            log.warn("Failed to add asset. Provided already exists currency: {}", asset.getName());
            throw new AssetNotCreatedException("Asset with name: '" + asset.getName() + "' already exists");
        }

        Asset addedAsset = assetsCache.addAsset(asset);
        //update after adding - to get fresh rates
        currenciesRatesCache.updateRates();

        log.info("Added asset: {}", addedAsset);
        return addedAsset;
    }


    public List<Asset> getAllAssets() {
        return assetsCache.getRegisteredAssets().values().stream().toList();
    }
}
