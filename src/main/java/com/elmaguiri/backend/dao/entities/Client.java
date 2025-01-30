package com.elmaguiri.backend.dao.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = "partners")
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column(name = "client_title", unique = true)
    private String title;

    @Column(name = "client_abbreviation")
    private String abbreviation;

    @Column(name = "client_address")
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "company_fax")
    private String fax;

    @Column(name = "tax_professionnel")
    private Long taxProfessionnel;

    @Column(name = "city")
    private String city;

    @Column(name = "company_ice")
    private Long ice;

    @Column(name = "company_form")
    private String form;

    @Column(name = "company_capital")
    private Long capital;

    @Column(name = "company_headquarters")
    private String headquarters;

    @Column(name = "company_rc", unique = true)
    private Long rc;

    @Column(name = "company_if")
    private Long ifNumber;

    @Column(name = "company_cnss")
    private Long cnss;

    @Column(name = "company_ownership")
    private String ownership;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "firstAppointment")
    private LocalDate firstAppointment;


    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("operation-client-reference")
    private List<Operation> operations;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("etape-client-reference")
    private List<Document> documents = new ArrayList<>();


    @Column(name = "contrat",columnDefinition = "bytea")
    private byte[] contrat;

    @Column(name = "client_type")
    private String clientType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private Person manager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private Owner owner;  // Utilisation de Owner comme type pour la relation

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Partner> partners;

    @OneToMany(mappedBy = "client")
    @JsonManagedReference("notification-reference")
    private List<Notification> notifications;

    public Client(Long clientId) {
        this.id=clientId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", address='" + address + '\'' +
                ", fax='" + fax + '\'' +
                ", city='" + city + '\'' +
                ", ice=" + ice +
                ", form='" + form + '\'' +
                ", capital=" + capital +
                ", headquarters='" + headquarters + '\'' +
                ", rc=" + rc +
                ", ifNumber=" + ifNumber +
                ", cnss=" + cnss +
                ", ownership='" + ownership + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", status=" + status +
                '}';
    }
}
