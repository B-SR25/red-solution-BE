package com.elmaguiri.backend.Service.dtos;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class PersonDTORsep {
    private Long id;
    private String firstName;
    private String lastName;
}

