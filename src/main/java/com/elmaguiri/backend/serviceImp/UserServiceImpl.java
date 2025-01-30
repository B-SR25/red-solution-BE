package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Service.dtos.UserDto;
import com.elmaguiri.backend.Service.dtos.UserDtoResp;
import com.elmaguiri.backend.dao.entities.User;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.dao.repositories.UserRepository;
import com.elmaguiri.backend.Service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapperConfig modelMapperConfig) {
        this.userRepository = userRepository;
        this.modelMapperConfig = modelMapperConfig;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(modelMapperConfig::fromUser)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return modelMapperConfig.fromUser(user);
    }

    @Override
    public Optional<UserDtoResp> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        UserDtoResp userDtoResp= userOptional.map(modelMapperConfig::toUserDtoResp).get();
        userDtoResp.setRoleId(userOptional.get().getRoles().get(0).getId());
        return Optional.of(userDtoResp);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = modelMapperConfig.fromUserDto(userDto);
        System.out.println("userDto: " + user);
        user.setUsername(userDto.getName()+"."+userDto.getSurname());
        User savedUser = userRepository.save(user);
        return modelMapperConfig.fromUser(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User userToUpdate = userOptional.get();
            modelMapperConfig.updateUserFromDto(userToUpdate, userDto);
            User updatedUser = userRepository.save(userToUpdate);
            return modelMapperConfig.fromUser(updatedUser);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public String deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return "User with id: " + id + " has been deleted successfully.";
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}
