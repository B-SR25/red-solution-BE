package com.elmaguiri.backend.web.controller;

import com.elmaguiri.backend.Service.dtos.RemarqueDto;
import com.elmaguiri.backend.Service.services.OperationService;
import com.elmaguiri.backend.Service.services.RemarqueOperationService;
import com.elmaguiri.backend.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/remarque")
public class RemarqueOperationController {
    private final RemarqueOperationService remarqueOperationService ;

    @Autowired
    public RemarqueOperationController( RemarqueOperationService remarqueOperationService) {
        this.remarqueOperationService = remarqueOperationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<RemarqueDto>> getAllRemarque() {
        List<RemarqueDto> allRemarques = remarqueOperationService.getAllRemarques();
        return new ResponseEntity<>(allRemarques, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> getRemarqueById(@PathVariable Long id) {
        RemarqueDto remarqueDto = remarqueOperationService.getRemarqueById(id)
                .orElse(null);
        if (remarqueDto != null) {
            return new ResponseEntity<>(remarqueDto, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.REMARQUE_NOT_FOUND);
        }
    }

    @GetMapping("/operations/{operationId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> getRemarqueByOperationId(@PathVariable Long operationId) {
        List<RemarqueDto> remarques = remarqueOperationService.getRemarqueByOperation(operationId);
        return new ResponseEntity<>(remarques, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> createRemarque(@RequestBody RemarqueDto remarqueDto) {
        RemarqueDto createdRemarque = remarqueOperationService.createRemarque(remarqueDto);
        return new ResponseEntity<>(createdRemarque, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> updateRemarque(@PathVariable Long id, @RequestBody RemarqueDto remarqueDto) {
        RemarqueDto updatedRemarque = remarqueOperationService.updateRemarque(id, remarqueDto);
        if (updatedRemarque != null) {
            return new ResponseEntity<>(updatedRemarque, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.OPERATION_NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRemarque(@PathVariable Long id) {
        remarqueOperationService.deleteRemarque(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
