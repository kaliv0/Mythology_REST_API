package com.kaliv.myths.entity.artefacts;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.Nationality;
import com.kaliv.myths.entity.TimePeriod;
import com.kaliv.myths.entity.artefacts.contracts.possessors.ArtworkPossessor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author extends ArtworkPossessor {
    @ManyToOne
    @JoinColumn(name = "time_period_id", referencedColumnName = "id")
    private TimePeriod timePeriod;

    @ManyToOne
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    private Nationality nationality;

    @OneToMany(mappedBy = "author")
    private Set<Statue> statues = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Painting> paintings = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Music> music = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Poem> poems = new HashSet<>();

    @PrePersist
    public void addAuthor() {
        this.getStatues()
                .forEach(statue -> statue.setAuthor(this));
        this.getPaintings()
                .forEach(painting -> painting.setAuthor(this));
        this.getMusic()
                .forEach(music -> music.setAuthor(this));
        this.getPoems()
                .forEach(poem -> poem.setAuthor(this));
    }

    @PreRemove
    public void deleteAuthor() {
        this.getStatues()
                .forEach(statue -> statue.setAuthor(null));
        this.getPaintings()
                .forEach(painting -> painting.setAuthor(null));
        this.getMusic()
                .forEach(music -> music.setAuthor(null));
        this.getPoems()
                .forEach(poem -> poem.setAuthor(null));
    }
}
