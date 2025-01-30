package com.elmaguiri.backend.Service.dtos;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PrestationDto {
    private Long id;
    private String name;
    private String description;
    private List<EtapePrestationDTO> etapes;
    private List<OperationDto> operations;
}
