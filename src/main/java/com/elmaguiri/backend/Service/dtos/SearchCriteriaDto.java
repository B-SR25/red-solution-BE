package com.elmaguiri.backend.Service.dtos;


import com.elmaguiri.backend.Enum.OperationState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteriaDto {
    private String prestationId;
    private String clientName;
    private String operationName;
    private OperationState operationStatus;
    private Date[] dateRange;
}
