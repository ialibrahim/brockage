package org.ialibrahim.brokerage.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.ialibrahim.brokerage.type.OrderSide;
import org.ialibrahim.brokerage.type.OrderStatus;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="ORDERS")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long customerId;
    private String assetName;
    @Enumerated(EnumType.STRING)
    private OrderSide orderSide;
    private Double size;
    private Double price;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @CreationTimestamp
    private LocalDateTime createDate;
}
