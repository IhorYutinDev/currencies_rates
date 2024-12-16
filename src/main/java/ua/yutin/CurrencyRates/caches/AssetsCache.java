package ua.yutin.CurrencyRates.caches;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.yutin.CurrencyRates.models.Asset;
import ua.yutin.CurrencyRates.providers.IExchangeProvider;
import ua.yutin.CurrencyRates.repositories.AssetsRepository;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@Component
public class AssetsCache {
    private Set<String> supportedAssets = new HashSet<>();
    private Map<Integer, Asset> registeredAssets = new HashMap<>();
    private final transient ReentrantReadWriteLock assetsCacheLock = new ReentrantReadWriteLock();

    private final AssetsRepository assetsRepository;
    private final IExchangeProvider exchangeProvider;


    @Autowired
    public AssetsCache(AssetsRepository assetsRepository, IExchangeProvider exchangeProvider) {
        this.assetsRepository = assetsRepository;
        this.exchangeProvider = exchangeProvider;
    }

    @PostConstruct
    public void init() {
        reload();
    }

    public void reload() {
        ReentrantReadWriteLock.WriteLock writeLock = this.assetsCacheLock.writeLock();

        writeLock.lock();
        try {
            supportedAssets = exchangeProvider.getSupportedCurrencies();
            log.info("Successfully loaded {} supported assets", supportedAssets.size());

            registeredAssets = assetsRepository.findAll().stream().collect(Collectors.toMap(Asset::getId, Function.identity()));
            log.info("Successfully loaded {} registered assets", registeredAssets.size());
        } finally {
            writeLock.unlock();
        }
    }

    public void updateSupportedAssets() {
        supportedAssets = exchangeProvider.getSupportedCurrencies();
        log.info("Successfully updated supported assets. Supported Currencies are: {}", supportedAssets);
    }

    public Set<String> getSupportedAssets() {
        ReentrantReadWriteLock.ReadLock readLock = this.assetsCacheLock.readLock();

        readLock.lock();
        try {
            return new HashSet<>(supportedAssets);
        } finally {
            readLock.unlock();
        }
    }

    public Asset getAsset(String name) {
        ReentrantReadWriteLock.ReadLock readLock = this.assetsCacheLock.readLock();

        readLock.lock();
        try {
            return registeredAssets.values().stream().filter(asset -> asset.getName().equals(name)).findFirst().orElse(null);
        } finally {
            readLock.unlock();
        }
    }

    public Asset getAsset(int id) {
        ReentrantReadWriteLock.ReadLock readLock = this.assetsCacheLock.readLock();

        readLock.lock();
        try {
            return registeredAssets.values().stream().filter(asset -> asset.getId() == id).findFirst().orElse(null);
        } finally {
            readLock.unlock();
        }
    }

    public Map<Integer, Asset> getRegisteredAssets() {
        ReentrantReadWriteLock.ReadLock readLock = this.assetsCacheLock.readLock();

        readLock.lock();
        try {
            return new HashMap<>(registeredAssets);
        } finally {
            readLock.unlock();
        }
    }

    public Asset addAsset(Asset asset) {
        ReentrantReadWriteLock.WriteLock writeLock = this.assetsCacheLock.writeLock();

        writeLock.lock();
        try {
            Asset addedAsset = assetsRepository.save(asset);
            registeredAssets.put(addedAsset.getId(), addedAsset);

            return addedAsset;
        } finally {
            writeLock.unlock();
        }
    }
}
