package com.elmaguiri.backend.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Long id;


    @Column(name = "date_notification")
    @Temporal(TemporalType.TIMESTAMP) // Utilisation de TIMESTAMP pour inclure la date et l'heure
    private Date NotificationDate;

    private String message;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference("notification-reference")
    private Client client;
}
