package org.ialibrahim.brokerage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="ASSETS")
public class Asset {
    @Id
    private Long id;
    private Long customerId;
    private String assetName;
    private Double size;
    private Double usableSize;
}
