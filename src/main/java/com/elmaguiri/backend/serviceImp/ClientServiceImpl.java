package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Service.dtos.ClientDTOResp;
import com.elmaguiri.backend.Service.dtos.ClientDto;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.Service.services.ClientService;
import com.elmaguiri.backend.dao.entities.Client;
import com.elmaguiri.backend.dao.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ModelMapperConfig modelMapperConfig) {
        this.clientRepository = clientRepository;
        this.modelMapperConfig = modelMapperConfig;
    }


    @Override
    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(modelMapperConfig::fromClient)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientDTOResp> getClientByStatus(Boolean status) {
        List<Client> clients = clientRepository.findByStatus(status);
        List<ClientDTOResp> clientDtos= clients.stream()
                .map(modelMapperConfig::fromClientToClientReq)
                .collect(Collectors.toList());
        return clientDtos;
    }


    @Override
    public List<ClientDto> getClientsByClientType(String clientType){
        List<Client> clients = clientRepository.findByClientType(clientType);
        List<ClientDto> clientsDtosList = clients.stream()
                .map(modelMapperConfig::fromClient)
                .collect(Collectors.toList());
        return clientsDtosList;
    }

    @Override
    public List<ClientDto> getClientsByClientTypeAndStatus(String clientType,Boolean status){
        List<Client> clients = clientRepository.findByClientTypeAndStatus(clientType,status);
        List<ClientDto> clientsDtosList = clients.stream()
                .map(modelMapperConfig::fromClient)
                .collect(Collectors.toList());
        return clientsDtosList;
    }
    @Override
    public Optional<ClientDto> getClientById(Long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        return clientOptional.map(modelMapperConfig::fromClient);
    }

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        Client client = modelMapperConfig.fromClientDto(clientDto);
        Client savedClient = clientRepository.save(client);
        return modelMapperConfig.fromClient(savedClient);
    }

    @Override
    public ClientDto updateClient(Long id, ClientDto clientDto) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            Client clientToUpdate = clientOptional.get();

            // Update fields from ClientDto
            modelMapperConfig.updateClientFromDto(clientToUpdate, clientDto);

            // Update contrat if contract file is provided
            if (clientDto.getContrat() != null) {
                clientToUpdate.setContrat(clientDto.getContrat());
                // Process the contrat file here if needed
            }

            // Save updated client
            Client updatedClient = clientRepository.save(clientToUpdate);
            return modelMapperConfig.fromClient(updatedClient);
        } else {
            throw new RuntimeException("Client not found with id: " + id);
        }
    }

    @Override
    public ClientDto partialUpdateClient(Long id, ClientDto clientDto) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            Client clientToUpdate = clientOptional.get();
            modelMapperConfig.partialUpdateClientFromDto( clientToUpdate,clientDto); // Map only non-null fields
            Client updatedClient = clientRepository.save(clientToUpdate);
            return modelMapperConfig.fromClient(updatedClient);
        } else {
            throw new RuntimeException("Client not found with id: " + id);
        }
    }


    @Override
    public String deleteClient(Long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            clientRepository.deleteById(id);
            return "Client with id: " + id + " has been deleted successfully.";
        } else {
            throw new RuntimeException("Client not found with id: " + id);
        }
    }

    @Override
    public Optional<ClientDto> getClientByRc(Long rc) {
        Optional<Client> clientOptional = clientRepository.findByRc(rc);
        return clientOptional.map(modelMapperConfig::fromClient);
    }

    @Override
    public Optional<ClientDto> getClientByTitle(String title) {
        Optional<Client> clientOptional = clientRepository.findByTitle(title);
        return clientOptional.map(modelMapperConfig::fromClient);
    }

    @Override
    public List<String> getAllClientTitles() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(Client::getTitle).collect(Collectors.toList());
    }
}
