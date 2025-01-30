package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Service.dtos.EtapePrestationDTO;
import com.elmaguiri.backend.dao.entities.EtapePrestation;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.dao.repositories.EtapePrestationRepo;
import com.elmaguiri.backend.Service.services.EtapePrestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EtapePrestationServiceImpl implements EtapePrestationService {

    private final EtapePrestationRepo etapePrestationRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Autowired
    public EtapePrestationServiceImpl(EtapePrestationRepo etapePrestationRepo, ModelMapperConfig modelMapperConfig) {
        this.etapePrestationRepository = etapePrestationRepo;
        this.modelMapperConfig = modelMapperConfig;
    }


    @Override
    public List<EtapePrestationDTO> getAllEtapesPrestation() {
        return etapePrestationRepository.findAll().stream()
                .map(modelMapperConfig::fromEtapePrestation)
                .collect(Collectors.toList());
    }

    @Override
    public EtapePrestationDTO getEtapeById(Long id) {
        return etapePrestationRepository.findById(id)
                .map(modelMapperConfig::fromEtapePrestation)
                .orElse(null);
    }

    @Override
    public EtapePrestationDTO createEtapePrestation(EtapePrestationDTO etapePrestationDto) {
        EtapePrestation etapePrestation = modelMapperConfig.fromEtapePrestationDto(etapePrestationDto);
        EtapePrestation savedEtapePrestation = etapePrestationRepository.save(etapePrestation);
        return modelMapperConfig.fromEtapePrestation(savedEtapePrestation);
    }

    @Override
    public EtapePrestationDTO updateEtapePrestation(Long id, EtapePrestationDTO etapePrestationDto) {
        Optional<EtapePrestation> existingEtapePrestationOptional = etapePrestationRepository.findById(id);
        if (existingEtapePrestationOptional.isPresent()) {
            EtapePrestation existingEtapePrestation = existingEtapePrestationOptional.get();
            existingEtapePrestation.setName(etapePrestationDto.getName());
            existingEtapePrestation.setAttributesTemplate(etapePrestationDto.getAttributesTemplate());
            EtapePrestation updatedEtapePrestation = etapePrestationRepository.save(existingEtapePrestation);
            return modelMapperConfig.fromEtapePrestation(updatedEtapePrestation);
        }
        return null;
    }

    @Override
    public void deleteEtapePrestation(Long id) {
        etapePrestationRepository.deleteById(id);
    }

    @Override
    public List<EtapePrestationDTO> getEtapesByPrestationId(Long prestationId){
        return etapePrestationRepository.findByPrestationId(prestationId)
                .stream()
                .map(modelMapperConfig::fromEtapePrestation)
                .collect(Collectors.toList());
    }
}
