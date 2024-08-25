package org.ialibrahim.brokerage.controller;

import org.ialibrahim.brokerage.model.Order;
import org.ialibrahim.brokerage.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        ResponseEntity<List<Order>> response = orderController.listOrders(customerId, startDate, endDate);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrders.size(), response.getBody().size());
    }

    @Test
    void testCreateOrder() {
        // Given
        Order order = new Order();
        Order createdOrder = new Order();
        when(orderService.createOrder(any(Order.class))).thenReturn(createdOrder);

        // When
        ResponseEntity<Order> response = orderController.createOrder(order);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdOrder, response.getBody());
    }

    @Test
    void testDeleteOrder() {
        // Given
        Long orderId = 1L;

        // When
        ResponseEntity<Void> response = orderController.deleteOrder(orderId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

