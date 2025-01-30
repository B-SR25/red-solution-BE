package com.elmaguiri.backend.web.controller;


import com.elmaguiri.backend.config.Constants;

import com.elmaguiri.backend.Service.dtos.DocumentDto;
import com.elmaguiri.backend.Service.dtos.DocumentResponseDto;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.dao.repositories.DocumentRepository;
import com.elmaguiri.backend.Service.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentRepository documentRepository;
    private final ModelMapperConfig modelMapperConfig;
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentRepository documentRepository, ModelMapperConfig modelMapperConfig, DocumentService documentService) {
        this.documentRepository = documentRepository;
        this.modelMapperConfig = modelMapperConfig;
        this.documentService = documentService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<DocumentDto>> getAllDocuments() {
        List<DocumentDto> allDocuments = documentService.getAllDocuments();
        return new ResponseEntity<>(allDocuments, HttpStatus.OK);
    }

    @GetMapping("client/{clientId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<List<DocumentDto>> getDocumentsByClientId( @PathVariable Long clientId) {
        List<DocumentDto> documentsByClient = documentService.getDocumentsByClientId(clientId);
        return new ResponseEntity<>(documentsByClient, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public byte[] downloadDocument(@PathVariable Long id) {
        return documentService.getDocumentById(id).getDocument();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> createDocument(@RequestParam("document") MultipartFile file, @RequestParam("etapeId") Long etapeId, @RequestParam("clientId") Long clientId) {
        try {
            DocumentDto createdDocument = documentService.createDocument(file, etapeId , clientId);
            return new ResponseEntity<>(createdDocument, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.DOCUMENT_NOT_FOUND);
        }
    }
    @PostMapping("/send")
    public ResponseEntity<Object> sendDocuments(@RequestBody Map<String, Object> request) {
        List<Number> documentIds = (List<Number>) request.get("documentIds"); // List of IDs
        String recipientEmail = (String) request.get("recipientEmail");
        String message = (String) request.get("message");

        // Convert List<Number> to List<Long>
        List<Long> documentIdList = documentIds.stream()
                .map(Number::longValue)
                .toList();

        // Send documents
        documentService.sendDocuments(documentIdList, recipientEmail, message);

        return ResponseEntity.ok(Map.of("message", "Documents sent successfully!"));
    }

//    @PostMapping("/send")
//    public ResponseEntity<String> sendDocument(@RequestBody Map<String, Object> request) {
//        Long documentId = ((Number) request.get("documentId")).longValue(); // Convert from Number to Long
//        String recipientEmail = (String) request.get("recipientEmail");
//        String message = (String) request.get("message");
//
//        documentService.sendDocument(documentId, recipientEmail, message);
//
//        return ResponseEntity.ok("Document sent successfully!");
//    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Object> updateDocument(@PathVariable Long id,@RequestParam("document") MultipartFile file, @RequestParam("etapeId") Long etapeId, @RequestParam("clientId") Long clientId) throws IOException {
        try{
            DocumentDto updatedDocument = documentService.updateDocument(id, file,etapeId,clientId);
            return new ResponseEntity<>(updatedDocument, HttpStatus.CREATED);
        }catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.DOCUMENT_NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER') or hasAuthority('SCOPE_ROLE_ADMIN')")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}