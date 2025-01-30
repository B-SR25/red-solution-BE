package com.elmaguiri.backend.Service.dtos;

import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DocumentResponseDto {
    private String fileName;
    @Temporal(TemporalType.DATE)
    private Date registrationDate;
    private String contentType;
    private Long etapeId;
    private Long clientId;
}
