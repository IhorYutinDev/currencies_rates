package ua.yutin.CurrencyRates.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.yutin.CurrencyRates.entities.ErrorResponse;
import ua.yutin.CurrencyRates.exceptions.RatesNotFoundException;
import ua.yutin.CurrencyRates.entities.RateEntity;
import ua.yutin.CurrencyRates.services.RatesService;

@RestController
@RequestMapping("/rest/rates")
public class RatesController {
    private final RatesService ratesService;

    @Autowired
    public RatesController(RatesService ratesService) {
        this.ratesService = ratesService;
    }


    @GetMapping("/get/{currency}")
    public ResponseEntity<RateEntity> addSensor(@PathVariable String currency) {
        return ResponseEntity.status(HttpStatus.OK).body(ratesService.getRate(currency));
    }


    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(RatesNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("Failed to get rates. Error: " + e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
