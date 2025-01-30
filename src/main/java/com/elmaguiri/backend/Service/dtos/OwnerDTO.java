package com.elmaguiri.backend.Service.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerDTO extends PersonDTO {
    private String someOwnerSpecificField;
}
