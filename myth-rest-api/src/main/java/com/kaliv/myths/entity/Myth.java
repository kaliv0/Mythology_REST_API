package com.kaliv.myths.entity;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.artefacts.Music;
import com.kaliv.myths.entity.artefacts.Painting;
import com.kaliv.myths.entity.artefacts.Poem;
import com.kaliv.myths.entity.artefacts.Statue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "myths")
public class Myth extends BaseEntity {
    @Column(name = "plot", nullable = false)
    private String plot;

    @ManyToOne
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    private Nationality nationality;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "myths_characters",
            joinColumns = @JoinColumn(name = "myth_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id")
    )
    private Set<MythCharacter> mythCharacters = new HashSet<>();

    @OneToMany(mappedBy = "myth", fetch = FetchType.LAZY)
    private Set<Statue> statues = new HashSet<>();

    @OneToMany(mappedBy = "myth", fetch = FetchType.LAZY)
    private Set<Painting> paintings = new HashSet<>();

    @OneToMany(mappedBy = "myth", fetch = FetchType.LAZY)
    private Set<Music> music = new HashSet<>();

    @OneToMany(mappedBy = "myth", fetch = FetchType.LAZY)
    private Set<Poem> poems = new HashSet<>();
}
