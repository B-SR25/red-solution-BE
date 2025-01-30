package com.elmaguiri.backend.Service.services;

import com.elmaguiri.backend.Service.dtos.FileDTO;

import java.util.List;

public interface FileService {
    List<FileDTO> getAllFiles();
    FileDTO getFileById(Long id);
    FileDTO createFile(FileDTO fileDTO);
    FileDTO updateFile(Long id, FileDTO fileDTO);
    void deleteFile(Long id);

    String getNextCode();
}
