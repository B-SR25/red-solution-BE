package com.elmaguiri.backend.Service.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RoleDto {
    private long roleId;
    private String roleName;
}
