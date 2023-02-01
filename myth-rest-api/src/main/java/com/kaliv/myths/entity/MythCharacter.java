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
@Table(name = "characters")
public class MythCharacter extends ArtworkPossessor {
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "father_id", referencedColumnName = "id")
    private MythCharacter father;

    @ManyToOne
    @JoinColumn(name = "mother_id", referencedColumnName = "id")
    private MythCharacter mother;

    @ManyToMany(mappedBy = "mythCharacters")
    private Set<Myth> myths = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "characters_statues",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "statue_id", referencedColumnName = "id"))
    private Set<Statue> statues = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "characters_paintings",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "painting_id", referencedColumnName = "id"))
    private Set<Painting> paintings = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "characters_music",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "music_id", referencedColumnName = "id"))
    private Set<Music> music = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "characters_poems",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "poem_id", referencedColumnName = "id"))
    private Set<Poem> poems = new HashSet<>();

    @PrePersist
    public void addMythCharacter() {
        this.getMyths()
                .forEach(myth -> myth.getMythCharacters().add(this));
    }

    @PreRemove
    public void deleteMythCharacter() {
        this.getMyths()
                .forEach(myth -> myth.setMythCharacters(null));
    }
}
