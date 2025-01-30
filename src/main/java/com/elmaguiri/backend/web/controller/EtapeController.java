package com.elmaguiri.backend.web.controller;

import com.elmaguiri.backend.Service.dtos.EtapePrestationDTO;
import com.elmaguiri.backend.Service.services.EtapePrestationService;
import com.elmaguiri.backend.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/etapes")
public class EtapeController {

    private final EtapePrestationService etapeService;

    @Autowired
    public EtapeController(EtapePrestationService etapeService) {
        this.etapeService = etapeService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<EtapePrestationDTO>> getAllEtapes() {
        List<EtapePrestationDTO> allEtapes = etapeService.getAllEtapesPrestation();
        return new ResponseEntity<>(allEtapes, HttpStatus.OK);
    }

    @GetMapping("/prestation/{prestationId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public List<EtapePrestationDTO> getEtapesByPrestationId(@PathVariable Long prestationId) {
        return etapeService.getEtapesByPrestationId(prestationId);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> getEtapeById(@PathVariable Long id) {
        EtapePrestationDTO etapeDto = etapeService.getEtapeById(id);
        if (etapeDto != null) {
            return new ResponseEntity<>(etapeDto, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ETAPE_NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<EtapePrestationDTO> createEtape(@RequestBody EtapePrestationDTO etapeDto) throws Exception {
        EtapePrestationDTO createdEtape = etapeService.createEtapePrestation(etapeDto);
        return new ResponseEntity<>(createdEtape, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/prestations/{prestationId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> updateEtape(@PathVariable Long id,
                                              @RequestBody EtapePrestationDTO etapeDto) {
        EtapePrestationDTO updatedEtape = etapeService.updateEtapePrestation(id, etapeDto);
        if (updatedEtape != null) {
            return new ResponseEntity<>(updatedEtape, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ETAPE_NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/prestations/{prestationId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Void> deleteEtape(@PathVariable Long id,@PathVariable Long prestationId) {
        etapeService.deleteEtapePrestation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
