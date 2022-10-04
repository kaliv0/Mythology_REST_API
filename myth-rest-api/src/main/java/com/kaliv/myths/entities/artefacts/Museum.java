package com.kaliv.myths.entities.artefacts;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "museums")
public class Museum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "museum")
    private Set<Statue> statues = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "museum")
    private Set<Painting> paintings = new HashSet<>();
}
