package ua.yutin.CurrencyRates.exceptions;

public class RatesNotFoundException extends RuntimeException {
    public RatesNotFoundException(String message) {
        super(message);
    }
}
