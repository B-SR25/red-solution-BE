package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Service.dtos.RoleDto;
import com.elmaguiri.backend.dao.entities.Role;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.dao.repositories.RoleRepository;
import com.elmaguiri.backend.Service.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapperConfig modelMapperConfig) {
        this.roleRepository = roleRepository;
        this.modelMapperConfig = modelMapperConfig;
    }


    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(modelMapperConfig::fromRole)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDto> getRoleById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        return roleOptional.map(modelMapperConfig::fromRole);
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role = modelMapperConfig.fromRoleDto(roleDto);
        Role savedRole = roleRepository.save(role);
        return modelMapperConfig.fromRole(savedRole);
    }

    @Override
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role roleToUpdate = roleOptional.get();
            modelMapperConfig.updateRoleFromDto(roleToUpdate, roleDto);
            Role updatedRole = roleRepository.save(roleToUpdate);
            return modelMapperConfig.fromRole(updatedRole);
        } else {
            throw new RuntimeException("Role not found with id: " + id);
        }
    }

    @Override
    public String deleteRole(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            roleRepository.deleteById(id);
            return "Prestation with id: " + id + " has been deleted successfully.";
        } else {
            throw new RuntimeException("Role not found with id: " + id);
        }
    }
}
