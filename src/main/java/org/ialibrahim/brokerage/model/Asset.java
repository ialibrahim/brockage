package org.ialibrahim.brokerage.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
public class Asset {
    private Long id;
    private Long customerId;
    private String assetName;
    private Double size;
    private Double usableSize;
}
