package com.elmaguiri.backend.Service.services;

import com.elmaguiri.backend.Service.dtos.EtapePrestationDTO;

import java.util.List;

public interface EtapePrestationService {
    List<EtapePrestationDTO> getAllEtapesPrestation();
    EtapePrestationDTO getEtapeById(Long id);
    List<EtapePrestationDTO> getEtapesByPrestationId(Long prestationId);
    EtapePrestationDTO createEtapePrestation(EtapePrestationDTO etapePrestationDto);
    EtapePrestationDTO updateEtapePrestation(Long id, EtapePrestationDTO etapePrestationDto);
    void deleteEtapePrestation(Long id);
}
