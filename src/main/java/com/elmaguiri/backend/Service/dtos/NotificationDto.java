package com.elmaguiri.backend.Service.dtos;

import com.elmaguiri.backend.dao.entities.Client;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationDto {
    private Long id;
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    private Date NotificationDate;
    private Long clientId;


}
