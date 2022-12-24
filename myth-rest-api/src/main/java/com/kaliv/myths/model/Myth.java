package com.kaliv.myths.model;

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

    @Column(name = "title", nullable = false, unique = true)
    private String title;

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
    private Set<MythCharacter> mythCharacters = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    private Nationality nationality;
}
