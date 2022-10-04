package com.kaliv.myths.entities.artefacts;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public abstract class VisualArtwork extends Artwork {
    @ManyToOne
    @JoinColumn(name = "museum_id", referencedColumnName = "id")
    private Museum museum;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Image> images = new HashSet<>();
//    @ElementCollection
//    private Set<String> imageUrls;
}
