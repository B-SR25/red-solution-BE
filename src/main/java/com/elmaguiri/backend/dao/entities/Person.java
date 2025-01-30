package com.elmaguiri.backend.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;
    @Column(name = "person_firstName")
    private String firstName;

    @Column(name = "person_lastName")
    private String lastName;

    @Column(name = "person_address")
    private String address;

    @Column(name = "address_complement")
    private String addressComplement;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "cin",columnDefinition = "bytea")
    private byte[] cin;

    @Column(name = "birth_place")
    private String birthPlace;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "gender")
    private String gender;

    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "nie_number")
    private String nieNumber;

    @Column(name = "passport_expiry_date")
    @Temporal(TemporalType.DATE)
    private Date passportExpiryDate;

    @Column(name = "nie_expiry_date")
    @Temporal(TemporalType.DATE)
    private Date nieExpiryDate;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(name = "civility")
    private String civility;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Client> clients;
}
