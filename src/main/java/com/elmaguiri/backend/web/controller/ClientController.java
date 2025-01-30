package com.elmaguiri.backend.web.controller;

import com.elmaguiri.backend.Service.dtos.ClientDTOResp;
import com.elmaguiri.backend.Service.dtos.PersonDTO;
import com.elmaguiri.backend.config.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.elmaguiri.backend.Service.dtos.ClientDto;
import com.elmaguiri.backend.Service.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /*@PostMapping
    public ResponseEntity<ClientDto> addClient(@RequestBody ClientDto clientDto) {
        ClientDto clientSaved = clientService.createClient(clientDto);
        return new ResponseEntity<>(clientSaved, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> updateClient(@PathVariable Long id, @RequestBody ClientDto clientDto) {
        ClientDto updatedClient = clientService.updateClient(id, clientDto);
        if (updatedClient != null) {
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.CLIENT_NOT_FOUND);
        }
    }*/

    @GetMapping("/{id}/download-pv")
    public ResponseEntity<byte[]> downloadPV(@PathVariable Long id) {
        Optional<ClientDto> clientDtoOptional = clientService.getClientById(id);
        if (clientDtoOptional.isPresent()) {
            ClientDto clientDto = clientDtoOptional.get();

        if(clientDto.getContrat()==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            // Create HTTP headers for the file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "pv_" + clientDto.getTitle()+ ".pdf");
            // Return the file data as a byte array
            return new ResponseEntity<>(clientDto.getContrat(), headers, HttpStatus.OK);
        }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ClientDto> addClient(@RequestPart(value = "contract", required = false) MultipartFile contractFile,
                                               @RequestPart("clientDto") ClientDto clientDto) {
        try {
            logger.info("Received request to add client with DTO: {}", clientDto);

            if (contractFile != null) {
                byte[] contratBytes = contractFile.getBytes();
                clientDto.setContrat(contratBytes);
                logger.info("Received contract file with name: {}", contractFile.getOriginalFilename());
            } else {
                logger.info("No contract file received.");
            }

            ClientDto clientSaved = clientService.createClient(clientDto);
            logger.info("Client successfully saved: {}", clientSaved);

            return new ResponseEntity<>(clientSaved, HttpStatus.CREATED);
        } catch (IOException e) {
            logger.error("Error processing client creation request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> updateClient(@PathVariable Long id,
                                               @RequestPart(value = "contract", required = false) MultipartFile contractFile,
                                               @RequestPart("clientDto") ClientDto clientDto) {
        try {
            if (contractFile != null) {
                byte[] contratBytes = contractFile.getBytes();
                clientDto.setContrat(contratBytes);
            }
            ClientDto updatedClient = clientService.partialUpdateClient(id, clientDto);
            if (updatedClient != null) {
                return new ResponseEntity<>(updatedClient, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.CLIENT_NOT_FOUND);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> partialUpdateClient(
            @PathVariable Long id,
            @RequestParam(value = "contractFile", required = false) MultipartFile contractFile,
            @RequestPart("clientDto") ClientDto clientDto) {

        try {
            if (contractFile != null) {
                clientDto.setContrat(contractFile.getBytes());
            }

            ClientDto updatedClient = clientService.partialUpdateClient(id, clientDto);

            if (updatedClient != null) {
                return new ResponseEntity<>(updatedClient, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @RequestMapping("/clientType/{clientType}")
    public ResponseEntity<List<ClientDto>> getClientsByClientType(@PathVariable String clientType) {
        List<ClientDto> clientDtos = clientService.getClientsByClientType(clientType);
        return new ResponseEntity<>(clientDtos, HttpStatus.OK);
    }
    @RequestMapping("/clientType/{clientType}/status/{status}")
    public ResponseEntity<List<ClientDto>> getClientsByClientTypeAndStatus(@PathVariable String clientType  ,@PathVariable Boolean status) {
        List<ClientDto> clientDtos = clientService.getClientsByClientTypeAndStatus(clientType,status);
        return new ResponseEntity<>(clientDtos, HttpStatus.OK);
    }
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<ClientDto>> findAllClients() {
        List<ClientDto> allClients = clientService.getAllClients();
        return new ResponseEntity<>(allClients, HttpStatus.OK);
    }
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<ClientDTOResp>> findClientsByStatus(@PathVariable Boolean status)  {
        List<ClientDTOResp> allClients = clientService.getClientByStatus(status);
        return new ResponseEntity<>(allClients, HttpStatus.OK);
    }


    @GetMapping("/{id}")
        @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> findClientById(@PathVariable Long id) {
        Optional<ClientDto> clientDtoOptional = clientService.getClientById(id);
        if (clientDtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(clientDtoOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.CLIENT_NOT_FOUND);
        }
    }


    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<ClientDto> searchClient(@RequestParam String query) {
        Long rc = null;
        String title = null;

        try {
            rc = Long.parseLong(query);
        } catch (NumberFormatException e) {
            title = query;
        }

        if (rc != null) {
            Optional<ClientDto> clientOptional = clientService.getClientByRc(rc);
            return clientOptional.map(clientDto -> new ResponseEntity<>(clientDto, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else if (title != null) {
            Optional<ClientDto> clientOptional = clientService.getClientByTitle(title);
            return clientOptional.map(clientDto -> new ResponseEntity<>(clientDto, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/names")
        @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<String>> getAllClientNames() {
        List<String> clientNames = clientService.getAllClientTitles();
        return new ResponseEntity<>(clientNames, HttpStatus.OK);
    }
}