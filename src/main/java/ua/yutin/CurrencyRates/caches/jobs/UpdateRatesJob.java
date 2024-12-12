package ua.yutin.CurrencyRates.caches.jobs;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class UpdateRatesJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Runnable updater = (Runnable) context.getMergedJobDataMap().get("rates_updater");
        updater.run();
    }
}
