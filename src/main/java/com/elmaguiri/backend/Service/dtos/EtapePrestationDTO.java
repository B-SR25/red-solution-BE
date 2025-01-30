package com.elmaguiri.backend.Service.dtos;


import lombok.*;

import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtapePrestationDTO {
    private Long id;
    private String name;
    private Map<String, Object> attributesTemplate;
    private Long prestationId;
    private Integer stepOrder;
    private String color;
}