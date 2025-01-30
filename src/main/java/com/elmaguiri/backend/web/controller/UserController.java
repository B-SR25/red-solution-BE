package com.elmaguiri.backend.web.controller;

import com.elmaguiri.backend.Service.dtos.UserDtoResp;
import com.elmaguiri.backend.config.Constants;
import com.elmaguiri.backend.Service.dtos.UserDto;
import com.elmaguiri.backend.dao.repositories.RoleRepository;
import com.elmaguiri.backend.Service.services.AccountService;
import com.elmaguiri.backend.Service.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final RoleRepository roleRepository;


    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = accountService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        UserDtoResp userDtoResp = userService.getUserById(id).orElse(null);
        if (userDtoResp != null) {
            return new ResponseEntity<>(userDtoResp, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.USER_NOT_FOUND);
        }
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> getUserByUsername(@PathVariable String username) {
        UserDto userDto = userService.getUserByUsername(username);
        if (userDto != null) {
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.USER_NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = accountService.addNewUser(userDto);
        accountService.addRoleToUser(createdUser,userDto.getRoleId());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = accountService.updateUser(id, userDto);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.USER_NOT_FOUND);
        }
    }

    @PutMapping("archive/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> archiveUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = accountService.archive(id, userDto);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.USER_NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
        @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}