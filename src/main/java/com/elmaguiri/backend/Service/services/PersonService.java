package com.elmaguiri.backend.Service.services;

import com.elmaguiri.backend.Service.dtos.*;

import java.util.List;

// PersonService.java
public interface PersonService {
    List<PersonDTORsep> getAllPersons();
    PersonDTO getPersonById(Long id);
    PersonDTO createPerson(PersonDTO personDTO);
    PersonDTO updatePerson(Long id, PersonDTO personDetailsDTO);
    void deletePerson(Long id);
    // Owner methods
    OwnerDTO createOwner(OwnerDTO ownerDTO);

    List<OwnerDTO> getAllOwners();

    OwnerDTO getOwnerById(Long id)  ;

    OwnerDTO updateOwner(Long id, OwnerDTO ownerDTO);

    // Collaborator methods
    CollaboratorDTO createCollaborator(CollaboratorDTO collaboratorDTO);

    List<CollaboratorDTO> getAllCollaborators();

    CollaboratorDTO getCollaboratorById(Long id) ;

    CollaboratorDTO updateCollaborator(Long id, CollaboratorDTO collaboratorDTO);

    // Partner methods
    PartnerDTO createPartner(PartnerDTO partnerDTO);

    List<PartnerDTO> getAllPartners();

    PartnerDTO getPartnerById(Long id);

    PartnerDTO updatePartner(Long id, PartnerDTO partnerDTO);

    List<PartnerDTO> getPartnersByClientId(Long clientId) ;
}

