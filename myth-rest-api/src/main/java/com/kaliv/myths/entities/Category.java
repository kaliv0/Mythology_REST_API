package com.kaliv.myths.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "character_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToOne(mappedBy = "category")
    private Character character;
}
