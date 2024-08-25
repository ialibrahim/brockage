package org.ialibrahim.brokerage.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.ialibrahim.brokerage.model.Asset;
import org.ialibrahim.brokerage.service.AssetsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AssetsControllerTest {

    @Mock
    private AssetsService assetsService;

    @InjectMocks
    private AssetsController assetsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAssetsByCustomer() {
        // Given
        Long customerId = 1L;
        List<Asset> mockAssets = Arrays.asList(new Asset(), new Asset());
        when(assetsService.listAssets(anyLong())).thenReturn(mockAssets);

        // When
        ResponseEntity<List<Asset>> response = assetsController.listAssetsByCustomer(customerId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAssets.size(), response.getBody().size());
    }

    @Test
    void testListAssetsByCustomerWithEmptyList() {
        // Given
        Long customerId = 1L;
        when(assetsService.listAssets(anyLong())).thenReturn(Arrays.asList());

        // When
        ResponseEntity<List<Asset>> response = assetsController.listAssetsByCustomer(customerId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }
}

