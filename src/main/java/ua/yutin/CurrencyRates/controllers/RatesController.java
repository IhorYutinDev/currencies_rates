package ua.yutin.CurrencyRates.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.yutin.CurrencyRates.dtos.ErrorResponse;
import ua.yutin.CurrencyRates.exceptions.RatesNotFoundException;
import ua.yutin.CurrencyRates.dtos.RateDTO;

import ua.yutin.CurrencyRates.services.RatesService;
import ua.yutin.CurrencyRates.utils.RatesMapper;

@RestController
@RequestMapping("/rest/rates")
public class RatesController {
    private final RatesService ratesService;
    private final RatesMapper ratesMapper;

    @Autowired
    public RatesController(RatesService ratesService, RatesMapper ratesMapper) {
        this.ratesService = ratesService;
        this.ratesMapper = ratesMapper;
    }

    @Operation(summary = "Get exchange rate for a specific currency",
            description = "Returns the exchange rate for the given currency.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the exchange rate")
    @ApiResponse(responseCode = "400", description = "Invalid currency code")
    @ApiResponse(responseCode = "404", description = "Currency not found")
    @GetMapping("/{currency}")
    public ResponseEntity<RateDTO> get(@PathVariable String currency) {
        return ResponseEntity.status(HttpStatus.OK).body(ratesMapper.mapToDTO(ratesService.getRate(currency)));
    }


    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(RatesNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("Failed to get rates. Error: " + e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
