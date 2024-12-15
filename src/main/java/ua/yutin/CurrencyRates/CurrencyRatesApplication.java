package ua.yutin.CurrencyRates;

import org.modelmapper.ModelMapper;
import org.quartz.CronExpression;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;



@EnableScheduling
@SpringBootApplication
public class CurrencyRatesApplication {
    public static void main(String[] args) {
        SpringApplication.run(CurrencyRatesApplication.class, args);
    }
}
