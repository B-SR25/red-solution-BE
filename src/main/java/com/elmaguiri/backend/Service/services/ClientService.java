package com.elmaguiri.backend.Service.services;

import com.elmaguiri.backend.Service.dtos.ClientDTOResp;
import com.elmaguiri.backend.Service.dtos.ClientDto;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<ClientDto> getAllClients();

    List<ClientDTOResp> getClientByStatus(Boolean status);

    List<ClientDto> getClientsByClientType(String clientType);

    List<ClientDto> getClientsByClientTypeAndStatus(String clientType, Boolean status);

    Optional<ClientDto> getClientById(Long id) ;

    ClientDto createClient(ClientDto clientDto);

    ClientDto updateClient(Long id, ClientDto clientDto);

    ClientDto partialUpdateClient(Long id, ClientDto clientDto);

    String deleteClient(Long id);

    Optional<ClientDto> getClientByRc(Long rc);

    Optional<ClientDto> getClientByTitle(String title);

    List<String> getAllClientTitles();


}
