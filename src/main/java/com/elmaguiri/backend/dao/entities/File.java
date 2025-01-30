package com.elmaguiri.backend.dao.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "files")
@Data
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "apostille", nullable = false)
    private boolean apostille;

    @Column(name = "translation", nullable = false)
    private String translation;
}
