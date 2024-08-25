package org.ialibrahim.brokerage.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.ialibrahim.brokerage.type.OrderSide;
import org.ialibrahim.brokerage.type.OrderStatus;

import java.util.Date;

@Entity
@Data
@Table(name="ORDERS")
public class Order {

    @Id
    private Long id;
    private Long customerId;
    private String assetName;
    @Enumerated(EnumType.STRING)
    private OrderSide orderSide;
    private Double size;
    private Double price;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Date createDate;
}
