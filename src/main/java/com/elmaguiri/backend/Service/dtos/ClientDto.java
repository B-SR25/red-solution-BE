package com.elmaguiri.backend.Service.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ClientDto {
    private Long id;
    private String title;
    private String abbreviation;
    private String address;
    private LocalDate creationDate;
    private String fax;
    private String city;
    private Long ice;
    private String form;
    private Long capital;
    private String headquarters;
    private Long rc;
    private Long ifNumber;
    private Long cnss;
    private String ownership;
    private String phoneNumber;
    private String clientEmail;
    private Boolean status;
    private Long  taxProfessionnel;
    private LocalDate startDate;
    private LocalDate endDate;
    private byte[] contrat =null;
    private Long ownerId;
    private Long managerId;
    private Long notificationId;
    private String clientType;
    private LocalDate firstAppointment;
}
