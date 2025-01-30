package com.elmaguiri.backend.web.controller;

import com.elmaguiri.backend.config.Constants;

import com.elmaguiri.backend.Service.dtos.PrestationDto;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.dao.repositories.PrestationRepository;
import com.elmaguiri.backend.Service.services.PrestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestations")
public class PrestationController{

    private final PrestationService prestationService;
    private final PrestationRepository prestationRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Autowired
    public PrestationController(PrestationService prestationService, PrestationRepository prestationRepository, ModelMapperConfig modelMapperConfig) {
        this.prestationService = prestationService;
        this.prestationRepository = prestationRepository;
        this.modelMapperConfig = modelMapperConfig;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<PrestationDto>> getAllPrestations() {
        List<PrestationDto> allPrestations = prestationService.getAllPrestations();
        System.out.println("hello");
        return new ResponseEntity<>(allPrestations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> getPrestationById(@PathVariable Long id) {
        PrestationDto prestationDto = prestationService.getPrestationById(id).orElse(null);
        if (prestationDto != null) {
            System.out.println(prestationDto);
            return new ResponseEntity<>(prestationDto, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.PRESTATION_NOT_FOUND);
        }
    }

    @GetMapping("prestationName/{prestationName}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> getPrestationById(@PathVariable String prestationName) {
        PrestationDto prestationDto = prestationService.getPrestationByName(prestationName).orElse(null);
        if (prestationDto != null) {
            return new ResponseEntity<>(prestationDto, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.PRESTATION_NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<PrestationDto> createPrestation(@RequestBody PrestationDto prestationDto) throws Exception {
        PrestationDto createdPrestation = prestationService.createPrestation(prestationDto);
        return new ResponseEntity<>(createdPrestation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> updatePrestation(@PathVariable Long id, @RequestBody PrestationDto prestationDto) {
        PrestationDto updatedPrestation = prestationService.updatePrestation(id, prestationDto);
        if (updatedPrestation != null) {
            return new ResponseEntity<>(updatedPrestation, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.PRESTATION_NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
      public ResponseEntity<Void> deletePrestation(@PathVariable Long id) {
        prestationService.deletePrestation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/names")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<String>> getAllClientNames() {
        List<String> prestationNamesNames = prestationService.getAllPrestationNames();
        return new ResponseEntity<>(prestationNamesNames, HttpStatus.OK);
    }
}
