package com.elmaguiri.backend.Service.dtos;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDtoResp {
    private int id;
    private String name;
    private String surname;
    private String username;
    private String jobPosition;
    private String mail;
    private boolean status;
    private Long roleId;

    public boolean getStatus() {
        return status;
    }
}
