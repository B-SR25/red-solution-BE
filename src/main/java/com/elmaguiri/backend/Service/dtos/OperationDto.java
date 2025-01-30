package com.elmaguiri.backend.Service.dtos;


import com.elmaguiri.backend.Enum.OperationState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OperationDto {
    private long id;
    private String nameOperation;
    @Temporal(TemporalType.DATE)
    private Date operationDate;
    private OperationState statutOperation;
    private List<RemarqueDto> remarqueOperation;
    private Long clientId;
    private Long prestationId;
    private Long userId;
    private List<EtapeOperationDTO> etapes;
    private Boolean facturer;
}
