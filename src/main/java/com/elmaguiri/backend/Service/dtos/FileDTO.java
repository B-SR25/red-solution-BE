package com.elmaguiri.backend.Service.dtos;

import lombok.Data;

@Data
public class FileDTO {

    private Long id;
    private String code;
    private String title;
    private boolean apostille;
    private String translation;
}