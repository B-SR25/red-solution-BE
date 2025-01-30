package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Service.dtos.*;
import com.elmaguiri.backend.Service.services.PersonService;
import lombok.AllArgsConstructor;


import com.elmaguiri.backend.dao.entities.Person;
import com.elmaguiri.backend.dao.entities.Owner;
import com.elmaguiri.backend.dao.entities.Collaborator;
import com.elmaguiri.backend.dao.entities.Partner;
import com.elmaguiri.backend.exceptions.ResourceNotFoundException;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.dao.repositories.OwnerRepository;
import com.elmaguiri.backend.dao.repositories.CollaboratorRepository;
import com.elmaguiri.backend.dao.repositories.PartnerRepository;
import com.elmaguiri.backend.dao.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {
//    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);


    private OwnerRepository ownerRepository;

    private CollaboratorRepository collaboratorRepository;

    private PartnerRepository partnerRepository;

    private PersonRepository personRepository;

    private ModelMapperConfig modelMapperConfig;

    @Override
    public List<PersonDTORsep> getAllPersons() {
        List<PersonDTORsep> persons = new ArrayList<>();
        //persons.addAll(ownerRepository.findAll().stream().map(modelMapperConfig::toOwnerDto).toList());
        //  persons.addAll(collaboratorRepository.findAll().stream().map(modelMapperConfig::toCollaboratorDto).toList());
       // persons.addAll(partnerRepository.findAll().stream().map(modelMapperConfig::toPartnerDto).toList());
        persons.addAll(personRepository.findAll().stream().map(modelMapperConfig::toPersonDTOResp).toList());
        return persons;
    }

    @Override
    public PersonDTO getPersonById(Long id) {
        Optional<Owner> owner = ownerRepository.findById(id);
        if (owner.isPresent()) {
            OwnerDTO ownerDTO=new OwnerDTO();
            ownerDTO= modelMapperConfig.toOwnerDto(owner.get());
            ownerDTO.setDType("owner");
            return ownerDTO;

        }
        Optional<Collaborator> collaborator = collaboratorRepository.findById(id);
        if (collaborator.isPresent()) {
            CollaboratorDTO collaboratorDTO=new CollaboratorDTO();
            collaboratorDTO= modelMapperConfig.toCollaboratorDto(collaborator.get());;
            collaboratorDTO.setDType("collaborator");
            return collaboratorDTO;
        }
        Optional<Partner> partner = partnerRepository.findById(id);
        if (partner.isPresent()) {
            PartnerDTO partnerDTO=new PartnerDTO();
            partnerDTO= modelMapperConfig.toPartnerDto(partner.get());
            partnerDTO.setDType("partner");
            return partnerDTO;
        }
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            PersonDTO personDTO=new PersonDTO();
            personDTO= modelMapperConfig.toPersonDto(person.get());
            personDTO.setDType("person");
            return personDTO;
        }
        throw new RuntimeException("Person not found with id " + id);
    }

    @Override
    public PersonDTO createPerson(PersonDTO personDTO) {
        //logger.debug("Creating person with DTO: {}", personDTO);
        Person person = modelMapperConfig.fromPersonDto(personDTO);
        //logger.debug("Mapped person entity: {}", person);
        Person savedPerson;
        if (person instanceof Owner) {
            savedPerson = ownerRepository.save((Owner) person);
        } else if (person instanceof Collaborator) {
            savedPerson = collaboratorRepository.save((Collaborator) person);
        } else if (person instanceof Partner) {
            savedPerson = partnerRepository.save((Partner) person);
        } else {
            savedPerson = personRepository.save(person);
        }
        //logger.debug("Saved person entity: {}", savedPerson);
        PersonDTO resultDTO = modelMapperConfig.toPersonDto(savedPerson);
        //logger.debug("Mapped result DTO: {}", resultDTO);
        return resultDTO;
    }


    @Override
    public OwnerDTO createOwner(OwnerDTO ownerDTO) {
        Owner owner =modelMapperConfig.fromOwnerDto(ownerDTO);
       Owner createdOwner= ownerRepository.save(owner);
       return modelMapperConfig.toOwnerDto(createdOwner);
    }

    public List<OwnerDTO> getAllOwners() {
        return ownerRepository.findAll().stream()
                .map(modelMapperConfig::toOwnerDto)
                .collect(Collectors.toList());
    }

    public OwnerDTO getOwnerById(Long id){
        return ownerRepository.findById(id)
                .map(modelMapperConfig::toOwnerDto)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

    }

    public OwnerDTO updateOwner(Long id, OwnerDTO ownerDTO) {
        Owner existingOwner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        modelMapperConfig.updateOwnerFromDto(existingOwner, ownerDTO);
        Owner updatedOwner = ownerRepository.save(existingOwner);
        return modelMapperConfig.toOwnerDto(updatedOwner);
    }

    @Override
    public CollaboratorDTO createCollaborator(CollaboratorDTO collaboratorDTO) {
        Collaborator collaborator = new Collaborator();
        modelMapperConfig.updateCollaboratorFromDto(collaborator, collaboratorDTO);
        Collaborator collaboratorCreated=collaboratorRepository.save(collaborator);
        return modelMapperConfig.toCollaboratorDto(collaboratorCreated);
    }


    public List<CollaboratorDTO> getAllCollaborators() {
        return collaboratorRepository.findAll().stream()
                .map(modelMapperConfig::toCollaboratorDto)
                .collect(Collectors.toList());
    }

    public CollaboratorDTO getCollaboratorById(Long id) {
        return collaboratorRepository.findById(id)
                .map(modelMapperConfig::toCollaboratorDto)
                .orElseThrow(() -> new ResourceNotFoundException("Collaborator not found"));
    }

    public CollaboratorDTO updateCollaborator(Long id, CollaboratorDTO collaboratorDTO) {
        Collaborator existingCollaborator = collaboratorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collaborator not found"));

        modelMapperConfig.updateCollaboratorFromDto(existingCollaborator, collaboratorDTO);
        Collaborator updatedCollaborator = collaboratorRepository.save(existingCollaborator);
        return modelMapperConfig.toCollaboratorDto(updatedCollaborator);
    }



    @Override
    public PartnerDTO createPartner(PartnerDTO partnerDTO) {
        Partner partner = new Partner();
        modelMapperConfig.updatePartnerFromDto(partner, partnerDTO);
        Partner partnerCreated=partnerRepository.save(partner);
        return modelMapperConfig.toPartnerDto(partnerCreated);
    }
    @Override
    public List<PartnerDTO> getAllPartners() {
        return partnerRepository.findAll().stream()
                .map(modelMapperConfig::toPartnerDto)
                .collect(Collectors.toList());
    }

    @Override
    public PartnerDTO getPartnerById(Long id) {
        return partnerRepository.findById(id)
                .map(modelMapperConfig::toPartnerDto)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found"));
    }

    @Override
    public PartnerDTO updatePartner(Long id, PartnerDTO partnerDTO) {
        Partner existingPartner = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found"));

        modelMapperConfig.updatePartnerFromDto(existingPartner, partnerDTO);
        Partner updatedPartner = partnerRepository.save(existingPartner);
        return modelMapperConfig.toPartnerDto(updatedPartner);
    }

    @Override
    public List<PartnerDTO> getPartnersByClientId(Long clientId) {
        List<Partner> partners = partnerRepository.findByClientId(clientId);
        return partners.stream().map(modelMapperConfig::toPartnerDto).collect(Collectors.toList());
    }


    @Override
    public PersonDTO updatePerson(Long id, PersonDTO personDTO) {
        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));

        modelMapperConfig.updatePersonFromDto(existingPerson, personDTO);
        Person updatedPerson = personRepository.save(existingPerson);
        return modelMapperConfig.toPersonDto(updatedPerson);
    }


    @Override
    public void deletePerson(Long id) {
        PersonDTO personDTO = getPersonById(id);
        if (personDTO instanceof OwnerDTO) {
            ownerRepository.delete(modelMapperConfig.fromOwnerDto((OwnerDTO) personDTO));
        } else if (personDTO instanceof CollaboratorDTO) {
            collaboratorRepository.delete(modelMapperConfig.fromCollaboratorDto((CollaboratorDTO) personDTO));
        } else if (personDTO instanceof PartnerDTO) {
            partnerRepository.delete(modelMapperConfig.fromPartnerDto((PartnerDTO) personDTO));
        } else {
            personRepository.delete(modelMapperConfig.fromPersonDto(personDTO));
        }
    }
}
