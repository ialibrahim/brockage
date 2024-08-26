package org.ialibrahim.brokerage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ialibrahim.brokerage.dao.OrderRepository;
import org.ialibrahim.brokerage.entity.OrderEntity;
import org.ialibrahim.brokerage.exception.InvalidOperationException;
import org.ialibrahim.brokerage.model.Order;
import org.ialibrahim.brokerage.type.OrderSide;
import org.ialibrahim.brokerage.type.OrderStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final AssetsService assetsService;
    private final ModelMapper modelMapper;

    public Order createOrder(Order order) {
        log.debug("Creating order, customer: {}", order.getCustomerId());

        PermissionChecker.checkPermission(order.getCustomerId());
        validateOrder(order);

        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);
        orderEntity.setStatus(OrderStatus.PENDING);
        orderRepository.saveAndFlush(orderEntity);

        return modelMapper.map(orderEntity, Order.class);
    }

    private void validateOrder(Order order) {
        log.debug("Validating order, customer: {}", order.getCustomerId());

        if (order.getOrderSide().equals(OrderSide.SELL)) {
            Double usableSize = assetsService.getUsableSize(order.getCustomerId(), order.getAssetName());
            if (usableSize < order.getSize()) {
                log.warn("invalid order size");
                throw new InvalidOperationException("Usable size is less than the requested size");
            }
        }

        if (order.getOrderSide().equals(OrderSide.BUY)) {
            Double usableFund = assetsService.getUsableFund(order.getCustomerId());
            if (usableFund < order.getPrice()) {
                log.warn("invalid order price");
                throw new InvalidOperationException("Usable fund is less than the offered price");
            }
        }
    }

    public List<Order> listOrders(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("List orders, customer: {}, date: {} - {}", customerId, startDate, endDate);
        PermissionChecker.checkPermission(customerId);
        return orderRepository.findByCustomerIdAndCreateDateBetween(customerId, startDate, endDate).stream()
                .map(o->modelMapper.map(o, Order.class)).toList();
    }

    public void cancelOrder(Long orderId) {
        log.debug("Cancelling order, orderId: {}", orderId);

        OrderEntity order = orderRepository.getReferenceById(orderId);
        PermissionChecker.checkPermission(order.getCustomerId());
        if (order.getStatus().equals(OrderStatus.PENDING)) {
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
        } else {
            throw new InvalidOperationException("Orders with " + order.getStatus() + " status cannot be canceled.");
        }
    }

    public Order matchOrder(Long orderId) {
        log.debug("Matching order, orderId: {}", orderId);
        PermissionChecker.onlyAdmin();

        OrderEntity order = orderRepository.getReferenceById(orderId);
        if (order.getStatus().equals(OrderStatus.PENDING)) {
            order.setStatus(OrderStatus.MATCHED);

            assetsService.createAssetFromOrder(order);
            orderRepository.save(order);
        } else {
            throw new InvalidOperationException("Orders with " + order.getStatus() + " status cannot be matched.");
        }

        return modelMapper.map(order, Order.class);
    }
}
