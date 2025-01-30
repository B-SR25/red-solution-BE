package com.elmaguiri.backend.serviceImp;
import com.elmaguiri.backend.Service.dtos.UserDto;
import com.elmaguiri.backend.Service.services.AccountService;
import com.elmaguiri.backend.dao.entities.User;
import com.elmaguiri.backend.Service.services.AccountService;
import com.elmaguiri.backend.exceptions.ResourceNotFoundException;
import com.elmaguiri.backend.exceptions.UserNotActiveException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("hello world ::::::::::::");
        User user = accountService.loadUserByUsername(username);
        System.out.println("username:" + username);
        if (user != null) {
            if (user.getStatus()) {
                String[] roles = user.getRoles().stream().map(u -> u.getRoleName()).toArray(String[]::new);
                System.out.println(roles);
                UserDetails userDetails = org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(roles).build();
                return userDetails;
            } else {
                throw new UserNotActiveException("User is not active");
            }
        }
        throw new UsernameNotFoundException(String.format("User %s not found", username));
    }
}


