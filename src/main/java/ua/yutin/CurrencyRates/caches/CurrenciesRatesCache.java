package ua.yutin.CurrencyRates.caches;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.yutin.CurrencyRates.models.Asset;
import ua.yutin.CurrencyRates.models.Rate;
import ua.yutin.CurrencyRates.providers.IExchangeProvider;
import ua.yutin.CurrencyRates.RatesRepository;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@Component
public class CurrenciesRatesCache {
    private Map<Asset, Rate> currenciesRates;
    private final transient ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();

    private final RatesRepository ratesRepository;
    private final IExchangeProvider exchangeProvider;
    private final AssetsCache assetsCache;


    @Autowired
    public CurrenciesRatesCache(RatesRepository ratesRepository, IExchangeProvider exchangeProvider, AssetsCache assetsCache) {
        this.ratesRepository = ratesRepository;
        this.exchangeProvider = exchangeProvider;
        this.assetsCache = assetsCache;
    }


    @PostConstruct
    public void init() {
        loadCache();
        updateRates();
    }


    public void loadCache() {
        ReentrantReadWriteLock.WriteLock writeLock = this.cacheLock.writeLock();
        writeLock.lock();
        try {
            currenciesRates = ratesRepository
                    .findAll()
                    .stream()
                    .collect(Collectors.toMap(rate -> assetsCache.getRegisteredAssets().get(rate.getAssetId()), Function.identity()));
        } finally {
            writeLock.unlock();
        }
    }


    @Scheduled(cron = "${exchange.cron_expression}")
    public void updateRates() {
        ReentrantReadWriteLock.WriteLock writeLock = this.cacheLock.writeLock();
        writeLock.lock();
        try {
            //update rates only for exists assets
            if (assetsCache.getRegisteredAssets().isEmpty()) {
                log.debug("No registered currencies found. Updating has been rejected");
                return;
            }

            Map<Asset, Double> currenciesRatesFromExchange = new HashMap<>(exchangeProvider.getExchangeRates(new HashSet<>(assetsCache.getRegisteredAssets().values())));

            currenciesRatesFromExchange.forEach((asset, rate) -> {
                if (currenciesRates.containsKey(asset)) {
                    Rate existingRate = currenciesRates.get(asset);
                    existingRate.setValue(rate);
                    ratesRepository.save(existingRate);
                } else {
                    Rate rateNew = new Rate(rate, asset.getId());
                    Rate addedRate = ratesRepository.save(rateNew);
                    currenciesRates.put(asset, addedRate);
                }
            });
            log.info("Currencies rates successfully updated");
        } finally {
            writeLock.unlock();
        }
    }

    public Rate getRate(String name) {
        ReentrantReadWriteLock.ReadLock readLock = this.cacheLock.readLock();

        readLock.lock();
        try {
            Asset asset = assetsCache.getRegisteredAssets().values().stream().filter(a -> a.getName().equals(name)).findFirst().orElse(null);
            if (asset != null) {
                return currenciesRates.get(asset);
            }

            return null;
        } finally {
            readLock.unlock();
        }
    }
}
