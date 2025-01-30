package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Enum.OperationState;
import com.elmaguiri.backend.Service.dtos.RemarqueDto;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.Service.services.RemarqueOperationService;
import com.elmaguiri.backend.dao.entities.Operation;
import com.elmaguiri.backend.dao.entities.RemarqueOperation;
import com.elmaguiri.backend.dao.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RemarqueOperationServiceImpl implements RemarqueOperationService {

    private final RemarqueRepository remarqueRepository;
    private final OperationRepository operationRepository;
    private final ModelMapperConfig modelMapperConfig;
    @Autowired
    public RemarqueOperationServiceImpl(OperationRepository operationRepository,RemarqueRepository remarqueRepository,  ModelMapperConfig modelMapperConfig) {
        this.operationRepository = operationRepository;
        this.remarqueRepository=remarqueRepository;
        this.modelMapperConfig = modelMapperConfig;
    }
    @Override
    public List<RemarqueDto> getRemarqueByOperation(Long operationId) {
        List<RemarqueOperation> remarqueOperation  = remarqueRepository.findByOperationId(operationId);
        return remarqueOperation.stream()
                .map(modelMapperConfig::fromRemarqueOperation)
                .collect(Collectors.toList());
    }

    @Override
    public List<RemarqueDto> getAllRemarques() {
        return remarqueRepository.findAll()
                .stream()
                .map(modelMapperConfig::fromRemarqueOperation)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RemarqueDto> getRemarqueById(Long id) {
        return remarqueRepository.findById(id)
                .map(modelMapperConfig::fromRemarqueOperation);
    }


    @Override
    public RemarqueDto createRemarque(RemarqueDto remarqueDto) {
        Date currentDate = new Date();
        RemarqueOperation remarque = modelMapperConfig.fromRemarqueOperationDto(remarqueDto);

        // Retrieve the operation and set its state to BLOQUEE
        Operation operation = operationRepository.findById(remarqueDto.getOperationId())
                .orElseThrow(() -> new IllegalArgumentException("Operation not found with id: " + remarqueDto.getOperationId()));
        //operation.setStatutOperation(OperationState.BLOQUEE);
        remarque.setOperation(operation);
        remarque.setRemarqueDate(currentDate);
        remarque.setNameRemarque(remarqueDto.getNameRemarque());
        remarque.setDesc(remarqueDto.getDesc());

        // Save the updated operation
        operationRepository.save(operation);

        // Save the new remarque
        RemarqueOperation savedRemarque = remarqueRepository.save(remarque);
        return modelMapperConfig.fromRemarqueOperation(savedRemarque);
    }


    @Override
    public RemarqueDto updateRemarque(Long id, RemarqueDto remarqueDto) {
        Optional<RemarqueOperation> existingRemarque = remarqueRepository.findById(id);
        if (existingRemarque.isPresent()) {
            modelMapperConfig.updateRemarqueFromDto(existingRemarque.get(), remarqueDto);
            RemarqueOperation updatedRemarque = remarqueRepository.save(existingRemarque.get());
            return modelMapperConfig.fromRemarqueOperation(updatedRemarque);
        } else {
            throw new IllegalArgumentException("Remarque not found with ID: " + id);
        }
    }

    @Override
    public String deleteRemarque(Long id) {
        Optional<RemarqueOperation> operationOptional = remarqueRepository.findById(id);
        if (operationOptional.isPresent()) {
            remarqueRepository.deleteById(id);
            return "Remarque with id: " + id + " has been deleted successfully.";
        } else {
            throw new RuntimeException("Remarque not found with id: " + id);
        }
    }

}
