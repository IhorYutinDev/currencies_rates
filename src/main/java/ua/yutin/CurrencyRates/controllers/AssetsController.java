package ua.yutin.CurrencyRates.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ua.yutin.CurrencyRates.entities.ErrorResponse;
import ua.yutin.CurrencyRates.exceptions.AssetNotCreatedException;
import ua.yutin.CurrencyRates.models.Asset;

import ua.yutin.CurrencyRates.entities.AssetEntity;
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

    @Operation(summary = "Add a new asset", description = "Adds a new asset to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid asset data provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/add")
    public ResponseEntity<Asset> addAsset(@RequestBody Asset asset) {
        return new ResponseEntity<>(assetsService.addAsset(asset), HttpStatus.OK);
    }

    @Operation(summary = "Get all assets", description = "Fetches all assets from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of assets"),
            @ApiResponse(responseCode = "404", description = "No assets found")
    })
    @GetMapping("/get")
    public ResponseEntity<List<AssetEntity>> getAssets() {
        return new ResponseEntity<>(assetsService.getAllAssets(), HttpStatus.OK);
    }

    @Operation(summary = "Handle exceptions", description = "Handles exception when adding asset")
    @ApiResponse(responseCode = "409", description = "Asset creation failed due to conflict")
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(AssetNotCreatedException e) {
        return new ResponseEntity<>(new ErrorResponse("Failed to add asset. Error: " + e.getMessage()), HttpStatus.CONFLICT);
    }
}
