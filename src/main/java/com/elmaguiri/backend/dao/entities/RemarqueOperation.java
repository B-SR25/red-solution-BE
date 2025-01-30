package com.elmaguiri.backend.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class RemarqueOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_remarque")
    private Long id;

    @Column(name = "name_remarque")
    private String nameRemarque;

    @Column(name = "date_remarque")
    @Temporal(TemporalType.TIMESTAMP) // Utilisation de TIMESTAMP pour inclure la date et l'heure
    private Date remarqueDate;

    @Column(name = "description")
    private String desc;

    @ManyToOne
    @JoinColumn(name = "id_operation")
    @JsonBackReference("remarque-operation-reference")
    private Operation operation;

    @Override
    public String toString() {
        return "RemarqueOperation{" +
                "id=" + id +
                ", message='" + desc + '\'' +
                ", operation=" + (operation != null ? operation.getId() : null) +
                '}';
    }

}
