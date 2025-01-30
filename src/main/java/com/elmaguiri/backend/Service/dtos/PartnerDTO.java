package com.elmaguiri.backend.Service.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class PartnerDTO extends PersonDTO {
    private BigDecimal participationAmount;
    private double participationPercentage;
    private Long clientId;
}
