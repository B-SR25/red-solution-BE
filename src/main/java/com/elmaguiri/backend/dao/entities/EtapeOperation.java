package com.elmaguiri.backend.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.TypeDef;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "documents, operation") // Exclure documents et operation pour éviter la récursion
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class EtapeOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attributes;

    @ManyToOne
    @JoinColumn(name = "operation_id")
    @JsonBackReference("etape-operation-reference")
    private Operation operation;

    @OneToMany(mappedBy = "etape")
    @JsonManagedReference("etape-document-reference")
    private List<Document> documents = new ArrayList<>();

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    private String color;

}
