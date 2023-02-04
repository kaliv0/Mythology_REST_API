package com.kaliv.myths.entity.artefacts;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.entity.artefacts.contracts.artworks.Artwork;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "poems")
public class Poem extends Artwork {
    @Column(name = "full_text_url", nullable = false)
    private String fullTextUrl;

    private String excerpt;

    @ManyToMany(mappedBy = "poems")
    private Set<MythCharacter> mythCharacters = new HashSet<>();

    @PrePersist
    public void addPoem() {
        this.getMythCharacters()
                .forEach(character -> character.getPoems().add(this));
    }

    @PreRemove
    public void deletePoem() {
        this.getMythCharacters()
                .forEach(character -> character.setPoems(null));
    }
}
