package com.elmaguiri.backend.Service.services;

import com.elmaguiri.backend.Service.dtos.RoleDto;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<RoleDto> getAllRoles();
    Optional<RoleDto> getRoleById(Long id);
    RoleDto createRole(RoleDto roleDto);
    RoleDto updateRole(Long id, RoleDto roleDto);
   String deleteRole(Long id);
}
