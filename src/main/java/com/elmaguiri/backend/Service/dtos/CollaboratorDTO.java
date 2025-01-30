package com.elmaguiri.backend.Service.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class CollaboratorDTO extends PersonDTO {
    private String someCollaboratorSpecificField;

}
