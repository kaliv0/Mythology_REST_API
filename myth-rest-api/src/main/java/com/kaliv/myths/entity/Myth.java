package com.kaliv.myths.entity;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.artefacts.Music;
import com.kaliv.myths.entity.artefacts.Painting;
import com.kaliv.myths.entity.artefacts.Poem;
import com.kaliv.myths.entity.artefacts.Statue;
import com.kaliv.myths.entity.artefacts.contracts.possessors.ArtworkPossessor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "myths")
public class Myth extends ArtworkPossessor {
    @Column(name = "plot", nullable = false)
    private String plot;

    @ManyToOne
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    private Nationality nationality;

    @ManyToMany
    @JoinTable(name = "myths_characters",
            joinColumns = @JoinColumn(name = "myth_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"))
    private Set<MythCharacter> mythCharacters = new HashSet<>();

    @OneToMany(mappedBy = "myth")
    private Set<Statue> statues = new HashSet<>();

    @OneToMany(mappedBy = "myth")
    private Set<Painting> paintings = new HashSet<>();

    @OneToMany(mappedBy = "myth")
    private Set<Music> music = new HashSet<>();

    @OneToMany(mappedBy = "myth")
    private Set<Poem> poems = new HashSet<>();

    @PrePersist
    public void addMyth() {
        this.getStatues()
                .forEach(statue -> statue.setMyth(this));
        this.getPaintings()
                .forEach(painting -> painting.setMyth(this));
        this.getMusic()
                .forEach(music -> music.setMyth(this));
        this.getPoems()
                .forEach(poem -> poem.setMyth(this));
    }

    @PreRemove
    public void deleteMyth() {
        this.getStatues()
                .forEach(statue -> statue.setMyth(null));
        this.getPaintings()
                .forEach(painting -> painting.setMyth(null));
        this.getMusic()
                .forEach(music -> music.setMyth(null));
        this.getPoems()
                .forEach(poem -> poem.setMyth(null));
    }
}
