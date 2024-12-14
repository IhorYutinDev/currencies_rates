package ua.yutin.CurrencyRates.caches;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.yutin.CurrencyRates.caches.jobs.UpdateRatesJob;
import ua.yutin.CurrencyRates.models.Asset;
import ua.yutin.CurrencyRates.models.Rate;
import ua.yutin.CurrencyRates.providers.IExchangeProvider;
import ua.yutin.CurrencyRates.repositories.RatesRepository;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.quartz.JobBuilder.newJob;

@Component
public class CurrenciesRatesCache {
    private Map<Asset, Rate> currenciesRates;
    private final transient ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();

    private final RatesRepository ratesRepository;
    private final IExchangeProvider exchangeProvider;
    private final AssetsCache assetsCache;
    private final Scheduler scheduler;
    private final CronExpression ratesUpdateExpression;
    private JobKey jobKey;


    @Autowired
    public CurrenciesRatesCache(RatesRepository ratesRepository, IExchangeProvider exchangeProvider, AssetsCache assetsCache,
                                Scheduler scheduler, CronExpression ratesUpdateExpression) {

        this.ratesRepository = ratesRepository;
        this.exchangeProvider = exchangeProvider;
        this.assetsCache = assetsCache;
        this.scheduler = scheduler;
        this.ratesUpdateExpression = ratesUpdateExpression;
    }

    @PostConstruct
    public void init() throws Exception {
        loadCache();

        scheduler.start();

        start();
    }



    @PreDestroy
    private void stop() throws SchedulerException {
        scheduler.deleteJob(jobKey);

        scheduler.shutdown();
    }

    public void start() throws SchedulerException {
        updateRates();

        createUpdateRatesJob();
    }

    private void createUpdateRatesJob() throws SchedulerException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("rates_updater", (Runnable) this::updateRates);
        JobDetail jobDetail = newJob(UpdateRatesJob.class)
                .withIdentity("update_rates_job")
                .usingJobData(dataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(ratesUpdateExpression)
                .inTimeZone(TimeZone.getTimeZone("UTC"))).forJob(jobDetail).build();

        jobKey = jobDetail.getKey();
        scheduler.scheduleJob(jobDetail, trigger);
    }


    public void loadCache() {
        ReentrantReadWriteLock.WriteLock writeLock = this.cacheLock.writeLock();
        writeLock.lock();
        try {
            currenciesRates = ratesRepository.findAll().stream()
                    .collect(Collectors.toMap(rate ->
                                    assetsCache.getRegisteredAssets().get(rate.getAssetId()),
                            Function.identity()));
        } finally {
            writeLock.unlock();
        }
    }

    public void updateRates() {
        ReentrantReadWriteLock.WriteLock writeLock = this.cacheLock.writeLock();
        writeLock.lock();
        try {
            //update rates only for exists assets
            if (assetsCache.getRegisteredAssets().isEmpty()) {
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
