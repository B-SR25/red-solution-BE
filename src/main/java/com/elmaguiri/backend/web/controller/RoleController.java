package com.elmaguiri.backend.web.controller;

import com.elmaguiri.backend.config.Constants;
import com.elmaguiri.backend.Service.dtos.RoleDto;
import com.elmaguiri.backend.Service.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> allRoles = roleService.getAllRoles();
        return new ResponseEntity<>(allRoles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> getRoleById(@PathVariable Long id) {
        RoleDto roleDto = roleService.getRoleById(id).orElse(null);
        if (roleDto != null) {
            return new ResponseEntity<>(roleDto, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ROLE_NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        RoleDto createdRole = roleService.createRole(roleDto);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> updateRole(@PathVariable Long id, @RequestBody RoleDto roleDto) {
        RoleDto updatedRole = roleService.updateRole(id, roleDto);
        if (updatedRole != null) {
            return new ResponseEntity<>(updatedRole, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ROLE_NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

