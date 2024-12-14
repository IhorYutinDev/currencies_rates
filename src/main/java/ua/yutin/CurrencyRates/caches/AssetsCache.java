package ua.yutin.CurrencyRates.caches;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.yutin.CurrencyRates.models.Asset;
import ua.yutin.CurrencyRates.providers.IExchangeProvider;
import ua.yutin.CurrencyRates.repositories.AssetsRepository;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;


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
    public void reload() {
        ReentrantReadWriteLock.WriteLock writeLock = this.assetsCacheLock.writeLock();

        writeLock.lock();
        try {
            //supportedAssets = exchangeProvider.getSupportedCurrencies();
            registeredAssets = assetsRepository.findAll().stream().collect(Collectors.toMap(Asset::getId, Function.identity()));
        } finally {
            writeLock.unlock();
        }
    }

    public void updateSupportedAssets() {
        supportedAssets = exchangeProvider.getSupportedCurrencies();
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
