package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Service.dtos.FileDTO;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.Service.services.FileService;
import com.elmaguiri.backend.dao.entities.File;
import com.elmaguiri.backend.dao.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ModelMapperConfig modelMapperConfig;

    private static final String PREFIX = "D";
    private static final int LENGTH = 4;

    @Override
    public List<FileDTO> getAllFiles() {
        return fileRepository.findAll().stream()
                .map(modelMapperConfig::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FileDTO getFileById(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return modelMapperConfig.toDTO(file);
    }

    @Override
    public FileDTO createFile(FileDTO fileDTO) {
        File file = modelMapperConfig.toEntity(fileDTO);
        return modelMapperConfig.toDTO(fileRepository.save(file));
    }

    @Override
    public FileDTO updateFile(Long id, FileDTO fileDTO) {
        File file = fileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
        modelMapperConfig.updateEntityFromDTO(fileDTO, file); // Update entity with DTO values
        return modelMapperConfig.toDTO(fileRepository.save(file));
    }

    @Override
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }

    @Override
    public String getNextCode() {
            String lastCode = fileRepository.findLastCode();
            lastCode = (lastCode != null) ? lastCode : PREFIX + "0000";

            int lastNumber = Integer.parseInt(lastCode.substring(1));
            int nextNumber = lastNumber + 1;

            return PREFIX + String.format("%0" + LENGTH + "d", nextNumber);
    }


}