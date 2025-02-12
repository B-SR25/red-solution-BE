package com.elmaguiri.backend.Service.dtos;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {
    private int id;
    private String name;
    private String surname;
    private String username;
    private String oldPassword;
    private String password;
    private String confirmPassword;
    private String jobPosition;
    private String mail;
    private boolean status;
    private Long roleId;
    private String editedBy;

    public boolean getStatus() {
        return status;
    }
}
