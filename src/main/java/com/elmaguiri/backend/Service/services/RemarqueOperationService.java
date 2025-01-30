package com.elmaguiri.backend.Service.services;

import com.elmaguiri.backend.Service.dtos.OperationDto;
import com.elmaguiri.backend.Service.dtos.RemarqueDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RemarqueOperationService {
    List<RemarqueDto> getRemarqueByOperation(Long operationId);
    List<RemarqueDto> getAllRemarques();
    Optional<RemarqueDto> getRemarqueById(Long id);
    RemarqueDto createRemarque(RemarqueDto remarqueDto);
    RemarqueDto updateRemarque(Long id, RemarqueDto remarqueDto);
    String deleteRemarque(Long id);
}
