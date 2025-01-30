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
public class DocumentDto {
    private Long id;
    private String fileName;
    @Temporal(TemporalType.DATE)
    private Date registrationDate;
    @Lob
    private byte[] document;
    private String contentType;
    private Long etapeId;
    private Long clientId;
}
