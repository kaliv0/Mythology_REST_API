package com.kaliv.myths.entity;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
@Table(name = "characters")
public class MythCharacter extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "father_id", referencedColumnName = "id")
    private MythCharacter father;

    @OneToOne
    @JoinColumn(name = "mother_id", referencedColumnName = "id")
    private MythCharacter mother;

    @ManyToMany(mappedBy = "mythCharacters")
    @OnDelete(action = OnDeleteAction.NO_ACTION) //TODO: does this work?
    private Set<Myth> myths = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY) //TODO: potentially should be CascadeType.ALL/REMOVE
    @JoinTable(
            name = "characters_statues",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "statue_id", referencedColumnName = "id")
    )
    private Set<Statue> statues = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "characters_paintings",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "painting_id", referencedColumnName = "id")
    )
    private Set<Painting> paintings = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "characters_music",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "music_id", referencedColumnName = "id")
    )
    private Set<Music> music = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "characters_poems",
            joinColumns = @JoinColumn(name = "character_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "poem_id", referencedColumnName = "id")
    )
    private Set<Poem> poems = new HashSet<>();
}
