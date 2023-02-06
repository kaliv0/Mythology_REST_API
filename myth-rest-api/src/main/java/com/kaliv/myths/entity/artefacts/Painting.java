package com.kaliv.myths.entity.artefacts;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.entity.artefacts.contracts.artworks.VisualArtwork;
import com.kaliv.myths.entity.artefacts.images.PaintingImage;
import com.kaliv.myths.entity.artefacts.images.SmallPaintingImage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "paintings")
public class Painting extends VisualArtwork {
    @OneToMany(mappedBy = "painting", cascade = CascadeType.ALL)
    private Set<PaintingImage> paintingImages = new HashSet<>();

    @OneToMany(mappedBy = "painting", cascade = CascadeType.ALL)
    private Set<SmallPaintingImage> smallPaintingImages = new HashSet<>();

    @ManyToMany(mappedBy = "paintings")
    private Set<MythCharacter> mythCharacters = new HashSet<>();

    @PrePersist
    public void addPainting() {
        this.getMythCharacters()
                .forEach(character -> character.getPaintings().add(this));
    }

    @PreRemove
    public void deletePainting() {
        this.getMythCharacters()
                .forEach(character -> character.getPaintings().remove(this));
    }
}
