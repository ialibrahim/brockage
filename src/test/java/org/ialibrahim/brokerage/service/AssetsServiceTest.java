package org.ialibrahim.brokerage.service;

import org.ialibrahim.brokerage.dao.AssetRepository;
import org.ialibrahim.brokerage.entity.AssetEntity;
import org.ialibrahim.brokerage.exception.InvalidOperationException;
import org.ialibrahim.brokerage.model.Asset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class AssetsServiceTest {

    @Mock
    private AssetRepository assetRepository;

    private ModelMapper modelMapper = new ModelMapper();

    private AssetsService assetsService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        assetsService = new AssetsService(assetRepository, modelMapper);
    }

    @Test
    void testListAssets() {
        // Given
        Long customerId = 1L;
        AssetEntity assetEntity = new AssetEntity(1L, customerId, "TRY", 100.0, 100.0);
        Asset asset = new Asset();
        when(assetRepository.findByCustomerId(anyLong())).thenReturn(Arrays.asList(assetEntity));

        // When
        List<Asset> result = assetsService.listAssets(customerId);

        // Then
        assertEquals(1, result.size());
    }

    @Test
    void testDeposite() {
        // Given
        Long customerId = 1L;
        Double amount = 50.0;
        AssetEntity existingAsset = new AssetEntity(1L, customerId, AssetsService.TRY_ASSETS, 100.0, 100.0);
        AssetEntity updatedAsset = new AssetEntity(1L, customerId, AssetsService.TRY_ASSETS, 150.0, 150.0);
        Asset asset = new Asset();
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), any(String.class)))
                .thenReturn(Optional.of(existingAsset));
        when(assetRepository.save(any(AssetEntity.class))).thenReturn(updatedAsset);

        // When
        Asset result = assetsService.deposite(customerId, amount);

        // Then
        assertEquals(150.0, result.getSize());
    }

    @Test
    void testWithdraw() {
        // Given
        Long customerId = 1L;
        Double amount = 50.0;
        AssetEntity existingAsset = new AssetEntity(1L, customerId, AssetsService.TRY_ASSETS, 100.0, 100.0);
        AssetEntity updatedAsset = new AssetEntity(1L, customerId, AssetsService.TRY_ASSETS, 50.0, 50.0);
        Asset asset = new Asset();
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), any(String.class)))
                .thenReturn(Optional.of(existingAsset));
        when(assetRepository.save(any(AssetEntity.class))).thenReturn(updatedAsset);

        // When
        Asset result = assetsService.withdraw(customerId, amount);

        // Then
        assertEquals(50.0, result.getSize());
    }

    @Test
    void testWithdrawThrowsInvalidOperationException() {
        // Given
        Long customerId = 1L;
        Double amount = 50.0;
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), any(String.class)))
                .thenReturn(Optional.empty());

        // When
        InvalidOperationException thrown = assertThrows(InvalidOperationException.class,
                () -> assetsService.withdraw(customerId, amount));

        // Then
        assertEquals("Customer has no TRY assets", thrown.getMessage());
    }

    @Test
    void testGetUsableSize() {
        // Given
        Long customerId = 1L;
        String assetName = "TRY";
        Double usableSize = 100.0;
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), any(String.class)))
                .thenReturn(Optional.of(new AssetEntity(1L, customerId, assetName, 100.0, usableSize)));

        // When
        Double result = assetsService.getUsableSize(customerId, assetName);

        // Then
        assertEquals(usableSize, result);
    }

    @Test
    void testGetUsableFund() {
        // Given
        Long customerId = 1L;
        Double usableFund = 100.0;
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), any(String.class)))
                .thenReturn(Optional.of(new AssetEntity(1L, customerId, AssetsService.TRY_ASSETS, 100.0, usableFund)));

        // When
        Double result = assetsService.getUsableFund(customerId);

        // Then
        assertEquals(usableFund, result);
    }
}

