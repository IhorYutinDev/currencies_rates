package ua.yutin.CurrencyRates.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssetDTO {
    @NotNull(message = "Field 'currency' should be not null")
    @NotEmpty(message = "Field 'currency' should be not empty")
    private String currency;
}
