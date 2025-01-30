package com.elmaguiri.backend.Service.services;


import com.elmaguiri.backend.Service.dtos.DocumentDto;

import com.elmaguiri.backend.dao.entities.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

public interface DocumentService {

    List<DocumentDto> getAllDocuments();
    List<DocumentDto> getDocumentsByClientId(Long clientId);
    Document getDocumentById(Long id);
  //  boolean sendDocument(Long documentId, String recipientEmail,String message);
    DocumentDto createDocument(MultipartFile file, Long etapeId, Long clientId) throws IOException;
    DocumentDto updateDocument(Long id, MultipartFile file, Long etapeId, Long clientId) throws IOException;
    String deleteDocument(Long id);

    void sendDocuments(List<Long> documentIdList, String recipientEmail, String message);
}

