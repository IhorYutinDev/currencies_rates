package ua.yutin.CurrencyRates.exceptions;

public class AssetNotCreatedException extends RuntimeException {
    public AssetNotCreatedException(String message) {
        super(message);
    }
}
