package com.elmaguiri.backend.Service.services;

import com.elmaguiri.backend.Service.dtos.EtapePrestationDTO;

import java.util.List;
import java.util.Optional;
import com.elmaguiri.backend.Service.dtos.PrestationDto;
public interface PrestationService {
    List<PrestationDto> getAllPrestations();
    Optional<PrestationDto> getPrestationById(Long id);
    List<EtapePrestationDTO> getEtapesByPrestationId(Long prestationId);
    Optional<PrestationDto> getPrestationByName(String prestationName);
    PrestationDto createPrestation(PrestationDto prestationDto);
    PrestationDto updatePrestation(Long id, PrestationDto prestationDto);
    String deletePrestation(Long id);
    List<String> getAllPrestationNames();
}


