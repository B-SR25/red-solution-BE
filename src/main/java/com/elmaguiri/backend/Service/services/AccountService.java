package com.elmaguiri.backend.Service.services;

import com.elmaguiri.backend.Service.dtos.UserDto;
import com.elmaguiri.backend.dao.entities.Role;
import com.elmaguiri.backend.dao.entities.User;

import java.util.List;

public interface AccountService {
    UserDto addNewUser (UserDto userDto);
    Role addNewRole (String role);
    void addRoleToUser (UserDto userDto,Long id);
    void removeRoleFromUser (String username, String role);
    User loadUserByUsername(String username);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long id, UserDto userDto);
    UserDto archive(Long id, UserDto userDto);
}
