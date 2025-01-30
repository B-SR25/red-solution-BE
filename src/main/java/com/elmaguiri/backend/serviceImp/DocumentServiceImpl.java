package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Service.dtos.DocumentDto;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.Service.services.DocumentService;
import com.elmaguiri.backend.config.Constants;
import com.elmaguiri.backend.dao.entities.Client;
import com.elmaguiri.backend.dao.entities.Document;
import com.elmaguiri.backend.dao.entities.EtapeOperation;
import com.elmaguiri.backend.dao.repositories.ClientRepository;
import com.elmaguiri.backend.dao.repositories.DocumentRepository;
import com.elmaguiri.backend.dao.repositories.EtapeOperationRepo;
import com.elmaguiri.backend.exceptions.DocumentNotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ModelMapperConfig modelMapperConfig;
    private final EtapeOperationRepo etapeOperationRepository;
    private final ClientRepository clientRepository;
    private SendEmailServiceImpl sendEmailService;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, ModelMapperConfig modelMapperConfig, EtapeOperationRepo etapeOperationRepository, ClientRepository clientRepository, SendEmailServiceImpl sendEmailService) {
        this.documentRepository = documentRepository;
        this.modelMapperConfig = modelMapperConfig;
        this.etapeOperationRepository = etapeOperationRepository;
        this.clientRepository = clientRepository;
        this.sendEmailService=sendEmailService;
    }

    @Override
    public List<DocumentDto> getAllDocuments() {
        List<Document> documents = documentRepository.findAll();
        return documents.stream()
                .map(modelMapperConfig::ToDocumentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentDto> getDocumentsByClientId(Long clientId) {
        List<Document> documents = documentRepository.getDocumentsByClientId(clientId);
        return documents.stream()
                .map(modelMapperConfig::ToDocumentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Document getDocumentById(Long id) {
        Document dbDocument = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document with ID " + id + " not found"));
            return dbDocument;

    }


//    @Override
//    public boolean sendDocument(Long documentId, String recipientEmail,String message) {
//        Document document = this.getDocumentById(documentId);
//        sendEmailService.sendEmailWithAttachment(recipientEmail,"documents",message, document.getDocument(), document.getFileName(),document.getContentType());
//        //sendEmailService.sendEmail(recipientEmail,"Documents",message);
//        return true;
//    }

    @Override
    public void sendDocuments(List<Long> documentIdList, String recipientEmail, String message) {
        List<Document> documentList=documentIdList.stream()
                .map(this::getDocumentById) // Fetch each document by ID
                .collect(Collectors.toList());
        sendEmailService.sendEmailWithAttachment(
                    recipientEmail,
                    "Documents",
                    message,
                    documentList
            );

    }
    @Override
    public DocumentDto createDocument(MultipartFile file, Long etapeId, Long clientId) throws IOException {
        String contentType = file.getContentType();
        Date currentDate = new Date();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
             InputStream fileInputStream = file.getInputStream()) {

            ZipEntry zipEntry = new ZipEntry(file.getOriginalFilename());
            zipOutputStream.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, length);
            }

            zipOutputStream.closeEntry();
        }
        byte[] archivedFileBytes = byteArrayOutputStream.toByteArray();

        DocumentDto documentDto = DocumentDto.builder()
                .fileName(file.getOriginalFilename())
                .etapeId(etapeId)
                .clientId(clientId)
                .registrationDate(currentDate)
                .contentType(contentType)
                .document(archivedFileBytes)
                .build();
        System.out.println("documentDto"+documentDto+"clientId"+clientId);

        Document document = modelMapperConfig.fromDocumentDto(documentDto);
        Document savedDocument = documentRepository.save(document);
        return modelMapperConfig.fromDocument(savedDocument);
    }

    @Override
    public DocumentDto updateDocument(Long id, MultipartFile file, Long etapeId, Long clientId) throws IOException {

        Optional<Document> documentOptional = documentRepository.findById(id);
        Client client=clientRepository.getReferenceById(clientId);
        if (documentOptional.isPresent()) {
            Optional<EtapeOperation> etapeOperationOptional = etapeOperationRepository.findById(etapeId);
            if (!etapeOperationOptional.isPresent()) {
                throw new IllegalArgumentException("EtapeOperation not found with ID: " + etapeId);
            }

            EtapeOperation etapeOperation = etapeOperationOptional.get();
            Document document = new Document();
            document.setId(id);
            document.setFileName(file.getOriginalFilename());
            document.setContentType(file.getContentType());
            document.setDocument(file.getBytes());
            document.setEtape(etapeOperation);
            document.setClient(client);
           Document document1=documentRepository.save(document);
           return modelMapperConfig.fromDocument(document1);
        } else {
            throw new RuntimeException("Document not found with id: " + id);
        }
    }

    @Override
    public String deleteDocument(Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
            return "Document with id: " + id + " has been deleted successfully.";
        } else {
            throw new RuntimeException("Document not found with id: " + id);
        }
    }

}

