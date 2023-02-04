package com.kaliv.myths.entity.artefacts;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.entity.artefacts.contracts.artworks.VisualArtwork;
import com.kaliv.myths.entity.artefacts.images.SmallStatueImage;
import com.kaliv.myths.entity.artefacts.images.StatueImage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "statues")
public class Statue extends VisualArtwork {
    @OneToMany(mappedBy = "statue", cascade = CascadeType.REMOVE)
    private Set<StatueImage> statueImages = new HashSet<>();

    @OneToMany(mappedBy = "statue", cascade = CascadeType.REMOVE)
    private Set<SmallStatueImage> smallStatueImages = new HashSet<>();

    @ManyToMany(mappedBy = "statues")
    private Set<MythCharacter> mythCharacters = new HashSet<>();

    @PrePersist
    public void addPainting() {
        this.getStatueImages()
                .forEach(image -> image.setStatue(this));
        this.getSmallStatueImages()
                .forEach(painting -> painting.setStatue(this));
        this.getMythCharacters()
                .forEach(character -> character.getStatues().add(this));
    }

    @PreRemove
    public void deleteStatue() {
        this.getStatueImages()
                .forEach(image -> image.setStatue(null));
        this.getSmallStatueImages()
                .forEach(painting -> painting.setStatue(null));
        this.getMythCharacters()
                .forEach(character -> character.getStatues().remove(this));
    }
}
