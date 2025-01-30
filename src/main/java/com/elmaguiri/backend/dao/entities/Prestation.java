package com.elmaguiri.backend.dao.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"etapes", "operations"}) // Exclure les étapes et opérations de toString
public class Prestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "prestation", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepOrder ASC")
    @JsonManagedReference("etape-prestation-reference")
    private List<EtapePrestation> etapes;

    @OneToMany(mappedBy = "prestation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("operation-prestation-reference")
    private List<Operation> operations;
}
