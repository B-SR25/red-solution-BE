package com.elmaguiri.backend.dao.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CrossOrigin("*")
@ToString(exclude = "createdOperations") // Exclure les opérations pour éviter la récursion
@Table(name = "Utilisateur") // Utilisation de guillemets doubles pour encapsuler le nom de la table

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "firstName_user")
    private String name;

    @Column(name = "lastName_user")
    private String surname;

    @Column(name = "status_user")
    private boolean status;

    @Column(name = "Username")
    private String username;

    @Column(name = "password_user")
    private String password;

    @Column(name = "confirmPassword_user")
    private String confirmPassword;

    @Column(name = "jobPosition_user")
    private String jobPosition;

    @Column(unique = true, name = "mailAddress_user")
    private String mail;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "createdByUser",fetch = FetchType.LAZY)
    @JsonManagedReference("user-operation-reference")
    private List<Operation> createdOperations = new ArrayList<>();

    public Boolean getStatus() {
        return this.status;
    }
}