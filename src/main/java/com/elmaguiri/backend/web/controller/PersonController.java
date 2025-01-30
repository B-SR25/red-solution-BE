package com.elmaguiri.backend.web.controller;


import com.elmaguiri.backend.Service.dtos.*;
import com.elmaguiri.backend.Service.services.PersonService;
import com.elmaguiri.backend.serviceImp.mailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    private mailService mailService;


    public PersonController(mailService mailService) {
        this.mailService = mailService;
    }


    @GetMapping("/{id}/download-cin")
    public ResponseEntity<byte[]> downloadCin(@PathVariable Long id) {
        PersonDTO person = personService.getPersonById(id);
        if(person.getCin()==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            // Create HTTP headers for the file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "cin_" + person.getFirstName()+"_"+person.getLastName() + ".pdf");

            // Return the file data as a byte array
            return new ResponseEntity<>(person.getCin(), headers, HttpStatus.OK);
        }
    }


    @GetMapping
    public ResponseEntity<List<PersonDTORsep>> getAllPersons() {
        List<PersonDTORsep> persons = personService.getAllPersons();
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id) {
            PersonDTO person = personService.getPersonById(id);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<PersonDTO> createPerson(@RequestPart("personDTO") PersonDTO personDTO,
                                                  @RequestPart(value = "cin", required = false) MultipartFile cinFile) {
        try {
            if (cinFile != null) {
                personDTO.setCin(cinFile.getBytes());
            }
            PersonDTO createdPerson = personService.createPerson(personDTO);

            return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
        } catch (IOException e) {
            // Handle exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/owners")
    public ResponseEntity<OwnerDTO> createOwner(@RequestPart("ownerDTO") OwnerDTO ownerDTO,
                                                @RequestPart(value = "cin", required = false) MultipartFile cinFile) {
        try {
            if (cinFile != null) {
                ownerDTO.setCin(cinFile.getBytes());
            }
            OwnerDTO createdOwner = personService.createOwner(ownerDTO);
            return new ResponseEntity<>(createdOwner, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/owners/{id}")
    public ResponseEntity<OwnerDTO> updateOwner(@PathVariable Long id,
                                                @RequestPart("ownerDTO") OwnerDTO ownerDTO,
                                                @RequestPart(value = "cin", required = false) MultipartFile cinFile) {
        try {
            if (cinFile != null) {
                ownerDTO.setCin(cinFile.getBytes());
            }
            OwnerDTO updatedOwner = personService.updateOwner(id, ownerDTO);
            return ResponseEntity.ok(updatedOwner);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/owners")
    public ResponseEntity<List<OwnerDTO>> getAllOwners() {
        List<OwnerDTO> owners = personService.getAllOwners();
        return ResponseEntity.ok(owners);
    }

    @GetMapping("/owners/{id}")
    public ResponseEntity<OwnerDTO> getOwnerById(@PathVariable Long id) {
        OwnerDTO owner = personService.getOwnerById(id);
        return ResponseEntity.ok(owner);
    }


    @PostMapping("/collaborators")
    public ResponseEntity<CollaboratorDTO> createCollaborator(@RequestPart("collaboratorDTO") CollaboratorDTO collaboratorDTO,
                                                              @RequestPart(value = "cin", required = false) MultipartFile cinFile) {
        try {
            if (cinFile != null) {
                collaboratorDTO.setCin(cinFile.getBytes());
            }
            CollaboratorDTO createdCollaborator = personService.createCollaborator(collaboratorDTO);
            return new ResponseEntity<>(createdCollaborator, HttpStatus.CREATED);
        } catch (IOException e) {
            // Handle exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/collaborators/{id}")
    public ResponseEntity<CollaboratorDTO> updateCollaborator(@PathVariable Long id,
                                                              @RequestPart("collaboratorDTO") CollaboratorDTO collaboratorDTO,
                                                              @RequestPart(value = "cin", required = false) MultipartFile cinFile) {
        try {
            if (cinFile != null) {
                collaboratorDTO.setCin(cinFile.getBytes());
            }
            CollaboratorDTO updatedCollaborator = personService.updateCollaborator(id, collaboratorDTO);
            return ResponseEntity.ok(updatedCollaborator);
        } catch (IOException e) {
            // Handle exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/collaborators")
    public ResponseEntity<List<CollaboratorDTO>> getAllCollaborators() {
        List<CollaboratorDTO> collaborators = personService.getAllCollaborators();
        return ResponseEntity.ok(collaborators);
    }

    @GetMapping("/collaborators/{id}")
    public ResponseEntity<CollaboratorDTO> getCollaboratorById(@PathVariable Long id) {
        CollaboratorDTO collaborator = personService.getCollaboratorById(id);
        return ResponseEntity.ok(collaborator);
    }








    @PostMapping("/partners")
    public ResponseEntity<PartnerDTO> createPartner(@RequestPart("partnerDTO") PartnerDTO partnerDTO,
                                                    @RequestPart(value = "cin", required = false) MultipartFile cinFile) {
        try {
            if (cinFile != null) {
                partnerDTO.setCin(cinFile.getBytes());
            }
            PartnerDTO createdPartner = personService.createPartner(partnerDTO);
            return new ResponseEntity<>(createdPartner, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/partners/{id}")
    public ResponseEntity<PartnerDTO> updatePartner(@PathVariable Long id,
                                                    @RequestPart("partnerDTO") PartnerDTO partnerDTO,
                                                    @RequestPart(value = "cin", required = false) MultipartFile cinFile) {
        try {
            if (cinFile != null) {
                partnerDTO.setCin(cinFile.getBytes());
            }
            PartnerDTO updatedPartner = personService.updatePartner(id, partnerDTO);
            return ResponseEntity.ok(updatedPartner);
        } catch (IOException e) {
            // Handle exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/partners")
    public ResponseEntity<List<PartnerDTO>> getAllPartners() {
        List<PartnerDTO> partners = personService.getAllPartners();
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/partners/{id}")
    public ResponseEntity<PartnerDTO> getPartnerById(@PathVariable Long id) {
        PartnerDTO partner = personService.getPartnerById(id);
        return ResponseEntity.ok(partner);
    }


    @GetMapping("/partners/client/{clientId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public List<PartnerDTO> getPartnersByClientId(@PathVariable Long clientId) {
        return personService.getPartnersByClientId(clientId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable Long id,
                                                  @RequestPart("personDTO") PersonDTO personDTO,
                                                  @RequestPart(value = "cin", required = false) MultipartFile cinFile) {
        try {
            if (cinFile != null) {
                personDTO.setCin(cinFile.getBytes());
            }
            PersonDTO updatedPerson = personService.updatePerson(id, personDTO);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}