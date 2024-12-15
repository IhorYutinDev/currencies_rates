package ua.yutin.CurrencyRates.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import ua.yutin.CurrencyRates.dtos.ErrorResponse;
import ua.yutin.CurrencyRates.exceptions.AssetNotCreatedException;
import ua.yutin.CurrencyRates.models.Asset;

import ua.yutin.CurrencyRates.dtos.AssetDTO;
import ua.yutin.CurrencyRates.services.AssetsService;
import ua.yutin.CurrencyRates.utils.AssetsMapper;


import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/rest/assets")
public class AssetsController {
    private final AssetsService assetsService;
    private final AssetsMapper assetsMapper;

    @Autowired
    public AssetsController(AssetsService assetsService, AssetsMapper assetsMapper) {
        this.assetsService = assetsService;
        this.assetsMapper = assetsMapper;
    }

    @Operation(summary = "Add a new asset", description = "Adds a new asset to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid asset data provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping()
    public ResponseEntity add(@RequestBody @Valid AssetDTO assetDTO, BindingResult bindingResult) throws AssetNotCreatedException {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return new ResponseEntity<>(new ErrorResponse("Validation failed: " + errors), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(toAssetDTO(assetsService.addAsset(toAsset(assetDTO))), HttpStatus.OK);
    }

    @Operation(summary = "Get all assets", description = "Fetches all assets from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of assets")})
    @GetMapping()
    public ResponseEntity<List<AssetDTO>> getAll() {
        return new ResponseEntity<>(assetsService.getAllAssets()
                .stream()
                .map(this::toAssetDTO)
                .toList(), HttpStatus.OK);
    }

    @Operation(summary = "Handle exceptions", description = "Handles exception when adding asset")
    @ApiResponse(responseCode = "409", description = "Asset creation failed due to conflict")
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(AssetNotCreatedException e) {
        return new ResponseEntity<>(new ErrorResponse("Failed to add asset. Error: " + e.getMessage()), HttpStatus.CONFLICT);
    }

    private Asset toAsset(AssetDTO assetDTO) {
        return assetsMapper.mapToAsset(assetDTO);
    }

    private AssetDTO toAssetDTO(Asset asset) {
        return assetsMapper.mapToAssetDTO(asset);
    }
}
