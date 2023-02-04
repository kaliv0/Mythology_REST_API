package com.kaliv.myths.entity.artefacts;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.artefacts.contracts.possessors.VisualArtworkPossessor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "museums")
public class Museum extends VisualArtworkPossessor {
    @OneToMany(mappedBy = "museum")
    private Set<Statue> statues = new HashSet<>();

    @OneToMany(mappedBy = "museum")
    private Set<Painting> paintings = new HashSet<>();

    @PrePersist
    public void addMuseum() {
        this.getStatues()
                .forEach(statue -> statue.setMuseum(this));
        this.getPaintings()
                .forEach(painting -> painting.setMuseum(this));
    }

    @PreRemove
    public void deleteMuseum() {
        this.getStatues()
                .forEach(statue -> statue.setMuseum(null));
        this.getPaintings()
                .forEach(painting -> painting.setMuseum(null));
    }
}
