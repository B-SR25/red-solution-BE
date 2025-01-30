package com.elmaguiri.backend.Service.mappers;

import com.elmaguiri.backend.Service.dtos.*;
import com.elmaguiri.backend.dao.entities.*;
import com.elmaguiri.backend.dao.repositories.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ModelMapperConfig {

    private final EtapeOperationRepo etapeOperationRepository ;
    private final EtapePrestationRepo etapePrestationRepository;
    private final PrestationRepository prestationRepository;
    private final OperationRepository operationRepository;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository; // Ajouter le UserRepository

    private final ModelMapper modelMapper;
    private final OwnerRepository ownerRepository;
    private final PersonRepository personRepository;
    private final RemarqueRepository remarqueRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public ModelMapperConfig(EtapeOperationRepo etapeOperationRepository, EtapePrestationRepo etapePrestationRepository, PrestationRepository prestationRepository, OperationRepository operationRepository, RoleRepository roleRepository, ClientRepository clientRepository, UserRepository userRepository, OwnerRepository ownerRepository, PersonRepository personRepository, RemarqueRepository remarqueRepository,NotificationRepository notificationRepository) {
        this.etapeOperationRepository = etapeOperationRepository;
        this.etapePrestationRepository = etapePrestationRepository;
        this.prestationRepository = prestationRepository;
        this.operationRepository = operationRepository;
        this.roleRepository = roleRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
        this.personRepository = personRepository;
        this.remarqueRepository = remarqueRepository;
        this.notificationRepository=notificationRepository;

        this.modelMapper = new ModelMapper();
        configureMappings ();
    }


    private void configureMappings () {

            // Custom mapping to exclude id during updates
            modelMapper.typeMap(FileDTO.class, File.class).addMappings(mapper -> {
                mapper.skip(File::setId); // Skip    the id field
            });

            modelMapper.addMappings(new PropertyMap<OperationDto, Operation>() {
                @Override
                protected void configure() {
                    map().setId(null);
                    map().setClient(null);
                    map().setPrestation(null);
                    map().setCreatedByUser(null); // Ajouter le mappage pour l'utilisateur
                }
            });

            modelMapper.addMappings(new PropertyMap<Operation, OperationDto>() {
                @Override
                protected void configure() {
                    map().setClientId(source.getClient().getId());
                    map().setPrestationId(source.getPrestation().getId());
                    map().setUserId(source.getCreatedByUser().getId()); // Ajouter le mappage pour l'utilisateur
                }
            });
            modelMapper.addMappings(new PropertyMap<UserDto, User>() {
                @Override
                protected void configure() {
                    map().setId(null);
                }
            });

            modelMapper.addMappings(new PropertyMap<User, UserDtoResp>() {
                @Override
                protected void configure() {
                    map().setRoleId(null);
                }
            });

            modelMapper.addMappings(new PropertyMap<EtapePrestationDTO, EtapePrestation>() {
                @Override
                protected void configure() {
                    map().setId(null);
                }
            });

            modelMapper.addMappings(new PropertyMap<EtapeOperationDTO, EtapeOperation>() {
                @Override
                protected void configure() {
                    map().setId(null);
                }
            });

            modelMapper.addMappings(new PropertyMap<RemarqueDto, RemarqueOperation>() {
                @Override
                protected void configure(){
                    map().setId(null);
                }
            });

            modelMapper.addMappings(new PropertyMap<ClientDto, Client>() {
                @Override
                protected void configure() {
                    map().setId(null);
                }
            });

            modelMapper.addMappings(new PropertyMap<NotificationDto, Notification>() {
                @Override
                protected void configure() {
                    map().setId(null);
                }
            });

            modelMapper.addMappings(new PropertyMap<PrestationDto, Prestation>() {
                @Override
                protected void configure() {
                    map().setId(null);
                }
            });


        modelMapper.addMappings(new PropertyMap<Document, DocumentDto>() {
            @Override
            protected void configure() {
                map().setClientId(source.getClient().getId());
                map().setEtapeId(source.getEtape().getId());
            }
        });

        modelMapper.typeMap(ClientDto.class, Client.class).addMappings(mapper -> {
            mapper.map(ClientDto::getContrat, Client::setContrat);
        });
    }


        public FileDTO toDTO(File file) {
            return modelMapper.map(file, FileDTO.class);
        }

        public File toEntity(FileDTO fileDTO) {
            return modelMapper.map(fileDTO, File.class);
        }

        public void updateEntityFromDTO(FileDTO fileDTO, File file) {
            modelMapper.map(fileDTO, file);
        }

        public OperationDto fromOperation (Operation operation){
            return this.modelMapper.map(operation, OperationDto.class);
        }

        public Operation fromOperationDto (OperationDto operationDto){
            Operation operation = modelMapper.map(operationDto, Operation.class);
            if (operationDto.getClientId() != null) {
                Optional<Client> clientOptional = clientRepository.findById(operationDto.getClientId());
                clientOptional.ifPresent(operation::setClient);
            }
            if (operationDto.getUserId() != null) {
                Optional<User> userOptional = userRepository.findById(operationDto.getUserId());
                userOptional.ifPresent(operation::setCreatedByUser);
            }
            return operation;
        }

    public void updateOperationFromDto(Operation operation, OperationDto operationDto) {
        if (operation != null && operationDto != null) {
            operation.setNameOperation(operationDto.getNameOperation());
            operation.setOperationDate(operationDto.getOperationDate());
            operation.setStatutOperation(operationDto.getStatutOperation());
            operation.setFacturer(operationDto.getFacturer());

            if (operationDto.getClientId() != null) {
                Optional<Client> clientOptional = clientRepository.findById(operationDto.getClientId());
                if (clientOptional.isPresent()) {
                    operation.setClient(clientOptional.get());
                } else {
                    throw new IllegalArgumentException("Client not found with id: " + operationDto.getClientId());
                }
            }

            if (operationDto.getPrestationId() != null) {
                Optional<Prestation> prestationOptional = prestationRepository.findById(operationDto.getPrestationId());
                if (prestationOptional.isPresent()) {
                    operation.setPrestation(prestationOptional.get());
                } else {
                    throw new IllegalArgumentException("Prestation not found with id: " + operationDto.getPrestationId());
                }
            }

        } else {
            throw new IllegalArgumentException("Operation or OperationDto is null");
        }
    }

    public ClientDto fromClient(Client client) {
        return this.modelMapper.map(client, ClientDto.class);
    }

    public ClientDTOResp fromClientToClientReq(Client client) {
        return this.modelMapper.map(client, ClientDTOResp.class);
    }


    public Client fromClientDto(ClientDto clientDto) {
        return this.modelMapper.map(clientDto, Client.class);
    }

    public void updateClientFromDto(Client client, ClientDto clientDto) {
        if (client == null || clientDto == null) {
            throw new IllegalArgumentException("Client or ClientDto is null");
        }

        Owner owner = null;
        Person manager = null;
        Notification notification = null;

        if (clientDto.getOwnerId() != null) {
            Optional<Owner> optionalOwner = ownerRepository.findById(clientDto.getOwnerId());
            if (optionalOwner.isPresent()) {
                owner = optionalOwner.get();
            }
        }

        if (clientDto.getManagerId() != null) {
            Optional<Person> optionalPerson = personRepository.findById(clientDto.getManagerId());
            if (optionalPerson.isPresent()) {
                manager = optionalPerson.get();
            }
        }

        if (clientDto.getNotificationId() != null) {
            Optional<Notification> optionalNotification = notificationRepository.findById(clientDto.getNotificationId());
            if (optionalNotification.isPresent()) {
                notification = optionalNotification.get();
            }
        }

        client.setTitle(clientDto.getTitle());
        client.setAbbreviation(clientDto.getAbbreviation());
        client.setAddress(clientDto.getAddress());
        client.setCreationDate(clientDto.getCreationDate());
        client.setFax(clientDto.getFax());
        client.setCity(clientDto.getCity());
        client.setIce(clientDto.getIce());
        client.setForm(clientDto.getForm());
        client.setCapital(clientDto.getCapital());
        client.setHeadquarters(clientDto.getHeadquarters());
        client.setRc(clientDto.getRc());
        client.setIfNumber(clientDto.getIfNumber());
        client.setCnss(clientDto.getCnss());
        client.setOwnership(clientDto.getOwnership());
        client.setPhoneNumber(clientDto.getPhoneNumber());
        client.setClientEmail(clientDto.getClientEmail());
        client.setStatus(clientDto.getStatus());
        client.setStartDate(clientDto.getStartDate());
        client.setEndDate(clientDto.getEndDate());
        client.setContrat(clientDto.getContrat());
        client.setOwner(owner);
        client.setManager(manager);
        client.setClientType(clientDto.getClientType());

        // Assure-toi de gérer la collection de notifications du client
        if (notification != null) {
            notification.setClient(client);
            client.getNotifications().add(notification);
        }
    }


    public EtapePrestationDTO fromEtapePrestation(EtapePrestation etapePrestation) {
        return this.modelMapper.map(etapePrestation, EtapePrestationDTO.class);
    }

    public EtapePrestation fromEtapePrestationDto(EtapePrestationDTO etapePrestationDto) {
        return this.modelMapper.map(etapePrestationDto, EtapePrestation.class);
    }

    public EtapeOperationDTO fromEtapeOperation(EtapeOperation etapeOperation) {
        return this.modelMapper.map(etapeOperation, EtapeOperationDTO.class);
    }

    public EtapeOperation fromEtapeOperationDto(EtapeOperationDTO etapeOperationDto) {
        return this.modelMapper.map(etapeOperationDto, EtapeOperation.class);
    }

    public void updateEtapeFromDto(EtapeOperation etape, EtapeOperationDTO etapeDto) {
        if (etape != null && etapeDto != null) {
            etape.setName(etapeDto.getName());
            etape.setStatus(etapeDto.getStatus());
            etape.setAttributes(etapeDto.getAttributes());

            if (etapeDto.getDocuments() != null) {
                List<Document> documents = etapeDto.getDocuments().stream()
                        .map(documentDto -> {
                            Document document = new Document();
                            document.setId(documentDto.getId());
                            document.setFileName(documentDto.getFileName());
                            document.setRegistrationDate(documentDto.getRegistrationDate());
                            document.setDocument(documentDto.getDocument());
                            document.setContentType(documentDto.getContentType());

                            // Associer le client existant au document
                            if (documentDto.getClientId() != null) {
                                Client existingClient = new Client();
                                existingClient.setId(documentDto.getClientId());
                                document.setClient(existingClient);
                            } else {
                                document.setClient(null); // ou gérer selon votre logique métier
                            }

                            document.setEtape(etape);
                            return document;
                        }).collect(Collectors.toList());
                etape.setDocuments(documents);
            }
        } else {
            throw new IllegalArgumentException("Etape or EtapeDto is null");
        }
    }

    public RemarqueDto fromRemarqueOperation(RemarqueOperation remarqueOperation) {
        return this.modelMapper.map(remarqueOperation, RemarqueDto.class);
    }

    public RemarqueOperation fromRemarqueOperationDto(RemarqueDto remarqueDto) {
        return this.modelMapper.map(remarqueDto, RemarqueOperation.class);
    }

    public NotificationDto fromNotification(Notification notification) {
        return this.modelMapper.map(notification, NotificationDto.class);
    }

    public Notification fromNotificationDto(NotificationDto notificationDto) {
        return this.modelMapper.map(notificationDto, Notification.class);
    }

    public void updateRemarqueFromDto(RemarqueOperation remarqueOperation,RemarqueDto remarqueDto) {
        Date currentDate=new Date();
        if (remarqueOperation != null && remarqueDto != null) {
            remarqueOperation.setNameRemarque(remarqueDto.getNameRemarque());
            remarqueOperation.setRemarqueDate(remarqueDto.getRemarqueDate());
            remarqueOperation.setOperation(operationRepository.findById(remarqueDto.getOperationId()).get());
            remarqueOperation.setDesc(remarqueDto.getDesc());
            remarqueOperation.setRemarqueDate(currentDate);
        }else {
            throw new IllegalArgumentException("Remarque or RemarqueDto is null");
        }
    }



    public PrestationDto fromPrestation(Prestation prestation) {
        return this.modelMapper.map(prestation, PrestationDto.class);
    }

    public Prestation fromPrestationDto(PrestationDto prestationDto) {
        return this.modelMapper.map(prestationDto, Prestation.class);
    }

    public void updatePrestationFromDto(Prestation prestation, PrestationDto prestationDto) {
        if (prestation != null && prestationDto != null) {
            prestation.setName(prestationDto.getName());
            prestation.setDescription(prestationDto.getDescription());
            prestation.setEtapes(prestationDto.getEtapes().stream()
                    .map(this::fromEtapePrestationDto)
                    .collect(Collectors.toList()));
            prestation.setOperations(prestationDto.getOperations().stream()
                    .map(this::fromOperationDto)
                    .collect(Collectors.toList()));
        } else {
            throw new IllegalArgumentException("Prestation or PrestationDto is null");
        }
    }

    public RoleDto fromRole(Role role) {
        return this.modelMapper.map(role, RoleDto.class);
    }

    public Role fromRoleDto(RoleDto roleDto) {
        return this.modelMapper.map(roleDto, Role.class);
    }

    public void updateRoleFromDto(Role role, RoleDto roleDto) {
        if (role != null && roleDto != null) {
            role.setRoleName(roleDto.getRoleName());
        } else {
            throw new IllegalArgumentException("Role or RoleDto is null");
        }
    }

    public UserDto fromUser(User user) {
        return this.modelMapper.map(user, UserDto.class);
    }
    public UserDtoResp toUserDtoResp(User user) {
        return this.modelMapper.map(user, UserDtoResp.class);
    }

    public User fromUserDto(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }

    public void updateUserFromDto(User user, UserDto userDto) {
        if (user == null || userDto == null) {
            throw new IllegalArgumentException("User or UserDto is null");
        }

        if (userDto.getName() != null && !userDto.getName().isEmpty()) {
            user.setName(userDto.getName());
        }
        if (userDto.getSurname() != null && !userDto.getSurname().isEmpty()) {
            user.setSurname(userDto.getSurname());
        }
        if (userDto.getUsername() != null && !userDto.getUsername().isEmpty()) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(userDto.getPassword());
        }
        if (userDto.getConfirmPassword() != null && !userDto.getConfirmPassword().isEmpty()) {
            user.setConfirmPassword(userDto.getConfirmPassword());
        }
        if (userDto.getJobPosition() != null && !userDto.getJobPosition().isEmpty()) {
            user.setJobPosition(userDto.getJobPosition());
        }

        // For boolean status, set it directly
        user.setStatus(userDto.getStatus());

        if (userDto.getMail() != null && !userDto.getMail().isEmpty()) {
            user.setMail(userDto.getMail());
        }

        if (userDto.getRoleId() != null) {
            Optional<Role> roleOptional = roleRepository.findById(userDto.getRoleId());
            if (roleOptional.isPresent()) {
                // Use a mutable list
                user.setRoles(new ArrayList<>(Collections.singletonList(roleOptional.get())));
            } else {
                throw new IllegalArgumentException("Role not found with id: " + userDto.getRoleId());
            }
        }

    }

    public DocumentDto fromDocument(Document document) {
        return this.modelMapper.map(document, DocumentDto.class);
    }

    public DocumentResponseDto ToDocumentResponseDto(Document document) {
        return this.modelMapper.map(document, DocumentResponseDto.class);
    }

    public Document fromDocumentDto(DocumentDto documentDto) {
        return this.modelMapper.map(documentDto, Document.class);
    }

    public DocumentDto ToDocumentDto(Document document) {
        return this.modelMapper.map(document, DocumentDto.class);
    }

    public void updateDocumentFromDto(Document document, DocumentDto documentDto) {
        if (document != null && documentDto != null) {
            document.setFileName(documentDto.getFileName());
            document.setRegistrationDate(documentDto.getRegistrationDate());
            document.setDocument(documentDto.getDocument());
            document.setContentType(documentDto.getContentType());

            if (documentDto.getEtapeId() != null) {
                Optional<EtapeOperation> etapeOptional = etapeOperationRepository.findById(documentDto.getEtapeId());
                if (etapeOptional.isPresent()) {
                    document.setEtape(etapeOptional.get());
                } else {
                    throw new IllegalArgumentException("Etape not found with ID: " + documentDto.getEtapeId());
                }
            }

            if (documentDto.getClientId() != null) {
                Optional<Client> clientOptional = clientRepository.findById(documentDto.getClientId());
                if (clientOptional.isPresent()) {
                    document.setClient(clientOptional.get());
                } else {
                    throw new IllegalArgumentException("Client not found with ID: " + documentDto.getClientId());
                }
            } else {
                throw new IllegalArgumentException("clientId in DocumentDto is null");
            }
        } else {
            throw new IllegalArgumentException("Document or DocumentDto is null");
        }
    }



    public void partialUpdateClientFromDto (Client client, ClientDto clientDto){
        Owner owner = null;
        Person manager = null;
        if (clientDto.getOwnerId() != null) {
            Optional<Owner> optionalOwner = this.ownerRepository.findById(clientDto.getOwnerId());
            owner = optionalOwner.get();
        }
        if (clientDto.getManagerId() != null) {
            Optional<Person> optionalPerson = this.personRepository.findById(clientDto.getManagerId());
            manager = optionalPerson.get();

        }
        if (client != null && clientDto != null) {
            if (clientDto.getTitle() != null) {
                client.setTitle(clientDto.getTitle());
            }
            if (clientDto.getAbbreviation() != null) {
                client.setAbbreviation(clientDto.getAbbreviation());
            }
            if (clientDto.getAddress() != null) {
                client.setAddress(clientDto.getAddress());
            }
            if (clientDto.getCreationDate() != null) {
                client.setCreationDate(clientDto.getCreationDate());
            }
            if (clientDto.getFax() != null) {
                client.setFax(clientDto.getFax());
            }
            if (clientDto.getCity() != null) {
                client.setCity(clientDto.getCity());
            }
            if (clientDto.getIce() != null) {
                client.setIce(clientDto.getIce());
            }
            if (clientDto.getForm() != null) {
                client.setForm(clientDto.getForm());
            }
            if (clientDto.getCapital() != null) {
                client.setCapital(clientDto.getCapital());
            }
            if (clientDto.getHeadquarters() != null) {
                client.setHeadquarters(clientDto.getHeadquarters());
            }
            if (clientDto.getRc() != null) {
                client.setRc(clientDto.getRc());
            }
            if (clientDto.getIfNumber() != null) {
                client.setIfNumber(clientDto.getIfNumber());
            }
            if (clientDto.getCnss() != null) {
                client.setCnss(clientDto.getCnss());
            }
            if (clientDto.getOwnership() != null) {
                client.setOwnership(clientDto.getOwnership());
            }
            if (clientDto.getPhoneNumber() != null) {
                client.setPhoneNumber(clientDto.getPhoneNumber());
            }
            if (clientDto.getClientEmail() != null) {
                client.setClientEmail(clientDto.getClientEmail());
            }
            if (clientDto.getStatus() != null) {
                client.setStatus(clientDto.getStatus());
            }
            if(clientDto.getStartDate() != null){
                client.setStartDate(clientDto.getStartDate());
            }
            if(clientDto.getEndDate() != null){
                client.setEndDate(clientDto.getEndDate());
            }
            if (clientDto.getContrat() != null) {
                client.setContrat(clientDto.getContrat());
            }
            if(clientDto.getTaxProfessionnel()!=null){
                client.setTaxProfessionnel(clientDto.getTaxProfessionnel());
            }
            if (clientDto.getOwnerId() != null) {
                client.setOwner(owner);
            }
            if (clientDto.getManagerId() != null) {
                client.setManager(manager);
            }
        } else {
            throw new IllegalArgumentException("Client or ClientDto is null");
        }
    }



        public void updatePersonFromDto (Person person, PersonDTO personDTO){
            if (person != null && personDTO != null) {
                person.setFirstName(personDTO.getFirstName());
                person.setLastName(personDTO.getLastName());
                person.setAddress(personDTO.getAddress());
                person.setAddressComplement(personDTO.getAddressComplement());
                person.setPostalCode(personDTO.getPostalCode());
                person.setCity(personDTO.getCity());
                person.setPhoneNumber(personDTO.getPhoneNumber());
                person.setClientEmail(personDTO.getClientEmail());
                person.setCin(personDTO.getCin());
                person.setBirthPlace(personDTO.getBirthPlace());
                person.setNationality(personDTO.getNationality());
                person.setGender(personDTO.getGender());
                person.setPassportNumber(personDTO.getPassportNumber());
                person.setNieNumber(personDTO.getNieNumber());
                person.setPassportExpiryDate(personDTO.getPassportExpiryDate());
                person.setNieExpiryDate(personDTO.getNieExpiryDate());
                person.setBirthDate(personDTO.getBirthDate());
            } else {
                throw new IllegalArgumentException("Person or PersonDTO is null");
            }
        }

        public void updateOwnerFromDto (Owner owner, OwnerDTO ownerDTO){
            if (owner != null && ownerDTO != null) {
                updatePersonFromDto(owner, ownerDTO);
                owner.setSomeOwnerSpecificField(ownerDTO.getSomeOwnerSpecificField());
            } else {
                throw new IllegalArgumentException("Owner or OwnerDTO is null");
            }
        }

        public void updateCollaboratorFromDto (Collaborator collaborator, CollaboratorDTO collaboratorDTO){
            if (collaborator != null && collaboratorDTO != null) {
                updatePersonFromDto(collaborator, collaboratorDTO);
                collaborator.setSomeCollaboratorSpecificField(collaboratorDTO.getSomeCollaboratorSpecificField());
            } else {
                throw new IllegalArgumentException("Collaborator or CollaboratorDTO is null");
            }
        }

        public void updatePartnerFromDto (Partner partner, PartnerDTO partnerDTO){
            if (partner != null && partnerDTO != null) {
                updatePersonFromDto(partner, partnerDTO);
                partner.setParticipationAmount(partnerDTO.getParticipationAmount());
                partner.setParticipationPercentage(partnerDTO.getParticipationPercentage());
                if (partnerDTO.getClientId() != null) {
                    Client client = clientRepository.findById(partnerDTO.getClientId()).get();
                    partner.setClient(client);
                }
            } else {
                throw new IllegalArgumentException("Partner or PartnerDTO is null");

            }
        }


    public Owner fromOwnerDto(OwnerDTO ownerDTO) {
        return modelMapper.map(ownerDTO, Owner.class);
    }

    public OwnerDTO toOwnerDto(Owner owner) {
        return modelMapper.map(owner, OwnerDTO.class);
    }

    public Collaborator fromCollaboratorDto(CollaboratorDTO collaboratorDTO) {
        return modelMapper.map(collaboratorDTO, Collaborator.class);
    }

    public CollaboratorDTO toCollaboratorDto(Collaborator collaborator) {
        return modelMapper.map(collaborator, CollaboratorDTO.class);
    }

    public Partner fromPartnerDto(PartnerDTO partnerDTO) {
        return modelMapper.map(partnerDTO, Partner.class);
    }

    public PartnerDTO toPartnerDto(Partner partner) {
        return modelMapper.map(partner, PartnerDTO.class);
    }

    public Person fromPersonDto(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    public PersonDTO toPersonDto(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    public PersonDTORsep toPersonDTOResp(Person person) {
        return modelMapper.map(person, PersonDTORsep.class);
    }
}


