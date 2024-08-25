package org.ialibrahim.brokerage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.ialibrahim.brokerage.type.OrderSide;
import org.ialibrahim.brokerage.type.OrderStatus;

import java.time.LocalDateTime;

@Data
public class Order {

    private Long id;
    private Long customerId;
    private String assetName;
    private OrderSide orderSide;
    private Double size;
    private Double price;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderStatus status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createDate;
}
