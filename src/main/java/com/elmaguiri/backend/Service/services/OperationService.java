package com.elmaguiri.backend.Service.services;

import com.elmaguiri.backend.Enum.OperationState;
import com.elmaguiri.backend.Service.dtos.EtapeOperationDTO;
import com.elmaguiri.backend.Service.dtos.OperationDto;

import com.elmaguiri.backend.Service.dtos.SearchCriteriaDto;
import com.elmaguiri.backend.dao.entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface OperationService {
/*
    private final OperationRepository operationRepository;
    private final ModelMapperConfig modelMapperConfig;
    private final ClientRepository clientRepository;
    private final PrestationRepository prestationRepository;
    private final UserRepository userRepository;
    private final ClientService clientService;

    @Autowired

    public OperationService(OperationRepository operationRepository, ModelMapperConfig modelMapperConfig, ClientRepository clientRepository, PrestationRepository prestationRepository, UserRepository userRepository, ClientService clientService) {
        this.operationRepository = operationRepository;
        this.modelMapperConfig = modelMapperConfig;
        this.clientRepository = clientRepository;
        this.prestationRepository = prestationRepository;
        this.userRepository = userRepository;
        this.clientService = clientService;
    }

    public List<OperationDto> getOperationsByClient(Long clientId) {
        List<Operation> operations  = operationRepository.findByClientId(clientId);
        return operations.stream()
                .map(modelMapperConfig::fromOperation)
                .collect(Collectors.toList());
    }

    public List<OperationDto> getAllOperations() {
        List<Operation> operations = operationRepository.findAll();
        return operations.stream()
                .map(modelMapperConfig::fromOperation)
                .collect(Collectors.toList());
    }

    public List<OperationDto> getActiveOperations() {
        List<Operation> operations = operationRepository.findAllOperationsWithActiveClients();
        return operations.stream()
                .map(modelMapperConfig::fromOperation)
                .collect(Collectors.toList());
    }
    public Optional<OperationDto> getOperationById(Long id) {
        Optional<Operation> operationOptional = operationRepository.findById(id);
        return operationOptional.map(modelMapperConfig::fromOperation);
    }

    public OperationDto createOperation(OperationDto operationDto) {
        Date currentDate = new Date();
        Operation operation = modelMapperConfig.fromOperationDto(operationDto);
        operation.setClient(clientRepository.findById(operationDto.getClientId()).get());
        operation.setPrestation(prestationRepository.findById(operationDto.getPrestationId()).get());
        operation.setCreatedByUser(userRepository.findById(operationDto.getUserId()).get());
        operation.setOperationDate(currentDate);
        operation.setRemarqueOperation(operationDto.getRemarqueOperation());
        operation.setStatutOperation(operationDto.getStatutOperation());
        Operation savedOperation = operationRepository.save(operation);
        return modelMapperConfig.fromOperation(savedOperation);
    }

    public OperationDto updateOperation(Long id, OperationDto operationDto) {
        Date currentDate = new Date();
        Optional<Operation> operationOptional = operationRepository.findById(id);
        if (operationOptional.isPresent()) {
            Operation operationToUpdate = operationOptional.get();
            modelMapperConfig.updateOperationFromDto(operationToUpdate, operationDto);
            operationToUpdate.setClient(clientRepository.findById(operationDto.getClientId()).get());
            operationToUpdate.setPrestation(prestationRepository.findById(operationDto.getPrestationId()).get());
            operationToUpdate.setCreatedByUser(userRepository.findById(operationDto.getUserId()).get());
            operationToUpdate.setOperationDate(currentDate);
            operationToUpdate.setRemarqueOperation(operationDto.getRemarqueOperation());
            operationToUpdate.setStatutOperation(operationDto.getStatutOperation());
            Operation updatedOperation = operationRepository.save(operationToUpdate);
            return modelMapperConfig.fromOperation(updatedOperation);
        } else {
            throw new RuntimeException("Operation not found with id: " + id);
        }
    }

    public String deleteOperation(Long id) {
        Optional<Operation> operationOptional = operationRepository.findById(id);
        if (operationOptional.isPresent()) {
            operationRepository.deleteById(id);
            return "Operation with id: " + id + " has been deleted successfully.";
        } else {
            throw new RuntimeException("Operation not found with id: " + id);
        }
    }*/
    List<OperationDto> getOperationsByClient(Long clientId);
    List<OperationDto> getAllOperations();
    Optional<OperationDto> getOperationById(Long id);
    OperationDto createOperation(OperationDto operationDto);
    OperationDto updateOperation(Long id, OperationDto operationDto);
    String deleteOperation(Long id);
    EtapeOperationDTO updateEtapeOperation(Long operationId, Long etapeId, EtapeOperationDTO etapeOperationDto, MultipartFile[] files) throws IOException;

    OperationDto createOperationWithEtapes( OperationDto operationDto);
    List<OperationDto> getActiveOperations();

    List<EtapeOperationDTO> getStepsByOperationId(Long operationId);
    Optional<EtapeOperationDTO> getEtapeById(long etapeId);

    Page<OperationDto> findByCriteria(SearchCriteriaDto searchCriteria, Pageable pageable);

    long getCountByStatus(OperationState status );

    Map<OperationState, Long> getAllCountsByStatus();

    Operation updateOperationStatus(Long id, OperationState newStatus);

    String getNextCode();

    //String getNextOperationCode();

}
