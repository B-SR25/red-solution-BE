package com.elmaguiri.backend.web.controller;

import com.elmaguiri.backend.Service.dtos.FileDTO;
import com.elmaguiri.backend.Service.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller
@RestController
@AllArgsConstructor
@RequestMapping("/files")
public class FileController {

    private FileService fileService;


    @GetMapping("/next-code")
    public ResponseEntity<String> getNextCode() {
        String nextCode = fileService.getNextCode();
        return ResponseEntity.ok(nextCode);
    }
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public List<FileDTO> getAllFiles() {
        return fileService.getAllFiles();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public FileDTO getFileById(@PathVariable Long id) {
        return fileService.getFileById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public FileDTO createFile(@RequestBody FileDTO fileDTO) {
        return fileService.createFile(fileDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public FileDTO updateFile(@PathVariable Long id, @RequestBody FileDTO fileDTO) {
        return fileService.updateFile(id, fileDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public void deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
    }
}