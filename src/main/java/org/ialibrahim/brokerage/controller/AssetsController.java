package org.ialibrahim.brokerage.controller;

import lombok.RequiredArgsConstructor;
import org.ialibrahim.brokerage.model.Asset;
import org.ialibrahim.brokerage.service.AssetsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetsController {

    private final AssetsService assetsService;

    @GetMapping
    public ResponseEntity<List<Asset>> listAssetsByCustomer(@RequestParam Long customerId) {

        List<Asset> assets = assetsService.listAssets(customerId);
        return new ResponseEntity<>(assets, HttpStatus.OK);
    }

}
