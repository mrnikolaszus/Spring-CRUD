package com.javarush.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(schema = "todo", name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

}
