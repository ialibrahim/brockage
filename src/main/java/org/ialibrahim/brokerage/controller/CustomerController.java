package org.ialibrahim.brokerage.controller;

import lombok.RequiredArgsConstructor;
import org.ialibrahim.brokerage.model.Asset;
import org.ialibrahim.brokerage.model.Order;
import org.ialibrahim.brokerage.service.AssetsService;
import org.ialibrahim.brokerage.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final AssetsService assetsService;
    private final OrderService orderService;

    @GetMapping("/{customerId}/assets")
    public ResponseEntity<List<Asset>> listAssets(@PathVariable("customerId") Long customerId) {

        List<Asset> assets = assetsService.listAssets(customerId);
        return new ResponseEntity<>(assets, HttpStatus.OK);
    }

    @GetMapping("/{customerId}/orders")
    public ResponseEntity<List<Order>> listOrders(
            @PathVariable("customerId") Long customerId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Order> orders = orderService.listOrders(customerId, startDate, endDate);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{customerId}/deposit/{amount}")
    public ResponseEntity<Asset> depositMoney(@PathVariable("customerId") Long customerId,
                                                    @PathVariable("amount") Double amount) {

        Asset assets = assetsService.deposite(customerId, amount);
        return new ResponseEntity<>(assets, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{customerId}/withdraw/{amount}")
    public ResponseEntity<Asset> withdrawMoney(@PathVariable("customerId") Long customerId,
                                                     @PathVariable("amount") Double amount) {

        Asset assets = assetsService.withdraw(customerId, amount);
        return new ResponseEntity<>(assets, HttpStatus.ACCEPTED);
    }
}
