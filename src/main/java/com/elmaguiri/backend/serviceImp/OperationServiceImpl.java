package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Enum.OperationState;
import com.elmaguiri.backend.Service.OperationSpecs;
import com.elmaguiri.backend.Service.dtos.EtapeOperationDTO;
import com.elmaguiri.backend.Service.dtos.OperationDto;
import com.elmaguiri.backend.Service.dtos.SearchCriteriaDto;
import com.elmaguiri.backend.Service.services.OperationService;
import com.elmaguiri.backend.dao.entities.*;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.dao.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    private final PrestationRepository prestationRepository;
    private final EtapeOperationRepo etapeOperationRepository;
    private final ModelMapperConfig modelMapperConfig;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final RemarqueRepository remarqueRepository;

    @Autowired
    public OperationServiceImpl(OperationRepository operationRepository, PrestationRepository prestationRepository, EtapeOperationRepo etapeOperationRepository, ModelMapperConfig modelMapperConfig, ClientRepository clientRepository, UserRepository userRepository, DocumentRepository documentRepository, RemarqueRepository remarqueRepository) {
        this.operationRepository = operationRepository;
        this.prestationRepository = prestationRepository;
        this.etapeOperationRepository = etapeOperationRepository;
        this.modelMapperConfig = modelMapperConfig;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.remarqueRepository = remarqueRepository;
    }

    private static final String PREFIX = "D";

    //search implementation
    public Page<OperationDto> findByCriteria(SearchCriteriaDto searchCriteria, Pageable pageable) {
        Specification<Operation> spec = Specification.where(null);

        try {

            if (searchCriteria.getClientName() != null && !searchCriteria.getClientName().isEmpty()) {
                spec = spec.and(OperationSpecs.hasClientName(searchCriteria.getClientName()));
            }
            if (searchCriteria.getPrestationId() != null && !searchCriteria.getPrestationId().isEmpty()) {
                spec = spec.and(OperationSpecs.hasPrestationId(Long.parseLong(searchCriteria.getPrestationId())));
            }
            if (searchCriteria.getOperationName() != null && !searchCriteria.getOperationName().isEmpty()) {
                spec = spec.and(OperationSpecs.containsName(searchCriteria.getOperationName()));
            }
            if (searchCriteria.getOperationStatus() != null ) {
                spec = spec.and(OperationSpecs.hasStatus(searchCriteria.getOperationStatus()));
            }
            if (searchCriteria.getDateRange() != null) {
                spec = spec.and(OperationSpecs.withinDateRange(searchCriteria.getDateRange()));
            }

            List<OperationDto> operationDtos = this.operationRepository.findAll(spec, pageable)
                    .stream()
                    .map(modelMapperConfig::fromOperation)
                    .collect(Collectors.toList());

            long totalElements = this.operationRepository.count(spec);

            return new PageImpl<>(operationDtos, pageable, totalElements);

        } catch (NumberFormatException e) {
            throw new HttpMessageNotReadableException("Invalid number format in search criteria", e);
        }
    }

    @Override
    public long getCountByStatus(OperationState status) {
        return operationRepository.countByStatus(status);
    }

    @Override
    public Map<OperationState, Long> getAllCountsByStatus() {
        List<Object[]> results = operationRepository.countAllByStatus();
        Map<OperationState, Long> counts = new EnumMap<>(OperationState.class);
        for (Object[] result : results) {
            counts.put((OperationState) result[0], (Long) result[1]);
        }
        return counts;
    }

    @Override
    public List<OperationDto> getOperationsByClient(Long clientId) {
        List<Operation> operations  = operationRepository.findByClientId(clientId);
        return operations.stream()
                .map(modelMapperConfig::fromOperation)
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationDto> getAllOperations() {
        return operationRepository.findAll()
                .stream()
                .map(modelMapperConfig::fromOperation)
                .collect(Collectors.toList());
    }

    @Override
    public List<OperationDto> getActiveOperations() {
        List<Operation> operations = operationRepository.findAllOperationsWithActiveClients();
        operations.sort(Comparator.comparing(Operation::getOperationDate).reversed());
        return operations.stream()
                .map(modelMapperConfig::fromOperation)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OperationDto> getOperationById(Long id) {
        return operationRepository.findById(id)
                .map(modelMapperConfig::fromOperation);
    }

    @Override
    public OperationDto createOperation(OperationDto operationDto) {
        Date currentDate = new Date();
        Operation operation = modelMapperConfig.fromOperationDto(operationDto);
        operation.setClient(clientRepository.findById(operationDto.getClientId()).get());
        operation.setPrestation(prestationRepository.findById(operationDto.getPrestationId()).get());
        operation.setCreatedByUser(userRepository.findById(operationDto.getUserId()).get());
        operation.setOperationDate(currentDate);
        operation.setStatutOperation(operationDto.getStatutOperation());
        Operation savedOperation = operationRepository.save(operation);
        return modelMapperConfig.fromOperation(savedOperation);
    }

    @Override
    public OperationDto updateOperation(Long id, OperationDto operationDto) {
        Optional<Operation> existingOperation = operationRepository.findById(id);
        if (existingOperation.isPresent()) {
            modelMapperConfig.updateOperationFromDto(existingOperation.get(), operationDto);
            Operation updatedOperation = operationRepository.save(existingOperation.get());
            return modelMapperConfig.fromOperation(updatedOperation);
        } else {
            throw new IllegalArgumentException("Operation not found with ID: " + id);
        }
    }

    @Override
    @Transactional
    public String deleteOperation(Long id) {
        Optional<Operation> operationOptional = operationRepository.findById(id);
        if (operationOptional.isPresent()) {
            Operation operation = operationOptional.get();

            // Supprimer les documents associés aux étapes
            operation.getEtapeOperations().forEach(etapeOperation -> {
                documentRepository.deleteAll(etapeOperation.getDocuments());
            });

            // Supprimer les étapes associées
            etapeOperationRepository.deleteAll(operation.getEtapeOperations());

            // Supprimer les remarques associées
            remarqueRepository.deleteAll(operation.getRemarqueOperation());

            // Supprimer l'opération
            operationRepository.delete(operation);

            return "Operation with id: " + id + " has been deleted successfully.";
        } else {
            throw new RuntimeException("Operation not found with id: " + id);
        }
    }

    @Override
    public OperationDto createOperationWithEtapes( OperationDto operationDto) {
        Long prestationId=operationDto.getPrestationId();
        Optional<Prestation> prestationOptional = prestationRepository.findById(prestationId);
        if (!prestationOptional.isPresent()) {
            throw new IllegalArgumentException("Prestation not found with ID: " + prestationId);
        }

        Operation operation = modelMapperConfig.fromOperationDto(operationDto);

        operation.setPrestation(prestationOptional.get());
        // Récupérer les étapes de la prestation et les ajouter à l'opération
        List<EtapePrestation> etapesPrestation = prestationOptional.get().getEtapes();
        List<EtapeOperation> etapesOperation = etapesPrestation.stream().map(etapePrestation -> {
            EtapeOperation etapeOperation = new EtapeOperation();
            etapeOperation.setStepOrder(etapePrestation.getStepOrder());
            etapeOperation.setColor(etapePrestation.getColor());
            etapeOperation.setName(etapePrestation.getName());
            etapeOperation.setAttributes(etapePrestation.getAttributesTemplate());
            etapeOperation.setOperation(operation);
            etapeOperation.setStatus("EN_COURS");
            return etapeOperation;
        }).collect(Collectors.toList());

        operation.setEtapeOperations(etapesOperation);

        // Ajouter la date d'aujourd'hui à l'opération
        Date currentDate = new Date();
        operation.setOperationDate(currentDate);

        Operation savedOperation = operationRepository.save(operation);
        etapeOperationRepository.saveAll(etapesOperation);  // Sauvegarder les étapes de l'opération

        return modelMapperConfig.fromOperation(savedOperation);
    }


    @Override
    public EtapeOperationDTO updateEtapeOperation(Long operationId, Long etapeId, EtapeOperationDTO etapeOperationDto, MultipartFile[] files) throws IOException {
        Optional<Operation> operationOptional = operationRepository.findById(operationId);
        if (!operationOptional.isPresent()) {
            throw new IllegalArgumentException("Operation not found with ID: " + operationId);
        }

        Optional<EtapeOperation> etapeOperationOptional = etapeOperationRepository.findById(etapeId);
        if (!etapeOperationOptional.isPresent()) {
            throw new IllegalArgumentException("EtapeOperation not found with ID: " + etapeId);
        }

        EtapeOperation etapeOperation = etapeOperationOptional.get();
        modelMapperConfig.updateEtapeFromDto(etapeOperation, etapeOperationDto);

        if (files != null) {
            Date today = new Date();
            for (MultipartFile file : files) {
                Document document = new Document();
                document.setFileName(file.getOriginalFilename());
                document.setContentType(file.getContentType());
                document.setDocument(file.getBytes());
                document.setEtape(etapeOperation);
                document.setRegistrationDate(today);
                document.setClient(operationOptional.get().getClient());
                documentRepository.save(document);
            }
        }

        EtapeOperation updatedEtapeOperation = etapeOperationRepository.save(etapeOperation);
        return modelMapperConfig.fromEtapeOperation(updatedEtapeOperation);
    }

    @Override
    public List<EtapeOperationDTO> getStepsByOperationId(Long operationId) {
        return etapeOperationRepository.findByOperationId(operationId).stream()
                .map(modelMapperConfig::fromEtapeOperation)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EtapeOperationDTO> getEtapeById(long etapeId){
        return etapeOperationRepository.findById(etapeId)
                .map(modelMapperConfig::fromEtapeOperation);
    }

    @Override
    public Operation updateOperationStatus(Long id, OperationState newStatus) {
        Optional<Operation> optionalOperation = operationRepository.findById(id);
        if (optionalOperation.isPresent()) {
            Operation operation = optionalOperation.get();
            operation.setStatutOperation(newStatus);
            return operationRepository.save(operation);
        } else {
            throw new RuntimeException("Operation not found with id " + id);
        }
    }

    @Override
    public String getNextCode() {
        String lastCode = operationRepository.findLastCode();
        lastCode = (lastCode != null) ? lastCode : PREFIX + getCurrentYearMonth() + "000";

        // Extract the numeric part of the code
        int lastNumber = Integer.parseInt(lastCode.substring(PREFIX.length() + 6)); // Skip the PREFIX and year/month
        int nextNumber = lastNumber + 1;

        // Format the next number with leading zeros
        String nextCode = PREFIX + getCurrentYearMonth() + String.format("%03d", nextNumber);

        return nextCode;
    }

    // Helper method to get current year and month in YYYYMM format
    private String getCurrentYearMonth() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getYear() + String.format("%02d", currentDate.getMonthValue());
    }


}