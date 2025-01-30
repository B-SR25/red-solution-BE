package com.elmaguiri.backend.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class  Document{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Temporal(TemporalType.DATE)
    private Date registrationDate;


    @Column(name = "document", columnDefinition = "bytea")
    private byte[] document=null;

    private String contentType;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference("etape-client-reference")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "etape_id")
    @JsonBackReference("etape-document-reference")
    private EtapeOperation etape;

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", registrationDate=" + registrationDate +
                ", contentType='" + contentType + '\'' +
                ", etape=" + (etape != null ? etape.getId() : null) + // Example of avoiding recursion
                ", client=" + (client != null ? client.getId() + " " + client.getTitle() + " " + client.getAbbreviation() : null) +
                '}';
    }

    public Long getClientId() {
        return this.client != null ? this.client.getId() : null;
    }

}

