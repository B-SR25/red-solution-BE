package com.elmaguiri.backend.Service.dtos;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ClientDTOResp {
    private Long id;
    private String title;
    private Long ice;
    private Long rc;
    private Long managerId;
    private Boolean status;
}
