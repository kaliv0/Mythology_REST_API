package com.kaliv.myths.entity;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "myths")
public class Myth extends BaseEntity {
    //TODO: decide for length
    @Column(name = "plot", nullable = false)
    private String plot;

    @ManyToOne
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    private Nationality nationality;

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
}
