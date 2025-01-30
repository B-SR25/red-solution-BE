package com.elmaguiri.backend.Service.dtos;

import com.elmaguiri.backend.dao.entities.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EtapeOperationDTO {
    private Long id;
    private String name;
    private String status;
    private Map<String, Object> attributes;
    private Long operationId;
    private List<Document> documents;
    private Integer stepOrder;
    private String color;
}
