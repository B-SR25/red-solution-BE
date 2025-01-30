package com.elmaguiri.backend.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.TypeDef;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "prestation") // Exclure la prestation de toString pour éviter la récursion
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class EtapePrestation extends Object{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attributesTemplate;

    @ManyToOne
    @JoinColumn(name = "prestation_id")
    @JsonBackReference("etape-prestation-reference")
    private Prestation prestation;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    private String color;
}
