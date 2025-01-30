package com.elmaguiri.backend.Service.services;

import com.elmaguiri.backend.Service.dtos.UserDto;
import com.elmaguiri.backend.Service.dtos.UserDtoResp;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserByUsername(String username);
    Optional<UserDtoResp> getUserById(Long id);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    String deleteUser(Long id);
}
