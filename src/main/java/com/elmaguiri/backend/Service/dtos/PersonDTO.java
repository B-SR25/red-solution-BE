package com.elmaguiri.backend.Service.dtos;

import jakarta.persistence.Lob;
import lombok.Data;

import java.util.Date;

@Data
public class PersonDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String addressComplement;
    private String postalCode;
    private String city;
    private String phoneNumber;
    private String clientEmail;
    private String dType;
    @Lob
    private byte[] cin;
    private String birthPlace;
    private String nationality;
    private String gender;
    private String passportNumber;
    private String nieNumber;
    private Date passportExpiryDate;
    private Date nieExpiryDate;
    private Date birthDate;
    private String civility;
}

