package org.ialibrahim.brokerage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Asset {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private Long customerId;
    private String assetName;
    private Double size;
    private Double usableSize;
}
