package com.kaliv.myths.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "myths")
public class Myth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "plot", nullable = false)
    private String plot;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "myths_characters",
            joinColumns = @JoinColumn(
                    name = "myth_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "character_id", referencedColumnName = "id"
            )
    )
    private Set<Character> characters = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    private Nationality nationality;
}
