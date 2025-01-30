package com.elmaguiri.backend.Service.dtos;

import com.elmaguiri.backend.dao.entities.Operation;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class RemarqueDto {
    private Long id;
    private String nameRemarque;
    @Temporal(TemporalType.TIMESTAMP)
    private Date remarqueDate;
    private String desc;
    private Long operationId;
}
