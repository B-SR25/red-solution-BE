package com.elmaguiri.backend.dao.entities;

import com.elmaguiri.backend.Enum.OperationState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "etapeOperations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operation")
    private Long id;

    @Column(name = "name_operation")
    private String nameOperation;

    @Column(name = "date_operation")
    @Temporal(TemporalType.DATE)
    private Date operationDate;

    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("remarque-operation-reference")
    private List<RemarqueOperation> remarqueOperation;

    @Column(name = "statut_operation")
    private OperationState statutOperation;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference("operation-client-reference")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "prestation_id")
    @JsonBackReference("operation-prestation-reference")
    private Prestation prestation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-operation-reference")
    private User createdByUser;

    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("stepOrder ASC")
    @JsonManagedReference("etape-operation-reference")
    private List<EtapeOperation> etapeOperations;
    private Boolean facturer;
}
