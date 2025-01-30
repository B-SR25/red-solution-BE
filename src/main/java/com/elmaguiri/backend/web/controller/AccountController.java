package com.elmaguiri.backend.web.controller;

import com.elmaguiri.backend.config.Constants;
import com.elmaguiri.backend.dao.entities.User;
import com.elmaguiri.backend.Service.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
        @Autowired
        AccountService accountService;
        @GetMapping("/{username}")
        public ResponseEntity<Object> findUsername(@PathVariable String username) {
            User clientDtoOptional = accountService.loadUserByUsername(username);
            if (clientDtoOptional!=null) {
                return ResponseEntity.status(HttpStatus.OK).body(clientDtoOptional);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.CLIENT_NOT_FOUND);
            }
        }

    }