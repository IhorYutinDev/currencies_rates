package ua.yutin.CurrencyRates.config;

import org.quartz.CronExpression;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;


@Configuration
public class SpringConfig {
    @Value("${exchange.cron_expression}")
    private String updatingTimeCronExpression;

    @Bean
    public CronExpression cronExpression() throws Exception {
        try {
            return new CronExpression(updatingTimeCronExpression);
        } catch (ParseException e) {
            throw new Exception("Provided incorrect cronExpression: '".concat(updatingTimeCronExpression).concat("'"));
        }
    }

    @Bean
    public StdSchedulerFactory schedulerFactory() {
        return new StdSchedulerFactory();
    }

    @Bean
    public Scheduler scheduler(StdSchedulerFactory schedulerFactory) throws SchedulerException {
        return schedulerFactory.getScheduler();
    }
}
