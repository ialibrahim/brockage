package org.ialibrahim.brokerage.service;

import org.ialibrahim.brokerage.dao.OrderRepository;
import org.ialibrahim.brokerage.entity.OrderEntity;
import org.ialibrahim.brokerage.exception.InvalidOperationException;
import org.ialibrahim.brokerage.model.Order;
import org.ialibrahim.brokerage.security.AccessAuthority;
import org.ialibrahim.brokerage.security.User;
import org.ialibrahim.brokerage.type.OrderSide;
import org.ialibrahim.brokerage.type.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetsService assetsService;

    private ModelMapper modelMapper = new ModelMapper();
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, assetsService, modelMapper);
        //SecurityContextHolder.getContext().getAuthentication()

        User applicationUser = new User();
        applicationUser.getAuthorities().add(AccessAuthority.ADMIN);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(applicationUser);
    }

    @Test
    void testCreateOrder() {
        // Given
        Order order = new Order();
        order.setOrderSide(OrderSide.BUY);
        order.setCustomerId(1L);
        order.setAssetName("TRY");
        order.setSize(10.0);
        order.setPrice(100.0);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStatus(OrderStatus.PENDING);

        when(orderRepository.saveAndFlush(any(OrderEntity.class))).thenReturn(orderEntity);
        when(assetsService.getUsableFund(anyLong())).thenReturn(200.0);  // Assumed sufficient fund for the test

        // When
        Order result = orderService.createOrder(order);

        // Then
        assertEquals(OrderStatus.PENDING, result.getStatus());
    }

    @Test
    void testCreateOrderThrowsInvalidOperationExceptionWhenSellingTooMuch() {
        // Given
        Order order = new Order();
        order.setOrderSide(OrderSide.SELL);
        order.setCustomerId(1L);
        order.setAssetName("TRY");
        order.setSize(10.0);
        order.setPrice(100.0);

        when(assetsService.getUsableSize(anyLong(), any(String.class))).thenReturn(5.0);  // Insufficient size for the test

        // When
        InvalidOperationException thrown = assertThrows(InvalidOperationException.class,
                () -> orderService.createOrder(order));

        // Then
        assertEquals("Usable size is less than the requested size", thrown.getMessage());
    }

    @Test
    void testCreateOrderThrowsInvalidOperationExceptionWhenBuyingTooExpensive() {
        // Given
        Order order = new Order();
        order.setOrderSide(OrderSide.BUY);
        order.setCustomerId(1L);
        order.setAssetName("TRY");
        order.setSize(10.0);
        order.setPrice(200.0);  // Price is more than the available fund

        when(assetsService.getUsableFund(anyLong())).thenReturn(100.0);  // Insufficient fund for the test

        // When
        InvalidOperationException thrown = assertThrows(InvalidOperationException.class,
                () -> orderService.createOrder(order));

        // Then
        assertEquals("Usable fund is less than the offered price", thrown.getMessage());
    }

    @Test
    void testListOrders() {
        // Given
        Long customerId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        OrderEntity orderEntity = new OrderEntity();
        Order order = new Order();
        when(orderRepository.findByCustomerIdAndCreateDateBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(orderEntity));

        // When
        List<Order> result = orderService.listOrders(customerId, startDate, endDate);

        // Then
        assertEquals(1, result.size());
    }

    @Test
    void testCancelOrder() {
        // Given
        Long orderId = 1L;
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStatus(OrderStatus.PENDING);

        when(orderRepository.getReferenceById(anyLong())).thenReturn(orderEntity);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        // When
        orderService.cancelOrder(orderId);

        // Then
        assertEquals(OrderStatus.CANCELED, orderEntity.getStatus());
    }

    @Test
    void testCancelOrderThrowsInvalidOperationExceptionForNonPendingStatus() {
        // Given
        Long orderId = 1L;
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStatus(OrderStatus.MATCHED);

        when(orderRepository.getReferenceById(anyLong())).thenReturn(orderEntity);

        // When
        InvalidOperationException thrown = assertThrows(InvalidOperationException.class,
                () -> orderService.cancelOrder(orderId));

        // Then
        assertEquals("Orders with MATCHED status cannot be canceled.", thrown.getMessage());
    }
}
