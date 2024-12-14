package ua.yutin.CurrencyRates.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Currency Rates API",
                version = "1.0",
                description = "API for currency management"
        )
)
public class SwaggerConfig {
}
