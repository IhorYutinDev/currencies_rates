package ua.yutin.CurrencyRates.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ua.yutin.CurrencyRates.entities.ErrorResponse;
import ua.yutin.CurrencyRates.exceptions.AssetNotCreatedException;
import ua.yutin.CurrencyRates.models.Asset;

import ua.yutin.CurrencyRates.models.AssetEntity;
import ua.yutin.CurrencyRates.services.AssetsService;


import java.util.List;

@RestController()
@RequestMapping("/rest/assets")
public class AssetsController {
    private final AssetsService assetsService;

    @Autowired
    public AssetsController(AssetsService assetsService) {
        this.assetsService = assetsService;
    }


    @PostMapping("/add")
    public ResponseEntity<Asset> addAsset(@RequestBody Asset asset) {
        return new ResponseEntity<>(assetsService.addAsset(asset), HttpStatus.OK);
    }


    @GetMapping("/get")
    public ResponseEntity<List<AssetEntity>> getAssets() {
        return new ResponseEntity<>(assetsService.getAllAssets(), HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(AssetNotCreatedException e) {
        return new ResponseEntity<>(new ErrorResponse("Failed to add asset. Error: " + e.getMessage()), HttpStatus.CONFLICT);
    }
}
