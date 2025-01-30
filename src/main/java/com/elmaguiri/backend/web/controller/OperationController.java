package com.elmaguiri.backend.web.controller;

import com.elmaguiri.backend.Enum.OperationState;
import com.elmaguiri.backend.Service.dtos.EtapeOperationDTO;
import com.elmaguiri.backend.Service.dtos.PrestationDto;
import com.elmaguiri.backend.Service.dtos.SearchCriteriaDto;
import com.elmaguiri.backend.config.Constants;
import com.elmaguiri.backend.Service.dtos.OperationDto;
import com.elmaguiri.backend.Service.services.ClientService;
import com.elmaguiri.backend.Service.services.OperationService;
import com.elmaguiri.backend.dao.entities.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Result;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/operations")
public class OperationController {

    private final OperationService operationService;
    private final ClientService clientService ;

    @Autowired
    public OperationController(OperationService operationService, ClientService clientService) {
        this.operationService = operationService;
        this.clientService = clientService;
    }

    @GetMapping("/next-code")
    public ResponseEntity<String> getNextCode() {
        String nextCode = operationService.getNextCode();
        return ResponseEntity.ok(nextCode);
    }

    //search controller
    @PostMapping("/search")
    public Page<OperationDto> findOperationsByCriteria(@RequestBody SearchCriteriaDto  searchCriteria, Pageable pageable){
        return this.operationService.findByCriteria(searchCriteria,pageable);
    }

    @GetMapping("/count")
    public long getCountByStatus(@RequestParam OperationState status) {
        return operationService.getCountByStatus(status);
    }

    @GetMapping("/count/all")
    public Map<OperationState, Long> getAllCountsByStatus() {
        return operationService.getAllCountsByStatus();
    }


    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<OperationDto>> getAllOperations() {
        List<OperationDto> allOperations = operationService.getAllOperations();
        return new ResponseEntity<>(allOperations, HttpStatus.OK);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<OperationDto>> getActiveOperations() {
        List<OperationDto> allOperations = operationService.getActiveOperations();
        return new ResponseEntity<>(allOperations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> getOperationById(@PathVariable Long id) {
        OperationDto operationDto = operationService.getOperationById(id)
                .orElse(null);
        if (operationDto != null) {
            return new ResponseEntity<>(operationDto, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.OPERATION_NOT_FOUND);
        }
    }

    //getting Etape
    @GetMapping("/etape/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> getEtapeById(@PathVariable Long id) {
        EtapeOperationDTO etapeOperationDTO = operationService.getEtapeById(id).orElse(null);
        if (etapeOperationDTO != null) {
            System.out.println(etapeOperationDTO);
            return new ResponseEntity<>(etapeOperationDTO, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ETAPE_NOT_FOUND);
        }
    }


    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> getOperationsByClientId(@PathVariable Long clientId) {
        List<OperationDto> operations = operationService.getOperationsByClient(clientId);
        return new ResponseEntity<>(operations, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> createOperation(@RequestBody OperationDto operationDto) {
        OperationDto createdOperation = operationService.createOperationWithEtapes(operationDto);
        return new ResponseEntity<>(createdOperation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> updateOperation(@PathVariable Long id, @RequestBody OperationDto operationDto) {
        OperationDto updatedOperation = operationService.updateOperation(id, operationDto);
        if (updatedOperation != null) {
            return new ResponseEntity<>(updatedOperation, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.OPERATION_NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Void> deleteOperation(@PathVariable Long id) {
        operationService.deleteOperation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{operationId}/etapes")
    public ResponseEntity<List<EtapeOperationDTO>> getStepsByOperationId(@PathVariable Long operationId) {
        List<EtapeOperationDTO> steps = operationService.getStepsByOperationId(operationId);
        return new ResponseEntity<>(steps, HttpStatus.OK);
    }


    @PutMapping("/{operationId}/etapes/{etapeId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<EtapeOperationDTO> updateEtapeOperation(
            @PathVariable Long operationId,
            @PathVariable Long etapeId,
            @RequestPart("etapeOperationDto") EtapeOperationDTO etapeOperationDto,
            @RequestPart(value = "files", required = false) MultipartFile[] files) throws IOException {

        EtapeOperationDTO updatedOperation = operationService.updateEtapeOperation(operationId, etapeId, etapeOperationDto, files);
        return new ResponseEntity<>(updatedOperation, HttpStatus.OK);
    }

    @PutMapping("/{id}/update-status")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Operation> updateOperationStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        Operation updatedOperation = operationService.updateOperationStatus(id, request.getNewStatus());
        return ResponseEntity.ok(updatedOperation);
    }

    public static class UpdateStatusRequest {
        private OperationState newStatus;

        public OperationState getNewStatus() {
            return newStatus;
        }

        public void setNewStatus(OperationState newStatus) {
            this.newStatus = newStatus;
        }
    }

}
