package org.ialibrahim.brokerage.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.ialibrahim.brokerage.exception.InvalidOperationException;
import org.ialibrahim.brokerage.model.Asset;
import org.ialibrahim.brokerage.model.Order;
import org.ialibrahim.brokerage.service.AssetsService;
import org.ialibrahim.brokerage.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CustomerControllerTest {

    @Mock
    private AssetsService assetsService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAssets() {
        // Given
        Long customerId = 1L;
        List<Asset> mockAssets = Arrays.asList(new Asset(), new Asset());
        when(assetsService.listAssets(anyLong())).thenReturn(mockAssets);

        // When
        ResponseEntity<List<Asset>> response = customerController.listAssets(customerId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAssets.size(), response.getBody().size());
    }

    @Test
    void testListOrders() {
        // Given
        Long customerId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        List<Order> mockOrders = Arrays.asList(new Order(), new Order());
        when(orderService.listOrders(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(mockOrders);

        // When
        ResponseEntity<List<Order>> response = customerController.listOrders(customerId, startDate, endDate);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrders.size(), response.getBody().size());
    }

    @Test
    void testDepositMoney() {
        // Given
        Long customerId = 1L;
        Double amount = 100.0;
        Asset mockAsset = new Asset();
        when(assetsService.deposite(anyLong(), anyDouble())).thenReturn(mockAsset);

        // When
        ResponseEntity<Asset> response = customerController.depositMoney(customerId, amount);

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(mockAsset, response.getBody());
    }

    @Test
    void testWithdrawMoney() throws BadRequestException {
        // Given
        Long customerId = 1L;
        Double amount = 50.0;
        Asset mockAsset = new Asset();
        when(assetsService.withdraw(anyLong(), anyDouble())).thenReturn(mockAsset);

        // When
        ResponseEntity<Asset> response = customerController.withdrawMoney(customerId, amount);

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(mockAsset, response.getBody());
    }

    @Test
    void testWithdrawMoneyThrowsBadRequestException()  {
        // Given
        Long customerId = 1L;
        Double amount = 100.0;
        when(assetsService.withdraw(anyLong(), anyDouble())).thenThrow(new InvalidOperationException("Invalid amount"));

        // When
        try {
            customerController.withdrawMoney(customerId, amount);
        } catch (InvalidOperationException e) {
            // Then
            assertEquals("Invalid amount", e.getMessage());
        }
    }
}
