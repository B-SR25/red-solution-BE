package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Service.dtos.EtapePrestationDTO;
import com.elmaguiri.backend.Service.dtos.PrestationDto;
import com.elmaguiri.backend.dao.entities.Prestation;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.dao.repositories.EtapePrestationRepo;
import com.elmaguiri.backend.dao.repositories.PrestationRepository;
import com.elmaguiri.backend.Service.services.PrestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrestationServiceImpl implements PrestationService {

    private final PrestationRepository prestationRepository;
    private final EtapePrestationRepo etapePrestationRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Autowired
    public PrestationServiceImpl(PrestationRepository prestationRepository, EtapePrestationRepo etapePrestationRepository, ModelMapperConfig modelMapperConfig) {
        this.prestationRepository = prestationRepository;
        this.etapePrestationRepository = etapePrestationRepository;
        this.modelMapperConfig = modelMapperConfig;
    }

    @Override
    public List<PrestationDto> getAllPrestations() {
        return prestationRepository.findAll()
                .stream()
                .map(modelMapperConfig::fromPrestation)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PrestationDto> getPrestationById(Long id) {
        return prestationRepository.findById(id)
                .map(modelMapperConfig::fromPrestation);
    }

    @Override
    public List<EtapePrestationDTO> getEtapesByPrestationId(Long prestationId) {
        return etapePrestationRepository.findByPrestationId(prestationId)
                .stream()
                .map(modelMapperConfig::fromEtapePrestation)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PrestationDto> getPrestationByName(String prestationName) {
        return prestationRepository.findByName(prestationName)
                .map(modelMapperConfig::fromPrestation);
    }

    @Override
    public PrestationDto createPrestation(PrestationDto prestationDto) {
        Prestation prestation = modelMapperConfig.fromPrestationDto(prestationDto);
        Prestation savedPrestation = prestationRepository.save(prestation);
        return modelMapperConfig.fromPrestation(savedPrestation);
    }

    @Override
    public PrestationDto updatePrestation(Long id, PrestationDto prestationDto) {
        Optional<Prestation> existingPrestation = prestationRepository.findById(id);
        if (existingPrestation.isPresent()) {
            Prestation prestation = existingPrestation.get();
            modelMapperConfig.updatePrestationFromDto(prestation, prestationDto);
            Prestation updatedPrestation = prestationRepository.save(prestation);
            return modelMapperConfig.fromPrestation(updatedPrestation);
        } else {
            throw new IllegalArgumentException("Prestation not found with ID: " + id);
        }
    }

    @Override
    public String deletePrestation(Long id) {
        if (prestationRepository.existsById(id)) {
            prestationRepository.deleteById(id);
            return "Prestation deleted successfully.";
        } else {
            return "Prestation not found with ID: " + id;
        }
    }

    @Override
    public List<String> getAllPrestationNames() {
        return prestationRepository.findAll()
                .stream()
                .map(Prestation::getName)
                .collect(Collectors.toList());
    }
}
