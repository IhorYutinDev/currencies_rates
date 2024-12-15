package ua.yutin.CurrencyRates.utils;

import org.springframework.stereotype.Component;
import ua.yutin.CurrencyRates.dtos.AssetDTO;
import ua.yutin.CurrencyRates.models.Asset;

@Component
public class AssetsMapper {
    public Asset mapToAsset(AssetDTO assetDTO) {
        return new Asset(assetDTO.getCurrency());
    }

    public AssetDTO mapToAssetDTO(Asset asset) {
        return new AssetDTO(asset.getName());
    }
}
